package practice.application.models.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Access, Refresh token 컨테이너
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenContainer {
    private String accessToken;
    private String refreshToken;
}
