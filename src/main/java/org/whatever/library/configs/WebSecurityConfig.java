package org.whatever.library.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Qualifier("userDetailsServiceImpl")
    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/admin/**", "/users/**", "/users").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/authors", "authors/{id}/**", "authors/{id}", "authors/all", "payments/resolve/**", "**/moderator/**").hasRole("MODERATOR")
                .antMatchers("payments/moderator/**", "**/moderator/**").hasRole("MODERATOR")
                .antMatchers("rental/**").hasRole("MODERATOR")
                .antMatchers("rental/reserve", "rental/return", "/payments/user/unsubscribe").hasRole("SUBSCRIBER")
                .antMatchers(HttpMethod.GET, "/authors/**", "/authors").hasRole("USER")
                .antMatchers("books/count", "books/size", "books/search", "**/user/**", "/payments/user/**", "/books/author/*", "/payments/currencies", "/user/roles").hasRole("USER")
                .antMatchers("/registration", "/error", "/test/**", "/logger/", "/temp/login", "/temp/register").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .permitAll()
                .and()
                .logout()
                .permitAll()
                .and().cors().and().csrf().disable();
    }

    @Bean
    public AuthenticationManager customAuthenticationManager() throws Exception {
        return authenticationManager();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }
}