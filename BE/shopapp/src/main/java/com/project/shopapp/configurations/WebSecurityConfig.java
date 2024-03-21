package com.project.shopapp.configurations;

import com.project.shopapp.components.JwtTokenFilter;
import com.project.shopapp.components.JwtTokenUtil;
import com.project.shopapp.models.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final JwtTokenFilter jwtTokenFilter;
    @Value("${api.basePath}")
    private String apiBasePath;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(requests ->{
                    requests
                            .antMatchers(
                                    String.format("%s/users/register",apiBasePath),
                                    String.format("%s/users/login",apiBasePath))
                            .permitAll()
                            // categories
                            .antMatchers(GET,
                                    String.format("%s/categories**",apiBasePath)).hasAnyRole(Role.USER,Role.ADMIN)
                            .antMatchers(POST,
                                    String.format("%s/categories/**",apiBasePath)).hasAnyRole(Role.ADMIN)
                            .antMatchers(PUT,
                                    String.format("%s/categories/**",apiBasePath)).hasAnyRole(Role.ADMIN)
                            .antMatchers(DELETE,
                                    String.format("%s/categories/**",apiBasePath)).hasAnyRole(Role.ADMIN)

                            // products
                            .antMatchers(GET,
                                    String.format("%s/products**",apiBasePath)).hasAnyRole(Role.USER,Role.ADMIN)
                            .antMatchers(POST,
                                    String.format("%s/products**",apiBasePath)).hasAnyRole(Role.ADMIN)
                            .antMatchers(PUT,
                                    String.format("%s/products/**",apiBasePath)).hasAnyRole(Role.ADMIN)
                            .antMatchers(DELETE,
                                    String.format("%s/products/**",apiBasePath)).hasAnyRole(Role.ADMIN)

                            //orders
                            .antMatchers(GET,
                                    String.format("%s/orders/**",apiBasePath)).hasAnyRole(Role.USER,Role.ADMIN)
                            .antMatchers(POST,
                                    String.format("%s/orders/**",apiBasePath)).hasAnyRole(Role.USER)
                            .antMatchers(PUT,
                                    String.format("%s/orders/**",apiBasePath)).hasRole(Role.ADMIN)
                            .antMatchers(DELETE,
                                    String.format("%s/orders/**",apiBasePath)).hasRole(Role.ADMIN)


                            // order_details
                            .antMatchers(GET,
                            String.format("%s/order_details/**",apiBasePath)).hasAnyRole(Role.USER,Role.ADMIN)
                            .antMatchers(POST,
                                    String.format("%s/order_details**",apiBasePath)).hasAnyRole(Role.ADMIN)
                            .antMatchers(PUT,
                                    String.format("%s/order_details/**",apiBasePath)).hasAnyRole(Role.ADMIN)
                            .antMatchers(DELETE,
                                    String.format("%s/order_details/**",apiBasePath)).hasAnyRole(Role.ADMIN)

                            .anyRequest().authenticated();
                });
        return http.build();
    }
}
