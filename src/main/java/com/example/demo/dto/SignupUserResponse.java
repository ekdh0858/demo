package com.example.demo.dto;

import com.example.demo.domain.User;
import java.util.ArrayList;
import java.util.List;

public record SignupUserResponse(Long id,String username, String nickname, List<RoleResponse> roles) {

    public static SignupUserResponse from(User user) {
        Long id = user.getId();
        String username = user.getUsername();
        String nickname = user.getNickname();
        List<RoleResponse> roles = new ArrayList<>();
        roles.add(new RoleResponse(user.getRole().name()));
        return new SignupUserResponse(id,username,nickname,roles);
    }

        public record RoleResponse(String role) {
    }
}
