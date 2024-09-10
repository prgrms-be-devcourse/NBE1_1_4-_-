package practice.application.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import practice.application.models.MemberEntity;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

    Boolean existsByEmail(String email);

    Optional<MemberEntity> findByEmail(String email);
}
