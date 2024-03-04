package pys.member.controller;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import common.controller.AbstractController;
import pys.member.model.MemberDAO;
import pys.member.model.MemberDAO_imple;

public class EmailDuplicateCheck2Action extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String method = request.getMethod(); // "GET" 또는 "POST" 
		
		if("POST".equalsIgnoreCase(method)) {
		
			String email = request.getParameter("email");
			String userid = request.getParameter("userid");
			
		//	System.out.println(">>> 확인용 email => " + email);
		//	>>> 확인용 email => ujkl021@gmail.com
			
		//	System.out.println(">>> 확인용 userid => " + userid);
		//	>>> 확인용 userid => ujkl021
			
			MemberDAO mdao = new MemberDAO_imple();
			
			Map<String,String> paraMap = new HashMap<>();
			paraMap.put("email", email);
			paraMap.put("userid", userid);
			
			boolean isExists = mdao.emailDuplicateCheck2(paraMap);
			
			JSONObject jsonObj = new JSONObject(); // {}
			jsonObj.put("isExists", isExists);     // {"isExists":true} 또는 {"isExists":false} 으로 만들어준다. 
			
			String json = jsonObj.toString(); // 문자열 형태인 "{"isExists":true}" 또는 "{"isExists":false}" 으로 만들어준다. 
		//	System.out.println(">>> 확인용 json => " + json);
		    // >>> 확인용 json => {"isExists":true}
			// >>> 확인용 json => {"isExists":false} 
			
			request.setAttribute("json", json);
			
		//	super.setRedirect(false); 
			super.setViewPage("/WEB-INF/jsonview.jsp");
		
		}
		
	}

}
