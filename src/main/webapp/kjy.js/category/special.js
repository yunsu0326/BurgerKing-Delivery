
$(document).ready(function(){
	
	// index 메뉴 리스트에 input 태그(타입 히든)으로 카테고리 할당 값을 둠
	// 메뉴 리스트에서 클릭을 했을 때 input 태그의 값을 let 변수에 저장, 페이지가 로딩되었을때
	// let 에 저장된 값의 카테고리가 뜨게끔
	
	let cnum = Number("${requestScope.cnum}");
	
	menufunction(cnum);
	
	$.ajax({
      url:"menuCategoryListJSON.bk",
      dataType:"json",
      success:function(json){
          let v_html = ``;
          
          if(json.length == 0) {
            v_html = `현재 카테고리 준비중 입니다...`;
            $("div#categoryList").html(v_html);
         }
         
         else if(json.length > 0) {
            // 데이터가 존재하는 경우
            
         /*   
            // 자바스크립트 사용하는 경우
            json.forEach(function(item, index, array){
               
            });
            
            // jQuery 를 사용하는 경우
            $.each(json, function(index, item){
               
            });
         */    
	//	console.log("~~~확인용 json =>",JSON.stringify(json));
         /*
            ~~~ 확인용 json => [{"code":"100000","cname":"전자제품","cnum":1}
                              ,{"code":"200000","cname":"의류","cnum":2}
                              ,{"code":"300000","cname":"도서","cnum":3}]    
         */ 
		
		
                          
            $.each(json, function(index, item){
               
                 v_html += `<a href="javascript:menufunction(${item.cnum})">${item.cname}</a>&nbsp;&nbsp;`;  
                           
            });// end of $.each(json, function(index, item)-----
            
              
			// 카테고리 출력하기
			$("div#categoryList").html(v_html);
            
             
         }// end of else if(json.length > 0)----------------
                  
      },
      error: function(request, status, error){
         alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
      }      
   });
                          	 
                          	 
});

//function declaration

function menufunction(cnum) {
    // 'cnum' 값을 사용하여 원하는 작업을 수행

    // 예: cnum 값을 사용하여 추가적인 AJAX 요청 수행
    $.ajax({
        url: "indexMenuListJSON.bk",
        data: { "cnum": cnum },
        dataType: "json",
        success: function(json) {
            
          let v_html = ``;
          
          if(json.length == 0) {
            v_html = `현재 카테고리 준비중 입니다...`;
            $("div#menuList").html(v_html);
         }
         
         else if(json.length > 0) {
            // 데이터가 존재하는 경우
            
         /*   
            // 자바스크립트 사용하는 경우
            json.forEach(function(item, index, array){
               
            });
            
            // jQuery 를 사용하는 경우
            $.each(json, function(index, item){
               
            });
         */    
	//	console.log("~~~확인용 json =>",JSON.stringify(json));
         /*
            ~~~ 확인용 json => [{"code":"100000","cname":"전자제품","cnum":1}
                              ,{"code":"200000","cname":"의류","cnum":2}
                              ,{"code":"300000","cname":"도서","cnum":3}]    
         */ 
		
			v_html += `<div class="row text-center">`;
                          
            $.each(json, function(index, item){
                 v_html += `<div class='col-lg-3'>
		                     <div class="card mb-3">
		                        <img src='/tempSemi/image/${item.pimage}' class='card-img-top' style='width: 100%'/>
		                        <div class='card-body' style='padding: 0; font-size: 9pt;'>
		                          <ul class='list-unstyled mt-2 text-center'> 
		                               <li>${item.pname}</li> 
		                               <li class='text-center'><a href='/tempSemi/category/prodDetail.bk?pnum=${item.pnum}' class='stretched-link' role='button'></a></li> 
		                          </ul>
		                        </div>
		                      </div>
		                    </div>`;  
                           
            });// end of $.each(json, function(index, item)-----
            	
            	v_html += `</div>`;
              
			// 카테고리 출력하기
			$("div#menuList").html(v_html);
            
             
         }// end of else if(json.length > 0)----------------
            
        },
        error: function(request, status, error){
         alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
      }      
    });
}