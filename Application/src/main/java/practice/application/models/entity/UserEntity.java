package practice.application.models.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import practice.application.models.UserRole;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@Table(name = "users")
public class UserEntity {
    @Id
    @Column(name = "email", length = 50, nullable = false)
    private String userEmail;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", length = 50)
    private UserRole userRole;

    @Column(name = "name", length = 100)
    private String userName;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, columnDefinition = "DATETIME(6)")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", columnDefinition = "DATETIME(6)")
    private LocalDateTime updatedAt;

}