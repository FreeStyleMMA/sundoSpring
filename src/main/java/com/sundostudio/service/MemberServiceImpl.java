package com.sundostudio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sundostudio.dto.LoginRequest;
import com.sundostudio.dto.LoginResponse;
import com.sundostudio.dto.MemberVO;
import com.sundostudio.mapper.MemberMapper;
import com.sundostudio.security.JwtTokenProvider;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class MemberServiceImpl implements MemberService {

	@Setter(onMethod_ = @Autowired)
	public MemberMapper mapper;
	
	@Setter(onMethod_ = @Autowired)
	JwtTokenProvider jwtProvider;


	
	public void join(MemberVO member) {
		mapper.join(member);
	}

	public LoginResponse logIn(LoginRequest request) {
		MemberVO user = mapper.findByUserid(request.userid);
		LoginResponse response  = new LoginResponse();
		if(user == null || user.getUserpw().equals(request.userpw)) {
			response.loginSuccess = false;
			response.token = null;
			response.message = "로그인 실패";
			return response;
		}else {
		String token = jwtProvider.generateToken(user.getUserid());
		response.loginSuccess = true;
		response.token = token;
		response.message = "로그인 성공";
		return response;
		}
	}

	public MemberVO findByUserid(String userid) {
		return mapper.findByUserid(userid);
	}
}
