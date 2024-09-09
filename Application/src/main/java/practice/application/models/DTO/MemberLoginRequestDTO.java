package practice.application.models.DTO;

import lombok.Data;

@Data
public class MemberLoginRequestDTO {

    private String email;

    private String password;

    public MemberLoginRequestDTO(String username, String password) {
        this.email = username;
        this.password = password;
    }

    public MemberLoginRequestDTO() {
    }
}
