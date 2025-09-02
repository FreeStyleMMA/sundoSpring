package com.sundostudio.service;

import com.sundostudio.dto.LoginRequest;
import com.sundostudio.dto.LoginResponse;
import com.sundostudio.dto.MemberVO;

public interface MemberService {
	public void join(MemberVO member);
	public LoginResponse logIn(LoginRequest request);
	public MemberVO findByUserid(String userid);

}
