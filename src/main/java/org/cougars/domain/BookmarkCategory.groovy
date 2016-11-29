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
