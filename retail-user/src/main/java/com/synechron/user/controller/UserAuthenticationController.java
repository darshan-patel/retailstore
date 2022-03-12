package com.synechron.user.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.synechron.exception.ApplicationRuntimeException;
import com.synechron.user.config.JwtTokenUtil;
import com.synechron.user.entity.AuthenticationRequest;
import com.synechron.user.entity.AuthenticationResponse;
import com.synechron.user.entity.User;
import com.synechron.user.service.JwtUserDetailsService;

@RestController
@RequestMapping("/users")
public class UserAuthenticationController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private JwtUserDetailsService userDetailsService;

	@PostMapping("/authenticate")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest, HttpServletResponse response) throws Exception {

		authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

		final String token = jwtTokenUtil.generateToken(userDetails);
		response.setHeader(HttpHeaders.AUTHORIZATION, token);
		return ResponseEntity.ok(new AuthenticationResponse(token));
	}
	
	@GetMapping("/validate")
	public ResponseEntity<?> validateAuthenticationToken(HttpServletRequest request) throws Exception {
		String authToken = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (jwtTokenUtil.validateToken(authToken, userDetailsService.loadUserByUsername(jwtTokenUtil.getUsernameFromToken(authToken)))) {
			return ResponseEntity.ok(new AuthenticationResponse(authToken));
		}
		return new ResponseEntity(HttpStatus.UNAUTHORIZED);
	}
	
	@PostMapping("/register")
	public ResponseEntity<?> saveUser(@RequestBody User user) throws Exception {
		return ResponseEntity.ok(userDetailsService.save(user));
	}
	
	@GetMapping
	public ResponseEntity<?> getAllUsers() throws Exception {
		return ResponseEntity.ok(userDetailsService.getAllUsers());
	}

	private void authenticate(String username, String password) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (Exception e) {
			throw new ApplicationRuntimeException(e);
		}
	}
}
