package org.cougars.controller

import groovy.util.logging.Slf4j
import org.cougars.bean.BookmarkBean
import org.cougars.bean.UserBean
import org.cougars.domain.Bookmark
import org.cougars.domain.BookmarkCategory
import org.cougars.domain.Status
import org.cougars.domain.User
import org.cougars.repository.BookmarkCategoryRepository
import org.cougars.repository.BookmarkRepository
import org.cougars.repository.UserRepository
import org.cougars.service.BookmarkIOService
import org.cougars.service.BookmarkValidatorService
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
 * Created by Dennis Rausch on 10/3/16.
 */

@Slf4j
@Controller
@RequestMapping("/admin")
class AdminController {
    @Autowired private UserRepository ur
    @Autowired private BookmarkRepository br
    @Autowired private BookmarkCategoryRepository bcr
    @Autowired private BookmarkIOService bios
    @Autowired private BookmarkValidatorService bvs

    @GetMapping("/edit-bookmark")
    String editBookmark(@RequestParam("id") Long bookmarkId, Model model) {
        Bookmark bookmark = br.findById(bookmarkId)
        BookmarkBean bean = new BookmarkBean(bookmark)
        model.addAttribute("bookmarkBean", bean)

        return "editBookmark"
    }

    @PostMapping("/edit-bookmark")
    String addBookmarkSubmission(@Valid BookmarkBean bean, BindingResult bindingResult) {
        String view = "editBookmarkSuccess"
        if (bindingResult.hasErrors()) {
            view = "editBookmark"
        } else {
            try {
                Bookmark bookmark = br.findById(bean.id)
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication()
                User user = ur.findByUsername(authentication.name)
                BookmarkCategory category = bcr.findByName(bean.bookmarkCategory.trim())
                if(!category) {
                    category = new BookmarkCategory(bean.bookmarkCategory.trim(), bcr.findByName("None"), user)
                }

                BookmarkCategory subcategory = bcr.findByName("None")
                if(bean.subcategory && !bean.subcategory.equalsIgnoreCase("None")) {
                    subcategory = bcr.findByName(bean.subcategory.trim()) ?: new BookmarkCategory(bean.subcategory.trim(), category, user)
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
        }

        return view
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

        if(bindingResult.hasErrors()){
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
        if(user.username != authentication.name) {
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

    /** Delete a single bookmark given the id
     *
     * @param id    id of the bookmark to delete
     * @return      view to return
     */
    @GetMapping("/delete-bookmark")
    String deleteBookmark(@RequestParam("id") Long id) {
        br.delete(id)

        return "redirect:/?view=table"
    }

    @GetMapping("/approve-bookmark")
    String approveBookmark(@RequestParam("id") Long id) {
        Bookmark bookmark = br.findById(id)
        bookmark.status = Status.ACTIVE
        br.save(bookmark)

        return "redirect:/admin/review-bookmarks"
    }

    @GetMapping("/approve-all-bookmarks")
    String approveAllBookmarks() {
        Set<Bookmark> bookmarks = br.findByStatus(Status.IN_REVIEW)
        bookmarks.each { it.status = Status.ACTIVE}
        br.save(bookmarks)

        return "redirect:/admin/review-bookmarks"
    }

    /** Delete all dead bookmarks
     *
     * @return  view to return
     */
    @GetMapping("/delete-dead-bookmarks")
    String deleteDeadBookmarks() {
        Set<Bookmark> deadBookmarks = br.findByStatus(Status.DEAD)
        deadBookmarks.each { br.delete(it.id) }

        return "redirect:/admin/dead-link-report"
    }
}
