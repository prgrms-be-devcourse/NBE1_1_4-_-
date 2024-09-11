package practice.application.models.DTO;

import lombok.Data;

/**
 * Access, Refresh 토큰 둘다 담긴 로그인 응답용 {@code DTO}
 */
@Data
public class MemberLoginResponseWithTokensDTO {

    private String email;

    private TokenContainer tokens;

    public MemberLoginResponseWithTokensDTO() {}

    public MemberLoginResponseWithTokensDTO(String email, TokenContainer tokens) {
        this.email = email;
        this.tokens = tokens;
    }

    public MemberLoginResponseWithTokensDTO(String email, String accessToken, String refreshToken) {
        this.email = email;
        this.tokens = new TokenContainer(accessToken, refreshToken);
    }
}
