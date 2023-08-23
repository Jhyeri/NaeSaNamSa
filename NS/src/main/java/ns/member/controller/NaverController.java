package ns.member.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
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
public class NaverController {

	Logger log = Logger.getLogger(this.getClass());

	@Resource(name = "loginService")
	private LoginService loginService;

	@Resource(name = "joinService")
	private JoinService joinService;

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/loginNaver")
	public ModelAndView loginNaver(CommandMap commandMap) throws Exception {
		log.debug("###### 네이버 로그인 ######");
		//네이버 로그인 창에서 아이디, 비밀번호 로그인하여 성공하면, 접근코드(승인코드)를 줌
		//그 승인코드를 통해 토큰을 요청함
		//토큰 : 이 회원에 대한 여러가지 정보를 요청할 수 있는 토큰
		//이 토큰으로 회원에 대한 정보를 요청할 수 있게 되는 것
		//이 메소드에 해당하는 요청이 토큰을 받는데, 그 토큰으로 네이버에다 필요한 회원정보를 요청함
		
		ModelAndView mv = new ModelAndView("redirect:/loginNaver/result");

		String clientId = "3kvMcStoYugzaUkMxzep";// 애플리케이션 클라이언트 아이디값";
		String clientSecret = "12yuGsDOrR";// 애플리케이션 클라이언트 시크릿값";
		
		//네이버 로그인창에서 로그인 성공했을 때 받는 응답에 관한 것이 commandMap을 통해 전송 옴
		//따라서 commandMap에서 jsp에서 넘겼던 파라미터를 꺼냄
		String code = (String) commandMap.get("code"); //jsp에서 전달한 파라미터
		String state = (String) commandMap.get("state"); //jsp에서 전달한 파라미터
		String redirectURI = URLEncoder.encode("http://localhost:8080/ns/loginNaver/result", "UTF-8");
		String apiURL;
		apiURL = "https://nid.naver.com/oauth2.0/token?grant_type=authorization_code&";
		//
		apiURL += "client_id=" + clientId;
		apiURL += "&client_secret=" + clientSecret;
		apiURL += "&redirect_uri=" + redirectURI; //토큰요청이 성공했을 때 리다이렉트시킬 uri
		apiURL += "&code=" + code; //네이버가 주는 승인코드
		apiURL += "&state=" + state;
		//
		String access_token = "";
		//String refresh_token = "";
		System.out.println("apiURL=" + apiURL);

		//자바코드 안에서 url 요청 보내기
		try {
			URL url = new URL(apiURL);
			HttpURLConnection con = (HttpURLConnection) url.openConnection(); // HttpURLConnection객체 생성
			con.setRequestMethod("GET"); //요청방식 지정
			int responseCode = con.getResponseCode(); //200, 400, 404 등 응답코드를 받아오도록 함
			//원래는 con.connect()를 해야 url요청이 들어가지만 getResponseCode()만 써도 요청이 되긴 함
			
			//여기까지가 url지정하고 전송방식 등 지정
			
			//원래는 outputStream으로 우리가 정보를 보내고, inputStream으로 정보를 받아야 하지만 
			//여기서부터는 네이버에서 받은 결과를 변환하는? 코드 ???
			BufferedReader br;
			System.out.print("responseCode=" + responseCode); //받은 응답코드를 변수에 저장
			if (responseCode == 200) { // 정상 호출
				br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			} else { // 에러 발생
				br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
			}
			String inputLine;
			StringBuffer res = new StringBuffer(); //네이버에서 받은 정보를 받아서 string 한 줄로 만들기 
			while ((inputLine = br.readLine()) != null) { //null이 아닐 경우 기존 문자열 끝에 받아온 정보를 추가함
				res.append(inputLine);
			}
			br.close();
			
			//개발자가 정보를 가공할 수 있는 코드
			if (responseCode == 200) { //요청 성공일 경우

				Map<String, String> map = new HashMap<>();
				Gson gson = new Gson();
				map = gson.fromJson(res.toString(), map.getClass()); //json으로 받은 문자열을 map으로 변환하는 코드
				//네이버에서 받아온 정보가 [이름: 김성택, 번호:010~~~] 와 같은 형태로 전송해주기 때문에
				//우리가 쓸 수 있도록 map으로 바꿔줌

				access_token = map.get("access_token"); //[access_token:~~~] 와 같은 형태인 정보를 넣고, map으로 변환시킴
				//map안에 키값이 access_token인 것의 값이 저장됨
				//토큰 얻기 완료 !
				
				mv.addObject("access_token", access_token); //모델앤뷰에 담기
				
				//이 메소드는 리다이렉트 시키기 때문에 /loginNaver/result로 토큰이 전송됨
			}
		} catch (Exception e) {
			System.out.println(e);
		}

		return mv;
	}

	@SuppressWarnings({ "unchecked" })
	@RequestMapping(value = "/loginNaver/result")
	public ModelAndView loginNaverResult(CommandMap commandMap, HttpServletRequest request) throws Exception {
		log.debug("###### 네이버 로그인2 ######");
		//해당 메소드에서는 위의 메소드에서 받은 토큰을 통해, 우리가 필요한 회원정보를 얻어올 것
		
		ModelAndView mv = null;

		String access_token = (String) commandMap.get("access_token"); //위에서 얻은 토큰
		String apiURL = "https://openapi.naver.com/v1/nid/me";
		//위의 메소드에서는 url 뒤에 물음표로 연결해서 url 요청하였지만
		//여기서는 파라미터 없이 주소만 작성하였음 -> 추가로 요청을 하지 않았기 때문. ??
		
		//api 요청 시 주의 점
		//1. 주소를 정확하게 요청하기
		//2. 파라미터, 헤더
		
 
		try {
			URL url = new URL(apiURL);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("Authorization", "Bearer " + access_token); //헤더 설정하는 메소드
			//이 요청을 할 때 헤더를 설정하겠다는 뜻. ?
			//Authorization이라는 헤더에 "Bearer " + access_token를 값으로 주겠다는 뜻 (키, 값 형태)

			int responseCode = con.getResponseCode();
			BufferedReader br;
			System.out.println("responseCode=" + responseCode); //받은 응답코드에 맞게 오류를 확인할 수 있는 코드
			if (responseCode == 200) { // 정상 호출
				br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			} else { // 에러 발생
				br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
			}
			
			//api가 준 정보를 읽어오는 과정
			String inputLine;
			StringBuffer res = new StringBuffer();
			while ((inputLine = br.readLine()) != null) {
				res.append(inputLine); //받아온 정보를 하나의 문자열에 저장
			}
			br.close();
			
			//여기부터는 받아온 정보를 가지고 로그인을 처리하는 코드
			if (responseCode == 200) {
				//gson을 써도 되지만 JsonParser 사용하였음
				//이유는, 받은 정보가 json형태 안에 또 json형태가 담겨있는 형태로 옴
				//response라는 것 안에 또 json이 담겨있는데 그 안에 우리가 필요한 회원의 개인정보가 담겨있음
				//현재 json형태의 문자열인 상태인데, json객체로 변환하여 :와 "를 통해 정보를 구분함
				
				JsonParser jp = new JsonParser();
				JsonObject jo = (JsonObject) jp.parse(res.toString()); //json문자열을 객체로 바꿔줌
				//resultcode, message, reponse라는 3개의 키와 그에 대한 값이 들어있는 상태임
				JsonObject jo2 = (JsonObject) jo.get("response"); //그 세개의 키 중 키값이 response인 것의 값을 꺼내오기 위해 다시 또 json형태의 객체를 따로 생성함

				
				//즉, 토큰을 통해 회원정보를 받아왔을 때, 네이버가 json형태로 결과정보를 주는데,
				//우리에게 필요한 정보는 json 안에 json으로 들어있었음
				//따라서 필요한 정보만 꺼내기 위해서 전체 json을 jsonParser를 통해 객체로 생성하고
				//그 json객체에서 response에 담겨있는 정보를 꺼내기 위해서 get("response")로 response만으로 json객체를 또 생성
				//그리고 그 객체를 맵으로 변환하면 우리가 사용할 수 있는 상태가 됨
				
				System.out.println("########## result + " + jo2);

				//그런데 현재 핸드폰번호, 생년월일의 포맷, 성별표현 등이 우리와 다르기 때문에 변환해줘야 함
				//처음 로그인할 때 최초로그인인지, 기존에 로그인 했었는지 구분을 해주어야 함. 왜냐하면 연동로그인은 회원가입이 아니기 때문에 
				Map<String, Object> resultMap = new HashMap<>(); 
				Gson gson = new Gson();
				resultMap = gson.fromJson(jo2, resultMap.getClass()); //gson의 fromJson메소드를 통해, 위에 생성한 json객체를 map으로 변환

				//resultMap에서 정보 하나씩 꺼내기
				String uniqueId = (String) resultMap.get("id"); //네이버에서 주는 회원의 고유ID
				resultMap.put("MEM_EMAIL", uniqueId);
				String phone = (String) resultMap.get("mobile");
				resultMap.put("MEM_PHONE1", phone.substring(0, 3));
				resultMap.put("MEM_PHONE2", phone.substring(4, 8));
				resultMap.put("MEM_PHONE3", phone.substring(9));
				resultMap.put("MEM_PHONE", phone.substring(0, 3) + phone.substring(4, 8) + phone.substring(9));
				// 받은 정보에서 핸드폰번호를 -가 제거된 하나의 문자열로 만듦
				resultMap.put("MEM_NAME", resultMap.remove("name"));
				//remove.("name")은 name이란 값을 지우면서 그 값을 리턴해줌. 그럼 받아온 map에서 name이란 것이 지워지고, MEM_NAME이란 키로 대체하는 결과를 만들 수 있음
				//name이란 키, MEM_NAME이란 키로 동일한 값이 있는 중복을 제거하기 위하여 이렇게 작성한 것

				resultMap.put("MEM_PW", uniqueId); //우리는 테이블의 비밀번호 컬럼에 회원의 고유ID를 동일하게 넣어주었음
				String gender = (String) resultMap.get("gender");
				if (gender.equals("M")) { //네이버로부터 성별을 M, F로 전송받음
					//회원가입 할 때 주민번호 뒷자리로 성별을 구분하여 가입처리하기 때문에 1, 2로 맞췄음
					resultMap.put("MEM_GEN", "1");
				} else {
					resultMap.put("MEM_GEN", "2");
				}

				StringBuffer sb = new StringBuffer();
				String birthyear = (String) resultMap.get("birthyear"); // 1994로 날아옴
				String birthday = (String) resultMap.get("birthday"); //12-06으로 날아옴
				sb.append(birthyear.substring(2)); //1994에서 94만 자름
				sb.append(birthday.substring(0, 2)); // 12-06에서 12만 자름
				sb.append(birthday.substring(3)); // 06만 자름
				resultMap.put("MEM_BIRTH", sb.toString()); //941206 완성

				Map<String, Object> emailMap = joinService.selectEmailCheck(resultMap);
				System.out.println("###################### emailMap : " + emailMap);
				if (emailMap == null) {

					System.out.println("########## resultMap + " + resultMap);

					mv = new ModelAndView("naverJoinForm"); //네이버 전용 로그인창으로 넘김
					//네이버에서 주는 정보가 한정적이라,,,
					mv.addObject("member", resultMap);
					return mv;
				} else {
					mv = new ModelAndView("redirect:/main"); //네이버로 가입했던 회원이면 main으로 리다이렉트

					int check = joinService.selectDelGB(resultMap); //회원탈퇴이력 조회

					if (check == 1) {
						// DB에 회원가입 처리 전, 회원 탈퇴한 이력이 있고 7일 지났는지 여부 확인
						int delCount = joinService.selectDelCount(resultMap);
						System.out.println("delCount : " + delCount);

						if (delCount == 0) { // 회원 탈퇴 후 7일이 지나지 않았을 경우
							mv.addObject("loginFail", "Y");
							return mv;
						} else {
							joinService.updateDelGB(resultMap);
						}
					}

					Map<String, Object> member = loginService.selectId(resultMap);

					//세션 영역에 올려 로그인 처리
					//연동 로그인일 경우 마이페이지에 들어갈 때와 탈퇴할 때 비밀번호 입력 안 시킴 ?
					HttpSession session = request.getSession();
					session.setAttribute("session_MEM_ID", emailMap.get("MEM_EMAIL")); // 지워도 문제 없을 듯
					session.setAttribute("session_MEM_INFO", member);
					session.setAttribute("session_MEM_NAVER", "Y");

					return mv;
				}

			}
		} catch (Exception e) {
			System.out.println(e);
		}

		return mv;
	}
}
