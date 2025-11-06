package hsf302.he187383.phudd.licensev3.config;

import hsf302.he187383.phudd.licensev3.utils.JwtLicenseUtil;
import hsf302.he187383.phudd.licensev3.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.UUID;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final CustomSuccessHandler customSuccessHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http

                .csrf(csrf -> csrf.ignoringRequestMatchers("/auth/**"))

//                .securityMatcher("/web")
//                .securityMatcher("/**")
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/api/license/**").denyAll()
                        .requestMatchers("/login","/register",
                                "/app.css", "/css/**", "/js/**", "/img/**").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .successHandler(customSuccessHandler)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                )
                .userDetailsService(userDetailsService);

        return http.build();
    }

    public UUID getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object principal = auth.getPrincipal();

        if (principal instanceof UserDetails userDetails) {
            String email = userDetails.getUsername(); // email = username
            var user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalStateException("User not found"));
            return user.getId();
        }

        throw new IllegalStateException("No authenticated user found");
    }

    @Bean
    @Order(1)
    public SecurityFilterChain licenseApiSecurity(HttpSecurity http, LicenseJwtFilter licenseJwtFilter) throws Exception {
        http
                .securityMatcher("/api/license/**")
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/license/activate").permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(licenseJwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public LicenseJwtFilter licenseJwtFilter(JwtLicenseUtil jwtLicenseUtil) {
        return new LicenseJwtFilter(jwtLicenseUtil);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
