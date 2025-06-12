package com.example.demo.controller;

import com.example.demo.domain.User;
import com.example.demo.dto.ErrorResponse;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.LoginResponse;
import com.example.demo.dto.SignupRequest;
import com.example.demo.dto.SignupUserResponse;
import com.example.demo.security.UserDetailsImpl;
import com.example.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }
    @Operation(
        summary = "회원가입",
        description = "새로운 사용자를 회원가입시킵니다."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "회원가입 성공", content = @Content(schema = @Schema(implementation = SignupUserResponse.class))),
        @ApiResponse(responseCode = "400", description = "이미 가입된 사용자", content = @Content(schema = @Schema(example = """
                    {
                      "error": {
                        "code": "USER_ALREADY_EXISTS",
                        "message": "이미 가입된 사용자입니다."
                      }
                    }
                    """)))
    })
    @PostMapping("/signup")
    public ResponseEntity<SignupUserResponse> signup(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "회원가입 정보",
            required = true,
            content = @Content(schema = @Schema(implementation = SignupRequest.class))
        )
        @RequestBody @Valid SignupRequest request) {
        SignupUserResponse response = userService.signup(request);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "관리자 권한 부여",
        description = "관리자가 다른 유저에게 관리자 권한을 부여합니다."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "권한 부여 성공", content = @Content(schema = @Schema(implementation = SignupUserResponse.class))),
        @ApiResponse(responseCode = "400", description = "존재하지 않는 사용자", content = @Content(schema = @Schema(example = """
                    {
                      "error": {
                        "code": "USER_NOT_FOUND",
                        "message": "해당 사용자가 존재하지 않습니다."
                      }
                    }
                    """))),
        @ApiResponse(responseCode = "403", description = "권한 부족(접근 제한)", content = @Content(schema = @Schema(example = """
                    {
                      "error": {
                        "code": "ACCESS_DENIED",
                        "message": "관리자 권한이 필요한 요청입니다. 접근 권한이 없습니다."
                      }
                    }
                    """)))
    })
    @SecurityRequirement(name = "bearerAuth")
    @PatchMapping("/admin/users/{userId}/roles")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SignupUserResponse> grantAdminRole(
        @Parameter(description = "관리자 권한을 부여할 유저 ID", example = "3")
        @PathVariable Long userId) {
        SignupUserResponse response = userService.grantAdminRole(userId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "로그인", description = "사용자 로그인 (JWT 발급)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "로그인 성공 (JWT 토큰 반환)", content = @Content(schema = @Schema(implementation = LoginResponse.class))),
        @ApiResponse(responseCode = "401", description = "로그인 실패", content = @Content(schema = @Schema(example = """
                    {
                      "error": {
                        "code": "INVALID_CREDENTIALS",
                        "message": "아이디 또는 비밀번호가 올바르지 않습니다."
                      }
                    }
                    """)))
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "로그인 요청 예시",
            required = true,
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "로그인 예시",
                    value = """
                        {
                          "username": "JINHO",
                          "password": "12341234"
                        }
                        """
                )
            )
        )
        @RequestBody LoginRequest request
    ) {
        return null; // Swagger 명세 목적!
    }
}
