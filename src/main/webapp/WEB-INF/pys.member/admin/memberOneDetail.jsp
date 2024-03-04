<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 

<%
    String ctxPath = request.getContextPath();
    //    /MyMVC
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>관리자 페이지</title>
<%-- Required meta tags --%>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

<%-- Bootstrap CSS --%>
<link rel="stylesheet" type="text/css" href="<%= ctxPath%>/bootstrap-4.6.2-dist/css/bootstrap.min.css" > 

<%-- Font Awesome 6 Icons --%>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">

<%-- 직접 만든 CSS --%>
<link rel="stylesheet" type="text/css" href="<%= ctxPath%>/pys.css/admin/admin.css"/>

<%-- Optional JavaScript --%>
<script type="text/javascript" src="<%= ctxPath%>/pys.js/jquery-3.7.1.min.js"></script>
<script type="text/javascript" src="<%= ctxPath%>/bootstrap-4.6.2-dist/js/bootstrap.bundle.min.js" ></script> 

<%-- jQueryUI CSS 및 JS --%>
<link rel="stylesheet" type="text/css" href="<%= ctxPath%>/jquery-ui-1.13.1.custom/jquery-ui.min.css" />
<script type="text/javascript" src="<%= ctxPath%>/jquery-ui-1.13.1.custom/jquery-ui.min.js"></script>
 <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/meyer-reset/2.0/reset.min.css">
<%-- 직접만든 JS --%>



<style type="text/css">

  table.table-bordered > tbody > tr > td:nth-child(1) {
  	 width: 25%;
  	 font-weight: bold;
  	 text-align: right;
  }

</style>

<script type="text/javascript">
  $(document).ready(function(){ 
	  
	  $("div#smsResult").hide();
	  
	  $("button#btnSend").click( () => {
		  
		  //console.log( $("input#reservedate").val()+ " " + $("input#reservetime").val());
		  //2023-10-24 11:32
		  
		  let reservedate = $("input#reservedate").val();
		  reservedate = reservedate.split("-").join(""); 
		  // ["2023","10,"21"] ==> "20231021"
		  
		  let reservetime = $("input#reservetime").val();
		  reservetime = reservetime.split(":").join("");
		// ["09","30] ==> 0930
		
			const datetime = reservedate + reservetime;
		  	//console.log(datetime);
		  	//202310301139
		  	
		  	let dataObj;
		  	
		  	if(reservedate == "" || reservetime == "" ) {
		  		// 문자를 바로 보내기인 경우 
		  		dataObj = {"mobile":"${requestScope.mvo.mobile}",
		  					"smsContent":$("textarea#smsContent").val()};
		  		
		  	}
		  	else {
		  		// 예약문자 보내기인 경우
		  		dataObj = {"mobile":"${requestScope.mvo.mobile}",
	  					"smsContent":$("textarea#smsContent").val(),
	  					"datetime":datetime};
	  		
		  	}
		  
		  	$.ajax({
		  		url:"<%=ctxPath%>/member/smsSend.bk",
		  		type:"post",
		  		data:dataObj,
		  		dataType:"json",
		  		success:function(json){
		  			// json 은 {"group_id":"R2GWPBT7UoW308sI","success_count":1,"error_count":0} 처럼 된다.
		  			
		  			if(json.success_count == 1){
		  				$("div#smsResult").html("<span style='color:red; font-weight:bold;'>문자전송이 성공되었습니다.</span>");
		  				
		  			}
		  			else if(json.error_count != 0){
		  				$("div#smsResult").html("<span style='color:red; font-weight:bold;'>문자전송이 실패되었습니다.</span>");
		  			}
		  			
		  			$("div#smsResult").show();
		  			$("textarea#smsContent").val("");
		  			
		  		
		  		},
		  		
		  		error: function(request, status, error){
					alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
			 }
		  	});
		  
	  });
	  
  });// end of $(document).ready(function(){})-----------
</script>

</head>
<body>
	<nav class="navbar navbar-expand-sm"  style="padding-left:100px;">
		 
		  <!-- Brand/logo -->
		  <a class="navbar-brand pl-5" href="#"><img src="../image/logo.png" /></a>
		  
		  <!-- Links -->
		  <ul class="navbar-nav">
		    <li class="nav-item pl-5">
		      <a class="nav-link" href="<%= ctxPath%>/index.bk">HOME</a>
		    </li>
		    <li class="nav-item pl-5">
		      <a class="nav-link" href="<%= ctxPath%>/category/special.bk?cnum=1">MENU</a>
		    </li>
		    <li class="nav-item pl-5">
		      <a class="nav-link" href="<%= ctxPath%>/member/memberList.bk">회원목록</a>
		    </li>
		    <li class="nav-item pl-5">
		      <a class="nav-link" href="<%= ctxPath%>/productregister.bk">제품등록</a>
		    </li>
		    <li class="nav-item pl-5">
		      <a class="nav-link" href="<%= ctxPath%>/shop/orderList.bk">전체주문조회</a>
		    </li>
		    
		  </ul>
		</nav>
		 <div id="horizon">
		 <span><a href="<%= ctxPath%>/member/adminIndex.bk">관리자 페이지</a></span>
		 </div>



<div class="container">
	<c:if test="${empty requestScope.mvo}">
		존재하지 않는 회원입니다.<br>
	</c:if>
	
	<c:if test="${not empty requestScope.mvo}">
		
		<c:set var="mobile" value="${requestScope.mvo.mobile}" />
		
		<p class="h3 text-center mt-5 mb-4">::: ${requestScope.mvo.name} 님의 회원 상세정보 :::</p>
		
		<table class="table table-bordered" style="width: 60%; margin: 0 auto;">
		   <tr>
		      <td>아이디&nbsp;:&nbsp;</td>
		      <td>${requestScope.mvo.userid}</td>
		   </tr>
		   <tr>
		      <td>회원명&nbsp;:&nbsp;</td>
		      <td>${requestScope.mvo.name}</td>
		   </tr>
		   <tr>
		      <td>이메일&nbsp;:&nbsp;</td>
		      <td>${requestScope.mvo.email}</td>
		   </tr>
		   <tr>
		      <td>휴대폰&nbsp;:&nbsp;</td>
		      <td>${fn:substring(mobile, 0, 3)}-${fn:substring(mobile, 3, 7)}-${fn:substring(mobile, 7, 11)}</td>
		   </tr>
		   <tr>
		      <td>우편번호&nbsp;:&nbsp;</td>
		      <td>${requestScope.mvo.postcode}</td>
		   </tr>
		   <tr>
		      <td>주소&nbsp;:&nbsp;</td>
		      <td>${requestScope.mvo.address}&nbsp;
		          ${requestScope.mvo.detailaddress}&nbsp;
		          ${requestScope.mvo.extraaddress}
		      </td>
		   </tr>
		   <tr>
		      <td>성별&nbsp;:&nbsp;</td>
		      <td>
		         <c:choose>
		            <c:when test="${requestScope.mvo.gender == '1'}">
		               남
		            </c:when>
		            <c:otherwise>
		               여
		            </c:otherwise>
		         </c:choose>
		      </td>
		   </tr>
		   <tr>
		      <td>생년월일&nbsp;:&nbsp;</td>
		      <td>${requestScope.mvo.birthday}</td>
		   </tr>
		   <tr>
		      <td>만나이&nbsp;:&nbsp;</td>
		      <td>${requestScope.mvo.age}&nbsp;세</td>
		   </tr>
		   <tr>
		      <td>가입일자&nbsp;:&nbsp;</td>
		      <td>${requestScope.mvo.registerday}</td>
		   </tr>
		</table>
		
		<%-- ==== 휴대폰 SMS(문자) 보내기 ==== --%>
		<div class="border my-5 text-center" style="width: 60%; margin: 0 auto;">
		  	<p class="h5 bg-info text-white">
		  	  &gt;&gt;&nbsp;&nbsp;휴대폰 SMS(문자) 보내기 내용 입력란&nbsp;&nbsp;&lt;&lt;
		  	</p>
		  	<div class="mt-4 mb-3">
		  		<span class="bg-danger text-white" style="font-size: 14pt;">문자발송 예약일자</span>
		  		<input type="date" id="reservedate" class="mx-2" />
		  		<input type="time" id="reservetime" />
		  	</div>
		  	<div style="display: flex;">
		  	   <div style="border: solid 0px red; width: 81%; margin: auto;">
		  	      <textarea rows="4" id="smsContent" style="width: 100%;"></textarea>
		  	   </div>
		  	   <div style="border: solid 0px blue; width: 19%; margin: auto;">
		  	      <button id="btnSend" class="btn btn-secondary">문자전송</button>
		  	   </div>
		  	</div>
		  	<div id="smsResult" class="p-3"></div>
		</div>
		
	</c:if>

    <div class="text-center mb-5">
       <button type="button" class="btn btn-secondary" onclick="javascript:location.href='memberList.bk'">회원목록[처음으로]</button> 
      <%--<button type="button" class="btn btn-success mx-5" onclick="javascript:history.back()">회원목록[history.back()]</button> --%>
       <button type="button" class="btn btn-primary" onclick="javascript:location.href='<%=ctxPath %>${requestScope.goBackURL}'">회원목록[검색된결과]</button>
    </div>
    
</div>


