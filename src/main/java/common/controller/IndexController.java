package common.controller;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pys.myshop.domain.ImageVO;
import pys.myshop.model.*;

public class IndexController extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		ProductDAO pdao = new ProductDAO_imple();
		
		List<ImageVO> imageList = pdao.selectbanner();
		
		
		request.setAttribute("imageList", imageList);
		
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/index.jsp");
			
		
		
	}

}
