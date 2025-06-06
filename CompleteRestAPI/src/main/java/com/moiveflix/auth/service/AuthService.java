package com.moiveflix.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.moiveflix.auth.entities.RefreshToken;
import com.moiveflix.auth.entities.User;
import com.moiveflix.auth.entities.UserRoles;
import com.moiveflix.auth.repositories.UserRepository;
import com.moiveflix.auth.utils.AuthResponse;
import com.moiveflix.auth.utils.LoginRequest;
import com.moiveflix.auth.utils.RegisterRequest;

@Service
public class AuthService {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private RefreshTokenService refreshTokenService;

	@Autowired
	private AuthenticationManager authenticationManager;


	public AuthResponse register(RegisterRequest registerRequest) {
		User user = User.builder()
				.name(registerRequest.getName())
				.email(registerRequest.getEmail())
				.userName(registerRequest.getUserName())
				.password(passwordEncoder.encode(registerRequest.getPassword()))
				.role(UserRoles.USER)
				.build();

		User savedUser = userRepository.save(user);
		String accessToken = jwtService.generateToken(savedUser);
		RefreshToken refreshToken = refreshTokenService.createRefreshToken(savedUser.getEmail());

		return AuthResponse.builder()
				.accessToken(accessToken)
				.refreshToken(refreshToken.getRefreshToken())
				.build();
	}


	public AuthResponse login(LoginRequest loginRequest) {
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						loginRequest.getEmail(),
						loginRequest.getPassword()));

		User user = userRepository.findByEmail(loginRequest.getEmail())
				.orElseThrow(() -> 
				new UsernameNotFoundException("User Not found for Email : "+ loginRequest.getEmail()));
		String accessToken = jwtService.generateToken(user);
		RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getEmail());

		return AuthResponse.builder()
				.accessToken(accessToken)
				.refreshToken(refreshToken.getRefreshToken())
				.build();

	}
}
