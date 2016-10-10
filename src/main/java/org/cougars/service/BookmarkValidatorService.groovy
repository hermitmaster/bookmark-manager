package org.cougars.service

import groovy.util.logging.Slf4j
import org.apache.http.HttpResponse
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClientBuilder


/**
 * Created by Dennis Rausch on 10/4/16.
 */

@Slf4j
class BookmarkValidatorService {
    /** Verifies that a url returns a valid status code (less than 400).
     *
     * @param url   url to be validated
     * @return      true if status code < 400, else false
     */
    static Boolean validateUrl(String url) {
        Boolean valid = false
        HttpClient client = HttpClientBuilder.create().build()
        HttpGet request = new HttpGet(url)

        try {
            HttpResponse response = client.execute(request)
            valid = response.getStatusLine().statusCode < 400
        } catch (IOException e) {
            log.error("Error validating url: ${url}!")
        }

        return valid
    }
}
