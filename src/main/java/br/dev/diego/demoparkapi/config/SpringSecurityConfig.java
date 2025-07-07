package br.dev.diego.demoparkapi.config;

import br.dev.diego.demoparkapi.config.jwt.JwtAuthenticationEntryPoint;
import br.dev.diego.demoparkapi.config.jwt.JwtAuthorizationFilter;
import br.dev.diego.demoparkapi.config.jwt.JwtUserDetailsService;
import br.dev.diego.demoparkapi.config.jwt.JwtUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@Configuration
@EnableMethodSecurity
public class SpringSecurityConfig {

    private static final String[] DOCUMENTATION_OPENAPI = {
            "/docs/index.html",
            "/docs-park.html", "/docs-park/**",
            "/v3/api-docs/**",
            "/swagger-ui-custom.html", "/swagger-ui.html", "/swagger-ui/**",
            "/**.html", "/webjars/**", "/configuration/**", "/swagger-resources/**"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthorizationFilter jwtAuthorizationFilter) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/usuarios").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth").permitAll()
                        .requestMatchers(DOCUMENTATION_OPENAPI).permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        .anyRequest().authenticated()
                )
                .headers(
                        headers ->
                                headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(
                        exceptionHandling ->
                                exceptionHandling.authenticationEntryPoint(new JwtAuthenticationEntryPoint())
                )
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter(JwtUserDetailsService jwtUserDetailsService, JwtUtils jwtUtils) {
        return new JwtAuthorizationFilter(jwtUserDetailsService, jwtUtils);
    }

}
