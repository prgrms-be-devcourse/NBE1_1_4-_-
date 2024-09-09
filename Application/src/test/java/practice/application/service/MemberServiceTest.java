package practice.application.service;

import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import practice.application.models.Address;
import practice.application.models.DTO.MemberJoinRequestDTO;
import practice.application.models.MemberEntity;
import practice.application.models.enumType.UserType;
import practice.application.models.exception.DuplicateEmailException;
import practice.application.repositories.MemberRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional

class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private EntityManager em;

    @Test
    public void 멤버_서비스_테스트() throws Exception {
       //given
        MemberJoinRequestDTO memberJoinRequestDTO = new MemberJoinRequestDTO("k12002@nate.com", "리", "1234", "010", "도봉구", "84", "집주소", UserType.CUSTOMER);


        //when
        Long save = memberService.save(memberJoinRequestDTO);

        em.flush();
        em.clear();

        MemberEntity memberEntity = memberRepository.findById(save).get();

        //then

        Assertions.assertThat(memberEntity.getEmail()).isEqualTo("k12002@nate.com");
    }

    @Test
    public void 중복_이메일_테스트() throws Exception {
       //given

        MemberJoinRequestDTO memberJoinRequestDTO = new MemberJoinRequestDTO("k12002@nate.com", "리", "1234", "010", "도봉구", "84", "집주소", UserType.CUSTOMER);


        //when
        Long save = memberService.save(memberJoinRequestDTO);

        em.flush();
        em.clear();


       //when
        assertThrows(DuplicateEmailException.class, ()->
                memberService.save(memberJoinRequestDTO));

       //then
    }

}