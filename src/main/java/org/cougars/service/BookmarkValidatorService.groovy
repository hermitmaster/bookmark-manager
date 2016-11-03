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

/**
 * Created by Dennis Rausch on 10/25/16.
 */

@Slf4j
@Service
class BookmarkValidatorService {
    @Autowired
    BookmarkRepository bookmarkRepository

    @Scheduled(cron = "0 0 0/12 * * *")
    void validateBookmarks() {
        Set<Bookmark> bookmarksToValidate = bookmarkRepository.findByLastValidatedBefore(new Date() - 7)
        log.info("Executing batch bookmark validation of ${bookmarksToValidate.size()} bookmarks on scheduled interval")
        validateUrls(bookmarksToValidate)
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
        } catch (IOException e) {
            log.error("Error validating url: ${url}!", e)
        }

        return valid
    }

    private void validateUrls(Set<Bookmark> bookmarks) {
        Date validationTimestamp = new Date()
        GParsPool.withPool {
            bookmarks.eachParallel {
                it.lastValidated = validationTimestamp

                if(!isValid(it.url)) {
                    it.lastModified = validationTimestamp
                    it.status = Status.DEAD
                }

                bookmarkRepository.save(it)
            }
        }
    }
}
