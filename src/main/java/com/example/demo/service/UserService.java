package com.example.demo.service;

import com.example.demo.domain.User;
import com.example.demo.domain.UserRepository;
import com.example.demo.dto.SignupRequest;
import com.example.demo.dto.SignupUserResponse;
import com.example.demo.exception.UserCustomException;
import com.example.demo.jwt.JwtUtil;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    // ADMIN_TOKEN
    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";
    private final JwtUtil jwtUtil;

    public SignupUserResponse signup(SignupRequest request) {
        String username = request.username();
        String password = passwordEncoder.encode(request.password());
        String nickname = request.nickname();

        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            throw new UserCustomException("USER_ALREADY_EXISTS","이미 가입된 사용자입니다.");
        }

        User newUser = new User(username, password, nickname);

        if (request.adminKey() != null && !request.adminKey().isEmpty()){
            if (!request.adminKey().equals(ADMIN_TOKEN)) {
                throw new UserCustomException("INVALID_TOKEN","관리자 암호가 틀려 등록이 불가능합니다.");
            }
            newUser.setRoleAdmin();
        }

        userRepository.save(newUser);
        return SignupUserResponse.from(newUser);
    }

    public SignupUserResponse grantAdminRole(Long userId) {
        User user = userRepository.findById(userId);
        if (user == null) {
            throw new UserCustomException("USER_NOT_FOUND", "해당 사용자가 존재하지 않습니다.");
        }
        user.setRoleAdmin();
        return SignupUserResponse.from(user);
    }
}
