package com.example.monitoring_communication.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import java.io.IOException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Value("${jwt.secret}")
    private String secretKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);

            try {
                Claims claims = Jwts.parser()
                        .setSigningKey(secretKey)
                        .parseClaimsJws(token)
                        .getBody();

                String username = claims.getSubject();
                Object rolesObject = claims.get("roles");

                List<SimpleGrantedAuthority> authorities;

                if (rolesObject instanceof String) {
                    authorities = List.of(((String) rolesObject).split(","))
                            .stream()
                            .map(SimpleGrantedAuthority::new)
                            .toList();
                } else if (rolesObject instanceof List) {
                    authorities = ((List<?>) rolesObject).stream()
                            .map(role -> new SimpleGrantedAuthority(role.toString()))
                            .toList();
                } else {
                    throw new IllegalArgumentException("Unexpected roles type: " + rolesObject.getClass());
                }

                if (username != null) {
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(username, null, authorities);
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }

        chain.doFilter(request, response);
    }

}