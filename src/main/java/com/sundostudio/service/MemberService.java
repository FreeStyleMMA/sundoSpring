package com.sundostudio.service;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import com.sundostudio.dto.LoginRequest;
import com.sundostudio.dto.LoginResponse;
import com.sundostudio.dto.MemberVO;

public interface MemberService {
	public void join(MemberVO member);
	public LoginResponse logIn(LoginRequest request);
	public MemberVO findByUserid(String userid);
}
