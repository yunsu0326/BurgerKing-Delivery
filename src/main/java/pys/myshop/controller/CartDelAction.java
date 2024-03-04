package pys.myshop.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import common.controller.AbstractController;
import pys.myshop.model.ProductDAO;
import pys.myshop.model.ProductDAO_imple;

public class CartDelAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String method = request.getMethod();
	      
	      if(!"POST".equalsIgnoreCase(method)) {
	         // GET 방식이라면
	         
	         String message = "비정상적인 경로로 들어왔습니다";
	         String loc = "javascript:history.back()";
	         
	         request.setAttribute("message", message);
	         request.setAttribute("loc", loc);
	         
	         super.setViewPage("/WEB-INF/msg.jsp");
	         return;
	      }
	      else if("POST".equalsIgnoreCase(method) && super.checkLogin(request)) {
	    	  //post 방식이고 로그인을 했다라면
	    	  
	    	  String cartno = request.getParameter("cartno");
	    	  
	    	  ProductDAO pdao = new ProductDAO_imple();
	    	  
	    	  //장바구니 테이블에서 특정제품을 장바구니에서 비우기
	    	  int n = pdao.delCart(cartno);
	    	  
	    	  JSONObject jsobj = new JSONObject(); // {}
	    	  
	    	  jsobj.put("n", n); // {"n":1}
	    	  
	    	  String json = jsobj.toString(); // "{"n":1}"
	    	  
	    	  request.setAttribute("json", json);
	    	  
	    	  super.setRedirect(false);
	    	  super.setViewPage("/WEB-INF/jsonview.jsp");
	    	  
	    	  
	    	  
	      }
		
	}

}
