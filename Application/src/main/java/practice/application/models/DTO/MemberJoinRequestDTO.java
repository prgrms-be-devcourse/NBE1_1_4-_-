package practice.application.models.DTO;

import lombok.Data;
import practice.application.models.Address;
import practice.application.models.MemberEntity;
import practice.application.models.enumType.UserType;

@Data
public class MemberJoinRequestDTO {



    private String email; //로그인은 이메일로

    private String name;

    private String password;

    private String phoneNumber;

    private String city;
    private String street;

    private String zipcode;

    private UserType userType;

    public MemberJoinRequestDTO(String email, String name, String password, String phoneNumber, String street, String city, String zipcode, UserType userType) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.street = street;
        this.city = city;
        this.zipcode = zipcode;
        this.userType = userType;
    }

    public MemberJoinRequestDTO() {
    }

    public MemberEntity toEntity(){
      return new MemberEntity(email, name, password, phoneNumber, userType, new Address(city, street, zipcode));
    }
}
