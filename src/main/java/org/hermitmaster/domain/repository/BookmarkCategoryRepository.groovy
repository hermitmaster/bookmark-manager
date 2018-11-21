package org.hermitmaster.domain.repository

import org.hermitmaster.domain.model.BookmarkCategory
import org.springframework.data.jpa.repository.JpaRepository

/** A JPA repository for accessing BookmarkCategory data
 * Created by hermitmaster on 10/3/16.
 */

interface BookmarkCategoryRepository extends JpaRepository<BookmarkCategory, Long> {
    /**
     *
     * @param name Name of the BookmarkCategory being searched for.
     * @return BookmarkCategory with the referenced name.
     */
    BookmarkCategory findByName(String name)
}
