package com.vivek.spring_boot_rest.config;

import com.vivek.spring_boot_rest.service.JwtService;
import com.vivek.spring_boot_rest.service.MyUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    ApplicationContext context;

    // Define permitAll endpoints
    private static final List<String> PERMIT_ALL_ENDPOINTS = Arrays.asList(
        "/",
        "/api/users/register",
        "/api/users/login", 
        "/api/users/test-password",
        "/api/jobs/all"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        
        String requestURI = request.getRequestURI();
        System.out.println("JWT Filter processing request: " + requestURI);
        
        // Skip JWT processing for permitAll endpoints
        if (PERMIT_ALL_ENDPOINTS.contains(requestURI)) {
            System.out.println("Skipping JWT processing for permitAll endpoint: " + requestURI);
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String authHeader = request.getHeader("Authorization");
            String token = null;
            String userName = null;

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
                userName = jwtService.extractUserName(token);
            }

            if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = context.getBean(MyUserDetailsService.class).loadUserByUsername(userName);

                if (jwtService.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
            // Log the exception but don't block the request
            System.err.println("Error processing JWT token: " + e.getMessage());
            e.printStackTrace();
        }

        filterChain.doFilter(request, response);
    }
}
