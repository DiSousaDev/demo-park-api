package br.dev.diego.demoparkapi.config.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthorizationFilter.class);

    private final JwtUserDetailsService jwtUserDetailsService;
    private final JwtUtils jwtUtils;

    public JwtAuthorizationFilter(JwtUserDetailsService jwtUserDetailsService, JwtUtils jwtUtils) {
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("Authorization");

        if (token == null || !token.startsWith("Bearer ")) {
            LOGGER.warn("Token de autorização não encontrado ou inválido");
            filterChain.doFilter(request, response);
            return;
        }
        if (!jwtUtils.isTokenValid(token)) {
            LOGGER.warn("Token de autorização inválido");
            filterChain.doFilter(request, response);
            return;
        }

        String username = jwtUtils.getUsernameFromToken(token);
        if (username == null) {
            LOGGER.warn("Nome de usuário não encontrado no token");
            filterChain.doFilter(request, response);
            return;
        }

        toAuthenticateUser(username, request);

        filterChain.doFilter(request, response);
    }

    private void toAuthenticateUser(String username, HttpServletRequest request) {
        UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
