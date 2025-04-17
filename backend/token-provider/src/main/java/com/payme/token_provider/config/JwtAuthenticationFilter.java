package com.payme.token_provider.config;

import com.payme.common.util.ServiceTokenValidator;
import com.payme.token_provider.component.SigningKeyManager;
import com.payme.token_provider.model.RecentPublicKeys;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final String AUTH_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer";
    private static final Set<String> PUBLIC_ENDPOINTS = Set.of("/api/v1/public-key");

    private final ServiceTokenValidator serviceTokenValidator;
    private final SigningKeyManager signingKeyManager;

// Need to redo, this is useless as only 2 services can communicate5
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        String requestUri = request.getRequestURI();
        if(isPublicEndpoint(requestUri)){
            filterChain.doFilter(request, response);
            return;
        }

        if(request.getMethod().equals("OPTIONS")){
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        String authHeader = request.getHeader(AUTH_HEADER);
        if(authHeader == null || !authHeader.startsWith(BEARER_PREFIX)){
            filterChain.doFilter(request, response);
            return;
        }

        String jwtToken = authHeader.substring(7);
        RecentPublicKeys publicKey = signingKeyManager.getCurrentAndPreviousPublicKeys();
        boolean hasValidClaims = serviceTokenValidator.hasValidClaims(jwtToken, publicKey, Set.of()); // havent figured out roles yet
        if(!hasValidClaims){
            filterChain.doFilter(request, response);
            return;
        }

        String clientName = serviceTokenValidator.extractClaim(jwtToken, publicKey, Claims::getSubject);
        List<String> authorities = serviceTokenValidator.extractClaim(
                jwtToken,
                publicKey,
                claims -> claims.get("roles", List.class)
        );

        if(serviceTokenValidator.isTokenValid(jwtToken, publicKey)){
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                            clientName,
                            null,
                            authorities.stream().map(SimpleGrantedAuthority::new).toList()
                    );

            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        filterChain.doFilter(request, response);
    }

    private boolean isPublicEndpoint(String urlPath){
        return PUBLIC_ENDPOINTS.contains(urlPath);
    }

}
