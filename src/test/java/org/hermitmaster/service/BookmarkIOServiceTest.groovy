package org.hermitmaster.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

/**
 * Created by hermitmaster on 11/2/16.
 */

@SpringBootTest
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
