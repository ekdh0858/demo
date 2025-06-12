package com.example.demo.security;

import com.example.demo.domain.User;
import com.example.demo.domain.UserRepository;
import com.example.demo.exception.UserCustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UserCustomException("INVALID_CREDENTIALS", "아이디 또는 비밀번호가 올바르지 않습니다."));

        return new UserDetailsImpl(user);
    }
}
