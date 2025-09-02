package com.sundostudio.security;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.sundostudio.dto.AuthVO;
import com.sundostudio.dto.MemberVO;
import com.sundostudio.mapper.MemberMapper;

import lombok.Setter;

@Service
public class CustomUserDetailsService implements UserDetailsService {
	
	@Setter(onMethod_ = @Autowired)
	public MemberMapper mapper;
	
    	
    @Override
    public UserDetails loadUserByUsername(String userid) throws UsernameNotFoundException {
    	MemberVO user = mapper.findByUserid(userid);
    	AuthVO auth = mapper.getAuthByUserid(userid);
    	
    	//권한 인증을 위한 role 정보 세팅
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + auth.getRole());
        
        //jwt 토큰 생성을 위한 User(member) 정보 세팅
    	return new org.springframework.security.core.userdetails.User(
                user.getUserid(),
                user.getUserpw(),
                Collections.singletonList(authority)
        );
    }
}