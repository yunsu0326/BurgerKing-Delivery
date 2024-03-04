package event.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;
import event.model.*;

public class Eventmain extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// === Ajax(JSON)를 사용하여 HIT 상품목록 "더보기" 방식으로 페이징 처리해서 보여주겠다. ===//
		EventDAO edao = new EventDAO_imple();
		
		int totalHITCount = edao.totalPspecCount();     /// HIT 상품의 전체개수를 알아온다.
		
	//	System.out.println("~~~~~ 확인용 totalHITCount: " + totalHITCount);
		
		request.setAttribute("totalHITCount",totalHITCount);
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/event/eventmain.jsp");
		
	}

}
