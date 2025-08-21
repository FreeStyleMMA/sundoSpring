package com.sundostudio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sundostudio.dto.MemberVO;
import com.sundostudio.mapper.MemberMapper;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class MemberServiceImpl implements MemberService{

	@Setter(onMethod_=@Autowired)
	public MemberMapper mapper;
	
	public void join(MemberVO member) {
			mapper.join(member);
	}
	
	
}
