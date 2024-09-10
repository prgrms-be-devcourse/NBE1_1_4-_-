package practice.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import practice.application.models.DTO.MemberJoinRequestDTO;
import practice.application.models.DTO.MemberLoginRequestDTO;
import practice.application.models.DTO.MemberLogoutDTO;
import practice.application.models.DTO.TokenContainer;
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

        @Transactional
        public Long save(MemberJoinRequestDTO memberJoinRequestDTO){
            String email = memberJoinRequestDTO.getEmail();

            if(memberRepository.existsByEmail(email)){
               throw new DuplicateEmailException("해당 이메일은 이미 존재합니다");
            }

            MemberEntity entity = memberJoinRequestDTO.toEntity();

            entity.encodePassword(bCryptPasswordEncoder.encode(entity.getPassword()));

            memberRepository.save(entity);

            return entity.getId();

        }


        public String login(MemberLoginRequestDTO memberLoginRequestDTO){
            MemberEntity memberEntity = memberRepository.findByEmail(memberLoginRequestDTO.getEmail()).orElseThrow(() -> new NotFoundException("이메일이 존재하지 않습니다"));


            if(!bCryptPasswordEncoder.matches(memberLoginRequestDTO.getPassword(), memberEntity.getPassword())){
                throw new BadCredentialsException("비밀번호가 일치하지 않습니다");
            }

            String accessToken = jwtUtil.createAccessToken(memberEntity);

            return accessToken;


        }

        public TokenContainer loginWithTokenContainer(MemberLoginRequestDTO memberLoginRequestDTO) {
            // DB 에서 이메일로 유저 정보 가져오기
            MemberEntity memberEntity = memberRepository
                    .findByEmail(memberLoginRequestDTO.getEmail())
                    .orElseThrow(() -> new NotFoundException("이메일이 존재하지 않습니다"));

            // 비밀번호 틀림
            if (!bCryptPasswordEncoder.matches(memberLoginRequestDTO.getPassword(), memberEntity.getPassword()))
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
