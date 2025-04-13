//package com.payme.token_provider.config;
//
//import com.payme.token_provider.service.JwtService;
//import io.jsonwebtoken.Claims;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.lang.NonNull;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//import java.util.List;
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class JwtAuthenticationFilter extends OncePerRequestFilter {
//    private static final String AUTH_HEADER = "Authorization";
//
//    private final JwtService jwtService;
//
//
//    @Override
//    protected void doFilterInternal(
//            @NonNull HttpServletRequest request,
//            @NonNull HttpServletResponse response,
//            @NonNull FilterChain filterChain
//    ) throws ServletException, IOException {
//
//        if(request.getMethod().equals("OPTIONS")){
//            response.setStatus(HttpServletResponse.SC_OK);
//            return;
//        }
//
//        String authHeader = request.getHeader(AUTH_HEADER);
//
//        if(authHeader.isEmpty() || !authHeader.startsWith("bearer")){
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        String jwtToken = authHeader.substring(7);
//        boolean hasValidClaims = validateTokenClaims(jwtToken);
//
//        if(!hasValidClaims){
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        String clientName = jwtService.extractClaim(jwtToken, Claims::getSubject);
//        List<String> authorities = jwtService.extractClaim(
//                jwtToken,
//                claims -> claims.get("roles", List.class)
//        );
//
//        if(jwtService.isTokenValid(jwtToken, clientName)){
//            UsernamePasswordAuthenticationToken authToken =
//                    new UsernamePasswordAuthenticationToken(
//                            clientName,
//                            null,
//                            authorities.stream().map(SimpleGrantedAuthority::new).toList()
//                    );
//
//            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//            SecurityContextHolder.getContext().setAuthentication(authToken);
//        }
//
//        filterChain.doFilter(request, response);
//    }
//
//    private boolean validateTokenClaims(String token){
//        String clientType = jwtService.extractClaim(token, claims -> claims.get("type", String.class));
//        if(clientType == null || clientType.equals("SERVICE")){
//            log.error("Client provided incorrect client-type. ");
//            return false;
//        }
//
//        String client = jwtService.extractClaim(token, Claims::getSubject);
//        if(client == null){
//            log.error("Client provided a null name identifier. ");
//            return false;
//        }
//
//        return true;
//    }
//}
