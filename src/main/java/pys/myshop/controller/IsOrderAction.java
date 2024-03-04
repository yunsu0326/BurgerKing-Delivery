package pys.myshop.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import common.controller.AbstractController;
import pys.myshop.model.ProductDAO;
import pys.myshop.model.ProductDAO_imple;

public class IsOrderAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		  String fk_pnum = request.getParameter("fk_pnum");
	      String fk_userid = request.getParameter("fk_userid");
	      
	      System.out.println(fk_pnum);
	      System.out.println(fk_userid);
	      
	      Map<String, String> paraMap = new HashMap<>();
	      
	      paraMap.put("fk_pnum", fk_pnum);
	      paraMap.put("fk_userid", fk_userid);
	      
	      ProductDAO pdao = new ProductDAO_imple();
	      
	      boolean bool = pdao.isOrder(paraMap);
	      
	      JSONObject jsonObj = new JSONObject();
	      jsonObj.put("isOrder", bool);
	      
	      String json = jsonObj.toString();
	      request.setAttribute("json", json);
	      
	   //   super.setRedirect(false);
	      super.setViewPage("/WEB-INF/jsonview.jsp");
		
	}

}
