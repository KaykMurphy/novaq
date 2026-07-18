package com.novaq.config;

import com.novaq.service.TokenService;
import com.novaq.service.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final UserDetailsServiceImpl userDetailsService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = this.recover(request);

        if (token != null) {
            String email = tokenService.validateToken(token);

            if (email != null && !email.isEmpty()) {
                try {
                    UserDetails user = userDetailsService.loadUserByUsername(email);

                    var usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                            user, null, user.getAuthorities()
                    );

                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

                } catch (UsernameNotFoundException ex) {
                   log.warn("Token válido recebido para usuário inexistente: {}", email);
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    private String recover(HttpServletRequest request){

        String authTokenHeader = request.getHeader("Authorization");

        if (authTokenHeader != null && authTokenHeader.startsWith("Bearer ")){
            return authTokenHeader.substring(7);
        }

        return null;
    }


}
