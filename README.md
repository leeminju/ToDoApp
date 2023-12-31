# ToDoApp
회원가입, 로그인 기능이 있는 투두앱 백엔드 서버

# **Notification: 과제 시작 전**

## Use Case Diagram
![todoapp drawio](https://github.com/leeminju/ToDoApp/assets/19209147/1a799051-5579-4fe1-b4bf-d9351be27b36)

    https://app.diagrams.net/#G1bg7ykj9f64yG5d-_or3VhkATJu7O0A9v
## API 명세서
- https://documenter.getpostman.com/view/30859314/2s9YXpTxXk
    
## ERD
  https://www.erdcloud.com/d/FSLx8mZbgzq532WXr 
![todoList ERD](https://github.com/leeminju/ToDoApp/assets/19209147/c9ff681b-e6fe-4967-b849-8ea14a4944cd)
- 실제 ERD
![실제 Diagram](https://github.com/leeminju/ToDoApp/assets/19209147/5eac8c8c-c690-4c9f-9ba9-7275be1d5447)


# 과제 설명
# 시연 영상

[![Video Label](https://github.com/leeminju/ToDoApp/assets/19209147/b7cb7dbe-59dc-43d5-a867-97c55e948183)](https://www.youtube.com/embed/Oy8WAXJbofs)

# 기능 설명
**1. 회원가입 기능**<br>
회원가입 버튼을 클릭 후 username, password를 설정한 validation 규칙에 맞게 입력하면 <br>
성공 메세지와 함께 users 테이블에 회원이 등록된다!<br>
규칙에 맞지 않거나 중복된 username입력 시 오류 메세지와 상태코드 400을 반환한다. <br>
<br>
**2. 로그인 기능**<br>
username, password을 입력한 후 로그인 버튼 클릭 하면 API 호출하고,<br> 
JwtAuthenticationFilter에서 입력값을 받아와 회원인지 검증하고,
회원이라면 jwt토큰 발급 후 응답 헤더에 추가한다.(상태코드 200, 로그인 상태 메세지 추가)
회원이 아니라면 로그인 실패 메세지와 상태코드 200 반환!<br><br>
로그인이 되면 모든 API 요청 처리 가능 해짐<br><br>
 
**3. 할일 목록 조회(회원별)**<br>
메인 페이지 진입하면 user마다 할일 목록을 조회할 수 있다.<br><br>
전체 user 목록을 조회 후, 각 user 마다 todoList테이블에 존재하는 할 일을 작성일 내림차순으로 조회해 목록을 만들어<br>
key가 username, value가 할일 목록인 Map을 생성한다<br> 

페이지가 열릴 때마다 사용자 이름으로 카드가 생성되고, 그안에 저장된 할일들이 순차적으로 생성되어 표시된다.

**4. 할일 작성 기능**<br>
로그인된 사용자의 card에만 제목/내용 입력 부분을 열고 닫을 수있는 버튼이 활성화되어있다.<br>
제목과 내용을 입력하고 저장하면 성공 메세지와 함께 DB에 저장되고, 내 카드 할일 목록에도 추가된다.
(저장시에 id, 작성일시가 자동 생성된다.)<br>
제목이나 내용에 공백을 입력하면 제목과 내용을 모두 입력하라는 메세지가 뜬다.(validation 기능 추가)


<br>**5. 선택한 할일 내용 조회**
<br>조회하고 싶은 할일의 제목을 클릭 하면 modal창이 열리면서 완료여부, 작성자, 작성일시, 내용을 확인할 수 있다.+ 댓글 목록 조회!

**6. 선택한 할일 내용 수정**
내가 작성한 할일을 클릭후 Edit 버튼을 누르면 제목과 내용을 입력할 수있는 modal로 이동하고, 제목이나 내용을 수정한 후 save를 누르면 내용이 수정된다.
내가 작성한 할일이 아니라면 작성자만 수정 가능하다는 메세지를 반환한다!<br>

제목이나 내용에 공백을 입력하면 제목과 내용을 모두 입력하라는 메세지가 뜬다.(validation 기능 추가)
<br>**7. 선택한 할일 삭제**<br>
내가 작성한 할일을 클릭후 Remove 버튼을 누르면 삭제 메세지와 함께 할일이 삭제된다.
<br>내가 작성한 할일이 아니라면 작성자만 삭제 가능하다는 메세지를 반환한다!
 
**8. 선택한 할일 완료/비완료 체크**<br>
내가 작성한 할일을 클릭후 finished 체크박스가 checked 상태라면 완료 상태,<br>
unchecked 상태라면 비완료 상태로 변경된다!
완료 상태에는 제목이 줄그어진 상태로 목록에 표시된다.
<br>내가 작성한 할일이 아니라면 작성자만 수정 할 수 있다는 메세지를 반환한다!

**9. 댓글 작성& 조회**<br>
모든 할일에 댓글을 작성할 수 있다. (작성자는 로그인한 사용자로 등록됨!)<br>
내용을 작성하고 +Add a comment를 누르면 댓글이 DB에 저장되고 목록에도 표시된다. (id, 작성일자 자동 생성/작성일시 내림차순으로 표시)
<br>내용에 공백을 입력하면 제목과 내용을 모두 입력하라는 메세지가 뜬다.(validation 기능 추가)

**9. 댓글 수정**<br>
내가 작성한 댓글 연필모양 버튼을 누르면 내용 수정 창이 활성화되고, 내용 수정 후 저장 버튼을 누르면
댓글이 수정된다!<br>
<br>내용에 공백을 입력하면 제목과 내용을 모두 입력하라는 메세지가 뜬다.(validation 기능 추가)
<br>내가 작성한 댓글이 아니라면 댓글 작성자만 수정가능하다는 메세지 반환.

<br>**9. 댓글 삭제**<br>
내가 작성한 댓글 휴지통 버튼을 누르면 댓글이 삭제된다!
<br>내가 작성한 댓글이 아니라면 댓글 작성자만 삭제 가능하다는 메세지 반환.

# 추가 구현 기능
- 회원가입시 dto에 validation을 적용한 것을 할일 작성/수정 & 댓글 작성/수정에도 적용하였다.
# Q & A

### Q1 ) 처음 설계한 API 명세서에 변경사항이 있었나요? 변경 되었다면 어떤 점 때문 일까요? 첫 설계의 중요성에 대해 작성해 주세요!  
기존에는 user 목록 불러오는 API + 이름별 목록 가져오는 API 두개를 만들어 Arraylist 두개를 반환 했는데!
response를 Map<String,ReposnseDto>로 변경해  API 하나로 해결했다!

### Q2) ERD를 먼저 설계한 후 Entity를 개발했을 때 어떤 점이 도움이 되셨나요?
어떤 연관관계로 Entity를 구성해야할 지 생각하기 쉬웠다.

### Q3) JWT를 사용하여 인증/인가를 구현 했을 때의 장점은 무엇일까요?
로그인 정보를 Server 에 저장하지 않고, Client 에 로그인 정보를 JWT 로 암호화하여 저장하기 때문에 동시 접속자가 많을 때 서버 측 부하 낮출 수 있다!  외부 DB와 연결해서 check하지 않고 서버 자체에서 검증이 가능하다!

### Q4) 반대로 JWT를 사용한 인증/인가의 한계점은 무엇일까요?
구현의 복잡도 증가, Secret key 유출 시 JWT 조작 가능, 알고리즘 해독 가능하다!

### Q5)  만약 댓글이 여러개 달려있는 할 일을 삭제하려고 한다면 무슨 문제가 발생할까요? Database 테이블 관점에서 해결 방법이 무엇일까요?
  - 댓글이 외래키로 todo_id를 가지고 있어서 자식이 있으면 부모가 삭제가 안되는 오류 발생!   
 CascadeType.REMOVE 옵션을 지정하니  부모객체를 삭제하면 연관되어 있는 자식 객체 모두 삭제됩니다!
```java
    @OneToMany(mappedBy = "todo", cascade = CascadeType.REMOVE)
    private List<Comment> comment = new ArrayList<>();
```

### Q6)  IoC / DI 에 대해 간략하게 설명해 주세요!
제어의 역전, 의존성 주입이란 뜻으로 메소드나 객체의 호출작업을 개발자가 결정하는 것이 아니라, 외부에서 결정되는 것을 말한다!
객체를 직접 생성하는 게 아니라 외부에서 생성한 후 주입 시켜주는 방식이다!

controller에서 service를 직접 생성하는 것이 아니라 주입시켜줌!(@Service에 의해  Bean으로 자동 등록 되어있기 때문에)<br>
service에서 repository를 직접 생성하는 것이 아니라 주입시켜줌!(@Bean이 포함된 JpaRepository를 상속받기 때문에)

주입 방식은 메소드, 필드, 생성자 주입이 있지만 대부분  생성자 주입을 사용하고
private final로 설정 후 @RequiredArgsConstructor하면 생성자 자동 생성된다!

# 후기
- 지난번 익명게시판 과제에 이어서 mvc 패턴으로 프로젝트를 설계 하는 법에 조금은 익숙해진 것 같다!
- jwt 인증/인가에 대해서 아직은 완벽한 이해는 못했지만, 예제 코드를 변형 시켜서
HttpServletRequest,  HttpServletResponse 에 헤더에 token name,value 넣기 /상태 코드/메시지 등 를 넣는 법을 새롭게 알게 되었다! 
- 화면에서 ajax 방식으로 API를 호출하는 방법(success,error 시에 응답메세지 출력)도 익히고
- bootstrap의 modal을 이용해 같은 위치에 내용만 바꿔주는 방법도 알게 되었다.
- DB상의 연관관계를 JPA를 통해 쉽게 표현할 수 있고, 사용하는 방법을 알게 되었다.
- cascade.REMOVE만 적용했는데 다른 옵션에 대해 더 공부하고 적용하고 싶다!
