package com.sundostudio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sundostudio.dto.AuthVO;
import com.sundostudio.dto.LoginRequest;
import com.sundostudio.dto.LoginResponse;
import com.sundostudio.dto.MemberVO;
import com.sundostudio.mapper.MemberMapper;
import com.sundostudio.security.JwtUtil;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class MemberServiceImpl implements MemberService {

	@Setter(onMethod_ = @Autowired)
	public MemberMapper mapper;

	@Setter(onMethod_ = @Autowired)
	JwtUtil jwtUtil;
	
	@Setter(onMethod_ = @Autowired)
	PasswordEncoder passwordEncoder;

//	@Setter(onMethod_ = @Autowired)
//	private CustomUserDetails userDetails;

	public void join(MemberVO member) {
		String encodedPw = passwordEncoder.encode(member.getUserpw());
		member.setUserpw(encodedPw);
		mapper.join(member);
	}

	public LoginResponse logIn(LoginRequest request) {
		MemberVO user = mapper.findByUserid(request.userid);
		AuthVO auth = mapper.getAuthByUserid(request.userid);
		
		if (user != null && user.getUserpw() != null && passwordEncoder.matches(request.getUserpw(),user.getUserpw())) {
			String token = jwtUtil.generateToken(user.getUserid());
			 return LoginResponse.builder()
		                .loginSuccess(true)
		                .token(token)
		                .authorities(auth.getAuthorities())
		                .message("로그인 성공")
		                .build();
		} else {
			return LoginResponse.builder()
					.loginSuccess(false)
					.message("로그인 실패")
					.token(null)
					.authorities(null)
					.build();
		
		}
	}

	public MemberVO findByUserid(String userid) {
		return mapper.findByUserid(userid);
	}
	
}
