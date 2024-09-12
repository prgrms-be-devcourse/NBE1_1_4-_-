package practice.application.models.DTO;

import lombok.Data;

/**
 * 로그아웃 응답 {@code DTO}
 */
@Data
public class MemberLogoutResponseDTO {
    private String message;
    private String detail;

    public MemberLogoutResponseDTO() {
    }

    public MemberLogoutResponseDTO(String message) {
        this.message = message;
    }

    public MemberLogoutResponseDTO(String result, String detail) {
        this.message = result;
        this.detail  = detail;
    }
}
