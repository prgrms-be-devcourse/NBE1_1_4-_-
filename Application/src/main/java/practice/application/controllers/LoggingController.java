package practice.application.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import practice.application.models.dto.MemberDTO;
import practice.application.models.entities.MemberEntity;
import practice.application.models.entities.MemberRole;
import practice.application.services.LoggingService;

@RestController
@RequestMapping("/logging")
public class LoggingController {

    private final LoggingService loggingService;

    @Autowired
    public LoggingController(LoggingService loggingService) {
        this.loggingService = loggingService;
    }

    /**
     * 로그인 엔드포인트
     *
     * <li>로그인에 반드시 필요한 {@link MemberDTO} {@code Fields} :
     * <pre class="code">
     *      String memberId;
     *      String memberPw;
     *  </pre>
     *
     * @param member 로그인할 유저 정보
     * @return {@link ResponseEntity}
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody MemberDTO member) {

        if (member.getMemberId() == null || member.getMemberPw() == null)
            return ResponseEntity.badRequest()
                                 .body("ID & Password required");

        MemberEntity searchResult = loggingService.login(member.toEntity());

        if (searchResult == null)
            return ResponseEntity.badRequest()
                                 .body("No such member found");

        String accessToken = loggingService.publishAccessToken(searchResult);

        MemberDTO response = searchResult.toDTO();
        response.setAccessToken(accessToken);
        response.setRefreshToken(null)
                .setMemberPw(null);

        return ResponseEntity.ok(response);
    }

    /**
     * 회원가입 엔드포인트
     *
     * <li>회원가입에 반드시 필요한 {@link MemberDTO} {@code Fields} :
     * <pre class="code">
     *      String memberId;
     *      String memberPw;
     *      String memberName;
     *  </pre>
     *
     * @param member 로그인할 유저 정보
     * @return {@link ResponseEntity}
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody MemberDTO member) {
        String id = member.getMemberId();

        if (id == null || member.getMemberPw() == null || member.getMemberName() == null)
            return ResponseEntity.badRequest()
                                 .body("ID, Password & user name required");

        if (loggingService.isThereDuplicates(id))
            return ResponseEntity.badRequest()
                                 .body("Given ID already exists");

        MemberDTO response = loggingService.registerNewMember(member.toEntity()
                                                                    .setRole(MemberRole.USER))
                                           .toDTO()
                                           .setMemberPw(null);

        return ResponseEntity.ok(response);
    }
}
