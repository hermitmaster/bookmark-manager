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
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.OneToMany

/** Represents a category and/or subcategory of bookmarks.
 * Created by Dennis Rausch on 10/3/16.
 */

@Entity
class BookmarkCategory {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    Long id

    @Column(nullable = false, unique = true)
    String name

    @ManyToOne
    BookmarkCategory parent

    @OneToMany(mappedBy="parent")
    Set<BookmarkCategory> children

    @OneToMany(mappedBy = "bookmarkCategory", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    Set<Bookmark> bookmarks

    @ManyToOne
    User createdBy

    @Column(nullable = false, updatable = false)
    Date dateCreated = new Date()

    @Column(nullable = false)
    Date dateModified = new Date()

    BookmarkCategory() {
        super()
    }

    BookmarkCategory(String name, BookmarkCategory parent, User createdBy) {
        this.name = name
        this.parent = parent
        this.createdBy = createdBy
    }
}
