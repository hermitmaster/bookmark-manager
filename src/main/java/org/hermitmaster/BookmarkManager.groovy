package org.hermitmaster

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@SpringBootApplication
class BookmarkManager {
    static void main(String[] args) {
        SpringApplication.run(BookmarkManager.class, args)
    }
}
