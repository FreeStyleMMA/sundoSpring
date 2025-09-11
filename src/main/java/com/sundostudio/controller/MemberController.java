package com.sundostudio.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sundostudio.dto.LoginRequest;
import com.sundostudio.dto.LoginResponse;
import com.sundostudio.dto.MemberVO;
import com.sundostudio.security.CustomUserDetails;
import com.sundostudio.security.JwtUtil;
import com.sundostudio.service.MemberService;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@RestController
@Log4j
@RequestMapping("/api/*")
@CrossOrigin(value = "http://localhost:3000", allowCredentials = "true")

public class MemberController {

	@Setter(onMethod_ = @Autowired)
	public MemberService service;

	@Setter(onMethod_ = @Autowired)
	public JwtUtil jwtUtil;

	@PostMapping("/join")
	public void join(@RequestBody MemberVO member) {
		service.join(member);
	}

	// 토큰 전달까지 하기 위한 ResponseEntity 형식으로 http 코드 전제 만지기.
	@PostMapping("/login")
	// http 리턴을 위한 ResponseEntity 사용
	public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request, HttpServletResponse response) {
		log.info("로그인 요청 도착");
		LoginResponse loginResponse = service.logIn(request);
		// 로그인 유효성 확인
		if (loginResponse.loginSuccess == true) {
			// 쿠키에 토큰 저장 jwt:token 형태.
			 jwtUtil.addJwtToCookie(loginResponse.token, response);
		}
		loginResponse.setToken(null);
		// http 형태로 리턴. header에는 jwt가 있는 쿠키, body에 loginResponse.
		return ResponseEntity.ok(loginResponse);
		
	}

	@GetMapping("/me")
	public ResponseEntity<?> checkUser(Authentication authentication) {
		log.info("로그인 상태 유지 확인 요청");
		  if (authentication == null || !authentication.isAuthenticated()) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	                     .body(Map.of("userid", "user", "roles", "user"));
	        }
		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		log.info("검증 아이디: " + userDetails.getUserid());
		
		return ResponseEntity.ok(Map.of("userid", userDetails.getUserid()));
	}
	

	
	@GetMapping("/auth")
	public ResponseEntity<?> checkRole() {
		 Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//		log.info("권한 인증 요청 도착");
//		log.info("권한 객체: "+authentication);
		  if (authentication == null || !authentication.isAuthenticated()) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	                     .body(Map.of("userid", "visitor", "roles", "USER"));
	        }
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//		log.info("검증 아이디: " + userDetails.getUsername());
		String userid = userDetails.getUsername();
		
		// 권한 여러개 가질 수 있기 때문에 리스트로. -> List<String>
		List<String> roles = userDetails.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.toList());
//		log.info("권한 리스트: "+roles);
		return ResponseEntity.ok(Map.of("userid", userid, "roles", roles));
	}
	
	// 로그아웃 위해서 쿠키 덮어쓰기
	@GetMapping("/logout")
	public ResponseEntity<?> logout(HttpServletResponse response) {
	    Cookie cookie = new Cookie("jwt", null); // 쿠키 이름과 동일하게
	    cookie.setHttpOnly(true);
	    cookie.setPath("/"); 
	    cookie.setMaxAge(0); // 0으로 설정하면 즉시 삭제
	    response.addCookie(cookie);
	    return ResponseEntity.ok(Map.of("message", "로그아웃 성공"));
	}
}
