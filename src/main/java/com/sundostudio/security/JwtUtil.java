package com.sundostudio.security;

import java.security.Key;
import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.sundostudio.mapper.MemberMapper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.Setter;

@Component
public class JwtUtil {

	@Setter(onMethod_=@Autowired)
	MemberMapper mapper;
	private final String SECRET_KEY = "MySuperSecretKeyForJWTGeneration12345";
	
	private final long EXPIRATION = 1000 * 60 * 60* 24;

	private Key getKey() {
		return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
	}

	public String generateToken(String username) {
		
		String authorities = mapper.getAuthByUserid(username).getAuthorities();
		
		return Jwts.builder()
				.setSubject(username)
				.claim("authorities", authorities)
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis()+EXPIRATION))
				.signWith(getKey())
				.compact();
	}
	
	 public boolean validateToken(String token, UserDetails userDetails) {
	        String username = extractUsername(token);
	        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
    
    public boolean isTokenExpired(String token) {
    	return Jwts.parserBuilder().setSigningKey(getKey())
    			.build().parseClaimsJws(token)
    			.getBody().getExpiration()
    			.before(new Date());
    }
    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
    
    public void addJwtToCookie(String token, HttpServletResponse response) {
    	Cookie cookie = new Cookie("jwt",token);
    	cookie.setHttpOnly(true);
    	cookie.setSecure(true);
    	cookie.setPath("/");
    	cookie.setMaxAge(60*60);
    	response.addCookie(cookie);
    }
 
 public String getJwtFromCookie(HttpServletRequest request) {
	    if (request.getCookies() != null) {
	        for (Cookie cookie : request.getCookies()) {
	            if ("jwt".equals(cookie.getName())) {
	                return cookie.getValue();
	            }
	        }
	    }
	    return null;
	}
   }
