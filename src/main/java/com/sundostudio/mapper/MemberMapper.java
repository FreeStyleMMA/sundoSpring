package com.sundostudio.mapper;

import com.sundostudio.dto.AuthVO;
import com.sundostudio.dto.LoginRequest;
import com.sundostudio.dto.LoginResponse;
import com.sundostudio.dto.MemberVO;

public interface MemberMapper {
	public void join(MemberVO member);
	public LoginResponse logIn(LoginRequest request);
	public MemberVO findByUserid(String userid);
	public AuthVO getAuthByUserid(String userid);
}
	