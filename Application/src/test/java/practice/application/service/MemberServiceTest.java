package practice.application.service;

import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.transaction.annotation.Transactional;
import practice.application.models.DTO.MemberJoinRequestDTO;
import practice.application.models.DTO.MemberLoginRequestDTO;
import practice.application.models.DTO.MemberLogoutRequestDTO;
import practice.application.models.DTO.TokenContainer;
import practice.application.models.Jwt.JwtUtil;
import practice.application.models.MemberEntity;
import practice.application.models.enumType.UserType;
import practice.application.models.exception.DuplicateEmailException;
import practice.application.models.exception.NotFoundException;
import practice.application.repositories.MemberRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private EntityManager em;

    private final String testingEmail = "k12002@nate.com";
    private final String testingPassword = "1234";
    private String refreshToken = null;

    @BeforeEach
    void setUp() {  // 각 테스트마다 필요한 이메일 넣는 메서드, 이미 넣어져 있으면 넘어감
        MemberJoinRequestDTO memberJoinRequestDTO = new MemberJoinRequestDTO(
                testingEmail, "리", testingPassword, "010", "도봉구", "84", "집주소", UserType.CUSTOMER);

        try {
            memberService.save(memberJoinRequestDTO);
        }
        catch (DuplicateEmailException e) {
            // User already saved in DB
        }

        // refresh 토큰 테스트 위해 DB 기록되는 토큰 가져와놓기
        MemberLoginRequestDTO memberLoginRequestDTO = new MemberLoginRequestDTO(testingEmail, testingPassword);
        TokenContainer tokens = memberService.loginWithTokenContainer(memberLoginRequestDTO);
        refreshToken = tokens.getRefreshToken();
    }

    @Test
    @Order(1)
    @DisplayName("멤버 서비스 테스트")
    public void memberServiceTest() throws Exception {
        //given
        String tempoEmail = "kdsfnkzkxssldkfnadf@nate.com";
        MemberJoinRequestDTO joinRequestDTO = new MemberJoinRequestDTO(
                tempoEmail, "testing", testingPassword, "010", "도봉구", "84", "집주소", UserType.CUSTOMER);

        //when
        Long save = memberService.save(joinRequestDTO);

        em.flush();
        em.clear();

        MemberEntity memberEntity = memberRepository
                .findById(save)
                .orElseThrow();

        //then

        Assertions
                .assertThat(memberEntity.getEmail())
                .isEqualTo(tempoEmail);
    }

    @Test
    @Order(2)
    @DisplayName("회원가입 중복 이메일 테스트")
    public void duplicateEmailTest() throws Exception {
        MemberJoinRequestDTO memberJoinRequestDTO = new MemberJoinRequestDTO(
                testingEmail, "리", testingPassword, "010", "도봉구", "84", "집주소", UserType.CUSTOMER);

        assertThrows(DuplicateEmailException.class, () -> memberService.save(memberJoinRequestDTO));
    }

    @Test
    @Order(3)
    @DisplayName("로그인 후 토큰 값 잘 찍히나")
    @Transactional
    public void testTokenOnLogin() throws Exception {
        MemberLoginRequestDTO loginRequestDTO = new MemberLoginRequestDTO(testingEmail, testingPassword);

        // 로그인 시 access, refresh 둘다 새로 생성해서 반환
        TokenContainer tokens = memberService.loginWithTokenContainer(loginRequestDTO);

        // 확인하기 위한 member 엔티티 가져오기
        MemberEntity member = memberRepository
                .findByEmail(testingEmail)
                .orElseThrow(NotFoundException::new);

        // refresh 토큰 생성 됬는지 확인
        assertNotNull(member.getRefreshToken());

        // 토큰 내용 꺼내기
        String accessToken = tokens.getAccessToken();
        String refreshToken = tokens.getRefreshToken();

        // 토큰에 저장된 유저 id 꺼내기
        Long idViaAccessToken = jwtUtil.getUserIdWithAccessToken(accessToken);
        Long idViaRefreshToken = jwtUtil.getUserIdWithRefreshToken(refreshToken);

        // 토큰에 저장된 id 랑 유저 id 맞는지 확인
        // DB 에 저장된 refresh 토큰이랑 생성된 거 맞는지 확인
        assertAll(() -> {
            assertEquals(idViaAccessToken, member.getId());
            assertEquals(idViaRefreshToken, member.getId());
            assertEquals(member.getRefreshToken(), refreshToken);
        });
    }

    @Test
    @Order(4)
    @DisplayName("로그인 예외처리")
    @Transactional
    public void loginExceptionHandlingTest() throws Exception {
        //given
        MemberLoginRequestDTO memberLoginRequestDTO = new MemberLoginRequestDTO(testingEmail, "123");
        //when
        assertThrows(BadCredentialsException.class, () -> memberService.login(memberLoginRequestDTO));
        //then
    }

    @Test
    @DisplayName("로그아웃 시 refresh 토큰 DB 에서 잘 없어졌는지 확인")
    @Order(5)
    @Transactional
    public void logoutTest() throws Exception {
        MemberLoginRequestDTO loginRequestDTO = new MemberLoginRequestDTO(testingEmail, testingPassword);

        // 로그인해서 DB 에 refresh 토큰 생성됨
        memberService.loginWithTokenContainer(loginRequestDTO);

        MemberLogoutRequestDTO logoutRequestDTO = new MemberLogoutRequestDTO(testingEmail);

        // 로그아웃해서 DB 에 토큰 없어져야 됨
        memberService.logout(logoutRequestDTO);

        // DB 에서 멤버 엔티티 가져옴
        MemberEntity member = memberRepository
                .findByEmail(testingEmail)
                .orElseThrow(NotFoundException::new);

        // 없어졌는지 확인
        assertNull(member.getRefreshToken());
    }

    @Test
    @DisplayName("Refresh 토큰 주어지면 access, refresh 잘 생성하고 DB 에 잘 update 되는지")
    @Order(6)
    @Transactional
    public void regenerateTokenViaRefreshTokenTest() throws Exception {
        // 토큰 줘서 서비스가 뱉어낸 결과
        TokenContainer regeneratedTokens = memberService.regenerateTokensViaRefreshToken(refreshToken);

        // 토큰 제대로 저장됐는지 확인하기 위해 멤버 꺼내기
        MemberEntity member = memberRepository
                .findByEmail(testingEmail)
                .orElseThrow(NotFoundException::new);

        // 서비스가 뱉어낸 결과 null 아니고 refresh 토큰 제대로 다시 저장 됐는지 확인
        assertAll(() -> {
            assertNotNull(regeneratedTokens.getAccessToken());
            assertNotNull(regeneratedTokens.getRefreshToken());
            assertEquals(member.getRefreshToken(), regeneratedTokens.getRefreshToken());
        });

    }
}