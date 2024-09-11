package practice.application.models;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import practice.application.models.enumType.MembershipGrade;
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

    @OneToMany(mappedBy = "member")
    private List<OrderEntity> orderEntityList = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private UserType userType;

    @Embedded
    private Address address;

    private int totalAmount = 0; // 현재까지 총 결제 금액

    public MemberEntity(String email, String name, String password, String phoneNumber, UserType userType, Address address) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.userType = userType;
        this.address = address;
    }

    public MembershipGrade getGrade() {
        if (this.totalAmount < 100000) {
            return MembershipGrade.GOLD;
        } else if (this.totalAmount < 500000) {
            return MembershipGrade.VIP;
        } else {
            return MembershipGrade.VVIP;
        }
    }

    public double applyDiscount(int originalPrice) {
        MembershipGrade grade = getGrade();
        switch (grade) {
            case GOLD:
                return originalPrice;
            case VIP:
                return originalPrice * 0.90; // 10% 할인
            case VVIP:
                return originalPrice * 0.80; // 20% 할인
            default:
                return originalPrice;
        }
    }

    public void encodePassword(String password){
        this.password = password;
    }

    public void updateTotalAmount(int amount) {
        this.totalAmount += amount;
    }

    public void updateTotalAmountOnCancellation(int amount) {
        this.totalAmount -= amount;
    }

}
