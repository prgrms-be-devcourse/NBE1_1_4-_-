package practice.application.models.DTO;

import lombok.Data;

@Data
public class MemberLoginRequestDTO {

    private String email;

    private String password;

    public MemberLoginRequestDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public MemberLoginRequestDTO() {
    }
}
