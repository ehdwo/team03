package ksmart39.mybatis.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import ksmart39.mybatis.domain.Member;

@Mapper
public interface MemberMapper {
//select는 list로 변환이된다.
	
	//회원 가입
	public int addMember(Member member);
	
	//회원 목록 조회
	public List<Member> getMemberList(Map<String, Object> paramMap);
	
	//회원 수정
	public Member getmemberInfoById(String MemberId); 
	
	//회원 정보 수정
	public int modifyMember(Member member);
	
	public int removeMemberById(String memberId);
	
	//회원 테이블
	public int removeGoodsById(String memberId);
	
	//상품 테이블(구매자)
	public int removeOrderById(String memberId);
	
	//주문 테이블(판매자)
	public int removeOrderSellerById(String memberId);
	
	//로그인 테이블
	public int removeLoginById(String memberId);
	
//	//로그인 체크
//	public int loginCheck(int result);
}
