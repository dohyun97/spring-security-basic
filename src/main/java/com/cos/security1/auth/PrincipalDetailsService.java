package com.cos.security1.auth;

import com.cos.security1.domain.User;
import com.cos.security1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
//시큐리티 설정에서 loginProcessingUrl("/login");
// /login 요청이 오면 자동으로 UserDetailsService 타입으로 IoC 되어 있는 loadUserByUsername function실행
@Service
@RequiredArgsConstructor
@Slf4j
public class PrincipalDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    //이때 @AunthenticationPrincipal 어노테이션이 생성 돼
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("loadUserByUsername = {}",username);
        User user = userRepository.findByUsername(username);
        if(user!= null){
            return new PrincipalDetails(user);  //여기가 리턴 될때 시큐리티세션(내부 Authentication(내부 UserDetails))) 자동으로 완성. login이 완료가 돼
        }
        return null;
    }
}
