package hsf302.he187383.phudd.licensev3.config;

import hsf302.he187383.phudd.licensev3.utils.JwtLicenseUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

public class LicenseJwtFilter extends OncePerRequestFilter {

    private JwtLicenseUtil jwtLicenseUtil;

    public LicenseJwtFilter(JwtLicenseUtil jwtLicenseUtil) {
        this.jwtLicenseUtil = jwtLicenseUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        System.out.println("API filter for URI: " + req.getRequestURI());

        String authHeader = req.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            res.getWriter().write("Missing or invalid Authorization header");
            return;
        }

        String token = authHeader.substring(7);

        try {
            // Xác thực JWT
            Jws<Claims> claims = jwtLicenseUtil.validateLicenseToken(token);
            String subject = claims.getBody().getSubject();

            // Gán Authentication vào SecurityContext
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(subject, null, Collections.emptyList());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            chain.doFilter(request, response);
        } catch (Exception e) {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            res.getWriter().write("Invalid or expired license token");
        }
    }

    /**
     *  Bỏ qua filter cho các URL không cần JWT
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        if (!path.startsWith("/api/license/")) {
            return true; // KHÔNG filter
        }
        // Bỏ qua khi gọi API kích hoạt
        return path.startsWith("/api/license/activate");
    }


//    @Override
//    protected boolean shouldNotFilter(HttpServletRequest request) {
//        String path = request.getRequestURI();
//        // Bỏ qua API kích hoạt và public
//        return path.equals("/api/license/activate")
//                || path.equals("/api/license/verify");
//    }

}
