package com.moiveflix.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moiveflix.auth.entities.RefreshToken;
import com.moiveflix.auth.entities.User;
import com.moiveflix.auth.service.AuthService;
import com.moiveflix.auth.service.JwtService;
import com.moiveflix.auth.service.RefreshTokenService;
import com.moiveflix.auth.utils.AuthResponse;
import com.moiveflix.auth.utils.LoginRequest;
import com.moiveflix.auth.utils.RefreshTokenRequest;
import com.moiveflix.auth.utils.RegisterRequest;

@RestController
@RequestMapping("api/v1/auth")
public class AuthController {
	
	@Autowired
	private AuthService authService;
	
	@Autowired
	private RefreshTokenService refreshTokenService;
	
	@Autowired
	private JwtService jwtService;

	@PostMapping("/register")
	public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest registerRequest){
		return ResponseEntity.ok(authService.register(registerRequest));
	}
	
	@PostMapping("/login")
	public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest){
		//System.out.println(loginRequest.getEmail() + " : " + loginRequest.getPassword());
		return ResponseEntity.ok(authService.login(loginRequest));
	}
	
	@PostMapping("/refresh")
	public ResponseEntity<AuthResponse> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest){
		RefreshToken verifiedRefreshToken = refreshTokenService.verifyRefreshToken(refreshTokenRequest.getRefreshToken());
		User user = verifiedRefreshToken.getUser();
		String accessToken = jwtService.generateToken(user);
		
		return ResponseEntity.ok(AuthResponse.builder()
				.accessToken(accessToken)
				.refreshToken(verifiedRefreshToken.getRefreshToken())
				.build());
		
	}
}
