package practice.application.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import practice.application.model.entity.UserEntity;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenProvider {
    private final String KEY = "anoaid93n1i190njanva91nkfadoa92klfdasdafasdfasdfadsfadsfasfasfdsfasdfasdvasdasdasd";

    public String createToken(UserEntity user, long expireTime){
        return Jwts.builder()
                .signWith(getTokenKey())
                .subject(String.valueOf(user.getId()))
                .issuer("programmers")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+expireTime))
                .compact();
    }

    public String validateUserToken(String token){
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(getTokenKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return claims.getSubject();
        } catch (SecurityException ex){
            throw new JwtException("잘못된 jwt 시그니처 입니다!!");
        } catch (MalformedJwtException ex){
            throw new JwtException(("유효하지 않은 토큰입니다! MalformedJwtException"));
        } catch(ExpiredJwtException ex){
            throw new JwtException("유효기간이 만료된 토큰입니다!");
        } catch(RuntimeException ex){
            ex.printStackTrace();
            throw ex;
        }
    }

    private SecretKey getTokenKey(){
        byte[] keyBytes = Decoders.BASE64.decode(this.KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
