package org.cougars.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

/**
 * Created by Dennis Rausch on 11/2/16.
 */

@SpringBootTest
@ActiveProfiles("test")
class BookmarkIOServiceTest extends Specification {
    @Autowired
    BookmarkIOService bookmarkIOService

    def "ImportBookmarksFail"() {
        //TODO: Write test case
        def result

        when:
        result = false

        then:
        assert !result
    }

    def "ImportBookmarksSuccess"() {
        //TODO: Write test case
        def result

        when:
        result = true

        then:
        assert result
    }

    def "ExportBookmarksFail"() {
        //TODO: Write test case
        def result

        when:
        result = false

        then:
        assert !result
    }

    def "ExportBookmarksSuccess"() {
        //TODO: Write test case
        def result

        when:
        result = true

        then:
        assert result
    }
}
