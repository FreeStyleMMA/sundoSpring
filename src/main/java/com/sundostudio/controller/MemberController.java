package com.sundostudio.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sundostudio.dto.LoginRequest;
import com.sundostudio.dto.LoginResponse;
import com.sundostudio.dto.MemberVO;
import com.sundostudio.security.JwtTokenProvider;
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
	public JwtTokenProvider jwtProvider;

	@PostMapping("/join")
	public void join(@RequestBody MemberVO member) {
		service.join(member);
	}

	// 토큰 전달까지 하기 위한 ResponseEntity 형식으로 http 코드 전제 만지기.
	@PostMapping("/login")
	// http 리턴을 위한 ResponseEntity 사용
	public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request, HttpServletResponse response) {

		log.info("로그인 요청 도착: " + request);

		LoginResponse loginResponse = service.logIn(request);
		// 로그인 유효성 확인
		if (loginResponse.loginSuccess == true) {
			// 쿠키에 토큰 저장 jwt:token 형태.
			jwtProvider.addJwtToCookie(loginResponse.token, response);
		}
		// http 형태로 리턴. header에는 jwt가 있는 쿠키, body에 loginResponse.
		return ResponseEntity.ok(loginResponse);

	}
}
