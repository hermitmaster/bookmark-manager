package org.hermitmaster

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.scheduling.annotation.EnableScheduling
import org.thymeleaf.dialect.springdata.SpringDataDialect

@EnableScheduling
@SpringBootApplication
class QuickAccessBookmarkApplication {
    static void main(String[] args) {
        SpringApplication.run(QuickAccessBookmarkApplication.class, args)
    }

    // Registers a SpringDataDialect bean for Thymeleaf pagination in views.
    @Bean
    SpringDataDialect springDataDialect() {
        return new SpringDataDialect()
    }
}
