package practice.application.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import practice.application.exceptions.InvalidTokenException;
import practice.application.exceptions.TokenExpiredException;
import practice.application.models.entities.MemberEntity;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JWTTokenManager {
    @Value("${access_token_key}")
    private String ACCESS_TOKEN_KEY;

    @Value("${refresh_token_key}")
    private String REFRESH_TOKEN_KEY;


    //<editor-fold desc="Create Tokens">
    public String createAccessToken(MemberEntity memberEntity, long min) {
        return createToken(Decoders.BASE64.decode(ACCESS_TOKEN_KEY), memberEntity.getMemberName(), min);
    }

    public String createRefreshToken(MemberEntity memberEntity, long min) {
        return createToken(Decoders.BASE64.decode(REFRESH_TOKEN_KEY), memberEntity.getMemberName(), min);
    }

    private String createToken(byte[] key, String memberName, long min) {
        return Jwts.builder()
                   .signWith(getTokenKey(key))
                   .subject(memberName)
                   .issuer("Grids & Circles")
                   .issuedAt(new Date())
                   .expiration(new Date(System.currentTimeMillis() + min * 60L * 1000L))
                   .compact();
    }
    //</editor-fold>

    //<editor-fold desc="Validate Token">
    public String validateAccessToken(String token) {
        return validateToken(Decoders.BASE64.decode(ACCESS_TOKEN_KEY), token);
    }

    public String validateRefreshToken(String token) {
        return validateToken(Decoders.BASE64.decode(REFRESH_TOKEN_KEY), token);
    }

    private String validateToken(byte[] key, String token) {
        try {
            Claims claims = Jwts.parser()
                                .verifyWith(getTokenKey(key))
                                .build()
                                .parseSignedClaims(token)
                                .getPayload();

            return claims.getSubject();
        }
        catch (SecurityException | MalformedJwtException e) {
            throw new InvalidTokenException("유효하지 않은 토큰입니다.");
        }
        catch (ExpiredJwtException e) {
            throw new TokenExpiredException("주어진 토큰이 만료되었습니다.");
        }
    }
    //</editor-fold>

    private SecretKey getTokenKey(byte[] keyBytes) {
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
