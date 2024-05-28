package com.project.shopapp.configurations;

import com.project.shopapp.components.JwtTokenFilter;
import com.project.shopapp.models.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebMvc
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
                                    String.format("%s/provincial",apiBasePath),
                                    String.format("%s/users/register",apiBasePath),
                                    String.format("%s/users/login",apiBasePath),
                                    String.format("%s/users/otp/verify",apiBasePath),
                                    String.format("%s/users/otp/resend",apiBasePath))
                            .permitAll()

                            // users
                            .antMatchers(PUT,
                                    String.format("%s/users",apiBasePath)).permitAll()
                            .antMatchers(PATCH,
                                    String.format("%s/users",apiBasePath)).permitAll()
                            .antMatchers(DELETE,
                                    String.format("%s/users/**",apiBasePath)).permitAll()
                            .antMatchers(POST,
                                    String.format("%s/users/comment",apiBasePath)).permitAll()
                            .antMatchers(GET,
                                    String.format("%s/users/comment/**",apiBasePath)).permitAll()
                            .antMatchers(PATCH,
                                    String.format("%s/users/**/**",apiBasePath)).permitAll()

//                            .antMatchers(GET,
//                                    "https://vapi.vnappmob.com/api/province/").permitAll()
                            .antMatchers(GET,
                                    String.format("%s/provincial/provinces",apiBasePath),
                                    String.format("%s/provincial/districts",apiBasePath),
                                    String.format("%s/provincial/communes",apiBasePath))    
                            .permitAll()

                            // carts
                            .antMatchers(GET,
                                    String.format("%s/carts/**",apiBasePath)).permitAll()
                            .antMatchers(POST,
                                    String.format("%s/carts",apiBasePath)).permitAll()
                            .antMatchers(DELETE,
                                    String.format("%s/carts/**",apiBasePath)).permitAll()

                            // categories
                            .antMatchers(GET,
                                    String.format("%s/categories**",apiBasePath)).permitAll()
                            .antMatchers(POST,
                                    String.format("%s/categories/**",apiBasePath)).hasRole(Role.ADMIN)
                            .antMatchers(PUT,
                                    String.format("%s/categories/**",apiBasePath)).hasRole(Role.ADMIN)
                            .antMatchers(DELETE,
                                    String.format("%s/categories/**",apiBasePath)).hasRole(Role.ADMIN)

                            // products
                            .antMatchers(GET,
                                    String.format("%s/products**",apiBasePath)).permitAll()
                            .antMatchers(GET,
                                    String.format("%s/products/**",apiBasePath)).permitAll()
                            .antMatchers(GET,
                                    String.format("%s/products/images/**",apiBasePath)).permitAll()
                            .antMatchers(POST,
                                    String.format("%s/products**",apiBasePath)).hasRole(Role.ADMIN)
                            .antMatchers(POST,
                                    String.format("%s/products/uploads",apiBasePath)).hasRole(Role.ADMIN)
                            .antMatchers(PUT,
                                    String.format("%s/products/**",apiBasePath)).hasRole(Role.ADMIN)
                            .antMatchers(DELETE,
                                    String.format("%s/products/**",apiBasePath)).hasRole(Role.ADMIN)
                            .antMatchers(DELETE,
                                    String.format("%s/products/images/**",apiBasePath)).hasRole(Role.ADMIN)

                            //orders
                            .antMatchers(GET,
                                    String.format("%s/orders/**",apiBasePath)).hasAnyRole(Role.USER,Role.ADMIN)
                            .antMatchers(POST,
                                    String.format("%s/orders",apiBasePath)).hasRole(Role.USER)
                            .antMatchers(PUT,
                                    String.format("%s/orders/**",apiBasePath)).hasRole(Role.USER)
                            .antMatchers(DELETE,
                                    String.format("%s/orders/**",apiBasePath)).hasRole(Role.USER)



                            // order_details
                            .antMatchers(GET,
                            String.format("%s/order_details/**",apiBasePath)).hasAnyRole(Role.USER,Role.ADMIN)
                            .antMatchers(POST,
                                    String.format("%s/order_details**",apiBasePath)).hasRole(Role.ADMIN)
                            .antMatchers(PUT,
                                    String.format("%s/order_details/**",apiBasePath)).hasRole(Role.ADMIN)
                            .antMatchers(DELETE,
                                    String.format("%s/order_details/**",apiBasePath)).hasRole(Role.ADMIN)

                            // payment
                            .antMatchers(GET,
                                    String.format("%s/payment",apiBasePath)).hasRole(Role.USER)

                            .anyRequest().authenticated();
                })
                .csrf(AbstractHttpConfigurer::disable);
        http.cors(new Customizer<CorsConfigurer<HttpSecurity>>() {
            @Override
            public void customize(CorsConfigurer<HttpSecurity> httpSecurityCorsConfigurer) {
                CorsConfiguration corsConfiguration = new CorsConfiguration();
                corsConfiguration.setAllowedOrigins(Collections.singletonList("*"));
                corsConfiguration.setAllowedMethods(Arrays.asList("GET","POST","PUT","PATCH","DELETE","OPTIONS"));
                corsConfiguration.setAllowedHeaders(Arrays.asList("authorization","content-type","x-auth-token"));
                corsConfiguration.setExposedHeaders(Collections.singletonList("x-auth-token"));
                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**",corsConfiguration);
                httpSecurityCorsConfigurer.configurationSource(source);
            }
        });
        return http.build();
    }
}
