package hello.login.web.member;


import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("members/")
public class MemberController {

	private final MemberRepository memberRepository;
	
	@GetMapping("/add")
	public String addForm(@ModelAttribute("member") Member member ) {		
		return "members/addMemberForm";
	}
	
	@PostMapping("/add")
	public String save(@Valid @ModelAttribute Member member, BindingResult result) {
		log.info("result={}",result);
		if(result.hasErrors()) {
			return "members/addMemberForm";
		}
		
		memberRepository.save(member);
		return "redirect:/";
	}
	
}
