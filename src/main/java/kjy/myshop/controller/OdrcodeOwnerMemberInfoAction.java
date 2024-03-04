package kjy.myshop.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.controller.AbstractController;
import pys.member.domain.MemberVO;
import kjy.myshop.model.*;

public class OdrcodeOwnerMemberInfoAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// === 로그인 유무 검사하기 === //
		if(!super.checkLogin(request)) {
			request.setAttribute("message", "주문내역을 조회하려면 먼저 로그인 부터 하세요!!");
			request.setAttribute("loc", "javascript:history.back()"); 
	       
		//	super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
			return;
		}
		else {
			HttpSession session = request.getSession();
			
			MemberVO loginuser = (MemberVO)session.getAttribute("loginuser");
			String userid = loginuser.getUserid();
			
			if(!"admin".equals(userid) ) {
				String message = "접근불가!! 관리자가 아닙니다.";
				String loc = "javascript:history.back()";
			   
				request.setAttribute("message", message);
				request.setAttribute("loc", loc);
				
				super.setRedirect(false);
				super.setViewPage("/WEB-INF/msg.jsp");
				
				return;
			}
	         
			else {
				// "admin" 으로 로그인 한 경우(관리자)이라면
	            
				String odrcode = request.getParameter("odrcode");
	            
				ProductDAO pdao = new ProductDAO_imple();
	            
				MemberVO mvo = pdao.odrcodeOwnerMemberInfo(odrcode);
				// 영수증전표(odrcode)소유주에 대한 사용자 정보를 조회해오는 것.
	            
				request.setAttribute("mvo", mvo);   
	            
				super.setRedirect(false);
				super.setViewPage("/WEB-INF/kjy.shop/admin/odrcodeOwnerMemberInfo.jsp");   
	                  
	         }
	      }
		
	}

}
