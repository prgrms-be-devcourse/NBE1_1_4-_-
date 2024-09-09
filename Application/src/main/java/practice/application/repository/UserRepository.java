package practice.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import practice.application.model.entity.UserEntity;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findById(Long userId);
    Optional<UserEntity> findByEmail(String email);
    boolean existsByEmail(String email);
}
