package practice.application.models.DTO;

import lombok.Data;
import practice.application.models.enumType.UserType;

/**
 * 회원가입 응답용 {@code DTO}, {@link MemberJoinRequestDTO} 랑 거의 비슷함. (비밀번호만 없음)
 *
 * @see MemberJoinRequestDTO
 */
@Data
public class MemberJoinResponseDTO {
    private String email;
    private String name;
    private String phoneNumber;
    private String city;
    private String street;
    private String zipcode;
    private UserType userType;
}
