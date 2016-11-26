/**
 MIT License

 Copyright (c) 2016 MetroState-Cougars

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in all
 copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 SOFTWARE.
 */

package org.cougars.controller

import groovy.util.logging.Slf4j
import org.cougars.WebConfiguration
import org.cougars.bean.BookmarkBean
import org.cougars.domain.Bookmark
import org.cougars.domain.BookmarkCategory
import org.cougars.domain.Status
import org.cougars.domain.User
import org.cougars.repository.BookmarkCategoryRepository
import org.cougars.repository.BookmarkRepository
import org.cougars.repository.UserRepository
import org.cougars.service.BookmarkValidatorService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.SortDefault
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam

import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse
import javax.validation.Valid

/** Controller class for mapping public functions.
 * Created by Dennis Rausch on 8/26/16.
 */

@Slf4j
@Controller
public class BaseController {
    @Autowired private UserRepository ur
    @Autowired private BookmarkRepository br
    @Autowired private BookmarkCategoryRepository bcr
    @Autowired private BookmarkValidatorService bvs

    @GetMapping("")
    String index(@RequestParam(value ="view", required = false) String viewParam,
                 @CookieValue(value = "view", defaultValue = "table") String viewCookie,
                 @CookieValue(value = "history", defaultValue = "") String history,
                 @SortDefault("dateCreated") Pageable pageable,
                 Model model, HttpServletResponse response) {
        String view = viewParam ?: viewCookie
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication()
        User user = ur.findByUsername(authentication.getName())

        // Preload history
        if(!history) {
            history = URLEncoder.encode(br.findByStatus(Status.ACTIVE, pageable).getContent().id.join(","), "UTF-8")
        }

        List<String> idString = URLDecoder.decode(history, "UTF-8")?.split(",")
        List<Long> ids = new ArrayList()

        if(history) {
            idString.each { ids.add(it as Long) }
        }

        if(view == "category") {
            Bookmark last = br.findById(ids.last())
            if(!last) {
                last = br.findByStatus(Status.ACTIVE, pageable).last()
            }

            model.addAttribute("page", bcr.findAll())
            model.addAttribute("bookmark", last)
        } else if(view == "top") {
            model.addAttribute("page", br.findByIds(ids, pageable))
        } else {
            // Table view
            Page<Bookmark> page
            if(user?.isAdmin()) {
                page = br.findAll(pageable)
            } else {
                page = br.findByStatus(Status.ACTIVE, pageable)
            }
            model.addAttribute("page", page)
        }

        response.addCookie(new Cookie("view", view))
        response.addCookie(new Cookie("pageSize", pageable.pageSize as String))

        return view
    }

    @GetMapping("/login")
    String login() {
        return "login"
    }

    @GetMapping("/add-bookmark")
    String addBookmark(Model model) {
        model.addAttribute("bookmarkBean", new BookmarkBean())

        return "addBookmark"
    }

    @PostMapping("/add-bookmark")
    String addBookmarkSubmission(@Valid BookmarkBean bean, BindingResult bindingResult) {
        String view = "addBookmarkSuccess"

        if (bindingResult.hasErrors()) {
            view = "addBookmark"
        } else {
            try {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication()
                User user = ur.findByUsername(authentication.getName())
                BookmarkCategory category = bcr.findByName(bean.bookmarkCategory.trim())
                if(!category) {
                    category = new BookmarkCategory(bean.bookmarkCategory.trim(), bcr.findByName("None"), user)
                }

                BookmarkCategory subcategory = bcr.findByName("None")
                if(bean.subcategory && !bean.subcategory.equalsIgnoreCase("None")) {
                    subcategory = bcr.findByName(bean.subcategory.trim()) ?: new BookmarkCategory(bean.subcategory.trim(), category, user)
                }

                Bookmark bookmark = new Bookmark(bean.url.trim(), bean.name.trim(), bean.description, category, subcategory, user)
                if(user?.isAdmin()) {
                    bookmark.status = Status.ACTIVE
                }

                br.save(bookmark)
                bvs.validateUrl(bookmark)
            } catch (Exception e) {
                view = "error"
                log.error("Error adding bookmark!", e)
            }
        }

        return view
    }

    @GetMapping("/bookmark-details")
    String getBookmarkDetails(@RequestParam(value = "id") Long id, Model model) {
        model.addAttribute("bookmark", br.findById(id))

        return "fragments/bookmarkDetail :: bookmarkDetail"
    }

    @GetMapping("/search")
    String search(@RequestParam(value ="query") String query,
                  @SortDefault("dateCreated") Pageable pageable,
                  Model model) {
        model.addAttribute("page", br.search(query.trim(), pageable))

        return "table"
    }

    @GetMapping("/advanced-search")
    String advancedSearchPage(Model model)  {
        //TODO: Add model attributes for category and subcategory to populate dynamic dropdowns on the view
        model.addAttribute("bookmarkBean", new BookmarkBean())

        return "advancedSearch"
    }

    @PostMapping("/advanced-search")
    String advancedSearch(Model model)  {
        //TODO: Create a bookmark repository for searching with multiple parameters
        throw new RuntimeException("Method not implemented!")
    }

    @GetMapping("/track-click")
    String trackClick(@RequestParam(value = "id") Long id, HttpServletResponse response,
                      @CookieValue(value = "history", defaultValue = "") String history) {
        Bookmark bookmark = br.findById(id)
        List<String> idString = URLDecoder.decode(history, "UTF-8").split(",")
        List<Long> ids = new ArrayList()
        if(history) {
            idString.each { ids.add(it as Long) }
            ids.remove(id)
        }

        ids.add(id)
        history = URLEncoder.encode(ids.takeRight(WebConfiguration.PAGE_SIZE).join(","), "UTF-8")
        response.addCookie(new Cookie("history", history))

        return "redirect:${bookmark.url}"
    }
}
