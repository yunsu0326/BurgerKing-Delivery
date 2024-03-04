package event.model;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import event.domain.EventVO;

public interface EventDAO {

	
	
	List<EventVO> selectEventlist(Map<String, String> paraMap) throws Exception;
	
	//이벤트 이미지 총 개수
	int totalPspecCount() throws Exception;

	List<EventVO> eventimgPresent() throws Exception;

	List<EventVO> eventdetail() throws Exception;


	EventVO selectOneProductByPnum(String eventno) throws SQLException; 

	





}
