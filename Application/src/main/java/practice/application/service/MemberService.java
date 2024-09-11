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


        @Transactional
        public void logout(MemberLogoutDTO memberLogoutDTO){

        }

    }
