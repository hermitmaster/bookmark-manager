package org.hermitmaster.bean


import org.hermitmaster.domain.model.Bookmark
import org.hermitmaster.validation.constraint.UniqueURL
import org.hibernate.validator.constraints.Length
import org.hibernate.validator.constraints.URL


/** A bean for holding "Add Bookmark" form data.
 * Created by hermitmaster on 10/16/16.
 */

class BookmarkBean {
    Long id

    @URL(message = "Provided URL is not a valid format!")
    @Length(min = 8, max = 1000, message = "Provided URL is not a valid format!")
    @UniqueURL(message = "URL already exists. Please contact an administrator to edit the existing entry.")
    String url

    @Length(max = 255, message = "Max length is 255 characters!")
    String bookmarkCategory

    @Length(max = 255, message = "Max length is 255 characters!")
    String subcategory

    @Length(max = 255, message = "Max length is 255 characters!")
    String name

    @Length(max = 2000, message = "Max length is 2000 characters!")
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
