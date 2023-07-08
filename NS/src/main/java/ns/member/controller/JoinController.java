package ns.member.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import ns.common.common.CommandMap;
import ns.member.service.JoinService;

@Controller
public class JoinController {

	Logger log = Logger.getLogger(this.getClass());

	@Resource(name = "joinService")
	private JoinService joinService;

	// 회원가입 폼 띄우기
	@RequestMapping(value = "/joinForm")
	public ModelAndView joinForm(CommandMap commandMap) throws Exception {
		log.debug("###### 회원가입 ######");

		ModelAndView mv = new ModelAndView("joinForm"); // tiles이름 (tiles.xml에서 이름에 해당하는 jsp 주소를 확인할 수 있음

		return mv;
	}

	// 닉네임 중복확인
	@RequestMapping(value = "/join/nickCheck")
	public @ResponseBody String nickCheck(CommandMap commandMap) throws Exception { // ajax로 요청받고 결과 리턴
		log.debug("###### 닉네임 중복확인 ######");

		String result = ""; // 닉네임 중복확인 후 결과를 해당 변수에 받아온다

		Map<String, Object> map = joinService.selectNickCheck(commandMap.getMap());

		if (map != null) {
			result = "fail";
		} else {
			result = "success";
		}

		return result;
	}

	// 이메일 중복확인
	@RequestMapping(value = "/join/emailCheck")
	public @ResponseBody String emailCheck(String email) throws Exception { // ajax로 요청받고 결과 리턴
		log.debug("###### 이메일 중복확인 ######");
		Map<String, Object> emailMap = new HashMap<>();
		emailMap.put("MEM_EMAIL", email);

		String result = ""; // 이메일 중복확인 후 결과를 해당 변수에 받아온다

		Map<String, Object> map = joinService.selectEmailCheck(emailMap);

		if (map != null) {
			result = "fail";
		} else {
			result = "success";
		}

		return result;

	}

	// 회원가입 가능 여부 검토 (처음 가입하는 회원 or 회원 탈퇴한지 7일 지났을 경우 회원가입 가능)
	@RequestMapping(value = "/joinAvailable")
	public @ResponseBody String joinAvailable(CommandMap commandMap) throws Exception {
		log.debug("###### 회원가입 가능 여부 검토 ######");

		// MEM_PHONE
		String phone = "";

		List<Object> list = new ArrayList<Object>();

		list.add(commandMap.get("MEM_PHONE1"));
		list.add(commandMap.get("MEM_PHONE2"));
		list.add(commandMap.get("MEM_PHONE3"));

		for (int i = 0; i < list.size(); i++) {
			phone += (String) list.get(i);
			// 번호를 합쳐 하나의 문자열로 만듦
		}

		commandMap.put("MEM_PHONE", phone); // 하나의 문자열로 합쳐진 번호를 맵에 저장

		System.out.println("map 확인 : " + commandMap.getMap());

		int check = joinService.selectDelGB(commandMap.getMap());
		System.out.println("check : " + check);

		String result = "";

		if (check == 1) {
			// DB에 회원가입 처리 전, 회원 탈퇴한 이력이 있고 7일 지났는지 여부 확인
			int delCount = joinService.selectDelCount(commandMap.getMap());
			System.out.println("delCount : " + delCount);

			if (delCount == 0) { // 회원 탈퇴 후 7일이 지나지 않았을 경우
				result = "fail";
			} else { // 회원 탈퇴 후 7일이 지났거나, 혹은 처음으로 가입할 경우
				// DB에 회원가입 처리
				joinService.insertMember(commandMap.getMap());
				result = "success";
			}
		} else {
			joinService.insertMember(commandMap.getMap());
			result = "success";
		}

		return result;
	}

	// 회원가입 성공
	@RequestMapping(value = "/joinSuccess")
	public ModelAndView insertMember(CommandMap commandMap) throws Exception {
		log.debug("###### 회원가입 성공 ######");

		ModelAndView mv = new ModelAndView("joinSuccess");

		mv.addObject("MEM_NAME", commandMap.get("MEM_NAME"));
		return mv;

	}
}
