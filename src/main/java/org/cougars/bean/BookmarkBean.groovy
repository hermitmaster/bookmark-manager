package org.cougars.bean

import org.hibernate.validator.constraints.URL

import javax.validation.constraints.Size

/**
 * Created by Dennis Rausch on 10/16/16.
 */
class BookmarkBean {
    @URL(message = "Provided URL is not a valid format!")
    @Size(min = 8, max = 1000, message = "Provided URL is not a valid format!")
    String url

    String bookmarkCategory

    String subcategory

    String name

    String description
}
