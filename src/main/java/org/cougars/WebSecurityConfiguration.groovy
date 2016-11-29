package org.cougars

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.util.matcher.AntPathRequestMatcher

import javax.sql.DataSource


/** A configuration class for configuring Spring Security.
 * Created by Dennis Rausch on 8/26/16.
 */

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired DataSource dataSource
    @Autowired Environment env

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .antMatchers("/*", "/css/**", "/js/**", "/img/**", "/console/**").permitAll()
            .anyRequest().authenticated()
            .and()
            .formLogin()
            .loginPage("/login").failureUrl("/login?error").permitAll()
            .and()
            .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/login?logout")
            .permitAll()

        // Disables some Spring Security features on "dev" spring profile to allow h2 console to work
        if (env.acceptsProfiles("dev")) {
            http.csrf().disable()
            http.headers().frameOptions().disable()
        }
    }

    /** Configures authentication source for Spring Security.
     *
     * @param auth          AuthenticationManagerBuilder
     * @throws Exception    Any exception
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
            .dataSource(dataSource)
            .passwordEncoder(new BCryptPasswordEncoder())
    }
}
