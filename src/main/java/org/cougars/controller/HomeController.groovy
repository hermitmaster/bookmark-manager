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
import org.cougars.bean.BookmarkBean
import org.cougars.domain.Bookmark
import org.cougars.domain.BookmarkCategory
import org.cougars.domain.Status
import org.cougars.repository.BookmarkCategoryRepository
import org.cougars.repository.BookmarkRepository
import org.cougars.service.BookmarkValidatorService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.data.web.SortDefault
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam

import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse

/**
 * Created by Dennis Rausch on 8/26/16.
 */

@Slf4j
@Controller
public class HomeController {
    @Autowired
    private BookmarkRepository bookmarkRepository

    @Autowired
    private BookmarkCategoryRepository bookmarkCategoryRepository

    @Autowired
    private BookmarkValidatorService bookmarkValidatorService

    @GetMapping("")
    String index(@RequestParam(value ="view", required = false) String view,
                 @CookieValue(value = "view", defaultValue = "table") String cookie,
                 @SortDefault("dateCreated") Pageable pageable,
                 Model model, HttpServletResponse response) {
        String returnPath = view ?: cookie

        if(returnPath == "category") {
            model.addAttribute("page", bookmarkCategoryRepository.findAll())
        } else {
            model.addAttribute("statusList", Status.values() as List<String>)
            model.addAttribute("page", bookmarkRepository.findByStatus(Status.ACTIVE, pageable))
        }

        response.addCookie(new Cookie("view", returnPath))

        return returnPath
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
    String addBookmarkSubmission(@ModelAttribute BookmarkBean bookmarkBean) {
        BookmarkCategory bookmarkCategory = bookmarkCategoryRepository.findByName(bookmarkBean.bookmarkCategory)
        if(!bookmarkCategory) {
            bookmarkCategory = new BookmarkCategory()
            bookmarkCategory.name = bookmarkBean.bookmarkCategory
            bookmarkCategory.parent = bookmarkCategoryRepository.findById(1)
            bookmarkCategoryRepository.save(bookmarkCategory)
        }
        BookmarkCategory subcategory
        if(bookmarkBean.subcategory && !bookmarkBean.subcategory.equalsIgnoreCase("none")) {
            subcategory = bookmarkCategoryRepository.findByName(bookmarkBean.subcategory)
            if(!subcategory) {
                subcategory = new BookmarkCategory()
                subcategory.name = bookmarkBean.subcategory
                subcategory.parent = bookmarkCategory
                bookmarkCategoryRepository.save(subcategory)
            }
        } else {
            subcategory = bookmarkCategoryRepository.findById(1)
        }
        Bookmark bookmark = new Bookmark()
        bookmark.url = bookmarkBean.url
        bookmark.name = bookmarkBean.name
        bookmark.description = bookmarkBean.description
        bookmark.bookmarkCategory = bookmarkCategory
        bookmark.subcategory = subcategory

        bookmarkRepository.save(bookmark)
        bookmarkValidatorService.validateUrl(bookmark)

        return "addBookmarkSuccess"
    }

    @GetMapping("/bookmark-details")
    String getBookmarkDetails(@RequestParam(value = "id") Long id, Model model) {
        model.addAttribute("bookmark", bookmarkRepository.findById(id))

        return "fragments/bookmarkDetail :: bookmarkDetail"
    }

    @PostMapping("/search")
    String search(Model model) {

    }
}
