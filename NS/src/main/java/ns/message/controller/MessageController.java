package ns.message.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import ns.common.common.CommandMap;
import ns.message.service.MessageService;

@Controller
public class MessageController {

	Logger log = Logger.getLogger(this.getClass());

	@Resource(name = "messageService")
	private MessageService messageService;

	// 메세지 목록
	@RequestMapping(value = "/message/list")
	public String message_list(HttpServletRequest request, HttpSession session, CommandMap commandMap)
			throws Exception {

		// 세션영역에서 회원정보 가져오기
		@SuppressWarnings("unchecked")
		Map<String, Object> member = (Map<String, Object>) session.getAttribute("session_MEM_INFO");
		// 현재 사용자의 회원번호
		int mem_num = Integer.parseInt(member.get("MEM_NUM").toString());

		// 쿼리로 전달할 맵에 회원번호 넣어주기
		commandMap.put("MEM_NUM", mem_num);

		// 메세지 리스트 가져오기
		List<Map<String, Object>> list = messageService.messageList(commandMap.getMap());
		List<Map<String, Object>> unreadList = new ArrayList<>();
		List<Map<String, Object>> nicknameList = new ArrayList<>();

		int send_num = 0;
		int recv_num = 0;

		for (int i = 0; i < list.size(); i++) {
			int chatRoom = Integer.parseInt(list.get(i).get("CHAT_ROOM").toString());
			System.out.println("chatRoom : " + chatRoom);
			commandMap.put("CHAT_ROOM", chatRoom);

			// 반복문에서 상대방 닉네임을 위해 저장했던 mem_num이 계속 유지되며 반복하기 때문에
			// 현재사용자의 회원번호 새로 넣어주기를 반복함
			commandMap.put("MEM_NUM", mem_num);

			// 안 읽은 메세지 개수 가져오기 (room번호, 받는사람 번호(내 번호) 필요)
			Map<String, Object> unreadMap = (Map<String, Object>) messageService.countUnread(commandMap.getMap());

			unreadList.add(unreadMap);

			// db에서 가져온 list에서 받는사람, 보낸사람 회원번호 꺼내기
			send_num = Integer.parseInt(list.get(i).get("CHAT_SEND_NUM").toString());
			recv_num = Integer.parseInt(list.get(i).get("CHAT_RECV_NUM").toString());

			if (send_num == mem_num) { // 현재 사용자가 보낸사람이라면
				commandMap.put("MEM_NUM", recv_num); // 받은사람의 회원번호를 commandMap에 저장
			} else if (recv_num == mem_num) { // 현재 사용자가 받은사람이라면
				commandMap.put("MEM_NUM", send_num); // 보낸사람의 회원번호를 commandMap에 저장
			}

			// 상대방 닉네임, DEL_GB여부, 정지여부 가져오기
			Map<String, Object> nickname = (Map<String, Object>) messageService.getOtherNickname(commandMap.getMap());

			nicknameList.add(nickname);

		}

		// 안 읽은 개수를 리스트에 추가
		request.setAttribute("unreadList", unreadList);
		// 닉네임을 리스트에 추가
		request.setAttribute("nicknameList", nicknameList);
		// 리스트를 request 영역에 저장
		request.setAttribute("list", list);

		request.setAttribute("MEM_INFO", member);

		return "messageList";

	}

	// 메세지 목록
	@RequestMapping(value = "/message/ajaxList")
	public ModelAndView message_ajax_list(HttpServletRequest request, HttpSession session, CommandMap commandMap)
			throws Exception {
		System.out.println("########################### message_ajax_list ################");

		ModelAndView mv = new ModelAndView("messageAjaxList");

		// 세션영역에서 회원정보 가져오기
		Map<String, Object> member = (Map<String, Object>) session.getAttribute("session_MEM_INFO");
		// 현재 사용자의 회원번호
		int mem_num = Integer.parseInt(member.get("MEM_NUM").toString());

		// 쿼리로 전달할 맵에 회원번호 넣어주기
		commandMap.put("MEM_NUM", mem_num);

		String keyword = "";
		if (request.getParameter("keyword") != null || request.getParameter("keyword") != "") {
			keyword = (String) request.getParameter("keyword");
		}

		commandMap.put("keyword", keyword);

		// 메세지 리스트 가져오기
		List<Map<String, Object>> list = messageService.messageList(commandMap.getMap());
		List<Map<String, Object>> unreadList = new ArrayList<>();
		List<Map<String, Object>> nicknameList = new ArrayList<>();

		int send_num = 0;
		int recv_num = 0;

		for (int i = 0; i < list.size(); i++) {
			int chatRoom = Integer.parseInt(list.get(i).get("CHAT_ROOM").toString());
			System.out.println("chatRoom : " + chatRoom);
			commandMap.put("CHAT_ROOM", chatRoom);

			// 반복문에서 상대방 닉네임을 위해 저장했던 mem_num이 계속 유지되며 반복하기 때문에
			// 현재사용자의 회원번호 새로 넣어주기를 반복함
			commandMap.put("MEM_NUM", mem_num);

			// 안 읽은 메세지 개수 가져오기 (room번호, 받는사람 번호(내 번호) 필요)
			Map<String, Object> unreadMap = (Map<String, Object>) messageService.countUnread(commandMap.getMap());

			unreadList.add(unreadMap);

			// db에서 가져온 list에서 받는사람, 보낸사람 회원번호 꺼내기
			send_num = Integer.parseInt(list.get(i).get("CHAT_SEND_NUM").toString());
			recv_num = Integer.parseInt(list.get(i).get("CHAT_RECV_NUM").toString());

			if (send_num == mem_num) { // 현재 사용자가 보낸사람이라면
				commandMap.put("MEM_NUM", recv_num); // 받은사람의 회원번호를 commandMap에 저장
			} else if (recv_num == mem_num) { // 현재 사용자가 받은사람이라면
				commandMap.put("MEM_NUM", send_num); // 보낸사람의 회원번호를 commandMap에 저장
			}

			// 상대방 닉네임, 탈퇴여부 가져오기
			Map<String, Object> nickname = (Map<String, Object>) messageService.getOtherNickname(commandMap.getMap());

			nicknameList.add(nickname);

		}

		mv.addObject("unreadList", unreadList);

		mv.addObject("nicknameList", nicknameList);

		mv.addObject("list", list);

		return mv;

	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/message/contentList")
	public ModelAndView messageContentList(CommandMap commandMap, HttpServletRequest request, HttpSession session)
			throws Exception {
		System.out.println("########################### messageContentList ################");
		ModelAndView mv = new ModelAndView("messageContentList");

		int chat_room = Integer.parseInt(commandMap.get("room").toString());

		commandMap.put("CHAT_ROOM", chat_room);

		// 세션영역에서 회원정보 가져오기
		Map<String, Object> member = (Map<String, Object>) session.getAttribute("session_MEM_INFO");

		// 세션 회원정보로부터 현재 사용자의 회원번호 꺼내기
		int mem_num = Integer.parseInt(member.get("MEM_NUM").toString());

		// 회원번호를 commandMap에 넣기 (쿼리실행을 위해)
		commandMap.put("MEM_NUM", mem_num);

		// 쿼리 실행 : 메세지 내용을 가져온다.
		List<Map<String, Object>> list = messageService.roomContentList(commandMap.getMap());

		// 회원번호, 회원 정지여부, 탈퇴여부를 map에 넣기 (list에 넣기 위해. jsp에서 사용해야 함)
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("MEM_NUM", commandMap.get("MEM_NUM").toString());
		map.put("MEM_STATUS", commandMap.get("MEM_STATUS"));
		map.put("MEM_DEL_GB", commandMap.get("MEM_DEL_GB"));
		// ajax 함수로부터 전송됨
		// 정지/탈퇴여부에 따라 사진 클릭 여부 결정하기 위해

		// 메세지 읽음처리
		messageService.messageReadChk(commandMap.getMap());

		mv.addObject("list", list);
		mv.addObject("map", map);

		return mv;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/message/messageSendInlist")
	public ModelAndView messageSendInlist(CommandMap commandMap, HttpServletRequest request) throws Exception {
		System.out.println("########################### messageSendInlist ################");
		ModelAndView mv = new ModelAndView("messageContentList");

		// 세션으로 사용자 회원번호 가져와서 SQL에 전달
		HttpSession session = request.getSession();
		Map<String, Object> map = (Map<String, Object>) session.getAttribute("session_MEM_INFO");
		int memNum = Integer.parseInt(map.get("MEM_NUM").toString());
		commandMap.put("MEM_NUM", memNum);

		int send_num = Integer.parseInt(commandMap.get("CHAT_SEND_NUM").toString());
		System.out.println("send_num:" + send_num);
		int recv_num = Integer.parseInt(commandMap.get("CHAT_RECV_NUM").toString());
		System.out.println("recv_num:" + recv_num);

		if (send_num == memNum) { // 현재 사용자가 보낸사람이라면
			commandMap.put("CHAT_RECV_NUM", recv_num); // 받은사람의 회원번호를 commandMap에 저장
		} else if (recv_num == memNum) { // 현재 사용자가 받은사람이라면
			commandMap.put("CHAT_RECV_NUM", send_num); // 보낸사람의 회원번호를 commandMap에 저장
		}

		// 방 번호를 구해서 SQL에 CHAT_ROOM 전달
		int exist_chat = messageService.existChat(commandMap.getMap());
		// 프로필에서 보낸 것 중 메세지 내역이 없어서 첫 메세지가 될 경우를 구분하기 위함
		int maxRoom = 0;
		int chatRoom = 0;
		// 메세지 내역이 없어서 0이면 message 테이블의 room 최댓값을 구해서 commandMap에 넣음
		if (exist_chat == 0) {
			maxRoom = messageService.maxRoom(commandMap.getMap());
			if (maxRoom < 1) {
				chatRoom = 1;
			} else {
				chatRoom = maxRoom + 1;
			}
		} else { // 메세지 내역이 있다면
			chatRoom = messageService.selectRoom(commandMap.getMap());
			// 해당 room 번호를 가져온다.
		}
		commandMap.put("CHAT_ROOM", chatRoom);
		messageService.messageSendInlist(commandMap.getMap());

		if (commandMap.get("viewName") != null) {
			mv = new ModelAndView("redirect:/message/list");
		} else {
			mv = new ModelAndView("messageContentList");
		}

		return mv;
	}
}