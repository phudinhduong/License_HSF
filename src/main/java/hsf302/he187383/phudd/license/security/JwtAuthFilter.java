package hsf302.he187383.phudd.license.security;

import hsf302.he187383.phudd.license.service.TokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final TokenService tokenService;

    public JwtAuthFilter(TokenService tokenService) { this.tokenService = tokenService; }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String jwt = header.substring(7);
            try {
                Jws<Claims> jws = tokenService.parseAndValidate(jwt);
                if (tokenService.isAccessToken(jws)) {
                    Claims c = jws.getBody();
                    String email = c.get("email", String.class);
                    String orgId = c.get("org_id", String.class);
                    String role = c.get("role", String.class);

                    String principal = orgId + "::" + email; // khớp với AppUserDetailsService
                    List<GrantedAuthority> auths = List.of(() -> "ROLE_" + role);

                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(principal, null, auths);
                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            } catch (Exception ignored) {
                // Không chặn; để qua handler nếu endpoint yêu cầu auth
            }
        }
        chain.doFilter(request, response);
    }
}
