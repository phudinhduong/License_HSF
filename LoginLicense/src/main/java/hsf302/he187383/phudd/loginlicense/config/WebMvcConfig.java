package hsf302.he187383.phudd.loginlicense.config;

import hsf302.he187383.phudd.loginlicense.AuthHeartbeatInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final AuthHeartbeatInterceptor heartbeatInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(heartbeatInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/login",
                        "/css/**", "/js/**", "/img/**", "/app.css"
                );
    }
}
