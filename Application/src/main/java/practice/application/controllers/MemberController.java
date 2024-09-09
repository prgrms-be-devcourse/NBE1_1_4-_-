package practice.application.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import practice.application.models.DTO.MemberJoinRequestDTO;
import practice.application.models.DTO.MemberLoginRequestDTO;
import practice.application.models.DTO.MemberLoginResponseDTO;
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


    @PostMapping("/login")
    public MemberLoginResponseDTO loginMember(@RequestBody MemberLoginRequestDTO memberLoginRequestDTO){
        String token = memberService.login(memberLoginRequestDTO);

        return new MemberLoginResponseDTO(token, memberLoginRequestDTO.getEmail());
    }




}
