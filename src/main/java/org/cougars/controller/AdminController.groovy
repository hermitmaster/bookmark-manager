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
import org.cougars.bean.UserBean
import org.cougars.domain.Bookmark
import org.cougars.domain.Status
import org.cougars.domain.User
import org.cougars.repository.BookmarkRepository
import org.cougars.repository.UserRepository
import org.cougars.service.BookmarkIOService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.data.web.SortDefault
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile

import javax.servlet.http.HttpServletResponse

/**
 * Created by Dennis Rausch on 10/3/16.
 */

@Slf4j
@Controller
@RequestMapping("/admin")
class AdminController {
    @Autowired
    private BookmarkRepository bookmarkRepository

    @Autowired
    private UserRepository userRepository

    @Autowired
    BookmarkIOService bookmarkIOService

    @GetMapping("/review-bookmarks")
    String reviewBookmark(Model model) {
        model.addAttribute("bookmarks", bookmarkRepository.findAll())

        return "admin/reviewBookmarks"
    }

    @PostMapping("/review-bookmarks")
    String reviewBookmarkSubmission(@ModelAttribute Set<Bookmark> bookmarks) {
        return "admin/reviewBookmarks"
    }

    @GetMapping("/dead-link-report")
    String deadLinkReport(@SortDefault("id") Pageable pageable, Model model) {
        model.addAttribute("page", bookmarkRepository.findByStatus(Status.DEAD, pageable))

        return "admin/deadLinkReport"
    }

    @GetMapping("/users")
    String users(Model model) {
        model.addAttribute("userBean", new UserBean())
        model.addAttribute("users", userRepository.findAll())

        return "admin/users"
    }

    @PostMapping("/add-user")
    String addUser(@ModelAttribute UserBean userBean) {
        userRepository.save(new User(userBean.username, userBean.password))

        return "redirect:/admin/users"
    }

    @PostMapping("/update-user")
    String updateUser(@ModelAttribute Set<User> users) {
        return "admin/manageUser"
    }

    @PostMapping("/edit-bookmark")
    String editBookmarks(@ModelAttribute Set<Bookmark> bookmarks) {
        bookmarkRepository.save(bookmarks)

        return "table"
    }

    @GetMapping("/data-management")
    String dataManagement() {
        return "admin/dataManagement"
    }

    @GetMapping("/bookmark-export")
    void bookmarkExport(HttpServletResponse response) {
        //TODO: Finish implementation
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
        response.setHeader("content-disposition", "filename=bookmark_export_${new Date().format("MM_dd_yyyy")}.xlsx")
        bookmarkIOService.exportBookmarks(response.outputStream)
        response.flushBuffer()
    }

    @PostMapping("/bookmark-import")
    String bookmarkImport(@RequestParam("file") MultipartFile file) {
        //TODO: Finish implementation
        bookmarkIOService.importBookmarks(file)

        return "admin/dataManagement"
    }
}
