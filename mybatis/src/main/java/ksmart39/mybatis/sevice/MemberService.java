package ksmart39.mybatis.sevice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ksmart39.mybatis.dao.MemberMapper;
import ksmart39.mybatis.domain.Member;

@Service
@Transactional //삭제시 하나라도 오류라면 다시 롤백 시킨다.
public class MemberService {
	/**
	 * 필드 주입방식(DI)
	 * MemberMapper memberMapper = new  MemberMapper(); <--인터페이스는 객체 생성이 안된다.
	 * setter메서드 memberMapper
	 * 생성자 메서드 memberMapper
	 * 
	 * 서비스에서 dao로 호출하고,
	 */
	
	private static final Logger log = LoggerFactory.getLogger(MemberService.class);

	@Autowired
	private MemberMapper memberMapper;
	
	@PostConstruct
	public void memberServiceInit() {
		log.info("===========================================");
		log.info("memberService 객체 생성");
		log.info("===========================================");
//		System.out.println("===========================================");
//		System.out.println("memberService 객체 생성");
//		System.out.println("===========================================");
	}
//	public int loginCheck(int result) {
//		return memberMapper.loginCheck(result);
//	}
	public Map<String, Object> loginMember(String memberId, String memberPw) {
		//로그인 여부
		boolean loginCheck = false;
		
		//로그인 결과를 담는 Map
		Map<String, Object> memberInfoMap = new HashMap<String, Object>();
		
		//로그인 처리
		Member member = memberMapper.getmemberInfoById(memberId);
		if(member != null && memberPw.equals(member.getMemberPw())) {
			loginCheck = true;
			memberInfoMap.put("loginMember", member);
		}
		memberInfoMap.put("loginCheck", loginCheck);
		
		return memberInfoMap;
	}
	
	/* public int removMember(Member member) { */
	public boolean removMember(String memberId, String memberPw) {
		//삭제 여부
		boolean removeCheck = false;
		
		//비밀번호가 맞다면
		Member member = memberMapper.getmemberInfoById(memberId);
		
		if(member != null && memberPw.equals(member.getMemberPw())) {
			//삭제 프로세스 진행
			//판매자
			//삭제 순서 주문 -> 상품 -> 로그인(회원테이블 전에 아무거나 삭제 가능) ->회원
			if("2".equals(member.getMemberLevel())) {
				//1.주문, 상품, 회원, 로그인
				memberMapper.removeOrderSellerById(memberId);
				memberMapper.removeGoodsById(memberId);
			}
			if("3".equals(member.getMemberLevel())) {
				//1.주문, 회원, 로그인
				//삭제 순서 주문 -> 로그인(회원테이블 전에 아무거나 삭제 가능) ->회원	
				memberMapper.removeOrderById(memberId);
			}
			//1.통합 로그인 이력, 회원 정보
			memberMapper.removeLoginById(memberId);
			memberMapper.removeMemberById(memberId);
			removeCheck = true;
		}
		
		
		return removeCheck;
	}
	
	
	public int modifyMember(Member member) {
		
		return memberMapper.modifyMember(member);
	}
	
	public Member getmemberInfoById(String memberId) {
				
		return memberMapper.getmemberInfoById(memberId);
	}
	
	public List<Member> getMemberList(Map<String, Object> paramMap){
		
		List<Member> memberList = memberMapper.getMemberList(paramMap);
		
		return memberList;
	}
	
	public int addMemnber(Member member) {
		
		int result = memberMapper.addMember(member);
		return result;
		//return memberMapper.addMember(member); 위에꺼 함축한거
	}
}
