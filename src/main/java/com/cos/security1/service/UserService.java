package com.cos.security1.service;

import com.cos.security1.domain.Role;
import com.cos.security1.domain.User;
import com.cos.security1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void save(User user){
        user.setRole(Role.ROLE_USER);
        String rawPw = user.getPassword();
        String encPw = bCryptPasswordEncoder.encode(rawPw);
        user.setPassword(encPw);
        userRepository.save(user);
    }
}
