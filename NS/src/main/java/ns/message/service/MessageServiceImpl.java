package ns.message.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import ns.common.dao.InformDAO;
import ns.message.dao.MessageDAO;

@Service("messageService")
public class MessageServiceImpl implements MessageService {

	Logger log = Logger.getLogger(this.getClass());

	@Resource(name = "messageDAO")
	private MessageDAO messageDAO;

	@Resource(name = "informDAO")
	private InformDAO informDAO;

	@Override
	public List<Map<String, Object>> messageList(Map<String, Object> map) throws Exception {
		return messageDAO.messageList(map);
	}

	@Override
	public Map<String, Object> getOtherNickname(Map<String, Object> map) throws Exception {
		return messageDAO.getOtherNickname(map);
	}

	@Override
	public Map<String, Object> countUnread(Map<String, Object> map) throws Exception {
		return messageDAO.countUnread(map);
	}

	@Override
	public List<Map<String, Object>> roomContentList(Map<String, Object> map) throws Exception {
		return messageDAO.roomContentList(map);
	}

	@Override
	public void messageReadChk(Map<String, Object> map) throws Exception {
		messageDAO.messageReadChk(map);
	}

	@Override
	public void messageSendInlist(Map<String, Object> map) throws Exception {
		messageDAO.messageSendInlist(map);
		map.put("INFORM_TYPE", 5);
		map.put("MEM_NUM", map.get("CHAT_RECV_NUM"));
		map.put("INFORM_PRONUM", map.get("CHAT_ROOM"));

		Map<String, Object> unreadMessage = messageDAO.countUnread(map);
		int message = Integer.parseInt(unreadMessage.get("UNREAD").toString());

		// 한사람이 보낸 message의 개수가 1개일 경우에만 알림 가도록 설정 (한 사람이 메세지 2개 보냈을 때도, 알림은 여전히 1개로 표시)
		if (message < 2) {
			informDAO.informInsert(map, "새로운 메시지가 도착했습니다");
		}
	}

	@Override
	public int maxRoom(Map<String, Object> map) throws Exception {
		return messageDAO.maxRoom(map);
	}

	@Override
	public int existChat(Map<String, Object> map) throws Exception {
		return messageDAO.existChat(map);
	}

	@Override
	public int selectRoom(Map<String, Object> map) throws Exception {
		return messageDAO.selectRoom(map);
	}

}