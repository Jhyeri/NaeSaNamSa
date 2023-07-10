<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/include-taglib.jspf" %>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
  </head>
  <body>
    <div class="container text-center pt-5 pb-5">
    
      <div class="row text-center mb-3">
        <h2 class="fs-1 fw-semibold">
          회원 탈퇴
        </h2>
      </div>

      <!-- 공지글 -->
      <div class="row justify-content-center mb-2">
        <div style="width:350px;">
          <h2 class="fs-5 text-start">
            #탈퇴하실 경우, 계정 정보 및 내 상점 정보를 복구할 수 없으며, 7일간
            재가입 하실 수 없습니다.
          </h2>
        </div>
      </div>
      <!-- 공지글 끝 -->

      <!-- 공지링크 -->
      <div class="row justify-content-center mb-2">
        <div class="text-end" style="width:350px;">
          <a href="/ns/help/noticeDetail?NOTICE_NUM=1" class="link-primary fs-5">공지사항 확인하기</a>
        </div>
      </div>
      <!-- 공지링크 끝 -->

      <!-- 체크박스 -->
      <form id="accountDeleteForm" name="accountDeleteForm" class="needs-validation" novalidate>
      <div class="row justify-content-center mb-4">
        <div class="text-start" style="width:350px;">
          <div class="form-check">
            <input class="form-check-input" type="checkbox" id="gridCheck" required />
            <label class="form-check-label" for="gridCheck">
              내용 숙지하였으며, 동의합니다.
            </label>
          </div>
        </div>
      </div>
      <!-- 체크박스 끝  -->

	<c:if test="${noPw == null }">
      <!-- 비밀번호 입력글 -->
      <div class="row justify-content-center mb-2">
        <div class="text-start" style="width:350px;">
          <h2 class="fs-5">
            비밀번호를 입력해주세요.
          </h2>
        </div>
      </div>
      <!-- 비밀번호 입력글 끝 -->

      <!-- 비밀번호 입력 창 -->
      <div class="row justify-content-center mb-3">
        <div class="text-start" style="width:350px;">
          <input
            type="password"
            class="form-control"
            id="MEM_PW"
            name="MEM_PW"
            placeholder="비밀번호"
            required
          />
        </div>
      </div>
     </c:if>
      </form>
      <!-- 비밀번호 입력 창 끝-->

      <!-- 비밀번호 확인 버튼 돌아가기 버튼 -->
      <div class="row justify-content-center">
        <div class="text-end" style="width:350px;">
          <button type="button" class="btn btn-secondary" name="accountDelete">확인</button>
          <button type="button" class="btn btn-secondary" name="myPageMain">돌아가기</button>
        </div>
      </div>
      <!-- 비밀번호 확인 버튼 돌아가기 버튼 끝 -->
      
    </div>

    
<script type="text/javascript">

$(document).ready(function() {
	
	$("#accountDeleteForm").keydown(function (event)
		    {
		        if (event.keyCode == '13') {
		            if (window.event)
		            {
		                event.preventDefault();
		                return;
		            }
		        }
	});
	
	$("button[name='myPageMain']").on("click", function(e) {  // 마이페이지 메인으로
		e.preventDefault();
		fn_myPageMain();
	});
	
	
	function fn_pwCheck() { //탈퇴 전 비밀번호 확인
		var formData = new FormData(document.getElementById("accountDeleteForm"));
		
		$.ajax({
			url:"<c:url value='/myPage/accountDeletePw'/>",
			type:'post',
			processData: false,
			contentType: false,
			data:formData,
			success:function(data){
				if(data=="success") { //비밀번호 일치할 때	
					fn_deleteAccount(); //삭제 처리 함수 호출
					//alert("회원탈퇴가 완료되었습니다.");
					//FlashAttribute의 값(회원 탈퇴 성공여부)이 오기전에 띄우는 창이라 순서가 맞지 않음
				} else if (data=="fail") {
					alert("비밀번호가 일치하지 않습니다.");
				}
			},
			error:function(){
				alert("에러입니다.");
			}
		})
	} //fn_pwCheck() end
	
	 function fn_deleteAccount() { //탈퇴처리
		
		$.ajax({
			url:"<c:url value='/myPage/accountDelete'/>",
			type:'post',
			success:function(data){
				if(data=='1'){
					alert("회원탈퇴가 완료되었습니다.");
					goMain();
				} else if(data=='0'){
					alert("탈퇴실패");
					location.href="/myPage/accountDeleteForm";
					//혹은 페이지만 새로고침
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
	
	function goMain() {
		location.href="/ns/main"; //탈퇴 -> 로그아웃 -> 메인으로 이동
	};
	
	(() => {
		  'use strict'

		  // Fetch all the forms we want to apply custom Bootstrap validation styles to
		  const form = document.querySelector('.needs-validation');

		  // Loop over them and prevent submission
		  $("button[name='accountDelete']").on("click", function(e) { 
			e.preventDefault();
			form.classList.add('was-validated')
			
			if (!form.checkValidity()) {
		        event.preventDefault();
		        event.stopPropagation();
		        return false;
		    }
			<c:choose>
			<c:when test="${noPw == null }">
			fn_pwCheck();
			</c:when>
			<c:when test="${noPw != null}">
			fn_deleteAccount()
			</c:when>
			</c:choose>
			});

		})()
	
});
</script>
</body>
</html>
