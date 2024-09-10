package practice.application.models.DTO;

import lombok.Data;

@Data
public class MemberLoginResponseDTO {

    private String email;

    private String token;

    public MemberLoginResponseDTO(String token, String email) {
        this.token = token;
        this.email = email;
    }

    public MemberLoginResponseDTO() {
    }
}
