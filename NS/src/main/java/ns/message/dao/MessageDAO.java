package ns.message.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import ns.common.dao.AbstractDAO;

@Repository("messageDAO")
public class MessageDAO extends AbstractDAO {

	// 메세지 리스트 데이터 여러 줄 가져오기
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> messageList(Map<String, Object> map) throws Exception {
		System.out.println("messageList 쿼리에 전달된 파라미터 : " + map);

		return (List<Map<String, Object>>) selectList("message.message_list", map);

	}

	// 현재 사용자가 해당 room에서 안읽은 메세지의 개수를 가져온다.
	@SuppressWarnings("unchecked")
	public Map<String, Object> countUnread(Map<String, Object> map) throws Exception {
		System.out.println("count_unread 쿼리에 전달된 파라미터 : " + map);

		return (Map<String, Object>) selectOne("message.count_unread", map);
	}

	// 현재 사용자가 메세지를 주고받는 상대의 닉네임을 가져온다.
	@SuppressWarnings("unchecked")
	public Map<String, Object> getOtherNickname(Map<String, Object> map) throws Exception {
		System.out.println("get_other_nickname 쿼리에 전달된 파라미터 : " + map);

		return (Map<String, Object>) selectOne("message.get_other_nickname", map);
	}

	// room 별 메세지 내역을 가져온다.
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> roomContentList(Map<String, Object> map) throws Exception {

		System.out.println("room_content_list 쿼리에 전달된 파라미터 : " + map);

		return (List<Map<String, Object>>) selectList("message.room_content_list", map);

	}

	// 해당 방의 메세지들 중 받는 사람이 현재사용자인 메세지를 모두 읽음 처리한다
	public void messageReadChk(Map<String, Object> map) throws Exception {
		System.out.println("message_read_chk 쿼리에 전달된 파라미터 : " + map);

		update("message.message_read_chk", map);
	}

	// 메세지 list에서 메세지를 보낸다.
	public void messageSendInlist(Map<String, Object> map) throws Exception {
		System.out.println("messageSendInlist 쿼리에 전달된 파라미터 : " + map);

		insert("message.messageSendInlist", map);
	}

	public int maxRoom(Map<String, Object> map) throws Exception {
		System.out.println("max_room 쿼리에 전달된 파라미터 : " + map);

		if (selectOne("message.max_room", map) == null) {
			return 0;
		} else {
			return (int) selectOne("message.max_room", map);
		}

	}

	public int existChat(Map<String, Object> map) throws Exception {
		System.out.println("exist_chat 쿼리에 전달된 파라미터 : " + map);

		return (int) selectOne("message.exist_chat", map);

	}

	public int selectRoom(Map<String, Object> map) throws Exception {
		System.out.println("select_room 쿼리에 전달된 파라미터 : " + map);

		return (int) selectOne("message.select_room", map);
	}
}
