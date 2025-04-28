package com.app.jwtauth.Controller;

import com.app.jwtauth.Payloads.AuthResponse;
import com.app.jwtauth.Payloads.LoginRequest;
import com.app.jwtauth.Payloads.RegisterRequest;
import com.app.jwtauth.config.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping(value = "login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request){
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping(value = "register")
    public ResponseEntity<AuthResponse> registerUser(@RequestBody RegisterRequest request) {
        AuthResponse response = null;
        try {
            response = authService.createUser(request);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<AuthResponse> handleException(RuntimeException e) {
        AuthResponse errorResponse = AuthResponse.builder()
                .message(e.getMessage())
                .build();

        return ResponseEntity.badRequest().body(errorResponse);
    }
}