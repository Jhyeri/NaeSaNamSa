package ns.member.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import ns.common.common.CommandMap;
import ns.member.service.JoinService;
import ns.member.service.LoginService;

@Controller
public class KakaoController {

	Logger log = Logger.getLogger(this.getClass());

	@Resource(name = "loginService")
	private LoginService loginService;

	@Resource(name = "joinService")
	private JoinService joinService;

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/loginKakao") //인가코드 요청을 하고나서 인가코드를 받는 redirectURI
	public ModelAndView loginKakao(CommandMap commandMap) throws Exception {
		log.debug("###### 카카오 로그인 ######");
		ModelAndView mv = null;

		String clientId = "b3fcf64864d96f4d4de5224fb6d56b33";// 애플리케이션 클라이언트 아이디값
		String code = (String) commandMap.get("code"); // 인증 코드. 카카오 서버로부터 받은 인가코드 (토큰 요청에 필요)
		String redirectURI = "http://localhost:8080/ns/loginKakao";
		String apiURL = "https://kauth.kakao.com/oauth/token"; //토큰을 요청할 url
		String access_token = "";

		System.out.println("################ apiURL : " + apiURL);

		try {
			//http 설정
			URL url = new URL(apiURL); //apiURL을 나타내는 URL 객체
			HttpURLConnection con = (HttpURLConnection) url.openConnection(); //openConnection() :  url 객체에 대한 연결을 담당하는 URLConnection 객체를 반환
			//HttpURLConnection : HTTP 프로토콜 통신을 위한 클래스
			//openConnection() 메소드를 통해 URLConnection 객체를 얻음
			// HttpURLConnection객체는 URLConnection 객체를 확장하고(상속받고)있기 때문에 Type Casting을 통해 HttpURLConnection객체를 쉽게 얻을 수 있음
			con.setRequestMethod("POST"); //전송방식 설정
			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); //Request Header 정의
			con.setDoOutput(true); //OutputStream 객체로 전송할 데이터가 있다는 옵션을 설정

			//카카오 서버에 전송할 파라미터들
			StringBuilder sb = new StringBuilder();
			sb.append("grant_type=authorization_code");
			sb.append("&client_id=" + clientId); // 본인이 발급받은 key
			sb.append("&redirect_uri=" + redirectURI); // 본인이 설정해 놓은 경로
			sb.append("&code=" + code); //인가코드 사용

//			int length = sb.toString().getBytes().length;
//			con.setRequestProperty("Content-Length", String.valueOf(length));

//			System.out.println("#################### params : " + sb.toString());

			OutputStream os = con.getOutputStream();
			OutputStreamWriter writer = new OutputStreamWriter(os);
			writer.write(sb.toString()); //데이터 쓰기
			writer.flush();
			writer.close();
			os.close();
			con.connect(); //연결

			//응답코드 얻기
			int responseCode = con.getResponseCode();
			BufferedReader br;
			System.out.println("responseCode=" + responseCode);
			if (responseCode == 200) { // 정상 호출
				br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			} else { // 에러 발생
				br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
			}
			String inputLine;
			StringBuffer res = new StringBuffer();
			while ((inputLine = br.readLine()) != null) {
				res.append(inputLine);
			}
			br.close();
			if (responseCode == 200) {
				System.out.println("##################### res : " + res);
				Map<String, String> map = new HashMap<>();
				//json형태로 온 응답 데이터를 Map 객체로 변환해주기
				Gson gson = new Gson();
				map = gson.fromJson(res.toString(), map.getClass());

				access_token = map.get("access_token");
				mv = new ModelAndView("redirect:/loginKakao/result");
				mv.addObject("access_token", access_token);
			}
		} catch (Exception e) {
			System.out.println(e);
		}

		return mv;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/loginKakao/result")
	public ModelAndView loginKakaoResult(CommandMap commandMap, HttpServletRequest request) throws Exception {
		log.debug("###### 카카오 로그인2 ######");
		ModelAndView mv = null;

		String access_token = (String) commandMap.get("access_token");
		String apiURL = "https://kapi.kakao.com/v2/user/me";

		try {
			URL url = new URL(apiURL);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
			con.setRequestProperty("Authorization", "Bearer " + access_token);

			int responseCode = con.getResponseCode();
			BufferedReader br;
			System.out.println("responseCode=" + responseCode);
			if (responseCode == 200) { // 정상 호출
				br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			} else { // 에러 발생
				br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
			}
			String inputLine;
			StringBuffer res = new StringBuffer();
			while ((inputLine = br.readLine()) != null) {
				res.append(inputLine);
			}
			br.close();
			if (responseCode == 200) {
				JsonParser jp = new JsonParser();
				JsonObject jo = (JsonObject) jp.parse(res.toString());
				JsonObject jo2 = (JsonObject) jo.get("kakao_account");
				JsonObject nickjo = (JsonObject) jo2.get("profile");

				System.out.println("########## result + " + jo2);

				Map<String, Object> resultMap = new HashMap<>();
				Map<String, Object> memNameMap = new HashMap<>();
				Map<String, Object> idMap = new HashMap<>();
				Gson gson = new Gson();
				resultMap = gson.fromJson(jo2, resultMap.getClass()); //카카오계정정보
				memNameMap = gson.fromJson(nickjo, memNameMap.getClass());//프로필정보
				idMap = gson.fromJson(jo, idMap.getClass()); //id(회원번호)

				System.out.println("########## resultMap + " + resultMap);
				System.out.println("########## memName + " + memNameMap);
				System.out.println("########## idMap + " + idMap);

				String uniqueId = String.valueOf(idMap.get("id"));
				resultMap.put("MEM_EMAIL", uniqueId);

				resultMap.put("MEM_PW", idMap.get("id"));
				String gender = (String) resultMap.get("gender");
				if (gender.equals("male")) {
					resultMap.put("MEM_GEN", "1");
				} else {
					resultMap.put("MEM_GEN", "2");
				}
				resultMap.put("MEM_NAME", memNameMap.get("nickname"));

				StringBuffer sb = new StringBuffer();
				String birthyear = (String) resultMap.get("age_range");
				String birthday = (String) resultMap.get("birthday");
				sb.append(birthyear.substring(0, 2));
				sb.append(birthday);
				resultMap.put("MEM_BIRTH", sb.toString());

				Map<String, Object> emailMap = joinService.selectEmailCheck(resultMap);
				System.out.println("###################### emailMap : " + emailMap);
				if (emailMap == null) { //가입한 사람이 아니면
					System.out.println("########## resultMap + " + resultMap);

					mv = new ModelAndView("kakaoJoinForm");
					mv.addObject("member", resultMap);
					return mv;
				} else {
					mv = new ModelAndView("redirect:/main");

					Map<String, Object> memPhone = joinService.selectMemPhone(resultMap);
					resultMap.put("MEM_PHONE", memPhone.get("MEM_PHONE"));

					int check = joinService.selectDelGB(resultMap);

					if (check == 1) {
						// DB에 회원가입 처리 전, 회원 탈퇴한 이력이 있고 7일 지났는지 여부 확인
						int delCount = joinService.selectDelCount(resultMap);
						System.out.println("delCount : " + delCount);

						if (delCount == 0) { // 회원 탈퇴 후 7일이 지나지 않았을 경우
							mv.addObject("loginFail", "Y");
							return mv;
						} else { //7일이 지났으면
							joinService.updateDelGB(resultMap);
						}
					}

					Map<String, Object> member = loginService.selectId(resultMap);

					HttpSession session = request.getSession();
					session.setAttribute("session_MEM_ID", emailMap.get("MEM_EMAIL"));
					session.setAttribute("session_MEM_INFO", member);
					session.setAttribute("session_MEM_KAKAO", "Y");
					session.setAttribute("access_token", access_token);

					return mv;
				}

			}
		} catch (Exception e) {
			System.out.println(e);
		}

		return mv;
	}

	@RequestMapping(value = "/logoutKakao")
	public ModelAndView logoutKakao(CommandMap commandMap, HttpServletRequest request) throws Exception {
		log.debug("###### 카카오 로그아웃 ######");
		ModelAndView mv = null;

		HttpSession session = request.getSession(false);
		String access_token = (String) session.getAttribute("access_token");
		String apiURL = "https://kapi.kakao.com/v1/user/logout";

		try {
			URL url = new URL(apiURL);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
			con.setRequestProperty("Authorization", "Bearer " + access_token);

			int responseCode = con.getResponseCode();
			BufferedReader br;
			System.out.println("responseCode=" + responseCode);
			if (responseCode == 200) { // 정상 호출
				br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			} else { // 에러 발생
				br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
			}
			String inputLine;
			StringBuffer res = new StringBuffer();
			while ((inputLine = br.readLine()) != null) {
				res.append(inputLine);
			}
			br.close();
			if (responseCode == 200) {
				mv = new ModelAndView("redirect:/main");
				session.invalidate();
				return mv;
			}
		} catch (Exception e) {
			System.out.println(e);
		}

		return mv;
	}

}
