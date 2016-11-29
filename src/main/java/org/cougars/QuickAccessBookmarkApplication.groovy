package org.cougars

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.web.support.SpringBootServletInitializer
import org.springframework.context.annotation.Bean
import org.springframework.scheduling.annotation.EnableScheduling
import org.thymeleaf.dialect.springdata.SpringDataDialect

/** Spring Boot default application class. This is the class that contains the
 * main method for the application.
 */

@EnableScheduling
@SpringBootApplication
public class QuickAccessBookmarkApplication extends SpringBootServletInitializer {

	/** Main method to begin execution.
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(QuickAccessBookmarkApplication.class, args)
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(QuickAccessBookmarkApplication.class)
	}

	/** Registers a SpringDataDialect bean used for Thymeleaf pagination
	 * in views.
	 * @return	SpringDataDialect
	 */
	@Bean
	public SpringDataDialect springDataDialect() {
		return new SpringDataDialect()
	}
}
