package kjy.myshop.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.controller.AbstractController;
import pys.member.domain.MemberVO;
import kjy.myshop.model.*;

public class OrderListAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// === 로그인 유무 검사하기 === //
		if(!super.checkLogin(request)) {
			// 로그인하지 않은 경우라면
			
			request.setAttribute("message", "주문내역을 조회하려면 먼저 로그인 부터 하세요!!");
			request.setAttribute("loc", "javascript:history.back()"); 
	         
		//	super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
			return; // 종료
		}
		
		else {// 로그인한 경우라면
			
			// *** 페이징 처리한 주문 목록 보여주기 *** //
			
			String currentShowPageNo = request.getParameter("currentShowPageNo");
			// currentShowPageNo 은 사용자가 보고자하는 페이지바의 페이지번호 이다.
			// 카테고리 메뉴에서 카테고리명만을 클릭했을 경우에는 currentShowPageNo 은 null 이 된다.
			// currentShowPageNo 이 null 이라면 currentShowPageNo 을 1 페이지로 바꾸어야 한다.
			
			if(currentShowPageNo == null) {
				currentShowPageNo = "1";
			}
			
			// === 페이징 처리를 위해 총 페이지 수 알아오기 === //
			ProductDAO pdao = new ProductDAO_imple();
			
			// 1. 일반 사용자로 로그인한 경우는 자신이 주문한 내역만 조회를 해오고,
			// 2. 관리자(admin)로 로그인한 경우 모든 사용자들의 주문한 내역을 조회해온다.
			HttpSession session = request.getSession();
			MemberVO loginuser = (MemberVO) session.getAttribute("loginuser");
			
			int totalCountOrder = pdao.getTotalCountOrder(loginuser.getUserid());
		//	System.out.println("~~~~ 확인용 totalCountOrder => " + totalCountOrder);
		//	~~~~ 확인용 totalCountOrder => 20
		//	~~~~ 확인용 totalCountOrder => 17
			
			// === 한 페이지당 보여줄 주문내역의 개수는 10개로 한다.
			int sizePerPage = 10;
			
			// === 전체 페이지 개수 ===
			int totalPage = (int)Math.ceil((double) totalCountOrder/sizePerPage);
			//										20.0/10 ==> 2.0 ==> 2.0 ==> 2
			//										17.0/10 ==> 1.7 ==> 2.0 ==> 2
			
		//	System.out.println("~~~ 확인용 totalPage : " + totalPage);
			
			
			// === GET 방식이므로 사용자가 웹브라우저 주소창에서 currentShowPageNo 에 totalPage 값보다 더 큰 값을 입력하여 장난친 경우
			// === GET 방식이므로 사용자가 웹브라우저 주소창에서 currentShowPageNo 에 0 또는 음수를 입력하여 장난친 경우
			// === GET 방식이므로 사용자가 웹브라우저 주소창에서 currentShowPageNo 에 숫자가 아닌 문자열을 입력하여 장난친 경우
			// 아래처럼 막아주도록 하겠다.
			try {
				if( Integer.parseInt(currentShowPageNo) > totalPage || 
					Integer.parseInt(currentShowPageNo) <= 0) {
					currentShowPageNo = "1";
				}
			} catch (NumberFormatException e) {
				currentShowPageNo = "1";
			}
			
			
			// *** 관리자가 아닌 일반사용자로 로그인 했을 경우에는 자신이 주문한 내역만 페이징 처리하여 조회를 해오고,
			//     관리자로 로그인을 했을 경우에는 모든 사용자들의 주문내역을 페이징 처리하여 조회해온다.
			Map<String, String> paraMap = new HashMap<>();
			paraMap.put("userid", loginuser.getUserid());
			paraMap.put("currentShowPageNo", currentShowPageNo);
			
			List<Map<String, String>> order_map_List = pdao.getOrderList(paraMap);
		//	System.out.println("~~~ 확인용 order_map_List.size() => " + order_map_List.size());
			
			request.setAttribute("order_map_List", order_map_List);
			
			// *** ==== 페이지바 만들기 시작 ==== *** //
			/*
	            1개 블럭당 10개씩 잘라서 페이지 만든다.
	            1개 페이지당 10개행을 보여준다면 총 몇개 블럭이 나와야 할까? 
	                총 제품의 개수가 412명 이고, 1개 페이지당 보여줄 제품수가 10 이라면
	            412/10 = 41.2 ==> 42(totalPage)        
	                
	            1블럭               1 2 3 4 5 6 7 8 9 10 [다음]
	            2블럭   [이전] 11 12 13 14 15 16 17 18 19 20 [다음]
	            3블럭   [이전] 21 22 23 24 25 26 27 28 29 30 [다음]
	            4블럭   [이전] 31 32 33 34 35 36 37 38 39 40 [다음]
	            5블럭   [이전] 41 42 
			*/
	        
			// ==== !!! pageNo 구하는 공식 !!! ==== // 
	     /*
	         1  2  3  4  5  6  7  8  9  10  -- 첫번째 블럭의 페이지번호 시작값(pageNo)은  1 이다.
	         11 12 13 14 15 16 17 18 19 20  -- 두번째 블럭의 페이지번호 시작값(pageNo)은 11 이다.   
	         21 22 23 24 25 26 27 28 29 30  -- 세번째 블럭의 페이지번호 시작값(pageNo)은 21 이다.
	         
	          currentShowPageNo        pageNo  ==> ( (currentShowPageNo - 1)/blockSize ) * blockSize + 1 
	         ---------------------------------------------------------------------------------------------
	                1                   1 = ( (1 - 1)/10 ) * 10 + 1 
	                2                   1 = ( (2 - 1)/10 ) * 10 + 1 
	                3                   1 = ( (3 - 1)/10 ) * 10 + 1 
	                4                   1 = ( (4 - 1)/10 ) * 10 + 1  
	                5                   1 = ( (5 - 1)/10 ) * 10 + 1 
	                6                   1 = ( (6 - 1)/10 ) * 10 + 1 
	                7                   1 = ( (7 - 1)/10 ) * 10 + 1 
	                8                   1 = ( (8 - 1)/10 ) * 10 + 1 
	                9                   1 = ( (9 - 1)/10 ) * 10 + 1 
	               10                   1 = ( (10 - 1)/10 ) * 10 + 1 
	                
	               11                  11 = ( (11 - 1)/10 ) * 10 + 1 
	               12                  11 = ( (12 - 1)/10 ) * 10 + 1
	               13                  11 = ( (13 - 1)/10 ) * 10 + 1
	               14                  11 = ( (14 - 1)/10 ) * 10 + 1
	               15                  11 = ( (15 - 1)/10 ) * 10 + 1
	               16                  11 = ( (16 - 1)/10 ) * 10 + 1
	               17                  11 = ( (17 - 1)/10 ) * 10 + 1
	               18                  11 = ( (18 - 1)/10 ) * 10 + 1 
	               19                  11 = ( (19 - 1)/10 ) * 10 + 1
	               20                  11 = ( (20 - 1)/10 ) * 10 + 1
	                
	               21                  21 = ( (21 - 1)/10 ) * 10 + 1 
	               22                  21 = ( (22 - 1)/10 ) * 10 + 1
	               23                  21 = ( (23 - 1)/10 ) * 10 + 1
	               24                  21 = ( (24 - 1)/10 ) * 10 + 1
	               25                  21 = ( (25 - 1)/10 ) * 10 + 1
	               26                  21 = ( (26 - 1)/10 ) * 10 + 1
	               27                  21 = ( (27 - 1)/10 ) * 10 + 1
	               28                  21 = ( (28 - 1)/10 ) * 10 + 1 
	               29                  21 = ( (29 - 1)/10 ) * 10 + 1
	               30                  21 = ( (30 - 1)/10 ) * 10 + 1                    

	      */
			String pageBar = "";
			
			int blockSize = 10;
			// blockSize 는 블럭(토막)당 보여지는 페이지 번호의 개수이다.
			
			int loop = 1;
			// loop 는 1 부터 증가하여 1개 블럭을 이루는 페이지 번호의 개수(지금은 10개)까지만 증가하는 용도이다.
			
			// ==== !!! 다음은 pageNo 구하는 공식이다 !!! ==== //
			int pageNo = ( ( Integer.parseInt(currentShowPageNo) - 1)/blockSize ) * blockSize + 1;
			// pageNo 는 페이지바에서 보여지는 첫번째 번호이다.
			
			
			// **** [맨처음] [이전] 만들기 **** //
			//
			pageBar += "<li class='page-item'><a class='page-link' href='orderList.bk?currentShowPageNo=1'>[맨처음]</a></li>";
			
			if( pageNo != 1) {
				pageBar += "<li class='page-item'><a class='page-link' href='orderList.bk?currentShowPageNo="+(pageNo-1)+"'>[이전]</a></li>";
			}
			
			while( !(loop > blockSize || pageNo > totalPage) ) {
				
				if(pageNo == Integer.parseInt(currentShowPageNo)) {
					pageBar += "<li class='page-item active'><a class='page-link' href='#'>"+pageNo+"</a></li>";
				}
				else {
					pageBar += "<li class='page-item'><a class='page-link' href='orderList.bk?currentShowPageNo="+pageNo+"'>"+pageNo+"</a></li>";
				}
				
				loop ++;  // 1 2 3 4 5 6 7 8 9 10
				
				pageNo++; // 1 2 3 4 5 6 7 8 9 10
						  // 11 12 13 14 15 16 17 18 19 20 
						  // 21 22 23 24 25 26 27 28 29 30 
						  // 31 32 33 34 35 36 37 38 39 40
				  		  // 41 42
			}// end of while-----------------------------------------------
			
			// **** [다음] [마지막] 만들기 **** //
			// pageNo ==> 11
			if(pageNo <= totalPage) {
				pageBar += "<li class='page-item'><a class='page-link' href='orderList.bk?currentShowPageNo="+pageNo+"'>[다음]</a></li>";
			}
			pageBar += "<li class='page-item'><a class='page-link' href='orderList.bk?currentShowPageNo="+totalPage+"'>[마지막]</a></li>";
				
			// *** ==== 페이지바 만들기 끝 ==== *** //

			request.setAttribute("pageBar", pageBar);
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/kjy.shop/orderList.jsp");
		}

	}

}
