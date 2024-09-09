package practice.application.models.dto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import practice.application.models.UserRole;
import practice.application.models.entity.OrderEntity;
import practice.application.models.entity.UserEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class UserDTO {

    private String userEmail;
    private UserRole userRole;
    private String userName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public UserDTO(UserEntity userEntity) {
        this.userEmail=userEntity.getUserEmail();
        this.userRole = userEntity.getUserRole();
        this.userName = userEntity.getUserName();
        this.createdAt = userEntity.getCreatedAt();
    }

    public UserEntity toUserEntity() {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserEmail(userEmail);
        userEntity.setUserRole(this.userRole);
        userEntity.setUserName(this.userName);
        userEntity.setCreatedAt(this.createdAt);
        userEntity.setUpdatedAt(this.updatedAt);
        return userEntity;
    }

}
