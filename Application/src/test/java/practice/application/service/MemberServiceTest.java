package practice.application.service;

import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.transaction.annotation.Transactional;
import practice.application.models.DTO.MemberJoinRequestDTO;
import practice.application.models.DTO.MemberLoginRequestDTO;
import practice.application.models.MemberEntity;
import practice.application.models.enumType.UserType;
import practice.application.models.exception.DuplicateEmailException;
import practice.application.repositories.MemberRepository;

import static org.junit.jupiter.api.Assertions.assertThrows;

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
    @DisplayName("멤버 서비스 테스트")
    public void memberServiceTest() throws Exception {
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
    @DisplayName("중복 이메일 테스트")
    public void duplicateEmailTest() throws Exception {
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

    @Test
    @DisplayName("로그인 후 토큰 값 잘 찍히나")
    public void testTokenOnLogin() throws Exception {
       //given
        MemberLoginRequestDTO memberLoginRequestDTO = new MemberLoginRequestDTO("k12002@nate.com", "1234");

        //when
        String token = memberService.login(memberLoginRequestDTO);

        //then

        System.out.println("token = " + token);


    }

    @Test
    @DisplayName("로그인 예외처리")
    public void loginExceptionHandlingTest() throws Exception {
       //given
        MemberLoginRequestDTO memberLoginRequestDTO = new MemberLoginRequestDTO("k12002@nate.com", "123");
       //when
        assertThrows(BadCredentialsException.class, ()->
                memberService.login(memberLoginRequestDTO));
       //then
    }

}