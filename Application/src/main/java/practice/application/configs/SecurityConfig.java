package practice.application.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import practice.application.filters.LoginFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final LoginFilter loginFilter;

    @Autowired
    public SecurityConfig(LoginFilter loginFilter) {
        this.loginFilter = loginFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.cors(AbstractHttpConfigurer::disable)
                   .csrf(AbstractHttpConfigurer::disable)
                   .httpBasic(AbstractHttpConfigurer::disable)
                   .sessionManagement(AbstractHttpConfigurer::disable)
                   .authorizeHttpRequests(req -> {

                       req.requestMatchers("/logging/**")
                          .permitAll();

                       req.anyRequest()
                          .authenticated();

                   })
                   .addFilterAfter(loginFilter, UsernamePasswordAuthenticationFilter.class)
                   .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
