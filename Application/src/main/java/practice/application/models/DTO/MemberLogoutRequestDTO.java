package practice.application.models.DTO;

import lombok.Data;

/**
 * 로그아웃 요청 응답용 {@code DTO}
 */
@Data
public class MemberLogoutRequestDTO {
    private String email;
    private String name;

    public MemberLogoutRequestDTO(String email) {
        this.email = email;
    }
}
