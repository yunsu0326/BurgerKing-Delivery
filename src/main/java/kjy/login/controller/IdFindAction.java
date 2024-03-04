package kjy.login.controller;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;
import kjy.member.model.*;

public class IdFindAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String method = request.getMethod(); // "GET" 또는 "POST"
		
		if("POST".equalsIgnoreCase(method)) { // post 방식이라면
			// 아이디 찾기 모달창에서 "찾기" 버튼을 클릭했을 경우
			String name =  request.getParameter("name");
			String email = request.getParameter("email");
			
			MemberDAO mdao = new MemberDAO_imple();
			
			Map<String, String> paraMap = new HashMap<>();
			paraMap.put("name", name);
			paraMap.put("email", email);
			
			String userid = mdao.findUserid(paraMap);
			
			if(userid != null) {
				request.setAttribute("userid", userid);
			}
			else {
				request.setAttribute("userid", "존재하지 않습니다.");
			}
			
			request.setAttribute("name", name);
			request.setAttribute("email", email);
			
		}// end of if("POST".equalsIgnoreCase(method)) {}----------
		
		request.setAttribute("method", method);
		
		super.setRedirect(false); // get 방식이든지 post 방식이든지 둘 다
		super.setViewPage("/WEB-INF/kjy.login/idFind.jsp");
		
	}

}
