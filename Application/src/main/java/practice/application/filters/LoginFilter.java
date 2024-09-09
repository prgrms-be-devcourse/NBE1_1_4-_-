package practice.application.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import practice.application.services.LoggingService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class LoginFilter extends OncePerRequestFilter {

    private final LoggingService loggingService;

    @Autowired
    @Lazy
    public LoginFilter(LoggingService loggingService) {
        this.loggingService = loggingService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String pureToken = parseBearerToken(request);

        if (pureToken == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String memberName = loggingService.parseAccessTokenToMemberName(pureToken);

            var authToken = new UsernamePasswordAuthenticationToken(memberName, null, AuthorityUtils.NO_AUTHORITIES);

            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
            securityContext.setAuthentication(authToken);
            SecurityContextHolder.setContext(securityContext);
        }
        catch (Exception e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());

            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("status", HttpServletResponse.SC_UNAUTHORIZED);
            responseBody.put("message", "Request rejected via unauthorized token");
            responseBody.put("error message", e.getMessage());
            responseBody.put("request URI", request.getRequestURI());
            responseBody.put("path", request.getServletPath());

            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(response.getOutputStream(), responseBody);
        }

        filterChain.doFilter(request, response);
    }

    private String parseBearerToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (bearerToken == null || !bearerToken.startsWith("Bearer "))
            return null;

        return bearerToken.substring(7);
    }
}
