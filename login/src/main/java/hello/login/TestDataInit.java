package hello.login;


import org.springframework.stereotype.Component;

import hello.login.domain.item.Item;
import hello.login.domain.item.ItemRepository;
import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TestDataInit {
	private final ItemRepository itemRepository;
	private final MemberRepository memberRepository;
	
	@PostConstruct
	public void init() {
		itemRepository.save(new Item("ItemA", 10000,10));
		itemRepository.save(new Item("ItemB", 20000,20));
		
		Member member = new Member();
		member.setLoginId("test");
		member.setPassword("test!");
		member.setName("테스터");
		memberRepository.save(member);
	}
}
