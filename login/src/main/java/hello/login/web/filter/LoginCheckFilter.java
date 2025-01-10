package hello.login.web.filter;

import java.io.IOException;

import org.springframework.util.PatternMatchUtils;

import hello.login.web.SessionConst;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j

public class LoginCheckFilter implements Filter{
	
	private static final String[] whiteList = {"/", "/members/add","/login","/logout","/css/*"};
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest httpRequest =  (HttpServletRequest) request;
		String requestURI = httpRequest.getRequestURI();
		
		HttpServletResponse httpResponse =  (HttpServletResponse) response;
		
		try {
			log.info("인증 체크 필터 시작{}", requestURI);
			if(isLoginCheckPath(requestURI)) {
				log.info("인증 체크 로직 실행{}", requestURI);
				HttpSession session = httpRequest.getSession(false);
				if(session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null) {
					log.info("미인증 사용자 요청 {}",requestURI);
					//로그인으로 리다이렉트
					httpResponse.sendRedirect("/login?redirectURL="+requestURI); //로그인 후 현재페이지로 다시 이동
					return;
				}
			}
			chain.doFilter(request, response);
		} catch (Exception e) { //예외 로깅 가능 하지만, 톰켓까지 예외를 보내주어야 함.
			throw e;
		} finally {
			log.info("인증 체크 필터 종료 {}",requestURI);
		}
	}
	
	//화이트 리스트의경우 인증 체크 안함.
	private boolean isLoginCheckPath(String requestURI) {
		return !PatternMatchUtils.simpleMatch(whiteList, requestURI);
	}

}
