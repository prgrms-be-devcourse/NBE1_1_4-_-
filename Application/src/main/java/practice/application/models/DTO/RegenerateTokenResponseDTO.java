package practice.application.models.DTO;

import lombok.Data;

/**
 * Refresh 토큰으로 Access, Refresh 재성하는 요청 응답용 {@code DTO}
 *
 * @see RegenerateTokenRequestDTO
 */
@Data
public class RegenerateTokenResponseDTO {
    private TokenContainer tokens;

    public RegenerateTokenResponseDTO(TokenContainer tokens) {
        this.tokens = tokens;
    }
}
