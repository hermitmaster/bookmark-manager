package org.hermitmaster.controller

import groovy.util.logging.Slf4j
import org.hermitmaster.bean.BookmarkBean
import org.hermitmaster.bean.UserBean
import org.hermitmaster.domain.model.Bookmark
import org.hermitmaster.domain.model.BookmarkCategory
import org.hermitmaster.domain.model.User
import org.hermitmaster.domain.repository.BookmarkCategoryRepository
import org.hermitmaster.domain.repository.BookmarkRepository
import org.hermitmaster.domain.repository.UserRepository
import org.hermitmaster.service.BookmarkIOService
import org.hermitmaster.service.BookmarkValidatorService
import org.hermitmaster.utility.Status
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.data.web.SortDefault
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile

import javax.servlet.http.HttpServletResponse
import javax.validation.Valid

/** Controller class for mapping administrative functions.
 * Created by hermitmaster on 10/3/16.
 */

@Slf4j
@Controller
@RequestMapping("/admin")
class AdminController {
    @Autowired
    private UserRepository ur
    @Autowired
    private BookmarkRepository br
    @Autowired
    private BookmarkCategoryRepository bcr
    @Autowired
    private BookmarkIOService bios
    @Autowired
    private BookmarkValidatorService bvs

    @GetMapping("/edit-bookmark")
    String editBookmark(@RequestParam("id") Long bookmarkId, Model model) {
        Bookmark bookmark = br.getOne(bookmarkId)
        BookmarkBean bean = new BookmarkBean(bookmark)
        model.addAttribute("bookmarkBean", bean)

        return "editBookmark"
    }

    @PostMapping("/edit-bookmark")
    String addBookmarkSubmission(BookmarkBean bean) {
        try {
            Bookmark bookmark = br.getOne(bean.id)
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication()
            User user = ur.findByUsername(authentication.name)
            BookmarkCategory category = bcr.findByName(bean.bookmarkCategory.trim()) ?:
                new BookmarkCategory(bean.bookmarkCategory.trim(), bcr.findByName("None"), user)

            BookmarkCategory subcategory = bcr.findByName("None")
            if (!bean?.subcategory?.equalsIgnoreCase("None")) {
                subcategory = bcr.findByName(bean.subcategory.trim()) ?:
                    new BookmarkCategory(bean.subcategory.trim(), category, user)
            }

            bookmark.url = bean.url.trim()
            bookmark.name = bean.name.trim()
            bookmark.description = bean.description
            bookmark.bookmarkCategory = category
            bookmark.subcategory = subcategory
            bookmark.dateModified = new Date()
            bookmark.status = Status.ACTIVE

            br.save(bookmark)
            bvs.validateUrl(bookmark)
        } catch (Exception e) {
            view = "error"
            log.error("Error adding bookmark!", e)
        }

        return "editBookmarkSuccess"
    }

    @GetMapping("/review-bookmarks")
    String reviewBookmark(Model model, Pageable pageable) {
        model.addAttribute("page", br.findByStatus(Status.IN_REVIEW, pageable))

        return "table"
    }

    @PostMapping("/review-bookmarks")
    String reviewBookmarkSubmission(@ModelAttribute Set<Bookmark> bookmarks) {
        return "table"
    }

    @GetMapping("/dead-link-report")
    String deadLinkReport(@SortDefault("id") Pageable pageable, Model model) {
        model.addAttribute("page", br.findByStatus(Status.DEAD, pageable))

        return "table"
    }

    @GetMapping("/users")
    String users(Model model) {
        model.addAttribute("userBean", new UserBean())
        model.addAttribute("users", ur.findByEnabledTrue())

        return "users"
    }

    @PostMapping("/add-user")
    String addUser(@Valid UserBean bean, BindingResult bindingResult, Model model) {
        String view

        if (bindingResult.hasErrors()) {
            model.addAttribute("users", ur.findByEnabledTrue())
            view = "/users"
        } else {
            ur.save(new User(bean.username, bean.password))
            view = "redirect:/admin/users"
        }


        return view
    }

    @PostMapping("/edit-user")
    String editUser(@ModelAttribute Set<User> users) {
        throw new RuntimeException("Not yet implemented!")
    }

    @GetMapping("/delete-user")
    String deleteUser(@RequestParam("username") String username) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication()
        User user = ur.findByUsername(username)
        if (user.username != authentication.name) {
            user.enabled = false
            ur.save(user)
        }

        return "redirect:/admin/users"
    }

    @GetMapping("/bookmark-export")
    void bookmarkExport(HttpServletResponse response) {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
        response.setHeader("content-disposition", "filename=bookmark_export_${new Date().format("MM_dd_yyyy")}.xlsx")
        bios.exportBookmarks(response.outputStream)
        response.flushBuffer()
    }

    @PostMapping("/bookmark-import")
    String bookmarkImport(@RequestParam("file") MultipartFile file) {
        bios.importBookmarks(file, false)

        return "importSuccess"
    }

    @GetMapping("/data-management")
    String dataManagement() {
        return "dataManagement"
    }

    @GetMapping("/approve-bookmark")
    String approveBookmark(@RequestParam("id") Long id) {
        Bookmark bookmark = br.getOne(id)
        bookmark.status = Status.ACTIVE
        br.save(bookmark)

        return "redirect:/admin/review-bookmarks"
    }

    @GetMapping("/approve-all-bookmarks")
    String approveAllBookmarks() {
        Set<Bookmark> bookmarks = br.findByStatus(Status.IN_REVIEW)
        bookmarks.each { it.status = Status.ACTIVE }
        br.save(bookmarks)

        return "redirect:/admin/review-bookmarks"
    }

    /** Delete a single bookmark given the id
     *
     * @param id id of the bookmark to delete
     * @return view to return
     */
    @GetMapping("/delete-bookmark")
    String deleteBookmark(@RequestParam("id") Long id) {
        br.delete(id)

        return "redirect:/?view=table"
    }

    /** Delete all dead bookmarks
     *
     * @return view to return
     */
    @GetMapping("/delete-dead-bookmarks")
    String deleteDeadBookmarks() {
        Set<Bookmark> deadBookmarks = br.findByStatus(Status.DEAD)
        deadBookmarks.each { br.delete(it.id) }

        return "redirect:/admin/dead-link-report"
    }
}
