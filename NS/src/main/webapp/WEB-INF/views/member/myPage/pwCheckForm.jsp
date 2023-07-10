<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/include-taglib.jspf" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
</head>
<body>

<div class="container text-start pt-5 pb-5">
	<div class="row text-center pt-5 pb-5">
        	<h2 class="fs-1 fw-semibold">마이 페이지</h2>
    </div>
    
    <div class="row text-center mb-2">
  	 	 <h2 class="fs-5">비밀번호를 입력해주세요.</h2>
    	
    </div>
    
    <!-- 비밀번호 입력 줄 -->
    <div class="row mt-2 pb-5 justify-content-center">
    	<form id="pwCheckForm" method="POST" class="d-flex justify-content-center needs-validation" novalidate>
    	<label for="inputPassword3" class="col-auto col-form-label text-end">Password</label>
	    <div class="col-auto">
	      <input type="password" class="form-control" id="MEM_PW" name="MEM_PW" required>
		      <div class="invalid-feedback">
			      비밀번호를 입력해주세요
			  </div>
	    </div>
	    <div class="col-auto">
	    	<button type="submit" class="btn btn-primary" name="pwCheck">확인</button>
	  	</div>
	  	</form>
   </div>
</div>
<!-- 비밀번호 입력 줄 끝-->
 
<script type="text/javascript">
$(document).ready(function() {
	history.replaceState({}, null, location.pathname);
});
	
	function fn_pwCheck() {
		
		var inputPw = $("#MEM_PW").val();
		var jsonPw = {"MEM_PW":inputPw};
			
		$.ajax({
			url:"<c:url value='/myPage/pwCheck'/>",
			type:'post',
			contentType : "application/json; charset=utf-8",
			data : JSON.stringify(jsonPw),
			success:function(data){
				if(data.result == "success"){
					fn_myPageMain();
				}else if(data.result == "fail"){
					alert("비밀번호가 틀렸습니다.");
				}
			},
			error:function(){
				alert("에러입니다.");
			}
			
		})
	};
	
	function fn_myPageMain() {
		var comSubmit = new ComSubmit();
		comSubmit.setUrl("/ns/myPage/main");
		comSubmit.submit();
	};

	//유효성 검증
	// Example starter JavaScript for disabling form submissions if there are invalid fields
	(() => {
		  'use strict'
		
		  // Fetch all the forms we want to apply custom Bootstrap validation styles to
		  const forms = document.querySelectorAll('.needs-validation')
		
		  // Loop over them and prevent submission
		  Array.from(forms).forEach(form => {
		    form.addEventListener('submit', event => {
		    	form.classList.add('was-validated');
		    	event.preventDefault();
		        event.stopPropagation();
		      if (!form.checkValidity()) {
		        return false;
		      }
		      fn_pwCheck();
		      
		    }, false)
		  })
		})()
	
	
</script>

</body>
</html>