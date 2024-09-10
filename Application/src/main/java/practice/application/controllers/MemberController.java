package practice.application.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import practice.application.models.DTO.*;
import practice.application.service.MemberService;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/members")

public class MemberController {

    private final MemberService memberService;

    @PostMapping()
    public Long joinMember(@RequestBody MemberJoinRequestDTO memberJoinRequestDTO){
        Long save = memberService.save(memberJoinRequestDTO);

        return save;
    }


    /**
     * 유저 로그인 시 Access 토큰 담아서 응답하는 endpoint
     * @deprecated 이제 로그인 반응에 토큰 2 개 담아야 되므로 삭제 or 변경될 가능성 높음.
     * @param memberLoginRequestDTO 로그인 요청 {@code DTO}
     * @return {@link MemberLoginResponseDTO}
     * @see #loginMemberWithToken(MemberLoginRequestDTO)
     */
    @PostMapping("/login")
    @Deprecated
    public MemberLoginResponseDTO loginMember(@RequestBody MemberLoginRequestDTO memberLoginRequestDTO){
        String token = memberService.login(memberLoginRequestDTO);

        return new MemberLoginResponseDTO(token, memberLoginRequestDTO.getEmail());
    }

    /**
     * 유저 로그인 시 Access, Refresh 토큰 담아서 응답하는 endpoint
     * @param memberLoginRequestDTO 로그인 요청 {@code DTO}
     * @return {@link MemberLoginResponseWithTokensDTO}
     * @see #loginMember(MemberLoginRequestDTO)
     */
    @PostMapping("/login-with-token")
    public MemberLoginResponseWithTokensDTO loginMemberWithToken(@RequestBody MemberLoginRequestDTO memberLoginRequestDTO){
        TokenContainer tokens = memberService.loginWithTokenContainer(memberLoginRequestDTO);

        return new MemberLoginResponseWithTokensDTO(memberLoginRequestDTO.getEmail(), tokens);
    }


}
