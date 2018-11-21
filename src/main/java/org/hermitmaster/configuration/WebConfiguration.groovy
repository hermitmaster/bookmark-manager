package org.hermitmaster.configuration

import org.h2.server.web.WebServlet
import org.springframework.boot.web.servlet.ServletRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.PageRequest
import org.springframework.data.web.PageableHandlerMethodArgumentResolver
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter

/** A configuration class for setting web configuration properties.
 * Created by hermitmaster on 10/29/16.
 */

@Configuration
class WebConfiguration implements WebMvcConfigurer {
    static final int PAGE_SIZE = 25
    /** Sets the default start page and page size for paginated data
     *
     * @param argumentResolvers The default PageableArgumentResolver
     */
    @Override
    void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        PageableHandlerMethodArgumentResolver resolver = new PageableHandlerMethodArgumentResolver()
        resolver.setFallbackPageable(new PageRequest(0, PAGE_SIZE))
        argumentResolvers.add(resolver)
    }
}
