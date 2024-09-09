package practice.application.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import practice.application.models.DTO.MemberJoinRequestDTO;
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




}
