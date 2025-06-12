package com.example.demo.domain;

import com.example.demo.exception.UserCustomException;
import lombok.Getter;

@Getter
public class User {
    private static long idCounter=1L;

    private final Long id;
    private final String username;
    private final String password;
    private final String nickname;
    private Role role;

    public User(String username, String password, String nickname) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.id = idCounter++;
        this.role = Role.USER;
    }

    public void setRoleAdmin() {
        if(this.role.equals(Role.ADMIN)) {
            throw new UserCustomException("Already_ADMIN","이미 관리자 입니다.");
        }
        this.role=Role.ADMIN;
    }
}
