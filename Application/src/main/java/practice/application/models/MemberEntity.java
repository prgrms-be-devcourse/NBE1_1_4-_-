package practice.application.models;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import practice.application.models.enumType.UserType;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "members")
public class MemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String email; //로그인은 이메일로

    private String name;

    private String password;

    private String phoneNumber;

    private String refreshToken;

    @OneToMany(mappedBy = "member")
    private List<OrderEntity> orderEntityList = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private UserType userType;

    @Embedded
    private Address address;

    // TODO [MemberEntity] Refresh Token 기능 구현 후 삭제해야 할 수도 있음.
    /**
     * @deprecated Refresh Token 기능 구현 후 삭제하는게 좋을 수도 있음.
     * @see #MemberEntity(String, String, String, String, String, UserType, Address)  MemberEntity
     */
    @Deprecated
    public MemberEntity(String email, String name, String password, String phoneNumber, UserType userType, Address address) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.userType = userType;
        this.address = address;
    }

    // Refresh Token 기능용 임시 생성자

    /**
     * Refresh Token 포함해서 엔티티 생성하는 생성자
     */
    public MemberEntity(String email, String name, String password, String phoneNumber, String refreshToken, UserType userType, Address address) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.refreshToken = refreshToken;
        this.userType = userType;
        this.address = address;
    }

    public void encodePassword(String password){
        this.password = password;
    }

    /**
     * Refresh Token 주입용 Setter
     * @param refreshToken 리프래쉬 토큰
     */
    @SuppressWarnings("LombokSetterMayBeUsed")
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
