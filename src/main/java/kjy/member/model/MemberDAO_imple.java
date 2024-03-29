package kjy.member.model;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import pys.member.domain.MemberVO;
import util.security.AES256;
import util.security.SecretMyKey;
import util.security.Sha256;

public class MemberDAO_imple implements MemberDAO {

	private DataSource ds; // DataSource ds 는 아파치톰캣이 제공하는 DBCP(DB Connection Pool)이다.  
	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;
	
	private AES256 aes;
	
	// 생성자
	public MemberDAO_imple() {
		
		try {
			Context initContext = new InitialContext();
		    Context envContext  = (Context)initContext.lookup("java:/comp/env");
		    ds = (DataSource)envContext.lookup("jdbc/semi_oracle");
		    
		    aes = new AES256(SecretMyKey.KEY);
		    // SecretMyKey.KEY 은 우리가 만든 암호화/복호화 키이다.
		    
		} catch(NamingException e) {
			e.printStackTrace();
		} catch(UnsupportedEncodingException e) {
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
	
	
	// 회원가입을 해주는 메소드 (tbl_member 테이블에 insert)
	@Override
	public int registerMember(MemberVO member) throws SQLException {
		
		int result = 0;
		
		try {
			conn = ds.getConnection();
			
			String sql = " insert into tbl_member(userid, pwd, name, email, mobile, postcode, address, detailaddress, extraaddress, gender, birthday) " 
					   + " values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, member.getUserid());
			pstmt.setString(2, Sha256.encrypt(member.getPwd()) ); // 암호를 SHA256 알고리즘으로 단방향 암호화 시킨다. 
			pstmt.setString(3, member.getName());
			pstmt.setString(4, aes.encrypt(member.getEmail()) );  // 이메일을 AES256 알고리즘으로 양방향 암호화 시킨다. 
			pstmt.setString(5, aes.encrypt(member.getMobile()) ); // 휴대폰번호를 AES256 알고리즘으로 양방향 암호화 시킨다. 
			pstmt.setString(6, member.getPostcode());
			pstmt.setString(7, member.getAddress());
			pstmt.setString(8, member.getDetailaddress());
			pstmt.setString(9, member.getExtraaddress());
			pstmt.setString(10, member.getGender());
			pstmt.setString(11, member.getBirthday());
			
			result = pstmt.executeUpdate();
			
		} catch(GeneralSecurityException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		 finally {
			close();
		}
		
		return result;
		
	}// end of public int registerMember(MemberVO member) throws SQLException-------


	// ID 중복검사 (tbl_member 테이블에서 userid 가 존재하면 true 를 리턴해주고, userid 가 존재하지 않으면 false 를 리턴한다) 
	@Override
	public boolean idDuplicateCheck(String userid) throws SQLException {
		
		boolean isExists = false;
		
		try {
			conn = ds.getConnection();
			
			String sql = " select userid "
					   + " from tbl_member "
					   + " where userid = ? ";
			
			pstmt = conn.prepareStatement(sql); 
			pstmt.setString(1, userid);
			
			rs = pstmt.executeQuery();
			
			isExists = rs.next(); // 행이 있으면(중복된 userid) true,
			                      // 행이 없으면(사용가능한 userid) false
			
		} finally {
			close();
		}
		
		return isExists;
	}// end of public boolean idDuplicateCheck(String userid) throws SQLException-------


	// email 중복검사 (tbl_member 테이블에서 email 이 존재하면 true 를 리턴해주고, email 이 존재하지 않으면 false 를 리턴한다) 
	@Override
	public boolean emailDuplicateCheck(String email) throws SQLException {

		boolean isExists = false;
		
		try {
			conn = ds.getConnection();
			
			String sql = " select email "
					   + " from tbl_member "
					   + " where email = ? ";
			
			pstmt = conn.prepareStatement(sql); 
			pstmt.setString(1, aes.encrypt(email));
			
			rs = pstmt.executeQuery();
			
			isExists = rs.next(); // 행이 있으면(중복된 email) true,
			                      // 행이 없으면(사용가능한 email) false
			
		} catch(GeneralSecurityException | UnsupportedEncodingException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
		return isExists;
	}// end of public boolean emailDuplicateCheck(String email) throws SQLException------


	// 입력받은 paraMap 을 가지고 한명의 회원정보를 리턴시켜주는 메소드(로그인 처리)  
	@Override
	public MemberVO selectOneMember(Map<String, String> paraMap) throws SQLException {
		
		MemberVO member = null;
		
		try {
			conn = ds.getConnection();
			
			String sql =  " SELECT userid, name, email, mobile, postcode, address, detailaddress, extraaddress, gender "
						+ "      , birthday, coin, point, registerday, pwdchangegap "
						+ "      , nvl( lastlogingap , trunc( months_between(sysdate, to_date(registerday, 'yyyy-mm-dd'))) ) AS lastlogingap " 
						+ "      , idle " 
						+ " FROM "
						+ " ( "
						+ " select userid, name, email, mobile, postcode, address, detailaddress, extraaddress, gender "
						+ "      , birthday, coin, point, to_char(registerday, 'yyyy-mm-dd') AS registerday "
						+ "      , trunc( months_between(sysdate, lastpwdchangedate) ) AS pwdchangegap "
						+ "      , idle "
						+ " from tbl_member "
						+ " where status = 1 and userid = ? and pwd = ? "
						+ " ) M "
						+ " CROSS JOIN "
						+ " ( "
						+ " select trunc( months_between(sysdate, max(logindate)) ) AS lastlogingap "
						+ " from tbl_loginhistory "
						+ " where fk_userid = ? "
						+ " ) H "; 
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, paraMap.get("userid"));
			pstmt.setString(2, Sha256.encrypt(paraMap.get("pwd")));
			pstmt.setString(3, paraMap.get("userid"));
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				member = new MemberVO();
				
				member.setUserid(rs.getString(1));
				member.setName(rs.getString(2));
				member.setEmail( aes.decrypt(rs.getString(3)) );  // 복호화
				member.setMobile( aes.decrypt(rs.getString(4)) ); // 복호화
				member.setPostcode(rs.getString(5));
				member.setAddress(rs.getString(6));
				member.setDetailaddress(rs.getString(7));
				member.setExtraaddress(rs.getString(8));
				member.setGender(rs.getString(9));
				member.setBirthday(rs.getString(10));
				member.setCoin(rs.getInt(11));
				member.setPoint(rs.getInt(12));
				member.setRegisterday(rs.getString(13));
				member.setIdle(rs.getInt(16));
				
				if(rs.getInt(16) == 0 && rs.getInt(14) >= 3) {
					// 휴면이 아니면서 
					// 마지막으로 암호를 변경한 날짜가 현재시각으로 부터 3개월이 지났으면 true
					// 마지막으로 암호를 변경한 날짜가 현재시각으로 부터 3개월이 지나지 않았으면 false 
					
					member.setRequirePwdChange(true); // 로그인시 암호를 변경해라는 alert 를 띄우도록 할때 사용한다. 
				}
				
				if(rs.getInt(16) == 0 && rs.getInt(15) >= 12) {
				    // 휴면이 아니면서
					// 마지막으로 로그인 한 날짜시간이 현재시각으로 부터 1년이 지났으면 휴면으로 지정 
					member.setIdle(1);
					
					// === tbl_member 테이블의 idle 컬럼의 값을 1로 변경하기 === //
					sql = " update tbl_member set idle = 1 "
						+ " where userid = ? "; 
					
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, paraMap.get("userid"));
					
					pstmt.executeUpdate();
				}
				
				// === 휴면이 아닌 회원만 tbl_loginhistory(로그인기록) 테이블에 insert 하기 === // 
				if(rs.getInt(16) == 0) {
					 
					sql = " insert into tbl_loginhistory(historyno, fk_userid, clientip) "
						+ " values(seq_historyno.nextval, ?, ?) "; 
					
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, paraMap.get("userid"));
					pstmt.setString(2, paraMap.get("clientip"));
					
					pstmt.executeUpdate();
				}
				
			} // end of if(rs.next())-------------------
			
			
		} catch(GeneralSecurityException | UnsupportedEncodingException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
		return member;
	}// end of public MemberVO selectOneMember(Map<String, String> paraMap) throws SQLException------


	@Override
	public String findUserid(Map<String, String> paraMap) throws SQLException {
		
		String userid = null;
		
		try {
			conn = ds.getConnection();
			String sql =" select userid "
					+ " from tbl_member "
					+ " where status = 1 and name = ? and mobile = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, paraMap.get("name"));
			pstmt.setString(2, aes.encrypt(paraMap.get("mobile")) );
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				userid = rs.getString(1);
			}
			
			
		}catch(GeneralSecurityException | UnsupportedEncodingException e) {
			e.printStackTrace();
		} finally {
			close();
		}
			
		
		
		return userid;
	}

	
	// 비밀번호 찾기(아이디,이메일을 입력받아서 해당 사용자가 존재하는지 유무를 알려준다.)
	@Override
	public boolean isUserExist(Map<String, String> paraMap) throws SQLException {
		
		boolean isUserExist = false;
		
		try {
			conn = ds.getConnection();
			String sql =" select userid "
					+ " from tbl_member "
					+ " where status = 1 and userid = ? and email = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, paraMap.get("userid"));
			pstmt.setString(2,aes.encrypt(paraMap.get("email")) );
			
			rs = pstmt.executeQuery();
			
			isUserExist = rs.next();
			
			
		}catch(GeneralSecurityException | UnsupportedEncodingException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
		return isUserExist;
	}// end of public boolean isUserExist(Map<String, String> paraMap) throws SQLException


	@Override
	public int pwdUpdate(Map<String, String> paraMap) throws SQLException {
		int result = 0;
		
		try {
			conn = ds.getConnection();
			String sql = "update tbl_member set pwd = ?"
						+ "               , lastpwdchangedate = sysdate "
						+ " where userid = ? ";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, Sha256.encrypt(paraMap.get("new_pwd")));// 암호를 SHA256 알고리즘으로 단방향 암호화 시킨다. 
			pstmt.setString(2, paraMap.get("userid"));
			
			result = pstmt.executeUpdate();
						
			
		} finally {
			close();
		}
		
		return result;
	}// end of public int pwdUpdate(Map<String, String> paraMap) throws SQLException

	
	
	// 회원정보 수정시 email 중복검사 (tbl_member 테이블에서 다른 사용자가 email 이 존재하면 true 를 리턴해주고, email 이 존재하지 않으면 false 를 리턴한다)   
	@Override
	public boolean emailDuplicateCheck2(Map<String, String> paraMap) throws SQLException {

		boolean isExists = false;
		
		try {
			conn = ds.getConnection();
			
			String sql = " select email "
					   + " from tbl_member "
					   + " where userid != ? and email = ? ";
			
			pstmt = conn.prepareStatement(sql); 
			pstmt.setString(1, paraMap.get("userid"));
			pstmt.setString(2, aes.encrypt(paraMap.get("email")));
			
			rs = pstmt.executeQuery();
			
			isExists = rs.next(); // 행이 있으면(중복된 email) true,
			                      // 행이 없으면(사용가능한 email) false
			
		} catch(GeneralSecurityException | UnsupportedEncodingException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
		return isExists;
		
	}// end of public boolean emailDuplicateCheck2(Map<String, String> paraMap) throws SQLException 

	
	// 비밀번호 변경시 현재 사용중인 비밀번호인지 아닌지 알아오기(현재 사용중인 비밀번호 이라면 true, 새로운 비밀번호이라면 false)
	@Override
	public boolean duplicatePwdCheck(Map<String, String> paraMap) throws SQLException {
		
		boolean isExists = false;
		
		try {
			conn = ds.getConnection();
			
			String sql = " select email "
					   + " from tbl_member "
					   + " where userid = ? and pwd = ? ";
			
			pstmt = conn.prepareStatement(sql); 
			pstmt.setString(1, paraMap.get("userid"));
			pstmt.setString(2, Sha256.encrypt(paraMap.get("new_pwd"))); 
			
			rs = pstmt.executeQuery();
			
			isExists = rs.next(); // 행이 있으면(현재 사용중인 비밀번호) true,
			                      // 행이 없으면(새로운 비밀번호) false
			
		} finally {
			close();
		}
		
		return isExists;
		
	}//end of public boolean duplicatePwdCheck(Map<String, String> paraMap) throws SQLException

	// 회원의 정보 변경하기
	@Override
	public int updateMember(MemberVO member) throws SQLException {
		
		int result = 0;
		
		try {
			conn = ds.getConnection();
			
			String sql = " update tbl_member set name = ?"
					   + "                     , pwd = ? "
					   + "                     , email = ? "
					   + "                     , mobile = ? "
					   + "                     , postcode = ? "
					   + "                     , address = ? "
					   + "                     , detailaddress = ? "
					   + "                     , extraaddress = ? "
					   + "                     , lastpwdchangedate = sysdate "
					   + " where userid = ? ";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, member.getName());
			pstmt.setString(2, Sha256.encrypt(member.getPwd()) ); // 암호를 SHA256 알고리즘으로 단방향 암호화 시킨다. 
			pstmt.setString(3, aes.encrypt(member.getEmail()) );  // 이메일을 AES256 알고리즘으로 양방향 암호화 시킨다. 
			pstmt.setString(4, aes.encrypt(member.getMobile()) ); // 휴대폰번호를 AES256 알고리즘으로 양방향 암호화 시킨다. 
			pstmt.setString(5, member.getPostcode());
			pstmt.setString(6, member.getAddress());
			pstmt.setString(7, member.getDetailaddress());
			pstmt.setString(8, member.getExtraaddress());
			pstmt.setString(9, member.getUserid());
			
			result = pstmt.executeUpdate();
			
		} catch(GeneralSecurityException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		 finally {
			close();
		}
		
		return result;
	}

	// DB에 코인 및 포인트 증가하기
	@Override
	public int coinUpdateLoginUser(Map<String, String> paraMap) throws SQLException {
		
		int result = 0;
		
		try {
			conn = ds.getConnection();
			
			String sql = " update tbl_member set coin = coin + ?"
					   + "                      ,point = point + ? "
					   + " where userid = ? ";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1,Integer.parseInt(paraMap.get("coinmoney")) );
			pstmt.setInt(2,(int)(Integer.parseInt(paraMap.get("coinmoney")) * 0.01)  ); // 0.01 을 곱했을때 double 타입이 나온다. int로 형변환 시켜준다.
			pstmt.setString(3, paraMap.get("userid") );  // 이메일을 AES256 알고리즘으로 양방향 암호화 시킨다. 
			
			
			result = pstmt.executeUpdate();
			
		} 
		 finally {
			close();
		}
		
		return result;
	}// end of public int coinUpdateLoginUser(Map<String, String> paraMap) throws SQLException

	
	

	
	
	
	
	
}
