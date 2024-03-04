<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
    
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
<link rel="stylesheet" type="text/css" href="<%= ctxPath%>/pys.css/admin/productregister.css"/>

<%-- Optional JavaScript --%>
<script type="text/javascript" src="<%= ctxPath%>/pys.js/jquery-3.7.1.min.js"></script>
<script type="text/javascript" src="<%= ctxPath%>/bootstrap-4.6.2-dist/js/bootstrap.bundle.min.js" ></script> 

<%-- jQueryUI CSS 및 JS --%>
<link rel="stylesheet" type="text/css" href="<%= ctxPath%>/jquery-ui-1.13.1.custom/jquery-ui.min.css" />
<script type="text/javascript" src="<%= ctxPath%>/jquery-ui-1.13.1.custom/jquery-ui.min.js"></script>
 <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/meyer-reset/2.0/reset.min.css">




</head>
<body>
	<nav class="navbar navbar-expand-sm"  style="padding-left:100px;">
		 
		  <!-- Brand/logo -->
		  <a class="navbar-brand pl-5" href="#"><img src="<%= ctxPath%>/image/logo.png" /></a>
		  
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
		 <style type="text/css">
   table#tblProdInput {border: solid gray 1px; 
                       border-collapse: collapse; }
                       
    table#tblProdInput td {border: solid gray 1px; 
                          padding-left: 10px;
                          height: 50px; }
                          
    .prodInputName {background-color: #e6fff2; 
                    font-weight: bold; }                                                 
   
   .error {color: red; font-weight: bold; font-size: 9pt;}

</style>

<script type="text/javascript">
	$(document).ready(function() {
	    $("span.error").hide();
	    
	
/*
	    $("#tblProdInput > tbody > tr:nth-child(1) > td:nth-child(2) > select").on("change", function(e) {
	        var selectedOption = $(e.target).val();

	        if (selectedOption == "") {
	            $("#tblProdInput :input").prop("disabled", true);
	            $(e.target).parent().find("span.error").show();
	            $(e.target).val("");
	        } else {
	            $("#tblProdInput :input").prop("disabled", false);
	            $(e.target).parent().find("span.error").hide();
	        }
	    });
*/
	    
	 
	
	    // 제품이미지 또는 추가이미지 파일을 선택하면 화면에 이미지를 미리 보여주기 구현하기 시작
	    $(document).on("change", "input.img_file", function (e) {
	    	//$("img#previewImg").hide();
	    	    
	    	
	    	$("img#previewImg").show();
	    	
	    	const input_file = $(e.target).get(0);
	      
	       
	       const fileReader = new FileReader();
	             
	  
	       fileReader.readAsDataURL(input_file.files[0]); //FileReader.readAsDataURL() --> 파일을 읽고, result속성에 파일을 나타내는 URL을 저장 시켜준다.
	  		
	       fileReader.onload = function(){ // FileReader.onload --> 파일 읽기 완료 성공시에만 작동하도록 하는 것임.
	    //	  console.log(fileReader.result);
	    	  /*
              data:image/jpeg;base64,/9j/4AAQSkZJRgABAQEAeAB4AAD/2wBDAAIBAQIBAQICAgICAgICAwUDAwMDAwYEBAMFBwYHBwcGBwcICQsJCAgKCAcHCg0KCgsMDAwMBwkODw0MDgsMDAz/2wBDAQICAg 
              이러한 형태로 출력되며, img.src 의 값으로 넣어서 사용한다.
               */
              document.getElementById("previewImg").src = fileReader.result;
               
	    	   
	       };
	       
	  		
	    }); // 제품이미지 또는 추가이미지 파일을 선택하면 화면에 이미지를 미리 보여주기 구현하기 끝
	    
	    //제품 등록하기
	    $("input:button[id='btnRegister']").click(function(){
	  
	    	
	    	$("span.error").hide();
	    	
	    	let b_flag = false;
	    	
	    	
	    	$(".infoData").each(function(index, elmt){
	 			const val = $(elmt).val().trim();   		
	    		if(val == ""){
	    			$(elmt).next().show();
	    			b_flag = true;
	    			return false;  ///일반적인 for문의 break;와 같은 기능이다.	
	    		}
	 			
	    	});
	 		
	    	// ==== 추가이미지파일(선택)에서 파일개수 모두에 파일을 적용하지 않고 일부만 적용한 상태인 경우 제품등록을 못하게 하도록 한다. 시작 ==== //
	          const add_attach_file_List = document.getElementsByClassName("add_attach_file");
	         // console.log(add_attach_file_List); // HTMLCollection(개수) 가 나온다.
	        //  console.log(add_attach_file_List.length); // 추가이미지파일(선택)의 파일개수 가 나온다.
	       
	           for(let i=0; i<add_attach_file_List.length; i++) {
	            // console.log(add_attach_file_List[i]);
	            /*
	               <input type="file" name="attach0" class="btn btn-default img_file add_attach_file" accept="image/*"> 
	               <input type="file" name="attach1" class="btn btn-default img_file add_attach_file" accept="image/*"> 
	               <input type="file" name="attach2" class="btn btn-default img_file add_attach_file" accept="image/*">
	               <input type="file" name="attach3" class="btn btn-default img_file add_attach_file" accept="image/*"> 
	            */
	            
	                try {
	                 console.log(add_attach_file_List[i].files[0].name);
	                 // berkelekle단가라포인트03.jpg
	                 // berkelekle덩크04.jpg
	                 // Uncaught TypeError: Cannot read properties of undefined (reading 'name')
	                } catch(e) {
	                   alert("추가이미지파일(선택)에서 파일선택에 \n파일을 모두 적용하지 않으셨습니다.");
	                   return; // 제품등록하기를 종료시켜 버림
	                }
	           }//end of for -------------------
	           // ==== 추가이미지파일(선택)에서 파일개수 모두에 파일을 적용하지 않고 일부만 적용한 상태인 경우 제품등록을 못하게 하도록 한다. 끝 ==== //
	    	
	    	if(!b_flag){
	    		const frm = document.prodInputFrm;
	    		//alert("서브밋 하러 간다 ㅎㅎㅎ");
	    		frm.submit();
	    	}
	    	
	    	
	    });
	    $("input[type='reset']").click(function(){
	    	$("span.error").hide();
	    	$("div#divfileattach").empty();
	    	$("img#previewImg").hide();

    	
	    });

	  
	    
	    $("#tblProdInput > tbody > tr:nth-child(2) > td:nth-child(2) > input").blur((e) => {
	    	
	    	const title = $(e.target).val().trim();
			if(title == "") {
				// 입력하지 않거나 공백만 입력했을 경우 
				
			    $("#tblProdInput :input").prop("disabled", true);
				$(e.target).prop("disabled", false); 

			    $(e.target).parent().find("span.error").show();
			     	
				$(e.target).val("").focus(); 
			}
			else {
				// 공백이 아닌 글자를 입력했을 경우
			  $("#tblProdInput :input").prop("disabled", false);
	
			    $(e.target).parent().find("span.error").hide();
			}
	    	
	    	
	    });	 	
	    	
	    
			$("#tblProdInput > tbody > tr:nth-child(3) > td:nth-child(2) > input").blur((e) => {
	    	
	    	const imgtitle = $(e.target).val().trim();
			if(imgtitle == "") {
				// 입력하지 않거나 공백만 입력했을 경우 
				
			    $("#tblProdInput :input").prop("disabled", true);
				$(e.target).prop("disabled", false); 

			    $(e.target).parent().find("span.error").show();
			     	
				$(e.target).val("").focus(); 
			}
			else {
				// 공백이 아닌 글자를 입력했을 경우
			  $("#tblProdInput :input").prop("disabled", false);
	
			    $(e.target).parent().find("span.error").hide();
			}
	    	
	    	
	    });	 
	    
		
	    $("#spinnerPqty").blur( (e) => {
   	        
   	    	
 	       const reg_pqty = new RegExp(/^(?:[1-9]|[1-9][0-9]|100)$/);  
 	       //1~100개 까지만 들어오게 하는 정규표현식
 	       
 	       const bool = reg_pqty.test($(e.target).val());   
 	      
 	      if(!bool) {
 	    	 $("#tblProdInput :input").prop("disabled", true);
 	         $(e.target).prop("disabled", false); 
 	          $(e.target).parent().find("span.error").show(); 
 	         $(e.target).val("").focus(); 
 	      }
 	      else {
 	    	 $("#tblProdInput :input").prop("disabled", false);
 	          $(e.target).parent().find("span.error").hide();
 	      }

 	   });	
	    
	    
	    
	    $("#tblProdInput > tbody > tr:nth-child(5) > td:nth-child(2) > input").blur( (e) => {
	        
	    	
	    	       const reg_price = new RegExp(/^(1|[1-9]\d{0,5}|999999)$/);  
	    	       //1~999999까지만 들어오게 했다.
	    	       
	    	       const bool = reg_price.test($(e.target).val());   
	    	      
	    	      if(!bool) {
	    	        
	    	    	  $("#tblProdInput :input").prop("disabled", true);
	    	         $(e.target).prop("disabled", false); 
	    	         
	    	         
	    	          $(e.target).parent().find("span.error").show();
	    	              
	    	         $(e.target).val("").focus(); 
	    	      }
	    	      else {
	    	    	  $("#tblProdInput :input").prop("disabled", false);
	    	          $(e.target).parent().find("span.error").hide();
	    	      }
	    	      
	    	   });// 아이디가 postcode 인 것은 포커스를 잃어버렸을 경우(blur) 이벤트를 처리해주는 것이다
	    	   
	   	if ($("#tblProdInput > tbody > tr:nth-child(6) > td:nth-child(2) > textarea").val().trim() == "") {
	       	    $("#tblProdInput > tbody > tr:nth-child(6) > td:nth-child(2) > textarea").val("");
	   	    }
	   	    
	    if ($("#tblProdInput > tbody > tr:nth-child(7) > td:nth-child(2) > input").val().trim() == "") {
	       	    $("#tblProdInput > tbody > tr:nth-child(7) > td:nth-child(2) > input").val("");
	       	}
	
	 	
	    if ($("#tblProdInput > tbody > tr:nth-child(1) > td:nth-child(2) > select > option:nth-child(2)" ).val() === "스페셜") {
	    	parseInt($("#tblProdInput > tbody > tr:nth-child(1) > td:nth-child(2) > select > option:nth-child(2)").val("1"));
		   }
	    if ($("#tblProdInput > tbody > tr:nth-child(1) > td:nth-child(2) > select > option:nth-child(3)" ).val() === "신제품") {
	    	parseInt($("#tblProdInput > tbody > tr:nth-child(1) > td:nth-child(2) > select > option:nth-child(3)").val("2"));
		   } 		
	     if ($("#tblProdInput > tbody > tr:nth-child(1) > td:nth-child(2) > select > option:nth-child(4)" ).val() === "프리미엄") {
	    	parseInt($("#tblProdInput > tbody > tr:nth-child(1) > td:nth-child(2) > select > option:nth-child(4)").val("3"));
		   } 		
	    if ($("#tblProdInput > tbody > tr:nth-child(1) > td:nth-child(2) > select > option:nth-child(5)" ).val() === "와퍼") {
	    	parseInt($("#tblProdInput > tbody > tr:nth-child(1) > td:nth-child(2) > select > option:nth-child(5)").val("4"));
		   } 		
	    if ($("#tblProdInput > tbody > tr:nth-child(1) > td:nth-child(2) > select > option:nth-child(6)" ).val() === "치킨버거") {
	    	parseInt($("#tblProdInput > tbody > tr:nth-child(1) > td:nth-child(2) > select > option:nth-child(6)").val("5"));
		   } 		
	     if ($("#tblProdInput > tbody > tr:nth-child(1) > td:nth-child(2) > select > option:nth-child(7)" ).val() === "사이드") {
	    	parseInt($("#tblProdInput > tbody > tr:nth-child(1) > td:nth-child(2) > select > option:nth-child(7)").val("6"));
		   } 			
	     if ($("#tblProdInput > tbody > tr:nth-child(1) > td:nth-child(2) > select > option:nth-child(8)" ).val() === "음료") {
	    	parseInt($("#tblProdInput > tbody > tr:nth-child(1) > td:nth-child(2) > select > option:nth-child(8)").val("7"));
		   } 			
	    		
    		
	   
});
</script>

<div align="center" style="margin-bottom: 20px;">

   <div style="border: solid green 2px; width: 250px; margin-top: 20px; padding-top: 10px; padding-bottom: 10px; border-left: hidden; border-right: hidden;">       
      <span style="font-size: 24pt;">제품등록&nbsp; <br>[관리자전용]</span>   
   </div>
   <br/>
   
   <%-- !!!!! ==== 중요 ==== !!!!! --%>
   <%-- 폼에서 파일을 업로드 하려면 반드시 method 는 POST 이어야 하고 
     enctype="multipart/form-data" 으로 지정해주어야 한다.!! --%>
<form name="prodInputFrm"
      action="<%= ctxPath%>/productregister.bk"
      method="POST"                         
      enctype="multipart/form-data"> 
      
	<table id="tblProdInput" style="width: 100%; height: 1000px;">
	<tbody>
	   <tr>
	      <td width="25%" class="prodInputName" style="padding-top: 35px;">카테고리</td>
	      <td width="75%" align="left" style="padding-top: 10px;" >
	         <select name="fk_cnum" class="infoData">
	            <option value="" style="text-align:center;" >:::선택하세요:::</option>
	            <%-- 
	               <option value="1">전자제품</option>
	               <option value="2">의  류</option>
	               <option value="3">도  서</option>
	            --%> 
	            <c:forEach var="cvo" items="${requestScope.categorynum}">
	               <option value="${cvo.cname}"> ${cvo.cname}</option>
	            </c:forEach>
	            
	         </select>
	         <span class="error">필수입력</span>
	      </td>   
	   </tr>
	   <tr>
	      <td width="25%" class="prodInputName"  style="padding-top: 35px; padding-left: -20px;">제품명</td>
	      <td width="75%" align="left" style="border-top: hidden; border-bottom: hidden;" >
	         <input type="text" style="width: 300px;" name="pname" class="box infoData" />
	         <span class="error">필수입력</span>
	      </td>
	   </tr>
	
	      <td width="25%" class="prodInputName" style="padding-top: 35px;">제품이미지</td>
	      <td width="75%" align="left" style="border-top: hidden; border-bottom: hidden;">
	         <input type="file" name="pimage" class="infoData img_file" accept='image/*' /><span class="error">필수입력</span>
	      </td>
	   </tr>
	
	   <tr>
	      <td width="25%" class="prodInputName" style="padding-top: 35px;">제품수량</td>
	      <td width="75%" align="left" style="border-top: hidden; border-bottom: hidden;">
	              <input id="spinnerPqty" name="pqty" value="1" style="width: 60px; height: 50px;" maxlength="100"> 개
	         <span class="error">제품수량은 필수이며 1~100개까지만 가능합니다.</span>
	      </td>
	   </tr>

	   <tr>
	      <td width="25%" class="prodInputName" style="padding-top: 35px;">제품판매가</td>
	      <td width="75%" align="left" style="border-top: hidden; border-bottom: hidden;">
	         <input type="text" style="width: 170px; height: 60px;" name="price" class="box infoData" /> 원
	         <span class="error">제품판매가는 필수입력이며 1원부터 999999원까지만 가능합니다.</span>
	      </td>
	   </tr>
	 
	   <tr>
	      <td width="25%" class="prodInputName" style="padding-bottom: 60px;">제품설명</td>
	      <td width="75%" align="left" style="border-top: hidden; border-bottom: hidden;">
	         <textarea name="pcontent" rows="5" cols="60" style="position: relative; top:45px;"></textarea>
	      </td>
	   </tr>

	    <tr>
	      <td width="25%" class="prodInputName" style="padding-top: 30px; padding-right: 10px;">제품구성</td>
	      <td width="75%" align="left" style="border-top: hidden; border-bottom: hidden;">
	         <input type="text" style="width: 300px; height:50px;" name="pdetail" class="box" style="position:relative; top:50px;" /> 
	      </td>
	   </tr>
	
	    
	    <%-- ==== 이미지파일 미리보여주기 ==== --%>
	    <tr>
	          <td width="25%" class="prodInputName" style="padding-top: 35px;">이미지파일 <br>미리보기</td>
	          <td>
	             <img id="previewImg" width="300" />
	          </td>
	    </tr>
	   
	   <tr style="height: 70px;">
	      <td colspan="2" align="center" style="border-left: hidden; border-bottom: hidden; border-right: hidden; padding: 50px 0;">
	          <input type="button" value="제품등록" id="btnRegister" style="width: 190px; height:70px; font-size:22pt; border-radius:13pt; background-color:rgb(80,36,22);" class="btn  btn-lg mr-5" /> 
	          <input type="reset" value="취소" style="width: 190px; height:70px; font-size:22pt; border-radius:13pt; background-color:rgb(215,35,0);" class="btn btn-lg" />   
	      </td>
	   </tr>
	</tbody>
	</table>
</form>
		 
		 </div>

</body>
</html>