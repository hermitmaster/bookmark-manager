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

package org.cougars.bean

import org.cougars.domain.Bookmark
import org.hibernate.validator.constraints.URL

import javax.validation.constraints.Size

/** A bean for holding "Add Bookmark" form data.
 * Created by Dennis Rausch on 10/16/16.
 */
class BookmarkBean {
    Long id
    @URL(message = "Provided URL is not a valid format!")
    @Size(min = 8, max = 1000, message = "Provided URL is not a valid format!")
    String url

    @Size(max = 255, message = "Max length is 255 characters!")
    String bookmarkCategory

    @Size(max = 255, message = "Max length is 255 characters!")
    String subcategory

    @Size(max = 255, message = "Max length is 255 characters!")
    String name

    @Size(max = 2000, message = "Max length is 2000 characters!")
    String description

    BookmarkBean() {

    }

    BookmarkBean(Bookmark bookmark) {
        this.id = bookmark.id
        this.url = bookmark.url
        this.bookmarkCategory = bookmark.bookmarkCategory.name
        this.subcategory = bookmark.subcategory.name
        this.name = bookmark.name
        this.description = bookmark.description
    }
}
