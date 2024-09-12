package practice.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import practice.application.models.DTO.*;
import practice.application.models.Jwt.JwtUtil;
import practice.application.models.MemberEntity;
import practice.application.models.exception.DuplicateEmailException;
import practice.application.models.exception.NotFoundException;
import practice.application.repositories.MemberRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtUtil jwtUtil;

    /**
     * 멤버 회원가입하는 메서드
     *
     * @param memberJoinRequestDTO 회원가입 요청 {@code DTO}
     * @return 회원가입 후 유저에게 지정된 {@code ID (Long)}
     * @throws DuplicateEmailException 중복된 이메일로 가입하려하면 throw
     */
    @Transactional
    public Long save(MemberJoinRequestDTO memberJoinRequestDTO) {
        String email = memberJoinRequestDTO.getEmail();

        if (memberRepository.existsByEmail(email))
            throw new DuplicateEmailException("해당 이메일은 이미 존재합니다");

        MemberEntity entity = memberJoinRequestDTO.toEntity();

        entity.encodePassword(bCryptPasswordEncoder.encode(entity.getPassword()));

        memberRepository.save(entity);

        return entity.getId();

    }

    /**
     * 로그인해주는 메서드
     *
     * @return 엑세스 토큰 {@code String}
     * @throws BadCredentialsException 비밀번호 틀리면 throw
     * @throws NotFoundException       계정 없으면 throw
     * @see #loginWithTokenContainer(MemberLoginRequestDTO)
     * @deprecated Refresh token 기능 도입 후 없는게 날수도 있음
     */
    @Deprecated
    public String login(MemberLoginRequestDTO memberLoginRequestDTO) {
        MemberEntity memberEntity = memberRepository
                .findByEmail(memberLoginRequestDTO.getEmail())
                .orElseThrow(() -> new NotFoundException("이메일이 존재하지 않습니다"));


        if (!bCryptPasswordEncoder.matches(memberLoginRequestDTO.getPassword(), memberEntity.getPassword())) {
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다");
        }

        String accessToken = jwtUtil.createAccessToken(memberEntity);

        return accessToken;


    }

    /**
     * 로그인 요청 받아서 로그인 처리 해주는 메서드.
     *
     * @param loginRequestDTO 로그인 요청 {@code DTO}
     * @return {@link TokenContainer} Access, Refresh 토큰 둘다 담긴 컨테이너
     * @throws BadCredentialsException 비밀번호 일치 안하면 throw
     * @throws NotFoundException       계정 없으면 throw
     */
    @Transactional
    public TokenContainer loginWithTokenContainer(MemberLoginRequestDTO loginRequestDTO) {
        // DB 에서 이메일로 유저 정보 가져오기
        MemberEntity memberEntity = memberRepository
                .findByEmail(loginRequestDTO.getEmail())
                .orElseThrow(() -> new NotFoundException("이메일이 존재하지 않습니다"));

        // 비밀번호 틀림
        if (!bCryptPasswordEncoder.matches(loginRequestDTO.getPassword(), memberEntity.getPassword()))
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다");

        // Access, Refresh 토큰 생성
        TokenContainer tokens = jwtUtil.createTokens(memberEntity);
        String refreshToken = tokens.getRefreshToken();

        // Refresh 토큰 엔티티에 주입해서 DB 에 저장
        memberEntity.setRefreshToken(refreshToken);
        memberRepository.save(memberEntity);

        return tokens;
    }


    /**
     * 로그아웃 요청 받아서 DB refresh 토큰 없애는 메서드
     *
     * @param logoutRequestDTO 로그아웃 요청 {@code DTO}
     * @return {@link MemberLogoutResponseDTO}
     * @throws NotFoundException 요청 이메일에 해당하는 계정 없을 시
     */
    @Transactional
    public MemberLogoutResponseDTO logout(MemberLogoutRequestDTO logoutRequestDTO) {
        MemberEntity member = memberRepository
                .findByEmail(logoutRequestDTO.getEmail())
                .orElseThrow(() -> new NotFoundException("이메일이 존재하지 않습니다"));

        String refreshToken = member.getRefreshToken();
        member.setRefreshToken(null);

        return new MemberLogoutResponseDTO(
                "성공적으로 로그아웃 되었습니다.", "Refresh token [" + refreshToken + "] has been expired.");
    }

    /**
     * 주어진 {@code refreshToken} 맞는 유저 골라서 Access, Refresh 토큰 재생성하는 메서드.
     * <p>
     * 유저 찾으면 재생성된 Refresh 토큰 DB 에 저장함.
     *
     * @param refreshToken 리프래쉬 토큰 {@code (String)}
     * @return {@link TokenContainer}
     * @throws NotFoundException 주어진 토큰에 해당하는 멤버 못찾으면 throw
     */
    @Transactional
    public TokenContainer regenerateTokensViaRefreshToken(String refreshToken) {
        MemberEntity member = memberRepository
                .findByRefreshToken(refreshToken)
                .orElseThrow(() -> new NotFoundException("주어진 토큰과 매칭되는 계정이 없습니다."));

        // 애초에 토큰으로 멤버를 찾으므로 이 아래는 매칭되는 멤버가 반드시 존재함
        // --> 주어진 토큰이랑 멤버에 저장된 토큰은 반드시 일치한다. 굳이 확인할 필요 없다.
        // 그냥 refreshToken 말고 더 정보가 없는 이상 더 좋은 솔루션은 모르겠음

        // 토큰 재생성 후 멤버 정보로 다시 저장
        TokenContainer newTokens = jwtUtil.createTokens(member);
        member.setRefreshToken(newTokens.getRefreshToken());

        return newTokens;
    }
}
