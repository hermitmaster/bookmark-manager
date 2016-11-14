package org.cougars

import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.PageRequest
import org.springframework.data.web.PageableHandlerMethodArgumentResolver
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter

/**
 * Created by Dennis Rausch on 10/29/16.
 */

@Configuration
class WebConfiguration extends WebMvcConfigurerAdapter {
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        PageableHandlerMethodArgumentResolver resolver = new PageableHandlerMethodArgumentResolver()
        resolver.setFallbackPageable(new PageRequest(0, 25))
        argumentResolvers.add(resolver)
    }
}
