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
class BaseController {
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

    @GetMapping("/track-click")
    void trackClick(@RequestParam(value = "id") Long id, HttpServletResponse response,
                      @CookieValue(value = "history", defaultValue = "") String history) {
        Bookmark bookmark = br.findById(id)

        if(bookmark) {
            List<String> idString = URLDecoder.decode(history, "UTF-8").split(",")
            List<Long> ids = new ArrayList()
            if(history) {
                idString.each { ids.add(it as Long) }
                ids.remove(id)
            }

            ids.add(id)
            history = URLEncoder.encode(ids.takeRight(WebConfiguration.PAGE_SIZE).join(","), "UTF-8")
            response.addCookie(new Cookie("history", history))
        }
    }
}
