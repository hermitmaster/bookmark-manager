package org.hermitmaster.domain.model

import org.hermitmaster.utility.Status

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
 * Created by hermitmaster on 10/3/16.
 */

@Entity
@Table(name = "bookmark")
class Bookmark {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    Date lastValidated = new Date()

    @ManyToOne(cascade = CascadeType.PERSIST, optional = false)
    BookmarkCategory bookmarkCategory

    @ManyToOne(cascade = CascadeType.PERSIST, optional = false)
    BookmarkCategory subcategory

    @Enumerated(EnumType.STRING)
    Status status = Status.IN_REVIEW

    @ManyToOne
    User createdBy
}
