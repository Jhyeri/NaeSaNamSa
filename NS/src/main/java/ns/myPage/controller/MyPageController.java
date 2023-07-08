package ns.myPage.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import ns.common.common.CommandMap;
import ns.myPage.service.MyPageService;

@Controller
public class MyPageController {

	Logger log = Logger.getLogger(this.getClass());

	@Resource(name = "myPageService")
	private MyPageService myPageService;

	@RequestMapping(value = "/myPage")
	public ModelAndView pwCheckForm(CommandMap commandMap, HttpServletRequest request) throws Exception {
		log.debug("###### 마이페이지 비밀번호 확인 폼 ######");
		HttpSession session = request.getSession();
		ModelAndView mv = null;

		if (session.getAttribute("session_MEM_NAVER") != null || session.getAttribute("session_MEM_KAKAO") != null) {
			mv = new ModelAndView("redirect:/myPage/main");
		} else {
			mv = new ModelAndView("myPage");
		}

		return mv;
	}

	@RequestMapping(value = "/myPage/pwCheck", consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Map<String, Object> pwCheck(@RequestBody Map<String, Object> map, HttpServletRequest request)
			throws Exception {
		log.debug("###### 마이페이지 비밀번호 확인 ######");
		Map<String, Object> result = new HashMap<>();

		HttpSession session = request.getSession();

		// 세션에 등록된 회원정보
		Map<String, Object> member = (Map<String, Object>) session.getAttribute("session_MEM_INFO");

		Map<String, Object> dbpasswd = myPageService.selectPwCheck(member);

		String realPw = (String) dbpasswd.get("MEM_PW");

		String inputPw = (String) map.get("MEM_PW");

		if (inputPw != null) {
			if (realPw.equals(inputPw)) { // 둘이 비교 후 일치하면
				result.put("result", "success");
			} else { // 일치하지 않으면
				result.put("result", "fail");
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/myPage/main")
	public ModelAndView myPageMain(CommandMap commandMap, HttpServletRequest request) throws Exception {
		log.debug("###### 마이페이지 메인 ######");
		ModelAndView mv = new ModelAndView("myPageMain");

		HttpSession session = request.getSession();
		Map<String, Object> map = (Map<String, Object>) session.getAttribute("session_MEM_INFO");

		String nickName = (String) map.get("MEM_NICKNAME"); // 세션영역에 저장된 닉네임
		int session_mem_num = Integer.parseInt(map.get("MEM_NUM").toString());// 세션영역에 저장된 회원번호

		mv.addObject("MEM_NUM", session_mem_num);
		mv.addObject("MEM_NICKNAME", nickName); // 마이페이지 메인에서 사용함

		return mv;
	}

	@RequestMapping(value = "/myPage/accountModifyForm")
	public ModelAndView accountModifyForm(CommandMap commandMap, HttpServletRequest request) throws Exception {
		log.debug("###### 마이페이지 회원정보 수정 폼 ######");
		ModelAndView mv = null;

		HttpSession session = request.getSession();
		Map<String, Object> resultMap = myPageService.selectAccountInfo(commandMap.getMap());

		if (session.getAttribute("session_MEM_NAVER") != null) {
			mv = new ModelAndView("naverAccountModifyForm");
		} else if (session.getAttribute("session_MEM_KAKAO") != null) {
			mv = new ModelAndView("kakaoAccountModifyForm");
		} else {
			mv = new ModelAndView("accountModifyForm");
		}

		mv.addObject("MEMBER", resultMap);

		return mv;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/myPage/accountModify")
	public ModelAndView accountModify(CommandMap commandMap, HttpServletRequest request) throws Exception {
		log.debug("###### 마이페이지 회원정보 수정 ######");
		ModelAndView mv = new ModelAndView("redirect:/myPage/main");

		HttpSession session = request.getSession();
		Map<String, Object> map = (Map<String, Object>) session.getAttribute("session_MEM_INFO");
		commandMap.put("MEM_NUM", Integer.parseInt(map.get("MEM_NUM").toString()));

		myPageService.updateAccountModify(commandMap.getMap());

		return mv;
	}

	@RequestMapping(value = "/myPage/accountDeleteForm")
	public ModelAndView accountDeleteForm(CommandMap commandMap, HttpServletRequest request) throws Exception {
		log.debug("###### 마이페이지 회원탈퇴 폼 ######");
		ModelAndView mv = new ModelAndView("accountDeleteForm");

		HttpSession session = request.getSession();
		if (session.getAttribute("session_MEM_NAVER") != null || session.getAttribute("session_MEM_KAKAO") != null) {
			mv.addObject("noPw", "Y");
		}

		return mv;
	}

	@RequestMapping(value = "/myPage/accountDeletePw") // ajax, 리턴값을 가지고 success 메소드에서 조건문으로 처리
	public @ResponseBody String accountDeletePw(CommandMap commandMap, HttpServletRequest request) throws Exception {
		log.debug("###### 마이페이지 회원탈퇴 비밀번호 확인 ######");
		String result = "";

		HttpSession session = request.getSession();
		// 세션에 등록된 회원정보를 가져온다.
		Map<String, Object> map = (Map<String, Object>) session.getAttribute("session_MEM_INFO");

		// db에서 가져온 비밀번호
		Map<String, Object> dbpasswd = myPageService.selectPwCheck(map);
		String realPw = (String) dbpasswd.get("MEM_PW");
		
		// 입력받은 비밀번호
		String inputPw = (String) commandMap.get("MEM_PW");

		if (realPw.equals(inputPw)) { // 둘이 비교 후 일치하면
			result = "success";
		} else { // 데이터가 존재하지 않는다면 비밀번호 틀림
			result = "fail";
		}

		return result;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/myPage/accountDelete")
	public @ResponseBody int accountDelete(@CookieValue(value = "emailCookie", required = false) Cookie emailCookie,
			@CookieValue(value = "pwCookie", required = false) Cookie pwCookie,
			CommandMap commandMap, HttpServletRequest request, HttpServletResponse response) throws Exception {

		log.debug("###### 마이페이지 회원탈퇴 ######");

		HttpSession session = request.getSession();
		Map<String, Object> map = (Map<String, Object>) session.getAttribute("session_MEM_INFO");
		commandMap.put("MEM_NUM", Integer.parseInt(map.get("MEM_NUM").toString()));

		int result = 0;

		result = myPageService.deleteAccount(commandMap.getMap());
		// 탈퇴처리된 회원의 수를 리턴

		if (result == 1) { // 탈퇴 성공이면 1

			// 탈퇴 후 등록한 상품 모두 삭제
			myPageService.updateGoodsDelGB(commandMap.getMap());

			// 탈퇴 후 로그아웃
			if (session != null) {
				session.invalidate();
			}

			// 탈퇴 시 쿠키 삭제하기
			if ((emailCookie != null) && (pwCookie != null)) { // 쿠키가 존재하면
				System.out.println("#########쿠키 삭제하기 #######");
				Cookie cookie1 = new Cookie("emailCookie", ""); // 쿠키의 값을 비움
				Cookie cookie2 = new Cookie("pwCookie", "");

				Cookie[] cookies = { cookie1, cookie2 };
				for (int i = 0; i < cookies.length; i++) {
					cookies[i].setMaxAge(0);
					// 쿠키의 유효시간을 0으로 지정하면 쿠키가 삭제됨
					cookies[i].setPath("/");
					// 쿠키 경로 설정. 쿠키를 생성할 때와 똑같이 지정해야만 삭제가 된다.
					response.addCookie(cookies[i]);
				}
			}

		}
		return result;
	}
}
