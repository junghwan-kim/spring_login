package hello.login.web.login;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import hello.login.domain.login.LoginService;
import hello.login.domain.member.Member;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

	private final LoginService loginService;
	
	@GetMapping("/login")
	public String loginForm(@ModelAttribute("loginForm") LoginForm loginForm) {
		return "login/loginForm";
	}
	
	@PostMapping("/login")
	public String login(@Valid @ModelAttribute LoginForm form, BindingResult bindingResult, HttpServletResponse response) {
		if(bindingResult.hasErrors()) {
			
			return "login/loginForm";
		}
		
		Member loginMember = loginService.login(form.getLoginId(), form.getPassword());
		if(loginMember == null) {
			bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
			return "login/loginForm";
		}
		
		//로그인 성공 처리
		//Cookie idCookie = new Cookie("memberId", String.valueOf(loginMember.getId()));
		Cookie idCookie =  new Cookie("memberId", String.valueOf(loginMember.getId()));
		response.addCookie(idCookie);		
		return "redirect:/";
	}
	
	@PostMapping("/logout")
	public String logout(HttpServletResponse response) {
		expireCookie(response, "memberId");
		return "redirect:/";
	}
	
	private void expireCookie(HttpServletResponse response, String cookieName) {
		Cookie cookie = new Cookie(cookieName, null);
		cookie.setMaxAge(0);
		response.addCookie(cookie);
	}
	
	
}
