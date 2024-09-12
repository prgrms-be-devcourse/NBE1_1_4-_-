package practice.application.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import practice.application.models.MemberEntity;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

    Boolean existsByEmail(String email);

    Optional<MemberEntity> findByEmail(String email);

    @Query("select distinct m from MemberEntity m left join fetch m.orderEntityList oe where m.id = :memberId")
    Optional<MemberEntity> findFetchById(@Param("memberId") Long memberId);
  
  
    Optional<MemberEntity> findByRefreshToken(String refreshToken);
}
