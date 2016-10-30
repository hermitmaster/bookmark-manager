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

import org.cougars.domain.Status
import org.cougars.repository.BookmarkCategoryRepository
import org.cougars.repository.BookmarkRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.data.web.SortDefault
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping

/**
 * Created by Dennis Rausch on 10/4/16.
 */

@Controller
@RequestMapping("/bookmarks")
class BookmarkSearchController {
    @Autowired
    private BookmarkRepository bookmarkRepository

    @Autowired
    private BookmarkCategoryRepository bookmarkCategoryRepository

    @PostMapping("/search-table")
    String tableView(@SortDefault("id") Pageable pageable, Model model) {
        model.addAttribute("bookmarks", bookmarkRepository.findByStatus(Status.ACTIVE, pageable))

        return "table"
    }

    @PostMapping("/search-category")
    String searchBookmarksCategoryView(@SortDefault("id") Pageable pageable, Model model) {
        model.addAttribute("categories", bookmarkCategoryRepository.findAll(pageable))

        return "category"
    }

    @PostMapping("/search-tree")
    String searchBookmarksSplayTreeView(@SortDefault("id") Pageable pageable, Model model) {
        model.addAttribute("bookmarks", bookmarkRepository.findByStatus(Status.ACTIVE, pageable))

        return "tree"
    }
}
