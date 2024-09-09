package practice.application.models.Jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import practice.application.models.MemberEntity;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Slf4j
@Component
public class JwtUtil {

    private final SecretKey secretKey;
    private final long accessTokenExpTime;

    public JwtUtil(
            @Value("${spring.jwt.secret}") String secret,
            @Value("${spring.jwt.expriration_time}") long accessTokenExpTime)
     {
         secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
        this.accessTokenExpTime = accessTokenExpTime;
    }

    /**
     * Access Token 생성
     * @param member
     * @return Access Token String
     */
    public String createAccessToken(MemberEntity member) {
        return createToken(member, accessTokenExpTime);
    }



    /**
     * x
     *
     * 토큰 생성
     *
     * @return
     */

    private String createToken(MemberEntity member, long exp) {

        return Jwts.builder()
                .claim("memberId", member.getId())
                .claim("email", member.getEmail())
                .claim("role", member.getUserType())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + exp))
                .signWith(secretKey)
                .compact();

    }

    /**
     * Token에서 User ID 추출
     * @param token
     * @return User ID
     */
    public Long getUserId(String token) {
        return parseClaims(token).get("memberId", Long.class);
    }

    public Boolean isExpired(String token) {

        try {
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
        }

        return false;
    }

    private Claims parseClaims(String token) {
        try{
            return
                    Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
        }catch (ExpiredJwtException e){
            return e.getClaims();
        }
    }


}
