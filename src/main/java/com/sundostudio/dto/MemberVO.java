package com.sundostudio.dto;

import java.util.Date;

import lombok.Data;

@Data
public class MemberVO {
	public String userid;
	public String userpw;
	public String username;
	public String nickname;
	public Date regdate;
	public Date updatedate;
	public int enabled;
}
