package com.sundostudio.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {
	public Boolean loginSuccess;
	public String token;
	public String message;
	public String authorities;
}
