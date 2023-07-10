<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/include-taglib.jspf"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<style>
		.col-13 {
				white-space:nowrap;
				max-width:60%;
				overflow:hidden;
				text-overflow:ellipsis;
				font-size:12px;
				padding:0 0 0 0px;
				}
		 .h522{
				white-space:nowrap;
				max-width:60%;
				overflow:hidden;
				text-overflow:ellipsis;
				font-size:17px;
				} 
		input::placeholder{
        		font-size: 15px;
        		}		
</style>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/message.css">
</head>
<body>

<div class="msg-container">
   
      <div class="messaging">
         <div class="inbox_msg">
            <!-- 메세지 목록 영역 -->
           <div class="inbox_people">
             <div class="headind_srch">
               <div class="recent_heading">
                 <h4>메시지</h4>
               </div>
               <!-- 메세지 검색 -->
               <div class="srch_bar">
                 <div class="stylish-input-group">
                   <input type="text" class="search-bar" placeholder="검색어를 입력해주세요." id="search" onfocus="this.placeholder = ''" >
                   <span class="input-group-addon">
                   <button type="button" id="searchBtn"> <i class="fa fa-search" aria-hidden="true"></i> </button>
                   </span> 
                 </div>
               </div>
             </div>
             
             <!-- 메세지 리스트 -->
             <div class="inbox_chat">
             
<c:forEach var="list" items="${list}" varStatus="status">
   <div class="chat_list_box${list.CHAT_ROOM} chat_list_box">
      <div type="button" class="chat_list" room="${list.CHAT_ROOM}" chatRecvNum="${list.CHAT_RECV_NUM}" chatSendNum="${list.CHAT_SEND_NUM}" mem_del_gb="${nicknameList[status.index].MEM_DEL_GB}" mem_status="${nicknameList[status.index].MEM_STATUS}">
         <!-- active-chat -->
         <div class="chat_people">
            <div class="chat_img" >
               <a href="#">
                  <img class="img" src="<%=request.getContextPath() %>/image/기본 프사.png" alt="sunil" >
               </a>
            </div>
            <div class="chat_ib">
            	<div class="d-flex justify-content-between">
               		<div class="h522">${nicknameList[status.index].MEM_NICKNAME}</div><div class="chat_date" style="font-size:0.5em;">${list.CHAT_SEND_TIME}</div>
            	</div>
             	<div class="d-flex justify-content-between">
             		<div class="col-13">${list.CHAT_CONTENT}</div>
                 	<div class="col-2 unread${list.CHAT_ROOM}">
                     	<c:if test="${unreadList[status.index].UNREAD > 0}">
                        <div class="badge bg-danger" style="font-size:0.5em;">${unreadList[status.index].UNREAD}</div>
                    </c:if>
                 	</div>
                    
             </div>
               <div class="row">
                  
                  <%-- 만약 현재사용자가 안읽은 메세지 갯수가 0보다 클때만 badge를 표시한다. --%>
               </div>
            </div>
         </div>
      </div>
   </div>
</c:forEach>
 </div>
           </div>
           <!-- 메세지 내용 영역 -->
           <div class="mesgs">
             <!-- 메세지 내용 목록 -->
             <div class="msg_history">
               <!-- 메세지 내용이 올 자리 -->
             </div>
             <div class="send_message">
             </div>
             <!-- 메세지 입력란이 올자리 -->
             
           </div>
         </div>
       </div>
   </div>            

<script>
   // 가장 처음 메세지 리스트를 가져온다.
   const FirstMessageList = function(){
      $.ajax({
         url:"/ns/message/ajaxList",
         method:"get",
         data:{
         },
         success:function(data){
            console.log("메세지 리스트 리로드 성공");
            
            $('.inbox_chat').html(data);
            
            // 메세지 리스트중 하나를 클릭했을 때
            $('.chat_list').on('click', function(){
               
               let room = $(this).attr('room');
               let chatRecvNum = $(this).attr('chatRecvNum');
               let chatSendNum = $(this).attr('chatSendNum');
               let mem_del_gb = $(this).attr('mem_del_gb');
               let mem_status = $(this).attr('mem_status');
               
               
               // 선택한 메세지빼고 나머지는 active 효과 해제하기
               $('.chat_list_box').not('.chat_list_box.chat_list_box'+room).removeClass('active_chat');
               // 선택한 메세지만 active 효과 주기
               $('.chat_list_box'+room).addClass('active_chat');
               
               let send_msg = "";
              
               send_msg += "<div class='type_msg'>";
               send_msg += "   <div class='input_msg_write row'>";
               send_msg += "      <div class='col-11'>";
               if(mem_del_gb == "N" && mem_status == "N") {
               send_msg += "         <input type='text' id='write' maxlength='666' class='write_msg form-control' placeholder='메세지를 입력...'>";
               } else if(mem_del_gb == "Y" || mem_status == "Y") {
               send_msg += "         <input type='text'  id='write' placeholder='활동중지/탈퇴한 회원으로 메세지를 보낼 수 없습니다.' readonly='readonly' />";
               } 
               send_msg += "      </div>";
                 send_msg += "      <div class='col-1'>";
               if(mem_del_gb == "N" && mem_status == "N") {
               send_msg += "         <button class='msg_send_btn' type='button'><i class='fa fa-paper-plane-o' aria-hidden='true'></i></button>";
               }
               send_msg += "      </div>";
               send_msg += "   </div>";
               send_msg += "</div>"; 
               send_msg +="			<div class='textLengthWrap'>";
               send_msg += "			<span class='textCount'>0</span>";
               send_msg += "			<span class='textTotal'>/ 666자</span>";
               send_msg += "		</div>";
               
               // 메세지 입력, 전송 칸을 보인다.
               $('.send_message').html(send_msg);
               
               //글자수 제한
               validate();
            
                // 메세지 전송버튼을 눌렀을 때
                $('.msg_send_btn').on('click',function(){
                  // 메세지 전송 함수 호출
                  SendMessage(room, chatRecvNum, chatSendNum); 
                  
                  // 전송버튼을 누르면 메세지 리스트가 리로드 되면서 현재 열린 메세지의 선택됨 표시가 사라진다.
                  // 이걸 해결하기 위해 메세지 전송버튼을 누르고 메세지 리스트가 리로드되면 메세지 리스트의 첫번째 메세지(현재 열린 메세지)가 선택됨 표시 되도록 한다.
                  $('.chat_list_box:first').addClass('active_chat');
               }); 
               
               // 메세지 내용을 불러오는 함수 호출
               MessageContentList(room, mem_status, mem_del_gb);
            });
            
         }
      })
   };
   
   // 메세지 리스트를 다시 가져온다.
   const MessageList = function(){
      $.ajax({
         url:"/ns/message/ajaxList",
         method:"get",
         data:{
         },
         dataType:'text',
         success:function(data){
            console.log("메세지 리스트 다시 리로드 성공");
      
            $('.inbox_chat').html(data);
            
         	// 메세지 리스트중 하나를 클릭했을 때
            $('.chat_list').on('click', function(){
               
               let room = $(this).attr('room');
               let chatRecvNum = $(this).attr('chatRecvNum');
               let chatSendNum = $(this).attr('chatSendNum');
               let mem_del_gb = $(this).attr('mem_del_gb');
               let mem_status = $(this).attr('mem_status');
               
               // 선택한 메세지빼고 나머지는 active 효과 해제하기
               $('.chat_list_box').not('.chat_list_box.chat_list_box'+room).removeClass('active_chat');
               // 선택한 메세지만 active 효과 주기
               $('.chat_list_box'+room).addClass('active_chat');
               
               
               let send_msg = "";
               
               send_msg += "<div class='type_msg'>";
               send_msg += "   <div class='input_msg_write row'>";
               send_msg += "      <div class='col-11'>";
               if(mem_del_gb == "N" && mem_status == "N") {
               send_msg += "         <input type='text' id='write' maxlength='666' class='write_msg form-control' placeholder='메세지를 입력...'>";
               } else if(mem_del_gb == "Y" || mem_status == "Y") {
               send_msg += "         <input type='text'  id='write' placeholder='활동중지/탈퇴한 회원으로 메세지를 보낼 수 없습니다.' readonly='readonly' />";
               } 
               send_msg += "      </div>";
                 send_msg += "      <div class='col-1'>";
               if(mem_del_gb == "N" && mem_status == "N") {
               send_msg += "         <button class='msg_send_btn' type='button'><i class='fa fa-paper-plane-o' aria-hidden='true'></i></button>";
               }
               send_msg += "      </div>";
               send_msg += "   </div>";
               send_msg += "</div>"; 
               send_msg +="			<div class='textLengthWrap'>";
               send_msg += "			<span class='textCount'>0</span>";
               send_msg += "			<span class='textTotal'>/ 666자</span>";
               send_msg += "		</div>"; 
               
               // 메세지 입력, 전송 칸을 보인다.
               $('.send_message').html(send_msg);
               
               //글자수 제한
              validate();
               
               // 메세지 전송버튼을 눌렀을 때
               $('.msg_send_btn').on('click',function(){
                  // 메세지 전송 함수 호출
                  SendMessage(room, chatRecvNum, chatSendNum, mem_status, mem_del_gb);
                  
                  // 전송버튼을 누르면 메세지 리스트가 리로드 되면서 현재 열린 메세지의 선택됨 표시가 사라진다.
                  // 이걸 해결하기 위해 메세지 전송버튼을 누르고 메세지 리스트가 리로드되면 메세지 리스트의 첫번째 메세지(현재 열린 메세지)가 선택됨 표시 되도록 한다.
                  $('.chat_list_box:first').addClass('active_chat');
               });
               
               // 메세지 내용을 불러오는 함수 호출
               MessageContentList(room, mem_status, mem_del_gb);
            });
            
            // 전송버튼을 누르면 메세지 리스트가 리로드 되면서 현재 열린 메세지의 선택됨 표시가 사라진다.
            // 이걸 해결하기 위해 메세지 전송버튼을 누르고 메세지 리스트가 리로드되면 메세지 리스트의 첫번째 메세지(현재 열린 메세지)가 선택됨 표시 되도록 한다.
            $('.chat_list_box:first').addClass('active_chat');
         }
      })
   };
    
    
   // 메세지 내용을 가져온다.
   // 읽지 않은 메세지들을 읽음으로 바꾼다.
   const MessageContentList = function(room, mem_status, mem_del_gb) {
      
      $.ajax({
         url:"/ns/message/contentList",
         method:"GET",
         data:{
            room : room,
            MEM_STATUS : mem_status,
            MEM_DEL_GB : mem_del_gb
         },
         success:function(data){
            console.log("메세지 내용 가져오기 성공" + '${CHAT_RECV_NUM}');
            
            // 메세지 내용을 html에 넣는다
            $('.msg_history').html(data);
           
            // 이 함수로 메세지 내용을 가져올때마다 스크롤를 맨아래로 가게 한다.
            $(".msg_history").scrollTop($(".msg_history")[0].scrollHeight);
          
         },
         error : function() {
            alert('서버 에러');
         }
      })
      
      $('.unread'+room).empty();
   };
   
   
  //메세지를 전송하는 함수
   const SendMessage = function(room, chatRecvNum, chatSendNum, mem_status, mem_del_gb){
      
      let content = $('.write_msg').val();
      
      content = content.trim();
      
      if(content == ""){
         alert("메세지를 입력하세요!");
         return false;
      }else{
         var formData = new FormData();
          formData.append("CHAT_ROOM", room);
          formData.append("CHAT_CONTENT", content);
          if(chatRecvNum != null) {
            formData.append("CHAT_RECV_NUM", chatRecvNum);
            }
            if(chatSendNum != null) {
            formData.append("CHAT_SEND_NUM", chatSendNum);
            }
         } 
         
         $.ajax({
            url:"/ns/message/messageSendInlist",
            method:"POST",
            data:formData,
            processData:false,
            contentType:false,
            success:function(data){
            console.log("메세지 전송 성공");
               
               //메세지 입력칸 비우기
               document.getElementById("write").value='';
               //메세지 내용  리로드
               MessageContentList(room, mem_status, mem_del_gb);
               
               //검색 입력칸 비우기
               document.getElementById("search").value='';
               
               //메세지 리스트 리로드
               MessageList();
            },
             error : function() {
               alert('서버 에러');
            }
         });
   }; 
   
   $("#searchBtn").on("click", function(e) {
      e.preventDefault();
      Search();
   });
   
   const Search = function(){
      let keyword = $('.search-bar').val();

      if(keyword==""){
         alert("검색어를 입력해주세요.");
      }else{
         var formData = new FormData();
         formData.append("keyword", keyword);
      }

      $.ajax({
         url:"/ns/message/ajaxList",
         type:"POST",
         data:formData,
         processData:false,
         contentType:false,
         success:function(data){
            console.log("검색어 전송 성공:" + keyword);
            //검색 입력칸 비우기
            //document.getElementById("search").value='';
            
               //메세지 리스트 리로드
            $('.inbox_chat').html(data);
               
               // 메세지 리스트중 하나를 클릭했을 때
               $('.chat_list').on('click', function(){
                  
                  let room = $(this).attr('room');
                  let chatRecvNum = $(this).attr('chatRecvNum');
                  let chatSendNum = $(this).attr('chatSendNum');
                  let mem_del_gb = $(this).attr('mem_del_gb');
                  let mem_status = $(this).attr('mem_status');
                  
                  // 선택한 메세지빼고 나머지는 active 효과 해제하기
                  $('.chat_list_box').not('.chat_list_box.chat_list_box'+room).removeClass('active_chat');
                  // 선택한 메세지만 active 효과 주기
                  $('.chat_list_box'+room).addClass('active_chat');
                  
                  let send_msg = "";
                  send_msg += "<div class='type_msg'>";
                  send_msg += "   <div class='input_msg_write row'>";
                  send_msg += "      <div class='col-11'>";
                  if(mem_del_gb == "N" && mem_status == "N") {
                  send_msg += "         <input type='text' id='write' maxlength='666' class='write_msg form-control' placeholder='메세지를 입력...'>";
                  } else if(mem_del_gb == "Y" || mem_status == "Y") {
                  send_msg += "         <input type='text'  id='write' placeholder='활동중지/탈퇴한 회원으로 메세지를 보낼 수 없습니다.' readonly='readonly' />";
                  } 
                  send_msg += "      </div>";
                    send_msg += "      <div class='col-1'>";
                  if(mem_del_gb == "N" && mem_status == "N") {
                  send_msg += "         <button class='msg_send_btn' type='button'><i class='fa fa-paper-plane-o' aria-hidden='true'></i></button>";
                  }
                  send_msg += "      </div>";
                  send_msg += "   </div>";
                  send_msg += "</div>"; 
                  send_msg +="			<div class='textLengthWrap'>";
                  send_msg += "			<span class='textCount'>0</span>";
                  send_msg += "			<span class='textTotal'>/ 666자</span>";
                  send_msg += "		</div>";
                      
                  // 메세지 입력, 전송 칸을 보인다.
                  $('.send_message').html(send_msg);
                  
                  //글자수 제한
                  validate(); 
                  
                  // 메세지 전송버튼을 눌렀을 때
                   $('.msg_send_btn').on('click',function(){
                     // 메세지 전송 함수 호출
                     SendMessage(room, chatRecvNum, chatSendNum); 
                     
                     // 전송버튼을 누르면 메세지 리스트가 리로드 되면서 현재 열린 메세지의 선택됨 표시가 사라진다.
                     // 이걸 해결하기 위해 메세지 전송버튼을 누르고 메세지 리스트가 리로드되면 메세지 리스트의 첫번째 메세지(현재 열린 메세지)가 선택됨 표시 되도록 한다.
                     $('.chat_list_box:first').addClass('active_chat');
                  });
                  
                  // 메세지 내용을 불러오는 함수 호출
                  MessageContentList(room, mem_status, mem_del_gb);
               });
               
               // 전송버튼을 누르면 메세지 리스트가 리로드 되면서 현재 열린 메세지의 선택됨 표시가 사라진다.
               // 이걸 해결하기 위해 메세지 전송버튼을 누르고 메세지 리스트가 리로드되면 메세지 리스트의 첫번째 메세지(현재 열린 메세지)가 선택됨 표시 되도록 한다.
               $('.chat_list_box:first').addClass('active_chat');   
         },
         error:function(){
            alert('server error');
         }
      });
    
   };
  
   const validate = function() {
   $('#write').keyup(function () {
  	  var inputLength = $(this).val().length;
  	   
  	  $('.textCount').html(inputLength);
  	  
  	  if(inputLength > 666) {
  		  alert("내용을 최대 666글자로 작성해주세요.");
  		  $('#write').focus();
  	      return false;
  	  }
     });
   };
   
    $(document).ready(function(){
      // 메세지 리스트 리로드
      FirstMessageList();
	}); 

   	
</script>
</body>
</html>