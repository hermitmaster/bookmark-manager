package org.hermitmaster.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

/**
 * Created by hermitmaster on 10/4/16.
 */

@SpringBootTest
class BookmarkValidatorServiceTest extends Specification {
    @Autowired
    BookmarkValidatorService bookmarkValidatorService

    def "ValidateUrlSuccess"() {
        def result
        when:
        result = bookmarkValidatorService.isValid("http://www.google.com")

        then:
        assert result
    }

    def "ValidateUrlFail"() {
        def result
        when:
        result = bookmarkValidatorService.isValid("http://google.com/404url")

        then:
        assert !result
    }
}
