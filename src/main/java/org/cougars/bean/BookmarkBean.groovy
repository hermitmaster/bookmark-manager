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

    @Size(max = 255, message = "Max length is 255 characters!")
    String bookmarkCategory

    @Size(max = 255, message = "Max length is 255 characters!")
    String subcategory

    @Size(max = 255, message = "Max length is 255 characters!")
    String name

    @Size(max = 2000, message = "Max length is 2000 characters!")
    String description
}
