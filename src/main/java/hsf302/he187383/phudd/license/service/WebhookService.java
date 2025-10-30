package hsf302.he187383.phudd.license.service;

import hsf302.he187383.phudd.license.model.*;
import hsf302.he187383.phudd.license.repository.*;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class WebhookService {
    private final WebhookRepository webhookRepo;
    private final WebhookDeliveryRepository deliveryRepo;
    private final RestTemplate http = new RestTemplate();

    public WebhookService(WebhookRepository webhookRepo, WebhookDeliveryRepository deliveryRepo) {
        this.webhookRepo = webhookRepo; this.deliveryRepo = deliveryRepo;
    }

    @Transactional
    public void emit(UUID orgId, String eventType, String payloadJson){
        List<Webhook> targets = webhookRepo.findByOrganizationIdOrOrganizationIsNullAndIsActiveTrue(orgId);
        for (Webhook w : targets) {
            WebhookDelivery d = WebhookDelivery.builder()
                    .webhook(w).eventType(eventType).payload(payloadJson)
                    .retryCount(0).build();
            try {
                HttpHeaders h = new HttpHeaders();
                h.setContentType(MediaType.APPLICATION_JSON);
                // HMAC ký body bằng secret (tùy chọn)
                // h.add("X-Signature", hmacSha256(payloadJson, w.getSecret()));
                ResponseEntity<String> resp = http.exchange(w.getUrl(), HttpMethod.POST,
                        new HttpEntity<>(payloadJson, h), String.class);
                d.setStatusCode(resp.getStatusCodeValue());
                d.setDeliveredAt(Instant.now());
            } catch (Exception ex) {
                d.setStatusCode(0);
                d.setLastError(ex.getMessage());
            }
            deliveryRepo.save(d);
        }
    }
}