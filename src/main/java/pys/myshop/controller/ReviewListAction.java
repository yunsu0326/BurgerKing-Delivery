package pys.myshop.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import common.controller.AbstractController;
import pys.myshop.domain.PurchaseReviewsVO;
import pys.myshop.model.ProductDAO;
import pys.myshop.model.ProductDAO_imple;

public class ReviewListAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
String fk_pnum = request.getParameter("fk_pnum"); // 제품번호
		
		ProductDAO pdao = new ProductDAO_imple();
	      List<PurchaseReviewsVO> reviewList = pdao.reviewList(fk_pnum);
	      
	      JSONArray jsArr = new JSONArray(); // []
	      
	      if(reviewList != null && reviewList.size() > 0) {
	         for(PurchaseReviewsVO reviewsvo : reviewList) {
	            JSONObject jsobj = new JSONObject();                // {} {}
	            jsobj.put("contents", reviewsvo.getContents());     // {"contents":"제품후기내용물"}  {"contents":"제품후기내용물2"}
	            jsobj.put("name", reviewsvo.getMvo().getName());    // {"contents":"제품후기내용물", "name":"작성자이름"}   {"contents":"제품후기내용물2", "name":"작성자이름2"}      
	            jsobj.put("writeDate", reviewsvo.getWriteDate());   // {"contents":"제품후기내용물", "name":"작성자이름", "writeDate":"작성일자"}   {"contents":"제품후기내용물2", "name":"작성자이름2", "writeDate":"작성일자2"}  
	            jsobj.put("userid", reviewsvo.getFk_userid());      // {"contents":"제품후기내용물", "name":"작성자이름", "writeDate":"작성일자", "userid":"사용자아이디1"}   {"contents":"제품후기내용물2", "name":"작성자이름2", "writeDate":"작성일자2", "userid":"사용자아이디2"}  
	            jsobj.put("review_seq", reviewsvo.getReview_seq()); // {"contents":"제품후기내용물", "name":"작성자이름", "writeDate":"작성일자", "userid":"사용자아이디1", "review_seq":제품후기글번호}   {"contents":"제품후기내용물2", "name":"작성자이름2", "writeDate":"작성일자2", "userid":"사용자아이디2", "review_seq":제품후기글번호2} 
	            
	            jsArr.put(jsobj);  // [{"contents":"제품후기내용물", "name":"작성자이름", "writeDate":"작성일자", "userid":"사용자아이디", "review_seq":제품후기글번호},{"contents":"제품후기내용물2", "name":"작성자이름2", "writeDate":"작성일자2", "userid":"사용자아이디2", "review_seq":제품후기글번호2}]
	         }// end of for----------------------
	      }
	      
	      String json = jsArr.toString();  // 문자열 형태로 변환해줌.
	      
	      request.setAttribute("json", json);
	      
	   //   super.setRedirect(false);
	      super.setViewPage("/WEB-INF/jsonview.jsp");
		
	}

}
