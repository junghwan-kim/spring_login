package hello.login.web.session;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletResponse;

@Component
public class SessionManager {
	
	public static final String SESSION_COOKIE_NAME = "mySessionId";
	
	private Map<String, Object> sessionStore = new ConcurrentHashMap<>(); //동시성 이슈시 ConcurrentHashMap 이거 써야함
	
	//세션생성
	public void createSession(Object value, HttpServletResponse response) {
		//세션 id를 생성, 값을 세션에 저장
		String sessionId = UUID.randomUUID().toString();
		sessionStore.put(sessionId, value);
		
		//쿠키생성
		Cookie mySessionCookie = new Cookie(SESSION_COOKIE_NAME, sessionId);
		response.addCookie(mySessionCookie);
	}
	
	//세션조회
	public Object getSession(HttpServletRequest request) {
		Cookie sessionCookie = findCookie(request, SESSION_COOKIE_NAME);
		if(sessionCookie == null) {
			return null;
		}
		return sessionStore.get(sessionCookie.getValue());
	}
	
	public void expire(HttpServletRequest request) {
		Cookie sessionCookie = findCookie(request, SESSION_COOKIE_NAME);
		if(sessionCookie != null) {
			sessionStore.remove(sessionCookie.getValue());
		}
	}
	
	private Cookie findCookie(HttpServletRequest request, String cookieName) {
		if(request.getCookies() == null) {
			return null;
		}
		
		return Arrays.stream(request.getCookies())
				.filter(cookie -> cookie.getName().equals(cookieName))
				.findAny()
				.orElse(null);
	}
}
