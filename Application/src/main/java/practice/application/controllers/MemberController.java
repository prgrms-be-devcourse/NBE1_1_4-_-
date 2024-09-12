package practice.application.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import practice.application.models.DTO.*;
import practice.application.service.MemberService;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/members")

public class MemberController {

    private final MemberService memberService;

    /**
     * 회원가입 시켜주는 endpoint
     *
     * @param memberJoinRequestDTO 회원가입 요청 {@code DTO}
     * @return 회원에게 assign 된 {@code ID} {@code (Long)}
     * @deprecated endpoint {@code /members/register} 인게 더 낫지 않나
     */
    @PostMapping()
    public Long joinMember(@RequestBody MemberJoinRequestDTO memberJoinRequestDTO) {
        Long save = memberService.save(memberJoinRequestDTO);

        return save;
    }

    /**
     * 회원가입 시켜주는 endpoint
     *
     * @param joinRequestDTO 회원가입 요청 {@code DTO}
     * @return {@link MemberJoinResponseDTO} 응답 {@code DTO}
     */
    @PostMapping("/register")
    public MemberJoinResponseDTO registerMember(@RequestBody MemberJoinRequestDTO joinRequestDTO) {
        memberService.save(joinRequestDTO);
        return joinRequestDTO.toResponseDTO();
    }


    /**
     * 유저 로그인 시 Access 토큰 담아서 응답하는 endpoint
     *
     * @param memberLoginRequestDTO 로그인 요청 {@code DTO}
     * @return {@link MemberLoginResponseDTO}
     * @see #loginMemberWithToken(MemberLoginRequestDTO)
     * @deprecated 이제 로그인 반응에 토큰 2 개 담아야 되므로 삭제 or 변경될 가능성 높음.
     */
    @PostMapping("/login")
    @Deprecated
    public MemberLoginResponseDTO loginMember(@RequestBody MemberLoginRequestDTO memberLoginRequestDTO) {
        String token = memberService.login(memberLoginRequestDTO);

        return new MemberLoginResponseDTO(token, memberLoginRequestDTO.getEmail());
    }

    /**
     * 유저 로그인 시 Access, Refresh 토큰 담아서 응답하는 endpoint
     *
     * @param memberLoginRequestDTO 로그인 요청 {@code DTO}
     * @return {@link MemberLoginResponseWithTokensDTO}
     * @see #loginMember(MemberLoginRequestDTO)
     */
    @PostMapping("/login-with-token")
    public MemberLoginResponseWithTokensDTO loginMemberWithToken(
            @RequestBody MemberLoginRequestDTO memberLoginRequestDTO
    ) {
        TokenContainer tokens = memberService.loginWithTokenContainer(memberLoginRequestDTO);

        return new MemberLoginResponseWithTokensDTO(memberLoginRequestDTO.getEmail(), tokens);
    }

    /**
     * 유저 로그아웃 요청으로 Refresh 토큰 없애는 endpoint
     *
     * @param logoutRequestDTO 로그아웃 요청 {@code DTO}
     * @return {@link MemberLogoutResponseDTO}
     */
    @PatchMapping("/logout")
    public MemberLogoutResponseDTO logoutMember(@RequestBody MemberLogoutRequestDTO logoutRequestDTO) {
        return memberService.logout(logoutRequestDTO);
    }
}
