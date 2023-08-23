# :pushpin: 내사남사
>중고거래 플랫폼 사이트

</br>

## 1. 제작 기간 & 참여 인원
- 2022년 12월 12일 ~ 2023년 1월 30일
- 팀 프로젝트 (6인) 

</br>

## 2. 프로젝트에 사용된 기술
#### `Back-end`
  - Java (JDK 1.8)
  - Spring Framework 4.0.4
  - JSP
  - Oracle 11g
  - MyBatis 3.0

#### `Front-end`
  - HTML5
  - CSS3
  - JavaScript
  - Ajax
  - jQuery
  - bootstrap

#### `WAS`
  - Apache Tomcat 9.0

#### `API & 라이브러리`
  - 네이버 로그인 API
  - 카카오 로그인 API
  - 카카오페이 API
  - JavaMail 라이브러리
  - RESTful API(JSON)

#### `IDE`
  - Spring Tool Suite 3
  - SQL Developer 
</br>

## 3. ERD 설계
<img width="60%" src="https://github.com/Jhyeri/NaeSaNamSa/assets/111175466/fa89c85b-4296-4af5-a608-7b2dc4a9e7e1"/>
<br/>
<br/>

## 4. 프로젝트의 핵심 기능  
- 상품에 대한 CRUD가 주기능을 이루고 있습니다.
- 회원가입 시 이메일 인증, 그리고 사용자간의 메시지 기능 또한 제공합니다.

<details>
<summary><b>핵심 기능 설명 펼치기</b></summary>
<div markdown="1">


### 4.1. 카카오페이 결제
  - 카카오페이 API를 통해 간편한 결제기능을 제공합니다.  :pushpin: [코드 확인](https://github.com/Jhyeri/NaeSaNamSa/blob/main/NS/src/main/java/ns/kakaopay/controller/KakaoPayController.java)



### 4.2. 메시지
  - 판매자와 구매자가 원활하게 소통하도록 메시지 기능을 제공합니다.

    <details>
    <summary><b>:pushpin: 코드 확인</b></summary>
    <div markdown="1">
      
    - [Controller 코드 확인](https://github.com/Jhyeri/NaeSaNamSa/blob/main/NS/src/main/java/ns/message/controller/MessageController.java)<br/>
    - [JSP 코드 확인](https://github.com/Jhyeri/NaeSaNamSa/blob/main/NS/src/main/webapp/WEB-INF/views/message/messageList.jsp)  
    </div>
    </details>


### 4.3. 이메일 인증 
  - JavaMail 라이브러리를 사용하여 회원가입 시 이메일 인증이 이루어집니다.<br/>
  
    <details>
    <summary><b>:pushpin: 코드 확인</b></summary>
    <div markdown="1">
      
    - [MailHandler 코드 확인](https://github.com/Jhyeri/NaeSaNamSa/blob/main/NS/src/main/java/ns/common/common/MailHandler.java)<br/>
    - [Service 코드 확인](https://github.com/Jhyeri/NaeSaNamSa/blob/main/NS/src/main/java/ns/member/service/MailServiceImpl.java)  
    </div>
    </details>

    
### 4.4. 파일 업로드
  - **다중 이미지 업로드** 
    - 상품 등록, 수정 시 다중 이미지 업로드가 가능합니다.
  - **썸네일**
    - 업로드 전 썸네일을 통해 첨부한 이미지를 미리 확인할 수 있습니다.
  - **이미지 수정** 
    - 이미지 수정이 간편하도록 하였습니다. <br/>

    <br/>
    
    <details>
    <summary><b>:pushpin: 코드 확인</b></summary>
    <div markdown="1">
      
    - [Controller 코드 확인](https://github.com/Jhyeri/NaeSaNamSa/blob/main/NS/src/main/java/ns/shop/controller/ShopController.java)<br/>
    - [Service 코드 확인](https://github.com/Jhyeri/NaeSaNamSa/blob/main/NS/src/main/java/ns/shop/service/ShopServiceImpl.java)  
  
    </div>
    </details>
    <br/>
    
<!-- ### 4.5. 판매자 추천, 후기 작성 
  - 판매자 상세보기 페이지에서 판매자 추천기능과 후기 작성 기능을 제공합니다. :pushpin: [코드 확인](https://github.com/Jhyeri/NaeSaNamSa/blob/main/NS/src/main/java/ns/seller/controller/SellerController.java) -->

</div>
</details>

</br>

## 5. 담당 기능
<details>
<summary><b>담당 기능 설명 펼치기</b></summary>
<div markdown="1">

### 5.1. 회원가입  
  - **닉네임 중복확인** 📌[코드 확인](https://github.com/Jhyeri/NaeSaNamSa/blob/main/NS/src/main/java/ns/member/controller/JoinController.java#L39)<br/>
    - ajax 요청을 통해 데이터베이스로부터 사용자가 입력한 것과 동일한 닉네임을 불러옵니다.
    - 데이터가 존재하면 "fail", 존재하지 않으면 "success" 문자열을 리턴합니다.
  
  - **회원가입 가능 여부 체크** 📌[코드 확인](https://github.com/Jhyeri/NaeSaNamSa/blob/main/NS/src/main/java/ns/member/controller/JoinController.java#L78)<br/>
    - 사용자가 입력한 정보를 통해 재가입/신규가입 여부를 확인합니다.
    - 재가입일 경우, 탈퇴 후 7일이 경과하였는지 확인하고 가입을 진행합니다.

### 5.2. 로그인 📌[코드 확인](https://github.com/Jhyeri/NaeSaNamSa/blob/main/NS/src/main/java/ns/member/controller/LoginController.java#L48)<br/>
  - ajax 요청을 통해 사용자가 입력한 이메일을 DB로부터 조회합니다.
  - 존재하지 않는 이메일이면 "emailfail", 비밀번호가 일치하지 않으면 "pwfail", 정지된 회원이면 "suspended" 문자열을 리턴합니다.<br/>
  
### 5.2.1. 로그인 유지
 - **최초 이용 시** 📌[코드 확인](https://github.com/Jhyeri/NaeSaNamSa/blob/main/NS/src/main/java/ns/member/controller/LoginController.java#L77)<br/>
   - 이메일과 비밀번호로 쿠키를 생성하고 response영역에 추가합니다.<br/>
 - **사이트 접속 시** 📌[코드 확인](https://github.com/Jhyeri/NaeSaNamSa/blob/main/NS/src/main/java/ns/common/interceptor/CookieInterceptor.java)<br/>
   - 인터셉터를 사용하여 접속 직후 바로 로그인 처리합니다.<br/>

### 5.2.2. 카카오 연동 로그인
  - **로그인 요청** 📌[코드 확인](https://github.com/Jhyeri/NaeSaNamSa/blob/main/NS/src/main/webapp/WEB-INF/views/member/login/loginSelect.jsp#L26)<br/>
    - 카카오 서버로 요청 후, 사용자가 카카오 계정으로 로그인 합니다.<br/>
    - 로그인 성공 후, 카카오 서버로부터 인가코드를 전송 받습니다.<br/>
  - **토큰 발급** 📌[코드 확인](https://github.com/Jhyeri/NaeSaNamSa/blob/main/NS/src/main/java/ns/member/controller/KakaoController.java#L42)<br/>
    - POST방식으로 인가 코드 및 필수 파라미터를 전송 후, 카카오 서버로부터 액세스 토큰을 발급 받습니다.<br/>
  - **회원가입** 📌[코드 확인](https://github.com/Jhyeri/NaeSaNamSa/blob/main/NS/src/main/java/ns/member/controller/KakaoController.java#L120)<br/>
    - GET방식으로 요청 헤더에 액세스 토큰을 전달하여 API를 호출하고, 사용자 정보를 받습니다.<br/>
    - 받은 사용자 정보를 활용하여 추가적인 회원가입을 진행하고 로그인 처리를 합니다.<br/>

### 5.2.3. 네이버 연동 로그인
   - **로그인 요청** 📌[코드 확인](https://github.com/Jhyeri/NaeSaNamSa/blob/main/NS/src/main/webapp/WEB-INF/views/member/login/loginSelect.jsp#L66)<br/>
     - GET방식으로 필수 파라미터를 카카오 서버에 전송하여 요청합니다.<br/>
     - 사용자가 네이버 계정으로 로그인합니다.<br/>
     - 로그인 성공 후, 네이버 서버로부터 인증코드를 전송 받습니다.<br/>
  - **토큰 발급** 📌[코드 확인](https://github.com/Jhyeri/NaeSaNamSa/blob/main/NS/src/main/java/ns/member/controller/NaverController.java#L41)<br/>
     - 받은 인증코드와 필수 파라미터를 GET방식으로 전송 후, 네이버 서버로부터 접근 토큰을 발급 받습니다.<br/>
  - **회원가입** 📌[코드 확인](https://github.com/Jhyeri/NaeSaNamSa/blob/main/NS/src/main/java/ns/member/controller/NaverController.java#L124)<br/>
     - GET방식으로 요청 헤더에 접근 코드를 전달하여 네이버 회원 프로필 조회 API를 호출하고, 사용자 정보를 받습니다.<br/>
     - 받은 사용자 정보를 활용하여 추가적인 회원가입을 진행하고 로그인 처리를 합니다.<br/>
      
### 5.3. 아이디/비밀번호 찾기 📌[코드 확인](https://github.com/Jhyeri/NaeSaNamSa/blob/main/NS/src/main/java/ns/member/controller/LoginController.java#L159)
  - ajax 요청을 통해 사용자가 입력한 정보와 일치하는 회원의 이메일을 string 형태로 리턴하여 JSP에 출력합니다. 
  
### 5.4. 마이페이지  
  - **회원정보 수정** 📌[코드 확인](https://github.com/Jhyeri/NaeSaNamSa/blob/main/NS/src/main/java/ns/myPage/controller/MyPageController.java#L115)
  
  - **회원 탈퇴** 📌[코드 확인](https://github.com/Jhyeri/NaeSaNamSa/blob/main/NS/src/main/java/ns/myPage/controller/MyPageController.java#L168)
    - 데이터베이스에서 회원의 탈퇴여부를 'Y'로 update합니다.
    - 회원이 등록한 상품의 삭제여부도 'Y'로 update합니다.
    - 로그인 유지를 위해 생성하였던 쿠키를 삭제합니다.

### 5.5. 메세지
 - **메세지 목록** :pushpin: [코드 확인](https://github.com/Jhyeri/NaeSaNamSa/blob/main/NS/src/main/java/ns/message/controller/MessageController.java#L30)
    - 메세지 내용, 안 읽은 메세지 개수, 상대 회원의 정보를 불러옵니다.
    - 상대 회원의 정보를 불러오기 위해 각 메세지의 발신자와 수신자의 회원번호를 로그인 유저와 비교하여 상대의 회원번호를 추출합니다.
  
  - **메세지 목록 새로고침** :pushpin: [코드 확인](https://github.com/Jhyeri/NaeSaNamSa/blob/main/NS/src/main/java/ns/message/controller/MessageController.java#L95)
    - 사용자의 모든 요청이 발생할 때마다 ajax 요청을 통해 메세지 목록을 리로드하여 새 메세지가 실시간으로 보이도록 합니다.
  
  - **메세지 상세보기** 📌 [코드 확인](https://github.com/Jhyeri/NaeSaNamSa/blob/main/NS/src/main/java/ns/message/controller/MessageController.java#L166)
    - ajax 요청을 통해 채팅방 내용을 비동기식으로 불러옵니다.
    - 채팅방 클릭 시 새 메세지를 읽음처리하여 DB에 반영합니다.
  
  - **메세지 전송** 📌 [코드 확인](https://github.com/Jhyeri/NaeSaNamSa/blob/main/NS/src/main/java/ns/message/controller/MessageController.java#L206)
    - 두 회원 간 메세지를 주고받은 이력이 있는지 체크합니다.
      - 이력이 없다면 DB에서 가져 온 채팅방 번호의 최대값에 1을 더하여 새로운 번호의 채팅방을 생성합니다.
       - 메세지함이 아닌 다른 페이지에서 메세지를 전송할 경우, "viewName"이라는 string 형태의 파라미터를 추가로 전송하여 view를 다르게 처리합니다. 

### 5.6. 접근 경로 제한
  - **비회원 접근 제한** 📌 [코드 확인](https://github.com/Jhyeri/NaeSaNamSa/blob/main/NS/src/main/java/ns/common/interceptor/LoginInterceptor)
    - 비로그인 상태에서 로그인이 필요한 기능에 접근할 경우, 인터셉터를 통해 로그인 화면으로 리다이렉트 합니다.
  - **관리자 페이지 접근 제한** 📌 [코드 확인](https://github.com/Jhyeri/NaeSaNamSa/blob/main/NS/src/main/java/ns/common/interceptor/LoginInterceptor.java)
    - 관리자 페이지에 일반 회원이 접근할 경우, alert창을 통해 접근을 제한합니다.
</div>
</details>

<br/>

## 6. 핵심 트러블 슈팅

### 6.1. 탈퇴 후 재가입 시 이메일 중복 처리 문제
탈퇴 후 재가입을 7일 간 불가하도록 제한을 두고자 하였고, 사용자가 입력한 이메일로 재가입 여부를 판단하고자 하였습니다.

#### 📍 문제점 발견
- 데이터베이스에 탈퇴여부만 **Y/N**로 구분하여 데이터가 보존되도록 설계한 상태
- 이메일 컬럼의 제약조건을 **unique**로 지정

따라서 같은 이메일로 재가입이 불가능하다는 문제점을 발견하였습니다.
 
#### 📍 해결 과정
동일한 이메일로 재가입 할 수 있는 방법을 고안해보았습니다.

 - **오라클 job 스케줄러 사용하기**
   - 잡 스케줄러를 통해 7일 후 데이터베이스에서 delete 처리하는 방법을 떠올렸습니다.
   - 그러나, 모든 게시판의 글 작성자는 회원번호를 참조하고 있기 때문에 delete 될 경우 오류가 발생하기에 비효율적이라 생각하였습니다.
     - **cascade**를 사용할 수 있으나, 프로젝트 기획 당시 탈퇴한 회원의 글도 보존하는 것으로 계획했으므로 적절하지 않은 방법이라 판단하였습니다.

#### 📍 결과
- 동일 이메일은 사용할 수 없도록 하였습니다.
- 재가입 여부 판단 기준을 변경하였습니다.
 - 입력한 이름, 생년월일, 연락처를 통해 동일인물임을 확인하여 재가입 기간 제한 기능을 구현할 수 있었습니다.


</br>

## 7. 그 외 트러블 슈팅
<details>
<summary>ajax 함수가 data를 받지 못하는 오류</summary>
<div markdown="1">
- 컨트롤러 메소드의 리턴타입을 ModelAndView에서 String으로 수정

</div>
</details>

<details>
<summary>비밀번호 체크 오류</summary>
<div markdown="1">

  - 마이페이지 접속 시 비밀번호를 체크할 때, 세션영역에 저장된 비밀번호와 비교할 경우
  - 사용자가 비밀번호 변경 후 로그아웃 하지 않고 다시 마이페이지에 접속하면 **변경된 비밀번호로 비교할 수 없음**
  - DB에 저장된 비밀번호와 비교하도록 수정
  
</div>
</details>

<details>
<summary>상대방 닉네임을 조회하는 select문 오류</summary>
<div markdown="1">
  
 <img width="80%" src="https://github.com/Jhyeri/NaeSaNamSa/assets/111175466/43b0448d-52b8-4a0d-ae1f-c5dfdbacc411"/><br/>
- 메세지를 보내기만 하고 받은 적은 없을 경우 데이터 조회가 되지 않음
- 다른 테이블과 조인하지 않고 회원번호에 해당하는 닉네임을 가져오도록 쿼리문 수정
  
</div>
</details>

<details>
<summary>메세지 목록 정렬 꼬임 문제</summary>
<div markdown="1">

- 메세지 리스트를 조회하는 쿼리문에서 오라클 힌트가 적용되지 않음
  <details>
  <summary><b>📌 실행된 쿼리문 확인</b></summary>
  <div markdown="2">
  
  ```sql
    SELECT /*+ INDEX_DESC(CHAT PK_CHAT) */ *
        FROM  CHAT
        WHERE CHAT_NUM IN
                (SELECT MAX(NUM)
                        FROM (SELECT
                            CHAT_NUM AS NUM,
                            CHAT_ROOM,
                            CHAT_SEND_NUM,
                            CHAT_RECV_NUM,
                            TO_CHAR(CHAT_SEND_TIME, 'YYYY.MM.DD HH24:MI:SS') AS CHAT_SEND_TIME,
                            TO_CHAR(CHAT_READ_TIME, 'YYYY.MM.DD HH24:MI:SS') AS CHAT_READ_TIME,
                            CHAT_CONTENT,
                            CHAT_READ_CHK
                            FROM CHAT
                        WHERE (CHAT_SEND_NUM = 3 OR CHAT_RECV_NUM= 3)   

      ) GROUP BY CHAT_ROOM)
  ```
  </div>
  </details>
 
 - 힌트 대신 **order by절**을 사용하여 해결
    <details>
    <summary><b>📌 수정된 코드 확인</b></summary>
    <div markdown="2">

    ```sql
    <select id="message_list" parameterType="hashmap" resultType="hashmap">
      <![CDATA[
       SELECT
        CHAT_NUM,
        CHAT_ROOM,
        CHAT_SEND_NUM,
        CHAT_RECV_NUM,
        TO_CHAR(CHAT_SEND_TIME, 'YYYY.MM.DD HH24:MI') AS CHAT_SEND_TIME,
        TO_CHAR(CHAT_READ_TIME, 'YYYY.MM.DD HH24:MI') AS CHAT_READ_TIME,
        CHAT_CONTENT,
        CHAT_READ_CHK
        FROM  CHAT
        WHERE CHAT_NUM IN
                (SELECT MAX(NUM)
                        FROM (SELECT 
                            CHAT_NUM AS NUM,
                            CHAT_ROOM,
                            CHAT_SEND_NUM,
                            CHAT_RECV_NUM,
                            CHAT_CONTENT,
                            CHAT_READ_CHK
                            FROM CHAT
                        WHERE (CHAT_SEND_NUM = #{MEM_NUM} OR CHAT_RECV_NUM= #{MEM_NUM})]]> <include refid="messageSearch"/><![CDATA[ ) GROUP BY CHAT_ROOM)
                        ORDER BY CHAT_NUM DESC
    ]]>    
    </select>
    <sql id="messageSearch">
       <if test="keyword != null">
          AND CHAT_CONTENT LIKE '%' || #{keyword} || '%'
       </if>
    </sql>
    ```
    </div>
    </details>
 
</div>
</details>
 
<details>
<summary>아이디찾기 오류</summary>
<div markdown="1">
  
 - 에러 메세지
    - `Expected one result (or null) to be returned by selectOne(), but found: 7 at org.apache.ibatis.session.defaults.DefaultSqlSession.selectOne(DefaultSqlSession.java:66)`
 - 이름과 생년월일이 동일한 회원이 존재할 경우 **한 개 이상의 result가 리턴**되어 오류 발생
 - 아이디 찾기 조건에 연락처까지 입력하도록 수정
   
</div>
</details>    

<details>
<summary>selectId 쿼리문이 계속 실행되는 오류</summary>
<div markdown="1">
  
  - 쿠키를 사용하여 로그인 유지 기능 구현 중, 로그인 유지에 체크했을 경우(쿠키가 존재할 경우)에만 selectId 쿼리를 호출하도록 함
  - 최근 본 상품 등 다른 쿠키가 존재하기 때문에 계속 호출됨
<img width="60%" src="https://github.com/Jhyeri/NaeSaNamSa/assets/111175466/1000f3f1-058f-41ec-9f5a-7232f72119a0"/><br/>

  - 이메일과 비밀번호 쿠키를 찾도록 조건을 수정
<img width="60%" src="https://github.com/Jhyeri/NaeSaNamSa/assets/111175466/b48084b5-fe2e-475a-a934-0b32ab5975ee"/><br/>
</div>
</details>    

<details>
<summary>JSTL 비교연산 오류</summary>
<div markdown="1">
  
  - 메세지 전송 후, 채팅방에서 상대방의 프로필이미지가 사라지는 문제 발생
  
  <img width="60%" src="https://github.com/Jhyeri/NaeSaNamSa/assets/111175466/aecaac35-57ac-4979-a420-48d0db1fe895"/>
  
  <br/>
  
    - 주석처리된 <c:when>의 조건절에서 오류 발견
    - <c:otherwise>로 수정하여 해결
 
</div>
</details>  
    
</br>

## 8. 회고 / 느낀점
>프로젝트 개발 회고 글: 
