package ns.member.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import ns.common.common.CommandMap;
import ns.member.service.LoginService;

@Controller
public class LoginController {

	Logger log = Logger.getLogger(this.getClass());

	@Resource(name = "loginService")
	private LoginService loginService;

	@RequestMapping(value = "/loginSelect")
	public ModelAndView loginSelect(@CookieValue(value = "emailCookie", required = false) String emailCookie,
			@CookieValue(value = "pwCookie", required = false) String pwCookie, CommandMap commandMap,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.debug("###### 로그인 선택 ######");

		ModelAndView mv = new ModelAndView("loginSelect");
		return mv;
	}

	@RequestMapping(value = "/loginForm")
	public ModelAndView loginForm(CommandMap commandMap) throws Exception {
		log.debug("###### ID/PW로 시작하기 ######");
		ModelAndView mv = new ModelAndView("loginForm");

		return mv;
	}

	@RequestMapping(value = "/login")
	public @ResponseBody String login(CommandMap commandMap, HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		log.debug("###### 로그인 ######");
		String result = "";
		System.out.println("로그인 요청에 들어온 commandMap : " + commandMap.getMap());

		// 입력받은 아이디를 꺼내 변수 id에 저장
		Map<String, Object> member = loginService.selectId(commandMap.getMap());
		log.debug("아이디 : " + (String) commandMap.get("MEM_EMAIL"));

		HttpSession session = request.getSession();

		if (member == null) { // 가져온 데이터가 없으면
			result = "emailfail";

			log.debug("맵이 비어있음");

		} else { // 가져온 데이터가 있으면

			if (member.get("MEM_STATUS").toString().equals("N")) { // 정지된 회원이 아니면

				if (member.get("MEM_PW").equals(commandMap.get("MEM_PW"))) { // 비밀번호 비교

					// 세션영역에 회원정보 올리기
					session.setAttribute("session_MEM_ID", commandMap.get("MEM_EMAIL"));
					session.setAttribute("session_MEM_PW", commandMap.get("MEM_PW"));
					session.setAttribute("session_MEM_INFO", member);

					// 로그인 유지 체크했을 경우
					if (commandMap.get("useCookie") != null) {
						log.debug("############# 로그인 유지 체크");
						System.out.println("로그인 유지 체크 여부 : " + commandMap.get("useCookie"));

						// 쿠키 생성
						Cookie cookie1 = new Cookie("emailCookie", (String) commandMap.get("MEM_EMAIL"));
						Cookie cookie2 = new Cookie("pwCookie", (String) commandMap.get("MEM_PW"));

						// 쿠키에 대한 설정
						Cookie[] cookies = { cookie1, cookie2 };
						for (int i = 0; i < cookies.length; i++) {
							// 유효시간 설정 (일주일)
							cookies[i].setMaxAge(60 * 60 * 24 * 7);

							cookies[i].setHttpOnly(true);

							// 쿠키 경로 설정
							cookies[i].setPath("/");

							// response로 쿠키 전달
							response.addCookie(cookies[i]);
						}
					}

					result = "success";

					log.debug("로그인 통과, 세션에 저장");

				} else { // 비밀번호가 일치하지 않을 때
					result = "pwfail";

					log.debug("비밀번호 틀림");
				}

			} else if (member.get("MEM_STATUS").equals("Y")) { // 정지된 회원이면
				result = "suspended";
			}

		}

		return result;
	}

	@RequestMapping(value = "/logout")
	public ModelAndView logout(@CookieValue(value = "emailCookie", required = false) Cookie emailCookie,
			@CookieValue(value = "pwCookie", required = false) Cookie pwCookie, CommandMap commandMap,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.debug("###### 로그아웃 ######");
		ModelAndView mv = new ModelAndView("redirect:/main");

		HttpSession session = request.getSession();
		if (session != null) {
			if (session.getAttribute("session_MEM_KAKAO") != null) {
				mv = new ModelAndView(
						"redirect:https://kauth.kakao.com/oauth/logout?client_id=b3fcf64864d96f4d4de5224fb6d56b33&logout_redirect_uri=http://localhost:8080/ns/logoutKakao");
				return mv;
			}

			// 로그아웃 시 쿠키 삭제하기
			if ((emailCookie != null) && (pwCookie != null)) { // 쿠키가 존재하면
				System.out.println("#########쿠키 삭제하기 #######");
				Cookie cookie1 = new Cookie("emailCookie", ""); // 쿠키의 값을 비움
				Cookie cookie2 = new Cookie("pwCookie", "");

				System.out.println("삭제할 쿠키 : " + emailCookie.toString() + ", " + pwCookie.toString());

				Cookie[] cookies = { cookie1, cookie2 };
				for (int i = 0; i < cookies.length; i++) {
					cookies[i].setMaxAge(0);
					// 쿠키의 유효시간을 0으로 지정하면 쿠키가 삭제됨
					cookies[i].setPath("/");
					// 쿠키 경로 설정. 쿠키를 생성할 때와 똑같이 지정해야만 삭제가 된다.
					response.addCookie(cookies[i]);
				}
			}
			session.invalidate();
		}
		return mv;
	}

	@RequestMapping(value = "/findId")
	public ModelAndView findId(CommandMap commandMap) throws Exception {
		log.debug("###### 아이디 찾기 ######");
		ModelAndView mv = new ModelAndView("jsonView");
		// jsonView를 사용하면 컨트롤러에서 @ResponseBody를 쓰지 않고 modelandview로 리턴하여도
		// ajax로 데이터를 받을 수 있다.

		Map<String, Object> map = loginService.findIdWithBirth(commandMap.getMap());
		String email = (String) map.get("MEM_EMAIL");

		mv.addObject("MEM_EMAIL", email);

		return mv;
	}

	@RequestMapping(value = "/findPw")
	public ModelAndView findPw(CommandMap commandMap) throws Exception {
		log.debug("###### 비밀번호 찾기 ######");
		ModelAndView mv = new ModelAndView("jsonView");

		Map<String, Object> map = loginService.findPwWithEmail(commandMap.getMap());
		String pw = (String) map.get("MEM_PW");

		mv.addObject("MEM_PW", pw);

		return mv;
	}
}
