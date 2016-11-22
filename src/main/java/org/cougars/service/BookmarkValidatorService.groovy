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

package org.cougars.service

import groovy.util.logging.Slf4j
import groovyx.gpars.GParsPool
import org.apache.http.HttpResponse
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClientBuilder
import org.cougars.domain.Bookmark
import org.cougars.domain.Status
import org.cougars.repository.BookmarkRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

/** A service class for validating bookmarks based on http response.
 * Created by Dennis Rausch on 10/25/16.
 */

@Slf4j
@Service
class BookmarkValidatorService {
    @Autowired private BookmarkRepository br

    /** Validates bookmarks that have expired validation based on
     * the provided cron expression.
     */
    @Scheduled(cron = "0 0 */3 * * *")
    void validateBookmarks() {
        Set<Bookmark> bookmarks = br.findByLastValidatedBefore(new Date() - 1)
        log.info("Starting batch bookmark validation on scheduled interval")
        validateBookmarks(bookmarks)
    }

    /** Validates a collection of bookmarks concurrently.
     *
     * @param bookmarks     Collection of bookmarks to validate
     */
    void validateBookmarks(Set<Bookmark> bookmarks) {
        log.info("Validating ${bookmarks.size()} bookmarks")
        GParsPool.withPool {
            bookmarks.eachParallel { Bookmark bookmark -> validateUrl(bookmark) }
        }
    }

    /** Validates a single bookmark and updates the record.
     *
     * @param bookmark  the bookmark to validate
     */
    void validateUrl(Bookmark bookmark) {
        Date validationTimestamp = new Date()
        bookmark.lastValidated = validationTimestamp

        if(!isValid(bookmark.url)) {
            bookmark.dateModified = validationTimestamp
            bookmark.status = Status.DEAD
        }

        br.save(bookmark)
    }

    /** Verifies that a url returns a valid status code (less than 400).
     *
     * @param url   url to be validated
     * @return      true if status code < 400, else false
     */
    static Boolean isValid(String url) {
        Boolean valid = false
        HttpClient client = HttpClientBuilder.create().build()
        HttpGet request = new HttpGet(url)

        try {
            HttpResponse response = client.execute(request)
            valid = response.getStatusLine().statusCode < 400
        } catch (IOException ignore) {
            log.error("Error validating url: ${url}!")
        }

        return valid
    }
}
