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

package org.cougars.domain


import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.Table

/** Represents a bookmark in the system.
 * Created by Dennis Rausch on 10/3/16.
 */

@Entity
@Table(name = "bookmark")
class Bookmark {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    Long id

    @Column(name = "url", nullable = false, unique = true, length = 2000)
    String url

    @Column(name = "name")
    String name

    @Column(name = "description", length = 4000)
    String description

    @Column(name = "dateCreated", nullable = false, updatable = false)
    Date dateCreated = new Date()

    @Column(name = "dateModified", nullable = false)
    Date dateModified = new Date()

    @Column(name = "lastValidated", nullable = false)
    Date lastValidated = new Date() - 7

    @ManyToOne(cascade = CascadeType.PERSIST, optional = false)
    BookmarkCategory bookmarkCategory

    @ManyToOne(cascade = CascadeType.PERSIST, optional = false)
    BookmarkCategory subcategory

    @Enumerated(EnumType.STRING)
    Status status = Status.IN_REVIEW

    @ManyToOne
    User createdBy

    Bookmark() {
        super()
    }

    Bookmark(String url, String name, String description, BookmarkCategory bookmarkCategory, BookmarkCategory subcategory, User createdBy) {
        this.url = url
        this.name = name ?: url
        this.description = description
        this.bookmarkCategory = bookmarkCategory
        this.subcategory = subcategory
        this.createdBy = createdBy
    }
}
