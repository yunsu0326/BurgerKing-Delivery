package kjy.myshop.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.controller.AbstractController;
import pys.member.domain.MemberVO;

public class ProductPurchaseAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// 원포트(구:아임포트) 결제창을 사용하기 위한 전제조건은 먼저 로그인을 해야하는 것이다.
		if(super.checkLogin(request)) {
			// 로그인을 했으면
			
			String userid = request.getParameter("userid");
			
			HttpSession session = request.getSession();
			MemberVO loginuser = (MemberVO) session.getAttribute("loginuser");
			
			if(loginuser.getUserid().equals(userid)) {
				// 로그인한 사용자가 자신의 코인을 수정하는 경우
				
				
				String productName = "제품구매"; // 제품명, "새우깡";
				int productPrice = 100;
				
				request.setAttribute("productName", productName);
				request.setAttribute("productPrice", productPrice);
				request.setAttribute("email", loginuser.getEmail());
				request.setAttribute("name", loginuser.getName());
				request.setAttribute("mobile", loginuser.getMobile());
				
				request.setAttribute("userid", userid);
				
			//	super.setRedirect(false);
				super.setViewPage("/WEB-INF/kjy.member/paymentGateway.jsp");
			}
			else {
				// 로그인한 사용자가 다른 사용자의 정보로 결제를 시도하는 경우
				String message = "다른 사용자의 제품 결제 시도는 불가합니다!!";
				String loc = "javascript:history.back()";
				
				request.setAttribute("message", message);
				request.setAttribute("loc", loc);
				
				super.setRedirect(false);
				super.setViewPage("/WEB-INF/msg.jsp");
			}
			
		}
		
		else {
			// 로그인을 안했으면
			String message = "코인충전 결제를 하기 위해서는 먼저 로그인을 하세요!!";
			String loc = "javascript:history.back()";
			
			request.setAttribute("message", message);
			request.setAttribute("loc", loc);
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
		}
			
		
	}

}
