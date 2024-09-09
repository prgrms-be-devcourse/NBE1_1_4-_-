package practice.application.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import practice.application.models.entity.UserEntity;
import practice.application.repositories.UserRepository;


// 유저 서비스에서는 이미 등록된 관리자유저의 email과 일치하는지 확인해주는 함수만 Service 클래스에 구현했습니다.
// 보안과 확장성 측면을 고려하지 않고, 딱 요구사항 명세에 맞게 구현하는데 집중했습니다.
// 추후 시간이 된다면, 모델 객체를 변경하여 User와 관련된 CRUD 서비스 기능을 확장하고 Spring Security를 도입해보려고 합니다.

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
//  현재 DB에는 super관리자User 하나만 저장되어 있다. 인자로 들어온 email이 super 관리자의 email이면 true를, super관리자의 email이 아니면 false를 반환
//   해당 email 조회 실패 시 일반 회원으로 간주하도록 설계하였다.
    public boolean isAdmin(String email) {
//        이메일로 사용자 조회
        UserEntity userEntity = userRepository.findById(email).orElse(null);
        System.out.println("userEntity = " + userEntity);
//        조회된 사용자 없으면 일반 회원으로 간주하고 false 반환
        if (userEntity == null) {
            return false;
        }
        return true;
    }

}
