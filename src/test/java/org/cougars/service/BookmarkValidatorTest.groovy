package org.cougars.service

import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

/**
 * Created by Dennis Rausch on 10/4/16.
 */

@SpringBootTest
class BookmarkValidatorTest extends Specification {

    def "ValidateUrl"() {
        def result
        when:
        result = BookmarkValidatorService.validateUrl("http://www.google.com")

        then:
        assert result
    }
}
