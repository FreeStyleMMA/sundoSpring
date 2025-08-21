package com.sundostudio.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sundostudio.dto.MemberVO;
import com.sundostudio.security.CustomUserDetailsService;
import com.sundostudio.security.JwtUtil;
import com.sundostudio.service.MemberService;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@RestController
@Log4j
@RequestMapping("/*")

public class MemberController {
	String uuid = java.util.UUID.randomUUID().toString();
	@Setter(onMethod_=@Autowired)
	public MemberService service;
	
	@RequestMapping("/join")
	public void join(@RequestBody MemberVO member) {
		service.join(member);
	}
	
//	  @Autowired
//	  @Qualifier("authenticationManager")
//	    private AuthenticationManager authManager;
//
//
////	    @Autowired
////	    private CustomUserDetailsService userDetailsService;
////
////	    @Autowired
////	    private JwtUtil jwtUtil;
//
//	    @PostMapping("/login")
//	    public String login(@RequestParam String username, @RequestParam String password) {
//	        authManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
//	        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//	        return jwtUtil.generateToken(userDetails.getUsername());
//	    }
//	
}
