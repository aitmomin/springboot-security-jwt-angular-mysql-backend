package com.snrt.security;

import com.snrt.security.jwt.JwtAuthEntryPoint;
import com.snrt.security.jwt.JwtAuthTokenFilter;
import com.snrt.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
		prePostEnabled = true
)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtAuthEntryPoint unauthorizedHandler;

    @Bean
    public JwtAuthTokenFilter authenticationJwtTokenFilter() {
        return new JwtAuthTokenFilter();
    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors()
                .and()
                    .csrf()
                        .disable()
                            .authorizeRequests()
                                .antMatchers("/artist/albums/**",
                                    "/test/without/jwt",
                                    "/artist/singles/**",
                                    "/artist/**",
                                    "/album/**",
                                    "/track/**",
                                    "/search/artists/**",
                                    "/artists/random",
                                    "/register",
                                    "/auth/login",
                                    "/auth/register",
                                    "/auth/register/noimg",
                                    "/artist/username/**",
                                    "/update/views/**",
                                    "/upload",
                                    "/count/albums/**",
                                    "/count/singles/**",
                                    "/artist/image/**",
                                    "/album/image/**",
                                    "/track/image/**",
                                    "/track/url/**",
                                    "/random/advertisements",
                                    "/advertisements/image/**",
                                    "/advertisements/logo/**",
                                    "/update/advertisement/**"
                                        )
                                        .permitAll()
                        .anyRequest()
                            .authenticated()
                .and()
                    .exceptionHandling()
                        .authenticationEntryPoint(unauthorizedHandler)
                .and()
                    .sessionManagement()
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}