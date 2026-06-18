package snippets.java.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.*;

/**
 * Spring Security — HTTP Security Headers Configuration
 */
@Configuration
public class SecurityHeaders {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Content-Security-Policy — prevent XSS
            .headers(headers -> headers
                .contentSecurityPolicy(csp -> csp
                    .policyDirectives(
                        "default-src 'self'; " +
                        "script-src 'self' https://cdn.jsdelivr.net; " +
                        "style-src 'self' 'unsafe-inline'; " +
                        "img-src 'self' data: https:; " +
                        "frame-ancestors 'none'"
                    )
                )
                // Strict-Transport-Security — force HTTPS
                .httpStrictTransportSecurity(hsts -> hsts
                    .maxAgeInSeconds(31_536_000)
                    .includeSubDomains(true)
                    .preload(true)
                )
                // Prevent MIME sniffing
                .contentTypeOptions(content -> {})
                // Prevent clickjacking
                .frameOptions(frame -> frame.deny())
                // Referrer policy
                .referrerPolicy(referrer -> referrer
                    .policy(org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN)
                )
                // Permissions Policy
                .permissionsPolicy(permissions -> permissions
                    .policy("camera=(), microphone=(), geolocation=()")
                )
            )
            // CSRF — enabled by default, disable only for stateless APIs
            .csrf(csrf -> csrf.disable()) // disable for REST APIs using JWT
            // CORS configuration
            .cors(cors -> cors.configurationSource(request -> {
                var config = new org.springframework.web.cors.CorsConfiguration();
                config.setAllowedOrigins(java.util.List.of("https://yourfrontend.com"));
                config.setAllowedMethods(java.util.List.of("GET","POST","PUT","DELETE","OPTIONS"));
                config.setAllowedHeaders(java.util.List.of("Authorization","Content-Type"));
                config.setAllowCredentials(true);
                config.setMaxAge(3600L);
                return config;
            }))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/actuator/health").permitAll()
                .requestMatchers("/api/public/**").permitAll()
                .anyRequest().authenticated()
            );
        return http.build();
        // Resulting headers:
        // Content-Security-Policy: default-src 'self'; ...
        // Strict-Transport-Security: max-age=31536000; includeSubDomains; preload
        // X-Content-Type-Options: nosniff
        // X-Frame-Options: DENY
        // Referrer-Policy: strict-origin-when-cross-origin
    }
}


package snippets.java.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.*;

/**
 * Spring Security — HTTP Security Headers Configuration
 */
@Configuration
public class SecurityHeaders {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Content-Security-Policy — prevent XSS
            .headers(headers -> headers
                .contentSecurityPolicy(csp -> csp
                    .policyDirectives(
                        "default-src 'self'; " +
                        "script-src 'self' https://cdn.jsdelivr.net; " +
                        "style-src 'self' 'unsafe-inline'; " +
                        "img-src 'self' data: https:; " +
                        "frame-ancestors 'none'"
                    )
                )
                // Strict-Transport-Security — force HTTPS
                .httpStrictTransportSecurity(hsts -> hsts
                    .maxAgeInSeconds(31_536_000)
                    .includeSubDomains(true)
                    .preload(true)
                )
                // Prevent MIME sniffing
                .contentTypeOptions(content -> {})
                // Prevent clickjacking
                .frameOptions(frame -> frame.deny())
                // Referrer policy
                .referrerPolicy(referrer -> referrer
                    .policy(org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN)
                )
                // Permissions Policy
                .permissionsPolicy(permissions -> permissions
                    .policy("camera=(), microphone=(), geolocation=()")
                )
            )
            // CSRF — enabled by default, disable only for stateless APIs
            .csrf(csrf -> csrf.disable()) // disable for REST APIs using JWT
            // CORS configuration
            .cors(cors -> cors.configurationSource(request -> {
                var config = new org.springframework.web.cors.CorsConfiguration();
                config.setAllowedOrigins(java.util.List.of("https://yourfrontend.com"));
                config.setAllowedMethods(java.util.List.of("GET","POST","PUT","DELETE","OPTIONS"));
                config.setAllowedHeaders(java.util.List.of("Authorization","Content-Type"));
                config.setAllowCredentials(true);
                config.setMaxAge(3600L);
                return config;
            }))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/actuator/health").permitAll()
                .requestMatchers("/api/public/**").permitAll()
                .anyRequest().authenticated()
            );
        return http.build();
        // Resulting headers:
        // Content-Security-Policy: default-src 'self'; ...
        // Strict-Transport-Security: max-age=31536000; includeSubDomains; preload
        // X-Content-Type-Options: nosniff
        // X-Frame-Options: DENY
        // Referrer-Policy: strict-origin-when-cross-origin
    }
}
