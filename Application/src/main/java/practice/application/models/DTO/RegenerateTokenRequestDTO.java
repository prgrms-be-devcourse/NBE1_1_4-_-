package practice.application.models.DTO;

import lombok.Data;
import practice.application.controllers.MemberController;
import practice.application.service.MemberService;

/**
 * Refresh 토큰으로 Access, Refresh 재성하는 요청 {@code DTO}
 *
 * @see MemberService#regenerateTokensViaRefreshToken
 * @see MemberController#regenerateTokens
 */
@Data
public class RegenerateTokenRequestDTO {
    private String refreshToken;
}
