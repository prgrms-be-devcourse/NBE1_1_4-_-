package practice.application.config;

import org.apache.catalina.filters.CorsFilter;
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
import practice.application.util.HunilFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private HunilFilter hunilFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return
                http
                        .cors(AbstractHttpConfigurer::disable)
                        .csrf(AbstractHttpConfigurer::disable)
                        .httpBasic(AbstractHttpConfigurer::disable)
                        .sessionManagement(AbstractHttpConfigurer::disable)
                        .authorizeHttpRequests(req -> {
                            req.requestMatchers("/auth/**").permitAll();
                            req.anyRequest().authenticated();
                        })
                        .addFilterBefore(hunilFilter, UsernamePasswordAuthenticationFilter.class)
                        .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
