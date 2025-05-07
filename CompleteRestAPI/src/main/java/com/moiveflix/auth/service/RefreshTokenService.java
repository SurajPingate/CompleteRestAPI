package com.moiveflix.auth.service;

import java.time.Instant;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.moiveflix.auth.entities.RefreshToken;
import com.moiveflix.auth.entities.User;
import com.moiveflix.auth.repositories.RefreshTokenRepository;
import com.moiveflix.auth.repositories.UserRepository;
import com.moiveflix.exceptions.RefreshTokenException;
import com.moiveflix.exceptions.RefreshTokenNotFoundException;

@Service
public class RefreshTokenService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RefreshTokenRepository refreshTokenRepository;

	public RefreshToken createRefreshToken(String userName) {
		long refreshTokenValidity = 240 * 1000;	//5*60*60*10000;
		User user = userRepository.findByEmail(userName)
				.orElseThrow(() -> new UsernameNotFoundException("User not found with email :"+ userName));

		RefreshToken refreshToken = user.getRefreshToken();
		
		if(refreshToken == null) {
			//refreshToken.setUser(null);
			//refreshTokenRepository.deleteById(refreshToken.getTokenId());
			
			refreshToken = RefreshToken.builder()
					.refreshToken(UUID.randomUUID().toString())
					.expirationTime(Instant.now().plusMillis(refreshTokenValidity))
					.user(user)
					.build();
			refreshTokenRepository.save(refreshToken);
		}
		
		if(refreshToken.getExpirationTime().compareTo(Instant.now()) < 0) {
			refreshToken = RefreshToken.builder()
					.tokenId(refreshToken.getTokenId())
					.refreshToken(UUID.randomUUID().toString())
					.expirationTime(Instant.now().plusMillis(refreshTokenValidity))
					.user(user)
					.build();
			
			refreshTokenRepository.save(refreshToken);
		}
		return refreshToken;
	}
	
	public RefreshToken verifyRefreshToken(String refreshToken) {
		RefreshToken refToken = refreshTokenRepository.findByRefreshToken(refreshToken)
				.orElseThrow(() -> new RefreshTokenNotFoundException("Refresh Token not found!"));
		if(refToken.getExpirationTime().compareTo(Instant.now()) < 0) {
			throw new RefreshTokenException("Refresh Token expired..! Please login again.");
		}
		
		return refToken;
	}
	
}
