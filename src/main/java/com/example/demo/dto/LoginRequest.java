package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "로그인 요청 DTO")
public record LoginRequest(
    @Schema(description = "아이디", example = "JINHO")
    @NotBlank(message = "아이디는 필수입니다.")
    String username,

    @Schema(description = "비밀번호", example = "12341234")
    @NotBlank(message = "비밀번호는 필수입니다.")
    String password
) {}
