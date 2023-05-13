package com.cos.security1.oauth;

import com.cos.security1.auth.PrincipalDetails;
import com.cos.security1.domain.Role;
import com.cos.security1.domain.User;
import com.cos.security1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;

    //구글로 받은 userRequest로 부터 후처리 되는 함수
    // 함수 종료시 @AunthenticationPrincipal 어노테이션이 생성 돼
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("getClientRegistration = {}",userRequest.getClientRegistration()); //여기서 registrationId 로 어떤 Oauth로 로그인 했는지 확은 가능(ex.google)
        log.info("getAccessToken = {}",userRequest.getAccessToken());
        log.info("getAttribute = {}",super.loadUser(userRequest).getAttributes()); //여기서 받은 회원프로필 정보를 활용해서 회원가입 가능
        OAuth2User oAuth2User = super.loadUser(userRequest);

        //회원가입을 oAuth2user 정보를 활용하여 User 필드 에 맞춰 강제로 진행
        String provider = userRequest.getClientRegistration().getRegistrationId();//google
        String providerId = oAuth2User.getAttribute("sub");
        String username = provider+"_"+providerId; //google_12312312 =>unique username
        String password = bCryptPasswordEncoder.encode("getinthere");
        String email = oAuth2User.getAttribute("email");
        Role role = Role.ROLE_USER;
        //이미 가입한 회원이면 가입 시키지마. 가입 안한 회원만 회원가입. google login시도 할 때 회원이 아니면 강제로 회원가입 시켜서 로그인 시켜
        User findUser = userRepository.findByUsername(username);
        if (findUser == null){
            log.info("This is first time for Google Login");
            findUser = User.builder()
                    .provider(provider)
                    .providerId(providerId)
                    .username(username)
                    .password(password)
                    .email(email)
                    .role(role)
                    .build();
            userRepository.save(findUser);
        }else {
            log.info("THis is not first time for google login");
        }
        return new PrincipalDetails(findUser,oAuth2User.getAttributes());
    }
}
