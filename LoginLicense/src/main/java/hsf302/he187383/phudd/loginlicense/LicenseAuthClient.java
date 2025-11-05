package hsf302.he187383.phudd.loginlicense;

import hsf302.he187383.phudd.loginlicense.config.AppConfig;
import hsf302.he187383.phudd.loginlicense.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class LicenseAuthClient {

    private final RestTemplate rt;
    private final AppConfig.LicenseApiProps props;

    public LoginResponse login(String username, String password, String deviceId, boolean preempt) throws ClientException {
        String url = props.getBaseUrl() + "/auth/login";

        preempt = true;

        var req = new LoginRequest();
                req.setUsername(username);
                req.setPassword(password);
                req.setDeviceId(deviceId);
                req.setDeviceName(null);
                req.setPreempt(preempt);

        try {
            var entity = new HttpEntity<>(req, defaultHeaders());
            var res = rt.exchange(url, HttpMethod.POST, entity, LoginResponse.class);
            return res.getBody();
        } catch (HttpStatusCodeException e) {
//            try {
//                var err = rt.getMessageConverters().stream()
//                        .filter(mc -> mc.canRead(ErrorPayload.class, MediaType.APPLICATION_JSON))
//                        .findFirst();
//                // parse payload lỗi
//                var payload = new com.fasterxml.jackson.databind.ObjectMapper()
//                        .readValue(e.getResponseBodyAsByteArray(), ErrorPayload.class);
//                throw new ClientException(payload.getError(), payload.getError_description());
//            } catch (Exception ignore) {
//                throw new ClientException("http_error", e.getStatusCode() + " " + e.getMessage());
//            }
            throw new ClientException(e.getStatusCode().toString(), e.getMessage());
        } catch (Exception e) {
            throw new ClientException("network_error", e.getMessage());
        }
    }

    public void logout(String jti) {
        String url = props.getBaseUrl() + "/auth/logout";
        var body = new java.util.HashMap<String,Object>();
        body.put("jti", jti);
        rt.exchange(url, HttpMethod.POST, new HttpEntity<>(body, defaultHeaders()), String.class);
    }

    public void heartbeat(String jti, String deviceId) throws ClientException {
        String url = props.getBaseUrl() + "/auth/heartbeat";
        var body = new java.util.HashMap<String,Object>();
        body.put("jti", jti);
        body.put("deviceId", deviceId);

        try {
            rt.exchange(url, HttpMethod.POST, new HttpEntity<>(body, defaultHeaders()), String.class);
        } catch (HttpStatusCodeException e) {
            // parse lỗi từ A
            try {
                var payload = new com.fasterxml.jackson.databind.ObjectMapper()
                        .readValue(e.getResponseBodyAsByteArray(), hsf302.he187383.phudd.loginlicense.dto.ErrorPayload.class);
                throw new ClientException(payload.getError(), payload.getError_description());
            } catch (Exception ignore) {
                throw new ClientException("http_error", e.getStatusCode() + " " + e.getMessage());
            }
        } catch (Exception e) {
            throw new ClientException("network_error", e.getMessage());
        }
    }



    private static HttpHeaders defaultHeaders() {
        var h = new HttpHeaders();
        h.setContentType(MediaType.APPLICATION_JSON);
        h.setAccept(java.util.List.of(MediaType.APPLICATION_JSON));
        return h;
    }

    public static class ClientException extends Exception {
        public final String code;
        public ClientException(String code, String msg) { super(msg); this.code = code; }
    }
}
