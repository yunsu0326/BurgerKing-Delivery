package event.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;
import event.model.*;

import event.domain.*;

public class EventlistjsonpresentAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		     EventDAO dao = new EventDAO_imple();
		     
		     List <EventVO> eventpresentlist = dao.eventimgPresent();
		     
		     request.setAttribute("eventpresentlist", eventpresentlist);
		     
		     super.setRedirect(false);
		     super.setViewPage("/WEB-INF/event/eventpresent.jsp");
		     
	}

}
