package org.hermitmaster.configuration

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.core.env.Profiles
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.util.matcher.AntPathRequestMatcher

import javax.sql.DataSource


/** A configuration class for configuring Spring Security.
 * Created by hermitmaster on 8/26/16.
 */

@Configuration
@EnableWebSecurity
class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    DataSource dataSource

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .antMatchers("/*", "/css/**", "/js/**", "/img/**", "/webjars/**", "/console/**").permitAll()
            .antMatchers("/h2-console/**").hasRole("ADMIN")
            .anyRequest().authenticated()
            .and().formLogin()
            .loginPage("/login").failureUrl("/login?error").permitAll()
            .and().logout()
            .logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/login?logout")
            .permitAll()
            .and().csrf().ignoringAntMatchers("/h2-console/**")
            .and().headers().frameOptions().sameOrigin()
    }

    /** Configures authentication source for Spring Security.
     *
     * @param auth AuthenticationManagerBuilder
     * @throws Exception    Any exception
     */
    @Autowired
    void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
            .dataSource(dataSource)
            .passwordEncoder(new BCryptPasswordEncoder())
    }
}
