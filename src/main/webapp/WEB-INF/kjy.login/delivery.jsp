<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"  %>
    
<%
    String ctxPath = request.getContextPath();
    //    /tempSemi
%>
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>딜리버리</title>

<%-- Required meta tags --%>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

<%-- Bootstrap CSS --%>
<link rel="stylesheet" type="text/css" href="<%= ctxPath%>/bootstrap-4.6.2-dist/css/bootstrap.min.css" > 

<%-- Font Awesome 6 Icons --%>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">

<%-- 직접 만든 CSS --%>
<link rel="stylesheet" type="text/css" href="<%= ctxPath%>/kjy.css/delivery.css" />

<%-- Optional JavaScript --%>
<script type="text/javascript" src="<%= ctxPath%>/js/jquery-3.7.1.min.js"></script>
<script type="text/javascript" src="<%= ctxPath%>/bootstrap-4.6.2-dist/js/bootstrap.bundle.min.js" ></script> 

<script type="text/javascript" src="<%= ctxPath%>/kjy.js/delivery.js"></script>

</head>
<body>
	
	<%-- 헤더시작 --%>
	
	<nav class="navbar navbar-expand-sm navbar-dark fixed-top" style="background-color: rgb(226, 34, 25); height: 180px;">
		
		<table id="navtbl">
			<tbody>
				<tr>
					<td colspan="2">
						<a class="nav-item" href="<%= ctxPath%>/index.bk">브랜드홈</a>
						<a class="nav-item" href="<%= ctxPath%>/login/logout.bk">로그아웃</a>
						<a class="nav-item" href="#">MY킹</a>
						<a class="nav-item" href="#">고객센터</a>
					</td>
				</tr>
				<tr>
					<td><img alt="deliveryLogo" src="<%= ctxPath%>/image/deliverylogo.png"> 딜리버리</td>
					<td>
						<table style="margin-left: auto;">
							<tr>
								<td rowspan="2"><img alt="man" src="<%= ctxPath%>/image/man.png" style="padding-right: 10px;"></td>
								<td>${(sessionScope.loginuser).name}님 안녕하세요</td>
							</tr>
							<tr>
								<td><strong>MY킹 바로가기  ></strong></td>
							</tr>
						</table>
					</td>
				</tr>
			</tbody>
		</table>
	</nav>
	<%-- 헤더끝 --%>
	
	<div id= "rlcontainer">
	
		<div id= "container" class="container-fluid p-4 text-white" style="background-color: #333332;">
			<div>
				<table id="firsttbl" class="ordcrt">
					<tbody>
						<tr>
							<td rowspan="2"><a class="navbar-brand" href="#"><img alt="deliveryLogo" src="<%= ctxPath%>/image/bike.png"></a></td>
							<td style="font-weight: bold">딜리버리 주문내역</td>
						</tr>
						<tr>
							<td class="smtd">주문내역이 없습니다.</td>
						</tr>
					</tbody>
				</table>
			</div>
			<span style="border-left: solid 1px white;"></span>
			<div>
				<table id="secondtbl" class="ordcrt">
					<tbody>
						<tr>
							<td rowspan="2"><img alt="deliveryLogo" src="<%= ctxPath%>/image/cart.png"></td>
							<td style="font-weight: bold">카트</td>
						</tr>
						<tr>
							<td class="smtd">카트에 담은 상품이 없습니다.</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		
		<nav class="navbar navbar-expand-sm navbar-dark" style="background-color: black;">
			  <!-- Links 추가하기 -->
			  <ul id="leftbar" class="navbar-nav mr-auto">
			    <li class="nav-item">
			      <a class="nav-link text-white" href="#">딜리버리</a>
			    </li>
			    <li class="nav-item">
			      <a class="nav-link text-white" href="#">>&nbsp;&nbsp;&nbsp;&nbsp;메뉴</a>
			    </li>
			  </ul>
			  <ul id="rightbar" class="navbar-nav ml-auto">  
			    <li class="nav-item">
			      <a class="nav-link disabled text-white" href="#" tabindex="-1" aria-disabled="true">배달지를 선택하세요</a>
			    </li>
			    <li class="nav-item">
			    	<button id="change" type="button" class="btn btn-light">변경</button>
			    </li>
			  </ul>
			</nav>
		
		<!-- 본문 콘텐츠 시작 -->
		
		<div id="content">
			<div id="item">
				<span style="font-size: 27pt; font-weight: bold;">메뉴</span>
				<img id="logo" src="<%= ctxPath%>/image/logo_white.png">
				<div>메뉴 준비중 입니다</div>
			</div>
			<div id="caution">
				<div style="font-weight: bold;">유의사항</div>
				<div style="font-weight: bold;"><i class="fa-solid fa-circle-info"></i>&nbsp;&nbsp;원산지, 영양성분, 알레르기 유발성분 ></div>
				<hr style="border: solid 1.5px black;">
				<ul>
					<li>매장별 주문금액이 상이하니, 반드시 최소금액을 확인하기 바랍니다.</li>
					<li>배달 소요시간은 기상조건이나 매장 사정상 지연 또는 제한될 수 있습니다.</li>
					<li>고객님과 수 차례 연락을 시도한 후 연락이 되지 않는 경우 배달음식이 변질되거나 부패될 우려로 식품위생법상 위반될 여지가 있어 별도로 보관 하지 않으며, <span style="font-weight: bold;">재배달 또는 환불처리가 어려울 수 있습니다.</span></li>
					<li>딜리버리 서비스 메뉴의 가격은 매장 가격과 상이하며, 딜리버리 시 타쿠폰을 사용하실 수 없습니다. (일부품목 배달 제외)</li>
					<li>배달 제품은 매장 행사(할인가격)에서 제외됩니다.</li>
					<li>제품 가격 및 메뉴 구성은 본사 사정상 변경될 수 있습니다.</li>
					<li>대량 주문의 경우 콜센터(1599-0505)주문으로만 가능합니다.</li>
					<li>주문 완료 후 변경 및 취소는 콜센터(1599-0505)에서만 가능합니다.</li>
				</ul>
			</div>
		</div>
	
		<div id="footer">
			<hr style="width: 65%; background-color: white;">
			<div id="ftcontent">
				<img id="bgk" src="<%= ctxPath%>/image/bgk.png">
				<p class="lastp" style="padding-top: 20px;">서울 종로구 삼봉로 71 G 타워 4F,5F 주식회사 비케이알  전화주문 1599-0505</p>
				<p class="lastp">사업자 등록번호 101-86-76277  (주)BKR 대표이사 이동형</p>
				<p class="lastp">Copyright 2019 BKR Co., Ltd. All right Reserved</p>
				<div style="position: absolute; bottom: 70%; left: 76%;">
					<a href="#" class="lasta">이용약관</a>&nbsp;&nbsp;
					<a href="#" class="lasta">개인정보처리방침</a>&nbsp;&nbsp;
					<a href="#" class="lasta">법적고지</a>
				</div>
			</div>
		</div>
	
	</div>
	
</body>
</html>