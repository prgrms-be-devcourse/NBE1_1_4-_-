package practice.application.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import practice.application.models.Jwt.CustomAccessDeniedHandler;
import practice.application.models.Jwt.CustomAuthenticationEntryPoint;
import practice.application.models.Jwt.JwtFilter;
import practice.application.models.Jwt.JwtUtil;
import practice.application.service.CustomUserDetailService;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SpringSecurity {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailService customUserDetailService;
    private final CustomAccessDeniedHandler accessDeniedHandler; // 인증은 되었지만 권한이 없을경우
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;  // 인증 되지 않은 사용자에 대한 excepton 처리

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {

        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.csrf((csrf) -> csrf.disable());
        http.cors(Customizer.withDefaults());

        //세션 관리 상태 없음으로 구성, Spring Security가 세션 생성 or 사용 X
        http.sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(
                SessionCreationPolicy.STATELESS));

        //FormLogin, BasicHttp 비활성화
        http.formLogin((form) -> form.disable());
        http.httpBasic((auth) -> auth.disable());

        http.authorizeRequests((authorize) -> authorize.requestMatchers("/members/**").permitAll()
                .anyRequest().authenticated());

        http.addFilterBefore(new JwtFilter(customUserDetailService, jwtUtil), UsernamePasswordAuthenticationFilter.class);

        http.exceptionHandling((exceptionHandling) -> exceptionHandling.accessDeniedHandler(accessDeniedHandler)
                .authenticationEntryPoint(authenticationEntryPoint));


        return http.build();

    }




}
