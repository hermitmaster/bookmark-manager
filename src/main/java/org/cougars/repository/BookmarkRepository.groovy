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

package org.cougars.repository

import org.cougars.domain.Bookmark
import org.cougars.domain.BookmarkCategory
import org.cougars.domain.Status
import org.springframework.data.jpa.repository.JpaRepository

/**
 * Created by Dennis Rausch on 10/3/16.
 */
interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    /** Find a bookmark by its id (primary key).
     *
     * @param id    Id of the bookmark being searched for.
     * @return      Bookmark with the referenced id.
     */
    Bookmark findById(Long id)

    /** Find a bookmark based on its primary category.
     *
     * @param bookmarkCategory  The category of bookmarks being searched for.
     * @return                  Collection of all books with the referenced category.
     */
    Set<Bookmark> findByBookmarkCategory(BookmarkCategory bookmarkCategory)

    /** Find a bookmark based on its current status.
     *
     * @param status    Status of bookmarks being searched for.
     * @return          Collection of all books with the referenced status.
     */
    Set<Bookmark> findByStatus(Status status)

    /**
     *
     * @param lastValidated
     * @return
     */
    Set<Bookmark> findByLastValidatedBefore(Date lastValidated)
}