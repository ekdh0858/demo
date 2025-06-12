package com.example.demo.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "회원가입 요청 DTO")
public record SignupRequest(
    @Schema(description = "아이디", example = "JINHO")
    @NotBlank(message = "아이디는 필수입니다.")
    @Size(min = 3, max = 20, message = "아이디는 3~20자여야 합니다.")
    String username,

    @Schema(description = "비밀번호", example = "12341234")
    @NotBlank(message = "비밀번호는 필수입니다.")
    @Size(min = 6, max = 30, message = "비밀번호는 6~30자여야 합니다.")
    String password,

    @Schema(description = "닉네임", example = "Mentos")
    @NotBlank(message = "닉네임은 필수입니다.")
    @Size(max = 20, message = "닉네임은 20자 이내여야 합니다.")
    String nickname,

    @Schema(description = "관리자 키(선택)", example = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC", nullable = true)
    String adminKey) {


}
