package org.cougars

import org.h2.server.web.WebServlet
import org.springframework.boot.web.servlet.ServletRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.PageRequest
import org.springframework.data.web.PageableHandlerMethodArgumentResolver
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter

/** A configuration class for setting web configuration properties.
 * Created by Dennis Rausch on 10/29/16.
 */

@Configuration
class WebConfiguration extends WebMvcConfigurerAdapter {
    static final int PAGE_SIZE = 25
    /** Sets the default start page and page size for paginated data
     *
     * @param argumentResolvers The default PageableArgumentResolver
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        PageableHandlerMethodArgumentResolver resolver = new PageableHandlerMethodArgumentResolver()
        resolver.setFallbackPageable(new PageRequest(0, PAGE_SIZE))
        argumentResolvers.add(resolver)
    }

    /** This is used solely for development. It maps the h2 database console so it can be accessed by
     * a developer.
     * @return  ServletRegistrationBean to register servlet for h2 console
     */
    @Bean
    ServletRegistrationBean h2servletRegistration(){
        ServletRegistrationBean registrationBean = new ServletRegistrationBean( new WebServlet())
        registrationBean.addUrlMappings("/console/*")
        return registrationBean
    }
}
