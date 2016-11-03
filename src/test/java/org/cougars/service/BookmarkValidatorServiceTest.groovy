package org.cougars.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

/**
 * Created by Dennis Rausch on 10/4/16.
 */

@SpringBootTest
@ActiveProfiles("test")
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
