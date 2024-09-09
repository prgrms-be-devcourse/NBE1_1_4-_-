package practice.application.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class HunilFilter extends OncePerRequestFilter {
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            String token = parseBearerToken(request);
            if(token != null){
                String userId = jwtTokenProvider.validateUserToken(token);

                AbstractAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userId,
                        null,
                        AuthorityUtils.NO_AUTHORITIES
                );

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                securityContext.setAuthentication(authToken);
                SecurityContextHolder.setContext(securityContext);
            }
        }catch(Exception ex){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE); // response는 기본 설정이 html 응답으로 되어있을 거라..

            Map<String, Object> body = new HashMap<>(); // front한테 인증 불가에 대한 에러 메세지를 응답하고 싶어서
            body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
            body.put("error", "Unauthorized~~~~~");
            body.put("message", ex.getMessage());
            body.put("path", request.getServletPath());

            ObjectMapper mapper = new ObjectMapper(); // 평소에 @RequestBody, @ResponseBody 처리하면서 자바 <-> json 작업할 때 쓰이던 lib
            mapper.writeValue(response.getOutputStream(), body);
        }
        filterChain.doFilter(request,response);
    }

    private String parseBearerToken(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");

        if(bearerToken!=null && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }
        return null;
    }
}
