package kjy.myshop.controller;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.controller.AbstractController;
import pys.member.domain.MemberVO;
import kjy.myshop.model.*;

public class DeliverEndAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String method = request.getMethod();
	      
		if(!"POST".equalsIgnoreCase(method)) {
			String message = "비정상적인 경로로 들어왔습니다.";
			String loc = "javascript:history.back();";
	         
			request.setAttribute("message", message);
			request.setAttribute("loc", loc);
	         
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
	         
			return; // 종료 
		}
		
		// === 로그인 유무 검사하기 === //
		if(!super.checkLogin(request)) {
			request.setAttribute("message", "배송완료를 하시려면 먼저 로그인 부터 하세요!!");
			request.setAttribute("loc", "javascript:history.back()"); 
	         
		//	super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
			return;  // 종료
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
	            
				return;  // 종료
	         }
			
			else {
				// admin(관리자)으로 로그인 한 경우 
				String[] odrcodeArr = request.getParameterValues("odrcode");
				String[] pnumArr = request.getParameterValues("pnum"); 
	             
	            
				StringBuilder sb = new StringBuilder();
				// 's20231103-4/60','s20231103-4/5','s20231103-5/3'
				// 's20231103-4은 주문코드(전표)'이고 /뒤에 붙은 60 은 제품번호이다.
				// 's20231103-4은 주문코드(전표)'이고 /뒤에 붙은 5 는 제품번호이다.
				// 's20231103-5은 주문코드(전표)'이고 /뒤에 붙은 3 은 제품번호이다.
				// 이것은 오라클에서 주문코드(전표)컬럼||'/'||제품번호 로 하겠다는 말이다.
				
				for(int i=0; i<odrcodeArr.length; i++) {
					sb.append("\'"+odrcodeArr[i]+"/"+pnumArr[i]+"\',");
					// sql 문의 where 절에  fk_odrcode || '/' || fk_pnum in('전표/제품번호','전표/제품번호','전표/제품번호') 을 사용하기 위한 것이다. 
				}
				
				String odrcodePnum = sb.toString();
				
				// 맨뒤의 콤마(,)제거하기 
				odrcodePnum = odrcodePnum.substring(0, odrcodePnum.length()-1);
				
			//	System.out.println("~~~ 확인용 odrcodePnum => " + odrcodePnum);
				// ~~~ 확인용 odrcodePnum => 'jy20231103-9/47','jy20231103-12/5','jy20231103-11/5'
				
				// tbl_orderdetail 테이블의 deliverstatus(배송상태) 컬럼의 값을 3(배송완료)로 변경하기
				ProductDAO pdao = new ProductDAO_imple(); 
				
				int n = 0;
				
				try {
				    n = pdao.updateDeliverEnd(odrcodePnum);
				} catch(SQLException e) {
				   e.printStackTrace();
				}
				
				if(n == odrcodeArr.length) {
					
					String message = "선택하신 제품들은 배송완료로 변경되었습니다.";
					String loc = request.getContextPath()+"/shop/orderList.bk";
	               
					request.setAttribute("message", message);
					request.setAttribute("loc", loc);
	               
					super.setRedirect(false);
					super.setViewPage("/WEB-INF/msg.jsp");
	            }
	            
				else {
	            	String message = "선택하신 제품들은 배송완료로 변경이 실패되었습니다.";
	            	String loc = "javascript:history.back();";
	               
	            	request.setAttribute("message", message);
	            	request.setAttribute("loc", loc);
	               
	            	super.setRedirect(false);
	            	super.setViewPage("/WEB-INF/msg.jsp");
				}
				
			}
			
		}
		
	}

}
