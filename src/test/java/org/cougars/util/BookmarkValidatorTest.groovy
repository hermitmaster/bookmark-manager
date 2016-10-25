package org.cougars.util

import org.cougars.service.BookmarkValidatorService
import spock.lang.Specification

/**
 * Created by Dennis Rausch on 10/4/16.
 */

//@SpringBootTest
class BookmarkValidatorTest extends Specification {

    def "ValidateUrlSuccess"() {
        def result
        when:
        result = BookmarkValidatorService.isValid("http://www.google.com")

        then:
        assert result
    }

    def "ValidateUrlFail"() {
        def result
        when:
        result = BookmarkValidatorService.isValid("http://google.com/404url")

        then:
        assert !result
    }
}
