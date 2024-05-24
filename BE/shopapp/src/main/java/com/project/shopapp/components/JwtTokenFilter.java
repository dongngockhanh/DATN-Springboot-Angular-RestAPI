package com.project.shopapp.components;

import com.project.shopapp.models.User;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    @Value("${api.basePath}")
    private String apiBasePath;
    private final UserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        try {
            if( isBypassToken(request)){
                filterChain.doFilter(request,response);
                return;
            }
            final String authHeader = request.getHeader("Authorization");
            if(authHeader ==null || !authHeader.startsWith("Bearer ")){
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Unauthorized");
                return;
            }
            final String token = authHeader.substring(7);
            final String phoneNumber = jwtTokenUtil.extractPhoneNumber(token);
            if(phoneNumber != null
                    && SecurityContextHolder.getContext().getAuthentication() == null){
                User userDetail =  (User) userDetailsService.loadUserByUsername(phoneNumber);
                if(jwtTokenUtil.validateToken(token,userDetail)){
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetail,
                                    null,
                                    userDetail.getAuthorities()
                            );
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
            filterChain.doFilter(request,response);
        }catch (Exception e)
        {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Unauthorized");
        }
    }

    private boolean isBypassToken(@NonNull HttpServletRequest request){
        final List<Pair<String,String>> bypassTokens = Arrays.asList(
                Pair.of(String.format("%s/products",apiBasePath),"GET"),
                Pair.of(String.format("%s/categories",apiBasePath),"GET"),
                Pair.of(String.format("%s/users/register",apiBasePath),"POST"),
                Pair.of(String.format("%s/users/login",apiBasePath),"POST"),
                Pair.of(String.format("%s/users/otp/verify",apiBasePath),"GET"),
                Pair.of(String.format("%s/users/otp/resend",apiBasePath),"GET"),
                Pair.of(String.format("%s/provincial",apiBasePath),"GET")
        );
        for (Pair<String,String> bypassToken:bypassTokens)
        {
            if(request.getServletPath().contains(bypassToken.getFirst()) &&
                    request.getMethod().equals(bypassToken.getSecond())){
                return true;
            }
        }
        return false;
    }
}
