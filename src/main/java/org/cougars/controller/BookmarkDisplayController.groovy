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

import org.cougars.repository.BookmarkRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

/**
 * Created by Dennis Rausch on 10/4/16.
 */

@Controller
@RequestMapping("/bookmarks")
class BookmarkDisplayController {
    @Autowired
    BookmarkRepository bookmarkRepository

    /**
     *
     * @param model
     * @return
     */
    @GetMapping("/table")
    String tableView(Model model) {
        model.addAttribute("bookmarks", bookmarkRepository.findAll())

        return "tableView"
    }

    /**
     *
     * @param model
     * @return
     */
    @GetMapping("/category")
    String categoryView(Model model) {
        return "categoryView"
    }

    /**
     *
     * @param model
     * @return
     */
    @GetMapping("/splay-tree")
    String splayTreeView(Model model) {
        return "splayTreeView"
    }
}
