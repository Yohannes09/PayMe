package com.payme.authentication.configuration;

import com.payme.authentication.service.token.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Recall, {@code AuthenticationFilterConfig} is the first part
 * of the authentication process.
 * Internally It calls {@code JwtService} to extract the information
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationFilterConfig extends OncePerRequestFilter {
    private static final String AUTH_HEADER = "Authorization";

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;


    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        // This allows pre-flight requests in-case client doesn't attach token
        if(request.getMethod().equals("OPTIONS")){
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        final String authHeader = request.getHeader(AUTH_HEADER);
        final String jwtToken;
        final String usernameOrEmail;

        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }

        jwtToken = authHeader.substring(7);
        usernameOrEmail = jwtService.extractClaim(jwtToken, Claims::getSubject);
        boolean isNotAuthenticated = SecurityContextHolder.getContext().getAuthentication() == null;

        if(usernameOrEmail != null && isNotAuthenticated){
            UserDetails fetchedUser = userDetailsService.loadUserByUsername(usernameOrEmail);

            if(jwtService.isTokenValid(jwtToken, fetchedUser)){

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                            fetchedUser,
                            null,
                            fetchedUser.getAuthorities()
                        );

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }

}
