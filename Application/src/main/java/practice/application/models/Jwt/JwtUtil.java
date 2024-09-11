package practice.application.models.Jwt;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import practice.application.models.DTO.TokenContainer;
import practice.application.models.MemberEntity;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Slf4j
@Component
public class JwtUtil {

    private final SecretKey accessTokensecretKey;
    private final long accessTokenExpTime;

    private final SecretKey refreshTokenSecretKey;
    private final long refreshTokenExpTime;

    /**
     * 토큰별 {@link SecretKey} 생성하는 메서드
     *
     * @param secretKey {@code Spring} 시크릿 키 문자열
     * @return {@link SecretKey}
     */
    private SecretKey genSecretKey(String secretKey) {
        return new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256
                .key()
                .build()
                .getAlgorithm());
    }

    /**
     * {@code Spring} 이 필요한 값들 주입하는 생성자
     *
     * @param accessTokenSecretKey  엑세스 토큰 시크릿 키
     * @param accessTokenExpTime    엑세스 토큰 만료 시간 {@code (ms)}
     * @param refreshTokenSecretKey 리프레쉬 토큰 시크릿 키
     * @param refreshTokenExpTime   리프레쉬 토큰 만료 시간 {@code (ms)}
     */
    public JwtUtil(
            @Value("${spring.jwt.access-token.secret}") String accessTokenSecretKey,
            @Value("${spring.jwt.access-token.expiration_time}") long accessTokenExpTime,
            @Value("${spring.jwt.refresh-token.secret}") String refreshTokenSecretKey,
            @Value("${spring.jwt.refresh-token.expiration_time}") long refreshTokenExpTime
    ) {
        this.accessTokenExpTime    = accessTokenExpTime;
        this.refreshTokenExpTime   = refreshTokenExpTime;
        this.accessTokensecretKey  = genSecretKey(accessTokenSecretKey);
        this.refreshTokenSecretKey = genSecretKey(refreshTokenSecretKey);
    }


    /**
     * Access 토큰만 생성하는 메서드
     *
     * @param member 멤버 엔티티
     * @return 엑세스 토큰 {@code String}
     */
    public String createAccessToken(MemberEntity member) {
        return createTokens(member, accessTokensecretKey, accessTokenExpTime);
    }

    /**
     * Refresh 토큰만 생성하는 메서드
     *
     * @param member 멤버 엔티티
     * @return 리프래쉬 토큰 {@code String}
     */
    public String createRefreshToken(MemberEntity member) {
        return createTokens(member, refreshTokenSecretKey, refreshTokenExpTime);
    }

    /**
     * Access, refresh 토큰 둘다 담은 {@link TokenContainer} 생성하는 메서드
     *
     * @param member 멤버 엔티티
     * @return 토큰 2 개 담긴 {@link TokenContainer}
     */
    public TokenContainer createTokens(MemberEntity member) {
        String accessToken = createTokens(member, accessTokensecretKey, accessTokenExpTime);
        String refreshToken = createTokens(member, refreshTokenSecretKey, refreshTokenExpTime);
        return new TokenContainer(accessToken, refreshToken);
    }


    /**
     * 토큰 생성 메서드
     *
     * @param member    멤버 엔티티
     * @param secretKey 토큰 시크릿 키
     * @param exp       토큰 만료 시간 {@code (ms)}
     * @return 토큰 {@code (String)}
     */
    private String createTokens(MemberEntity member, SecretKey secretKey, long exp) {
        return Jwts
                .builder()
                .claim("memberId", String.valueOf(member.getId()))
                .claim("email", member.getEmail())
                .claim("role", member.getUserType())
                .issuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + exp))
                .signWith(secretKey)
                .compact();

    }

    /**
     * Access Token 에서 User ID 꺼내는 메서드
     *
     * @param accessToken 엑세스 토큰
     * @return {@code User ID}
     */

    public Long getUserIdWithAccessToken(String accessToken) {
        return Long.valueOf(parseClaims(accessTokensecretKey, accessToken).get("memberId", String.class));
    }

    /**
     * Refresh Token 에서 User ID 꺼내는 메서드
     *
     * @param refreshToken 리프래쉬 토큰
     * @return {@code User ID}
     */
    public Long getUserIdWithRefreshToken(String refreshToken) {
        return Long.valueOf(parseClaims(refreshTokenSecretKey, refreshToken).get("memberId", String.class));
    }

    /**
     * Access 토큰 기간 만료됬는지 확인하는 메서드.
     * @param accessToken 엑세스 토큰
     * @return 토큰이 유효하지만 기한만 지나면 {@code true}, 유효하지 않거나 만료 안됐으면 {@code false}
     */
    public boolean isAccessTokenExpired(String accessToken) {
        return isExpired(accessToken, accessTokensecretKey);
    }

    /**
     * Refresh 토큰 기간 만료됬는지 확인하는 메서드.
     * @param refreshToken 엑세스 토큰
     * @return 토큰이 유효하지만 기한만 지나면 {@code true}, 유효하지 않거나 만료 안됐으면 {@code false}
     */
    public boolean isRefreshTokenExpired(String refreshToken) {
        return isExpired(refreshToken, refreshTokenSecretKey);
    }

    /**
     * 주어진 토큰 만료됬는지 확인하는 메서드
     * @param token 토큰
     * @param secretKey 토큰 시크릿 키
     * @return 토큰이 유효하지만 기한만 지나면 {@code true}, 유효하지 않거나 만료 안됐으면 {@code false}
     */
    private boolean isExpired(String token, SecretKey secretKey) {
        try {
            Jwts
                    .parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getExpiration()
                    .before(new Date());
            return true;
        }
        catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        }
        catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
        }
        catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        }
        catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
        }

        return false;
    }

    // TODO 사용되는 곳 없는데 그냥 isExpired(String token, SecretKey key) 로 시그니처 바꿔도 되는지 확인
    /**
     * @deprecated 사용되는 곳 없는데 그냥 isExpired(String token, SecretKey key) 로 사용하는게 좋을 수도 있음
     * @see #isExpired(String, SecretKey)
     */
    public Boolean isExpired(String token) {

        try {
            Jwts
                    .parser()
                    .verifyWith(accessTokensecretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getExpiration()
                    .before(new Date());
            return true;
        }
        catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        }
        catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
        }
        catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        }
        catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
        }

        return false;
    }

    /**
     * 주어진 토큰 파싱하는 메서드
     *
     * @param token 토큰
     * @return {@link Claims}
     * @throws UnsupportedJwtException 토큰 {@code SecretKey} 맞지 않을시
     */
    private Claims parseClaims(SecretKey secretKey, String token) throws UnsupportedJwtException {
        return Jwts
                .parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }


}
