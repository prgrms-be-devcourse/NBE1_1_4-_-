package practice.application.service;

    import lombok.RequiredArgsConstructor;
    import lombok.extern.slf4j.Slf4j;
    import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
    import org.springframework.stereotype.Service;
    import org.springframework.transaction.annotation.Transactional;
    import practice.application.models.DTO.MemberJoinRequestDTO;
    import practice.application.models.MemberEntity;
    import practice.application.models.exception.DuplicateEmailException;
    import practice.application.repositories.MemberRepository;

    @Service
    @Slf4j
    @RequiredArgsConstructor
    public class MemberService {

        private final MemberRepository memberRepository;
        private final BCryptPasswordEncoder bCryptPasswordEncoder;

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


    }
