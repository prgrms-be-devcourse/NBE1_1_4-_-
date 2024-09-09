package practice.application.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import practice.application.models.entities.MemberEntity;

import java.util.UUID;

public interface MemberRepository extends JpaRepository<MemberEntity, UUID> {

    @Query("SELECT m FROM MemberEntity m WHERE m.memberId = :id AND m.memberPw = :pw")
    MemberEntity findMember(String id, String pw);

    MemberEntity findByMemberId(String id);
}
