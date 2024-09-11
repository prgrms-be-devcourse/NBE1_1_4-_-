package practice.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import practice.application.models.Jwt.CustomUserDetails;
import practice.application.models.MemberEntity;
import practice.application.models.exception.NotFoundException;
import practice.application.repositories.MemberRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        MemberEntity memberEntity = memberRepository.findById(Long.parseLong(id)).orElseThrow(() -> new NotFoundException(id));


        return new CustomUserDetails(memberEntity);
    }

}
