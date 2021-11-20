package org.utcn.socialapp.common.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.utcn.socialapp.common.filter.JwtFilter;

import static org.utcn.socialapp.user.Role.ADMIN;
import static org.utcn.socialapp.user.Role.USER;

@Configuration
@EnableWebSecurity(debug = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailsService userDetailsService;
    BCryptPasswordEncoder bCryptPasswordEncoder;
    private JwtFilter jwtFilter;

    public SecurityConfig(@Qualifier("userService") UserDetailsService userDetailsService,
                          BCryptPasswordEncoder bCryptPasswordEncoder, JwtFilter jwtFilter) {
        this.userDetailsService = userDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtFilter=jwtFilter;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // To allow Pre-flight [OPTIONS] request from browser
        web.ignoring()
                .antMatchers(HttpMethod.OPTIONS, "/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf()
                .disable().cors();
        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests()
                .antMatchers("/api/register", "/api/register/confirm", "/api/login")
                .permitAll()
                .antMatchers("/api/profile")
                .hasAnyRole(USER.name(), ADMIN.name())
                .antMatchers("/api/admin")
                .hasRole(ADMIN.name())
                .anyRequest()
                .authenticated()
                .and()
                .formLogin().disable()
                .httpBasic();
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }


}
