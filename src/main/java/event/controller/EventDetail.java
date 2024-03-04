package event.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;
import event.domain.*;
import event.model.*;

public class EventDetail extends AbstractController {

   @Override
   public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
      
	   String eventno = request.getParameter("eventno");// 제품번호
      
      EventDAO pdao = new EventDAO_imple();
      
      // 제품번호를 가지고서 해당 제품의 정보를 조회해오기
      EventVO pvo = pdao.selectOneProductByPnum(eventno);
      
      
      if(pvo == null) {
         // GET 방식이므로 사용자가 웹브라우저 주소창에서 장난쳐서 존재하지 않는 제품번호를 입력한 경우
           String message = "검색하신 제품은 존재하지 않습니다.";
           String loc = "javascript:history.back()";
           
           request.setAttribute("message", message);
           request.setAttribute("loc", loc);
            
       //  super.setRedirect(false);
           super.setViewPage("/WEB-INF/msg.jsp");
            
           return;
      }
      else {
         // 제품이 있는 경우
         
         request.setAttribute("pvo", pvo);         // 제품의 정보
         
       //  super.setRedirect(false);
           super.setViewPage("/WEB-INF/event/eventDetail.jsp");
         
      }
      
   }

}