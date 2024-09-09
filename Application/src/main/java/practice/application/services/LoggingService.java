package practice.application.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import practice.application.models.entities.MemberEntity;
import practice.application.repositories.MemberRepository;
import practice.application.utils.JWTTokenManager;

@Service
public class LoggingService {

    private final MemberRepository memberRepo;
    private final JWTTokenManager tokenManager;
    private final PasswordEncoder passwordEncoder;

    @Value("${access_token_expiration}")
    private int accessTokenExpiration;
    @Value("${refresh_token_expiration}")
    private int refreshTokenExpiration;

    @Autowired
    public LoggingService(MemberRepository memberRepo, JWTTokenManager tokenManager, PasswordEncoder passwordEncoder) {
        this.memberRepo      = memberRepo;
        this.tokenManager    = tokenManager;
        this.passwordEncoder = passwordEncoder;
    }

    public MemberEntity registerNewMember(MemberEntity member) {
        return memberRepo.save(encryptPw(member));
    }

    public MemberEntity login(MemberEntity member) {
        MemberEntity searchResult = memberRepo.findByMemberId(member.getMemberId());
        return searchResult == null || !passwordEncoder.matches(member.getMemberPw(), searchResult.getMemberPw()) ?
               null :
               searchResult;
    }

    public MemberEntity findByMemberName(String memberName) {
        return memberRepo.findByMemberName(memberName);
    }

    public boolean isThereDuplicates(String id) {
        return memberRepo.findByMemberId(id) != null;
    }

    public String publishAccessToken(MemberEntity member) {
        return tokenManager.createAccessToken(member, accessTokenExpiration);
    }

    public String refreshAccessToken(MemberEntity member) {
        return tokenManager.createRefreshToken(member, refreshTokenExpiration);
    }

    public String parseAccessTokenToMemberName(String token) {
        return tokenManager.validateAccessToken(token);
    }

    public String parseRefreshTokenToMemberName(String token) {
        return tokenManager.validateRefreshToken(token);
    }

    private MemberEntity encryptPw(MemberEntity member) {
        return member.setMemberPw(passwordEncoder.encode(member.getMemberPw()));
    }
}
