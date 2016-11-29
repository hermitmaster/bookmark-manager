package org.cougars.repository

import org.cougars.domain.Bookmark
import org.cougars.domain.BookmarkCategory
import org.cougars.domain.Status
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

/** A JPA repository for accessing Bookmark data
 * Created by Dennis Rausch on 10/3/16.
 */

interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    /** Searches relevant fields for the passed query
     *
     * @param query     String query
     * @param pageable  Pageable object for pagination
     * @return          Page of bookmarks that matched the query
     */
    @Query("select b from Bookmark b where lower(url) like lower(concat('%', ?1, '%')) or lower(name) like lower(concat('%', ?1, '%')) or lower(description) like lower(concat('%', ?1, '%'))")
    Page<Bookmark> search(String query, Pageable pageable)

    /** Find a bookmark by its id (primary key).
     *
     * @param id    Id of the bookmark being searched for.
     * @return      Bookmark with the referenced id.
     */
    Bookmark findById(Long id)

    @Query("select b from Bookmark b where id in ?1")
    Page<Bookmark> findByIds(List<Long> ids, Pageable pageable)

    /** Find a bookmark by its url.
     *
     * @param url   url of the bookmark being searched for.
     * @return      Bookmark with the referenced id.
     */
    Bookmark findByUrl(String url)

    /** Find a bookmark based on its primary category.
     *
     * @param bookmarkCategory  The category of bookmarks being searched for.
     * @return                  Collection of all books with the referenced category.
     */
    Set<Bookmark> findByBookmarkCategory(BookmarkCategory bookmarkCategory)

    /** Find a bookmark based on its primary category.
     *
     * @param bookmarkCategory  The category of bookmarks being searched for.
     * @param pageable          Pageable object for pagination
     * @return                  Page of all books with the referenced category.
     */
    Page<Bookmark> findByBookmarkCategory(BookmarkCategory bookmarkCategory, Pageable pageable)

    /** Find a bookmark based on its current status.
     *
     * @param status    Status of bookmarks being searched for.
     * @return          Collection of all books with the referenced status.
     */
    Set<Bookmark> findByStatus(Status status)

    /** Find a bookmark based on its current status.
     *
     * @param status    Status of bookmarks being searched for.
     * @param pageable  Pageable object for pagination
     * @return          Page of all books with the referenced status.
     */
    Page<Bookmark> findByStatus(Status status, Pageable pageable)

    /**
     *
     * @param lastValidated
     * @return
     */
    Set<Bookmark> findByLastValidatedBefore(Date lastValidated)
}