package org.cougars

import javax.validation.constraints.NotNull

/**
 * Created by Dennis Rausch on 10/16/16.
 */
class BookmarkBean {
    @NotNull
    String url

    String bookmarkCategory

    String subcategory

    String name

    String description
}
