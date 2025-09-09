package com.sundostudio.security;

import java.io.IOException;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.extern.log4j.Log4j;

@Log4j
@Component

public class JwtFilter extends OncePerRequestFilter {

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private CustomUserDetailsService userDetailsService;

//	private static final List<String> EXCLUDE_URLS = List.of("/api/auth");

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
//
//		String path = request.getRequestURI();

		// 권한 확인용 URL 예외 처리 먼저.
//		if (EXCLUDE_URLS.contains(path)) {
//			chain.doFilter(request, response);
//			return;
//		}

		if (request != null) {
			log.info("http 요청 도착");
		}
		
		//	jwt 검증
		// 쿠키에서 jwt 꺼내기
		String token = jwtUtil.getJwtFromCookie(request);
		if (token != null) {
			log.info("쿠키토큰: "+token);
			String userid = jwtUtil.extractUsername(token);
			log.info("유저아이디: "+userid);
			//User 정보 세팅
			if (userid != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				UserDetails userDetails = userDetailsService.loadUserByUsername(userid);
				//토큰 유효성 검증 후 권한 정보 세팅
				if (jwtUtil.validateToken(token, userDetails)) {
					UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
							null, userDetails.getAuthorities());
					authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authToken);
					log.info("권한 정보 필터링 결과: " + authToken);
				}
			}
		}
		chain.doFilter(request, response);
	}
}
