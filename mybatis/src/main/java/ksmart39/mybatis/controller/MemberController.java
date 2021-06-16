package ksmart39.mybatis.controller;

import java.lang.ProcessBuilder.Redirect;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import groovy.util.logging.Log;
import ksmart39.mybatis.domain.Member;
import ksmart39.mybatis.sevice.GoodsService;
import ksmart39.mybatis.sevice.MemberService;

@Controller
public class MemberController {
	/**
	 * 필드 주입방식(DI)
	 * MemberMapper memberMapper = new  MemberMapper();
	 * 
	 * setter 주입방식
	 * setter 메서드 memberService 
	 * 
	 * 생성자메서드 주입방식
	 * 생성자메서드 memberSevice 
	 */
	/**
	 * 	@Autowired // 객체를 생성하는것이 아니라 상속 받았다. 
	 *	private MemberService memberService;
	 *	@Autowired
	 *	private GoodsService  goodsService// 필드주입방식 ...
	 */
	
	/*setter 주입방식
	 * 
	 * private MemberService memberService; 
	 * @Autowired 
	 * public void setmenberService(MemberService memberService){ 
	 * this.memberService =memberService;
	 * }
	 * 
	 * private MemberService memberService; 
	 * private MemberService memberMapper; 
	 * @Autowired 
	 * public void setmenberService(MemberService memberService, GoodsService  goodsService){ 
	 * this.memberService = memberService;
	 * this.memberMapper = memberMapper;
	 * }
	 * 
	 */
	
	private static final Logger log = LoggerFactory.getLogger(MemberController.class);
		
	
	// 생성자메서드 주입방식 final을 써서 service와 왕복 될 수 있는게 아니고 그냥 한 번 가면 편도로 갈 수 있다.
	private final MemberService memberService;
	private final GoodsService  goodsService; //여기서 쓰는 노란줄은 사용하지 않아서 뜨는 노란줄
	
	@Autowired
	public MemberController(MemberService memberService, GoodsService goodsService) {
		this.memberService = memberService;
		this.goodsService = goodsService;
	}
	
	@PostConstruct
	public void memberControllerInit() {
		log.info("===========================================");
		log.info("memberController 객체 생성");
		log.info("===========================================");
//		System.out.println("===========================================");
//		System.out.println("memberController 객체 생성");
//		System.out.println("===========================================");
	}
	
	@PostMapping("/memberIdCheck")
	@ResponseBody
	public boolean memberIdCheck(@RequestParam(name="memberId", required = false) String memberId) {
		boolean idCheck = true;
		log.info("memberIdCheck   memberId :::: {}", memberId);
		
		Member member = memberService.getmemberInfoById(memberId);
		
		if(member != null) idCheck = false;
		//중복된 아이디가 있는경우에는 false
		
		return idCheck;
	}
	
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}
	
	@PostMapping("/login")  /*HttpServletRequest request*/
	public String login(@RequestParam(value = "memberId", required = false)String memberId,
						@RequestParam(value = "memberPw", required = false)String memberPw,
						HttpSession session
						,RedirectAttributes reAttr) {
		if(memberId != null && !"".equals(memberId) && memberPw != null && !"".equals(memberPw)) {
			Map<String, Object> resultMap = memberService.loginMember(memberId, memberPw);
			
			boolean loginCheck = (boolean) resultMap.get("loginCheck");
			Member loginMember = (Member) resultMap.get("loginMember");
			if(loginCheck) {
				session.setAttribute("SID",		 loginMember.getMemberId());
				session.setAttribute("SNAME",	 loginMember.getMemberName());
				session.setAttribute("SLEVEL",	 loginMember.getMemberLevel());
				if("1".equals(loginMember.getMemberLevel())){
					session.setAttribute("SLEVELNAME", "관리자");
				}else if("2".equals(loginMember.getMemberLevel())){
					session.setAttribute("SLEVELNAME", "판매자");
				}else{
					session.setAttribute("SLEVELNAME", "구매자");
				}
				return "redirect:/";
			}
		}
		reAttr.addAttribute("loginResult", "등록된 회원이 없습니다."); //RedirectAttributes --> 알아서 한글 인코딩 해줌
		return "redirect:/login";								//return "redirect:/login?loginResult=등록된 회원이 없습니다.";
	}
	
	@GetMapping("/login")
	public String login(Model model
						,@RequestParam(name = "loginResult", required = false)String loginResult) {
		model.addAttribute("title", "로그인 화면");
		if(loginResult != null) model.addAttribute("loginResult",loginResult);
		return "login/login";
	}
	
	@PostMapping("/removeMember")
	public String removeMember(@RequestParam(value = "memberId",required = false) String memberId,
							   @RequestParam(value = "memberPw",required = false) String memberPw
							   ,RedirectAttributes redorectAttr) {
			log.info("-----------------------------------------------------------------");
			log.info("화면에서 입력 받은 값 (회원탈퇴폼)memberId : {}", memberId);
			log.info("화면에서 입력 받은 값 (회원탈퇴폼)memberPw : {}", memberPw);
			log.info("-----------------------------------------------------------------");
			
//			System.out.println("-----------------------------------------------------------------");
//			System.out.println("화면에서 입력 받은 값 (회원탈퇴폼)memberId :"+ memberId);
//			System.out.println("화면에서 입력 받은 값 (회원탈퇴폼)memberPw :"+ memberPw);
//			System.out.println("-----------------------------------------------------------------");
			
			if(memberPw != null && !"".equals(memberPw)) {
				boolean result =  memberService.removMember(memberId, memberPw);
				if(result) {
					return "redirect:/memberList";
				}
			}
			redorectAttr.addAttribute("memberId", memberId);
			redorectAttr.addAttribute("result", "삭제실패");
			
		return "redirect:/removeMember";
	}
	@GetMapping("/removeMember")
	public String removeMember(@RequestParam(value = "memberId",required = false) String memberId,
							   @RequestParam(value = "result",required = false) String result,
							   Model model) {
			log.info("-----------------------------------------------------------------");
			log.info("화면에서 입력 받은 값 (회원탈퇴폼) : {}", memberId);
			log.info("-----------------------------------------------------------------");
//			System.out.println("-----------------------------------------------------------------");
//			System.out.println("화면에서 입력 받은 값 (회원탈퇴폼) :"+ memberId);
//			System.out.println("-----------------------------------------------------------------");
			
		model.addAttribute("title","회원탈퇴폼");
		model.addAttribute("memberId",memberId);
		
		if(result != null) model.addAttribute("result",result);
		return "member/removeMember";
	}
	
	//00000000수정 화면에서 리스트로 갈 때
	@PostMapping("/modifyMember")
	public String modifyMember(Member member) {
		log.info("===========================================");
		log.info("화면에서 입력 받은 값 (수정화면 폼) : {}", member);
		log.info("===========================================");
//		System.out.println("===========================================");
//		System.out.println("화면에서 입력 받은 값 (수정화면 폼) : "+ member);
//		System.out.println("===========================================");

		memberService.modifyMember(member);
		
		return "redirect:/memberList";
	}
	
	/**
	 * @RequestParam(name = "memberId", required =false)  = request.getParmeter("memberId");
	 * @RequestParam(name = "memberId", required =false) == String memberId == 
	 * */
	//00000000리스트에서 수정화면으로 갈 때 
	@GetMapping("/modifyMember")
	public String modifyMember(@RequestParam(name = "memberId", required = false) String memberId, Model model) {
		
		log.info("===========================================================================");
		log.info("화면에서 입력받는 값(회원 수정 폼) : {}", memberId);
		log.info("===========================================================================");
//		System.out.println("============================================================================");
//		System.out.println("화면에서 입력받는 값(회원 수정 폼) : "+ memberId);
//		System.out.println("============================================================================");
		//1.회원아이디로 회원테이블을 조회한 Member객체
		Member member = memberService.getmemberInfoById(memberId);
		
		//2.model 화면에 전달할 객체
		model.addAttribute("title","회원수정폼");
		model.addAttribute("member", member);
		return "member/modifyMember";
	}
	/**
	 * @parm memberId, memberPw ...... => Member dto의 멤버 변수와 이름이 같다면 스프링이 알아서 바인딩(담아준다) 해준다.
	 *			커멘드 객체(Member) 
	 *
	 * */
	
	//00000000회원 가입 화면에서 리스트로 갈 때
	@PostMapping("/addMember")
	public String addMember(Member member) {
		
		log.info("-----------------------------------------------------------------");
		log.info("화면에서 입력 받은 값 (회원가입폰) : {}", member);
		log.info("-----------------------------------------------------------------");
//		System.out.println("-----------------------------------------------------------------");
//		System.out.println("화면에서 입력 받은 값 (회원가입폰) :"+ member);
//		System.out.println("-----------------------------------------------------------------");
		
		memberService.addMemnber(member);
		
		return "redirect:/memberList";
	}
	//00000000회원가입 화면으로 갈 때
	@GetMapping("/addMember")
	public String addMember(Model model) {
		
		model.addAttribute("title","회원가입폼");
		
		return "member/addMember";
	}
	//00000000select member 전체 조회
	@GetMapping("/memberList")
	public String getMemberList(Model model
								,@RequestParam(name = "searchKey", required = false)String searchKey
								,@RequestParam(name = "searchValue", required = false)String searchValue
	/* ,RedirectAttributes redorectAttr */) {
		
		log.info("==========================================================");
		log.info("searchKey : {}", searchKey);
		log.info("searchValue : {}", searchValue);
		log.info("==========================================================");
//		System.out.println("==========================================================");
//		System.out.println("searchKey : "+ searchKey);
//		System.out.println("searchValue : "+ searchValue);
//		System.out.println("==========================================================");
		
		//map을 활용해서 검색 키워드를 정리
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("searchKey",searchKey);
		paramMap.put("searchValue",searchValue);
		
//		if(memberPw != null && !"".equals(memberPw)) {
//			boolean result =  memberService.removMember(memberId, memberPw);
//			if(result) {
//				return "redirect:/memberList";
//			}
//		}
//		redorectAttr.addAttribute("memberId", memberId);
//		redorectAttr.addAttribute("result", "삭제실패");
		
		List<Member> memberList = memberService.getMemberList(paramMap);
//		
//		if(searchValue != null && !"".equals(searchValue)) {
//			List<Member> result = memberService.getMemberList(paramMap);
//			if(result != null) {
//				redorectAttr.addAttribute("result", "검색결과를 입력해주세요");
//				
//			}
//		}
		log.info("==========================================================");
		log.info("memberList : {}", memberList);
		log.info("==========================================================");
//		System.out.println("==========================================================");
//		System.out.println("memberList : "+ memberList);
//		System.out.println("==========================================================");
		model.addAttribute("title", "회원목록");
		model.addAttribute("memberList", memberList);
		return "member/memberList";
	}
}
