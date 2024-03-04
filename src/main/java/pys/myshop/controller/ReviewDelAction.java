package pys.myshop.controller;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import common.controller.AbstractController;
import pys.myshop.model.ProductDAO;
import pys.myshop.model.ProductDAO_imple;

public class ReviewDelAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String method = request.getMethod();
	      
	      if("POST".equalsIgnoreCase(method)) {
	         // **** POST 방식으로 넘어온 것이라면 **** //
	         
	         String review_seq = request.getParameter("review_seq");
	         
	         ProductDAO pdao = new ProductDAO_imple();
	         
	         int n = 0;
	         try {
	        	 n = pdao.reviewDel(review_seq);
	         } catch(SQLException e) {
	        	 
	         }
	         
	         JSONObject jsobj = new JSONObject();  
	         jsobj.put("n", n);
	         
	         String json = jsobj.toString();  // 문자열 형태로 변환해줌.
	         
	         request.setAttribute("json", json);
	         
	      //   super.setRedirect(false);
	         super.setViewPage("/WEB-INF/jsonview.jsp");
	         
	      }
	      else {
	         // **** POST 방식으로 넘어온 것이 아니라면 **** //
	         
	         String message = "비정상적인 경로를 통해 들어왔습니다.!!";
	         String loc = "javascript:history.back()";
	         
	         request.setAttribute("message", message);
	         request.setAttribute("loc", loc);
	         
	         super.setViewPage("/WEB-INF/msg.jsp");
	      }
		
	}

}
