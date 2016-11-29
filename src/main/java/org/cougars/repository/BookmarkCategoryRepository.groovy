package org.cougars.repository

import org.cougars.domain.BookmarkCategory
import org.springframework.data.jpa.repository.JpaRepository

/** A JPA repository for accessing BookmarkCategory data
 * Created by Dennis Rausch on 10/3/16.
 */

interface BookmarkCategoryRepository extends JpaRepository<BookmarkCategory, Long> {
    /** Find a BookmarkCategory by its id (primary key).
     *
     * @param id    Id of the BookmarkCategory being searched for.
     * @return      BookmarkCategory with the referenced id.
     */
    BookmarkCategory findById(Long id)

    /**
     *
     * @param name  Name of the BookmarkCategory being searched for.
     * @return      BookmarkCategory with the referenced name.
     */
    BookmarkCategory findByName(String name)
}