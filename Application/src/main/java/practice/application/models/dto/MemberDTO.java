package practice.application.models.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import practice.application.models.entities.MemberEntity;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
public class MemberDTO implements DTOContracts<MemberEntity> {

    private String memberId;
    private String memberPw;
    private String memberName;

    private String accessToken;
    private String refreshToken;

    @Override
    public MemberEntity toEntity() {
        return new MemberEntity().setMemberId(memberId)
                                 .setMemberPw(memberPw)
                                 .setMemberName(memberName);
    }

    @Override
    public String toString() {
        return "MemberDTO{" + "memberId='" + memberId + '\'' + ", memberPw='" + memberPw + '\'' + ", memberName='" +
               memberName + '\'' + '}';
    }
}
