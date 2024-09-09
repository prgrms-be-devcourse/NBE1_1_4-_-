package practice.application.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.ColumnDefault;
import practice.application.models.dto.MemberDTO;

import java.util.UUID;

@Entity
@Table(name = "members")
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
public class MemberEntity implements EntityContracts<MemberDTO> {
    @Id
    @GeneratedValue
    @ColumnDefault("(unhex(replace(uuid(),_utf8mb4'-',_utf8mb4'')))")
    @Column(name = "member_identifier", nullable = false, length = 16)
    private UUID memberIdentifier;

    @Column(name = "member_id", nullable = false, length = 30)
    private String memberId;

    @Column(name = "member_pw", nullable = false)
    private String memberPw;

    @Column(name = "member_name", length = 30)
    private String memberName;

    @Lob
    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private MemberRole role;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Override
    public MemberDTO toDTO() {
        return new MemberDTO().setMemberId(memberId)
                              .setMemberPw(memberPw)
                              .setMemberName(memberName);
    }

    @Override
    public String toString() {
        return "MemberEntity{" + "memberIdentifier=" + memberIdentifier + ", memberId='" + memberId + '\'' +
               ", memberPw='" + memberPw + '\'' + ", memberName='" + memberName + '\'' + ", role=" + role +
               ", refreshToken='" + refreshToken + '\'' + '}';
    }
}