package snippets.java.spring;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.security.Key;
import java.util.Date;

/**
 * JWT Security — Spring Boot Configuration
 */
public class JwtSecurityConfig {

    // ---- JWT Utility ----
    public static class JwtUtil {
        private static final Key SECRET = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        private static final long EXPIRY = 15 * 60 * 1000L; // 15 min

        public static String generateToken(String userId, String role) {
            return Jwts.builder()
                .setSubject(userId)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRY))
                .signWith(SECRET)
                .compact();
        }

        public static Claims validateToken(String token) {
            return Jwts.parserBuilder()
                .setSigningKey(SECRET)
                .build()
                .parseClaimsJws(token)
                .getBody();
        }

        public static String getUserId(String token) {
            return validateToken(token).getSubject();
        }
    }

    // ---- JWT Filter ----
    public static class JwtAuthFilter extends OncePerRequestFilter {
        @Override
        protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res,
                                        FilterChain chain) throws java.io.IOException, jakarta.servlet.ServletException {
            String header = req.getHeader("Authorization");
            if (header != null && header.startsWith("Bearer ")) {
                String token = header.substring(7);
                try {
                    Claims claims = JwtUtil.validateToken(token);
                    // Set authentication in SecurityContext
                    var auth = new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                        claims.getSubject(), null, java.util.Collections.emptyList());
                    org.springframework.security.core.context.SecurityContextHolder
                        .getContext().setAuthentication(auth);
                } catch (JwtException e) {
                    res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
                    return;
                }
            }
            chain.doFilter(req, res);
        }
    }

    // ---- Security Filter Chain ----
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(s -> s.sessionCreationPolicy(
                org.springframework.security.config.http.SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .addFilterBefore(new JwtAuthFilter(), UsernamePasswordAuthenticationFilter.class)
            .build();
    }
}
