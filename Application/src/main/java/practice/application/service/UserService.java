package practice.application.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import practice.application.model.dto.request.LoginRequestDto;
import practice.application.model.dto.request.UserRequestDTO;
import practice.application.model.entity.UserEntity;
import practice.application.repository.UserRepository;
import practice.application.service.utils.UserUtils;
import practice.application.util.JwtTokenProvider;
import practice.application.service.utils.SecurityUtils;

import java.util.Optional;


@Service
public class UserService implements UserUtils {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Transactional
    public Long join(UserRequestDTO userRequestDTO) {
        if(userRequestDTO.getEmail() == null || userRequestDTO.getPassword() == null) {
            throw new RuntimeException("Invalid Password!!");
        }

        Optional<UserEntity> optionalUser = userRepository.findByEmail(userRequestDTO.getEmail());
        if(optionalUser.isPresent()) {
            throw new RuntimeException("email 중복");
        }

        UserEntity user = userRepository.save(createUser(userRequestDTO));
        return user.getId();
    }

    public String login(LoginRequestDto loginRequestDto) {
        if(loginRequestDto.getEmail() == null || loginRequestDto.getPassword() == null) {
            throw new RuntimeException("Invalid Password!!");
        }

        Optional<UserEntity> optionalUser = userRepository.findByEmail(loginRequestDto.getEmail());
        if(!optionalUser.isPresent()) {
            throw new RuntimeException("해당하는 email 정보가 없습니다");

        } else {
            UserEntity user = optionalUser.get();
            if(!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
                throw new RuntimeException("비밀번호가 일치하지 않습니다.");
            }
        }

        UserEntity user = optionalUser.get();
        return jwtTokenProvider.createToken(user, 100000000L);
    }

    @Override
    public UserEntity getUserFromSecurityContext() {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        UserEntity user = findUser(currentUserId);
        return user;
    }

    private UserEntity createUser(UserRequestDTO userRequestDTO) {
        String encodePassword = passwordEncoder.encode(userRequestDTO.getPassword());

        UserEntity user = new UserEntity();
        user.setEmail(userRequestDTO.getEmail());
        user.setPassword(encodePassword);
        user.setName(userRequestDTO.getName());
        user.setPhoneNum(userRequestDTO.getPhoneNum());
        return user;
    }

    private UserEntity findUser(Long userId) {
        Optional<UserEntity> optionalUser = userRepository.findById(userId);
        UserEntity user = optionalUser.orElseThrow(() -> new RuntimeException("Not Find User"));
        return user;
    }
}
