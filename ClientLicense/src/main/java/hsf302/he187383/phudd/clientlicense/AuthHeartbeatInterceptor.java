package hsf302.he187383.phudd.clientlicense;



import hsf302.he187383.phudd.clientlicense.config.SessionUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.Duration;
import java.time.Instant;

@Component
@RequiredArgsConstructor
public class AuthHeartbeatInterceptor implements HandlerInterceptor {

    private final LicenseAuthClient authClient;

    // khoảng cách giữa 2 lần ping (gợi ý 120s, thấp hơn window 600s bên A)
    private static final Duration HEARTBEAT_INTERVAL = Duration.ofSeconds(10);

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {
        String path = req.getRequestURI();

        // Bỏ qua các đường public/static
        if (path.equals("/login") || path.startsWith("/css/") || path.startsWith("/js/") || path.startsWith("/img/")) {
            return true;
        }

        HttpSession session = req.getSession(false);
        SessionUser u = (session == null) ? null : (SessionUser) session.getAttribute("user");
        if (u == null) {
            res.sendRedirect("/login");
            return false;
        }

        Instant now = Instant.now();
        Instant last = u.getLastHeartbeatAt();
        if (last == null || Duration.between(last, now).compareTo(HEARTBEAT_INTERVAL) >= 0) {
            try {
                authClient.heartbeat(u.getJti(), u.getDeviceId());
                u.setLastHeartbeatAt(now);
                session.setAttribute("user", u); // cập nhật lại
            } catch (LicenseAuthClient.ClientException e) {
                // các code có thể gặp từ A: token_expired / token_revoked / device_mismatch
                session.invalidate();
                String reason = switch (e.code) {
                    case "token_expired" -> "expired=1";
                    case "token_revoked" -> "revoked=1";
                    case "device_mismatch" -> "device=1";
                    default -> "err=1";
                };
                res.sendRedirect("/login?" + reason);
                return false;
            }
        }
        return true;
    }
}
