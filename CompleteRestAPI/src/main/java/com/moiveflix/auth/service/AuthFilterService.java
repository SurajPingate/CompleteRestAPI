package com.moiveflix.auth.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class AuthFilterService extends OncePerRequestFilter{

	@Autowired
	private JwtService jwtService;

	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request, 
			@NonNull HttpServletResponse response, 
			@NonNull FilterChain filterChain)
					throws ServletException, IOException {

		String authHeader = request.getHeader("Authorization");
		String jwt;
		String userName;

		if(authHeader == null || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}

		//extract JWT
		jwt = authHeader.substring(7);

		//Extract username from JWT
		userName = jwtService.extractUsername(jwt);

		if(userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = userDetailsService.loadUserByUsername(userName);

			if(jwtService.isTokenValid(jwt, userDetails)) {
				UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
						userDetails,null,userDetails.getAuthorities());
				authenticationToken.setDetails(
						new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authenticationToken);
			}
		}
		
		filterChain.doFilter(request, response);
	}


}
