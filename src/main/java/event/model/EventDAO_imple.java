package event.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import event.domain.*;


public class EventDAO_imple implements EventDAO {
	
	private DataSource ds; // DataSource ds 는 아파치톰캣이 제공하는 DBCP(DB Connection Pool)이다.  
	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;
	
	// 생성자
	public EventDAO_imple() {
		
		try {
			Context initContext = new InitialContext();
		    Context envContext  = (Context)initContext.lookup("java:/comp/env");
		    ds = (DataSource)envContext.lookup("jdbc/semi_oracle");
		    
		} catch(NamingException e) {
			e.printStackTrace();
		}
		
	}
	
	
	// 사용한 자원을 반납하는 close() 메소드 생성하기 
	private void close() {
		try {
			if(rs != null)    {rs.close();    rs=null;}
			if(pstmt != null) {pstmt.close(); pstmt=null;}
			if(conn != null)  {conn.close();  conn=null;}
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	@Override
	public int totalPspecCount() throws Exception {
		
		int totalCount = 0;
	      
	      try {
	         conn = ds.getConnection();
	         
	         String sql = " select count(*)"
	                  + " from event ";
	                  
	         
	         pstmt = conn.prepareStatement(sql);
	         
	         rs = pstmt.executeQuery();
	         
	         rs.next();
	         
	         totalCount = rs.getInt(1);
	         
	         
	         
	      } finally {
	         close();
	      }
	      
	      return totalCount;
	}
	
	
	
	
	
	
	
	@Override
	public List<EventVO> selectEventlist(Map<String, String> paraMap) throws Exception {

		List<EventVO> eventList = new ArrayList<>();
	    
	    try {
	       conn = ds.getConnection();
	       
	       String sql = " select eventimg,eventdate, eventno "
		       		  + " from event "
		       		  + " WHERE eventno between ? and ? ";
	       
	       pstmt = conn.prepareStatement(sql);
	       
	       pstmt.setString(1, paraMap.get("start"));
	       pstmt.setString(2, paraMap.get("end"));
	       
	       rs = pstmt.executeQuery();
	       while(rs.next()) {
	          
	          EventVO evo = new EventVO();
	          
	          evo.setEventimg(rs.getString(1));
	          evo.setEventdate(rs.getString(2));
	          evo.setEventno(rs.getInt(3));
	          eventList.add(evo);
	            
	         }// end of while
	         
	      } finally {
	         close();
	      }
	      
	      return eventList;
	          
	       
	

	}


	@Override
	public List<EventVO> eventimgPresent() throws Exception {
		List<EventVO> eventpresentlist = new ArrayList<>();
	    
	    try {
	       conn = ds.getConnection();
	       
	       String sql = " SELECT eventno, eventimg, eventdate "
	       		+ " FROM event "
	       		+ " WHERE LENGTH(eventdate) < 12 OR "
	       		+ " (LENGTH(eventdate) >= 12 AND  TO_DATE(SUBSTR(eventdate, 12, 21),'YYYY-MM-DD') > SYSDATE ) "; 
	    		   
	       
	       pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				
				EventVO evtvo = new EventVO();
				evtvo.setEventno(rs.getInt(1));
				evtvo.setEventimg(rs.getString(2));
				evtvo.setEventdate(rs.getString(3));
				
				eventpresentlist.add(evtvo);
			}// end of while(rs.next())-----------------
			
		} finally {
			close();
		}
		
		return eventpresentlist;
	}


	@Override
	public List<EventVO> eventdetail() throws Exception {
				
		List<EventVO> eventdetail_list = new ArrayList<>();
	    
	    try {
	       conn = ds.getConnection();
	       
	       String sql =  " select eventimgdetail,eventdate,eventitle, eventno "
	    		   + " from event ";
	       
	       pstmt = conn.prepareStatement(sql);
	       
	       
	       rs = pstmt.executeQuery();
	       while(rs.next()) {
	          
	          EventVO eao = new EventVO();
	          
	          eao.setEventimgdetail(rs.getString(1));
	          eao.setEventdate(rs.getString(2));
	          eao.setEventitle(rs.getString(3));
	          eao.setEventno(rs.getInt(4));
	         
	          eventdetail_list.add(eao);
	            
	         }// end of while
	         
	      } finally {
	         close();
	      }
	      
	      return eventdetail_list;

	}


	//제품번호를 가지고서 해당 제품의 정보를 조회해오기 
	@Override
	public EventVO selectOneProductByPnum(String eventno) throws SQLException {

		EventVO pvo = null;
	   
	   try {
	      conn = ds.getConnection();
	      
	      String sql =  " select eventno, eventimgdetail, eventitle, eventdate "
		      		+ "from event "
		      		+ "where eventno = ? ";
		      
		      pstmt = conn.prepareStatement(sql);
		      
		      pstmt.setString(1, eventno);
		      
	      
	      rs = pstmt.executeQuery();
	      
	      if(rs.next()) {
	         
	         pvo = new EventVO();
	         
	         pvo.setEventno(rs.getInt("eventno"));            // 제품번호
	         pvo.setEventimgdetail(rs.getString("eventimgdetail"));       // 제품명
	         pvo.setEventitle(rs.getString("eventitle")); // 제조회사명
	         pvo.setEventdate(rs.getString("eventdate"));   // 제품이미지1   이미지파일명

	                     
	      }// end of while(rs.next())-------------------------
	      
	      
	   } finally {
	      close();
	   }
	   
	   return pvo;            
	   
	}// end of public ProductVO selectOneProductByPnum(String pnum) throws SQLException--------





	



    

}

