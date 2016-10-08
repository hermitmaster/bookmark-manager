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
