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

import org.cougars.domain.Bookmark
import org.cougars.repository.BookmarkCategoryRepository
import org.cougars.repository.BookmarkRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping

/**
 * Created by Dennis Rausch on 8/26/16.
 */

@Controller
@RequestMapping("/")
class HomeController {
    @Autowired
    BookmarkRepository bookmarkRepository

    @Autowired
    BookmarkCategoryRepository bookmarkCategoryRepository

    /** RequestMapping for displaying the home page.
     *
     * @return index
     */
    @GetMapping("")
    String index() {
        return "index"
    }

    /** RequestMapping for displaying the add bookmark page.
     *
     * @param model Data model returned to view.
     * @return addBookmark
     */
    @GetMapping("/add-bookmark")
    String addBookmark(Model model) {
        model.addAttribute("bookmark", new Bookmark())

        return "addBookmark"
    }

    /** RequestMapping for adding a new bookmark.
     *
     * @param bookmark
     * @return addBookmark
     */
    @PostMapping("/add-bookmark")
    String addBookmarkSubmission(@ModelAttribute Bookmark bookmark) {
        bookmarkRepository.save(bookmark)

        return "addBookmark"
    }
}
