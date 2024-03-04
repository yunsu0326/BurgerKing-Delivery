package pys.myshop.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import common.controller.AbstractController;
import pys.myshop.model.ProductDAO;
import pys.myshop.model.ProductDAO_imple;

public class DeliveryCountAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String userid = request.getParameter("userid");
		
		ProductDAO pdao = new ProductDAO_imple();
		
		Map<String, String> orderCountMap = pdao.delivertCount(userid);
		
		 JSONObject jsonObj = new JSONObject();
	        
	        String pname = orderCountMap.get("pname");
	        String cnt = orderCountMap.get("cnt");
	        
	        // JSON 객체에 두 개의 값을 추가
	        jsonObj.put("pname", pname);
	        jsonObj.put("cnt", cnt);
	      
	        String json = jsonObj.toString(); // JSON 객체를 문자열로 변환
		   
		      
		      request.setAttribute("json", json);
		      
		      super.setRedirect(false);
		      super.setViewPage("/WEB-INF/jsonview.jsp");
		
	}

}
