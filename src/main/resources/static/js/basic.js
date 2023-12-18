const host = 'http://' + window.location.host;
let login_user;

// 화면 시작하자마자
$(document).ready(function () {

    authorizationCheck();//인가
    addMemberCard();//멤버 카드 추가
})

// 인가 : 토큰 유효성 판단
function authorizationCheck() {
    const auth = getToken();

    if (auth !== undefined && auth !== '') {
        $.ajaxPrefilter(function (options, originalOptions, jqXHR) {
            jqXHR.setRequestHeader('Authorization', auth);
        });
    } else {
        window.location.href = host + '/api/users/login-page';
        return;
    }

    //로그인한 회원 정보
    $.ajax({
        type: 'GET',
        url: `/api/user-info`,
        contentType: 'application/json',
    })
        .done(function (res, status, xhr) {
            const username = res;

            if (!username) {
                window.location.href = '/api/users/login-page';
                return;
            }

            $('#username').text(username);
            $('#login_username').text(username);
            login_user = username;
        })
        .fail(function (jqXHR, textStatus) {
            logout();
        });
}

function logout() {
    // 토큰 삭제
    Cookies.remove('Authorization', {path: '/'});
    window.location.href = host + '/api/user/login-page';
}

function getToken() {
    let auth = Cookies.get('Authorization');

    if (auth === undefined) {
        return '';
    }

    return auth;
}

//멤버 카드 추가
function addMemberCard() {
    $.ajax({
        type: 'GET',
        url: '/api/posts',
        contentType: 'application/json',
        success: function (response) {
            var keys = Object.keys(response); //키를 가져옵니다. 이때, keys 는 반복가능한 객체가 됩니다.

            for (var i = 0; i < keys.length; i++) {
                var username = keys[i];

                let todolist = response[username];

                let temp = `<div class="col">
                <div class="card" style="width: 20rem;">
                    <div class="card-header" id="login_username">
                    ${username}
                    </div>
                    <ul class="list-group list-group-flush" id=${username}-list>
                    </ul>          
                    <div id="${username}-box" class="input-box" style="display: none">
                         <input type="text" class="form-control" id="${username}-title" placeholder="제목">  
                         <div class="mb-3">
                         <textarea class="form-control" id="${username}-contents" placeholder="내용" rows="2"></textarea>
                        </div>
                        <button id="addBtn" class="btn btn-primary"  onclick="saveTodo('${username}')">추가</button>
                   </div>              
                    <button id="${username}-add_btn" style="border: transparent" onclick="toggle_control('${username}')">+ Add TO DO</button>                 
                </div></div>`;

                $('#card').append(temp);

                if (login_user != username) {
                    let btn_id = '#' + username + '-add_btn';
                    $(btn_id).attr("disabled", true);
                }

                addTodoList(username, todolist);//멤버별 todoList 추가
            }
        },
        error(error, status, request) {
            console.log(error);
        }
    });
}

//멤버별 할일 추가
function addTodoList(username, todolist) {
    let list_name = "#" + username + "-list";
    $(list_name).empty();

    for (i = 0; i < todolist.length; i++) {
        let todo = todolist[i];


        let id = todo['id'];
        let title = todo['title'];
        let finished = todo['finished'];

        let tempHtml;
        if (!finished) {
            tempHtml = `<li class="list-group-item" onclick="showDetails(${id})" 
                    data-bs-toggle="modal" data-bs-target="#TodoModal">${title}</li>`;
        } else {
            tempHtml = `<li class="list-group-item" onclick="showDetails(${id})" 
                    data-bs-toggle="modal" data-bs-target="#TodoModal"
                    style="text-decoration-line: line-through">${title}</li>`;//완료된 할일
        }

        $(list_name).append(tempHtml);
    }
}


//할일 저장
function saveTodo(username) {
    let title = $('#' + username + '-title').val();
    let contents = $('#' + username + '-contents').val().replace("\r\b", "<br>");//textarea에서 엔터-> <br>로 변환

    let data = {
        'title': title,
        'contents': contents
    };

    $.ajax({
            type: 'POST',
            url: '/api/posts',
            contentType: 'application/json',
            data: JSON.stringify(data),
            success: function (response) {
                alert(response['responseMessage']);
                window.location.reload();
            },
            error(error, status, request) {
                alert(error['responseJSON']['responseMessage']);
            }
        }
    );
}

//할일 작성 부분 열었다 닫았다하기
function toggle_control(username) {
    let box_id = '#' + username + '-box';
    $(box_id).toggle();
}

//할일 카드 세부 내용 출력
function showDetails(id) {
    $.ajax({
        type: 'GET',
        url: `/api/posts/${id}`,
        success: function (response) {
            let id = response['id'];
            let title = response['title'];
            let username = response['username'];
            let contents = response['contents'];
            let createdAt = response['createdAt'];
            var finished = response['finished'];

            contents = contents.replaceAll("<br>", "\r\n");

            $('#response_title').text(title);
            $('#response_contents').text(contents);
            $('#response_username').text(username);
            $('#response_createdAt').text(createdAt);

            $('#delete_btn').val(id);// deleteTodo() 실행 시 value 값 가져옴!
            $('#update_btn').val(id);// updateTodo updateFinished 시 value값 가져옴

            $('#edit_title').val(title);
            $('#edit_contents').val(contents);

            $('#finished').prop('checked', finished);
        }
    })

    showComment(id);
}

//할일 업데이트(Save 버튼 클릭 시)
function updateTodo() {
    let id = $('#update_btn').val();

    let title = $('#edit_title').val();
    let contents = $('#edit_contents').val().replace("\r\b", "<br>");

    let data = {
        'title': title,
        'contents': contents,
    };

    $.ajax({
            type: 'PUT',
            url: `/api/posts/${id}`,
            contentType: 'application/json',
            data: JSON.stringify(data),
            success: function (response) {
                alert(response['responseMessage']);
                window.location.reload();
            },
            error(error, status, request) {
                alert(error['responseJSON']['responseMessage']);
                showDetails(id);
            }
        }
    );
}

//할일 삭제(삭제 버튼 클릭)
function deleteTodo() {
    let id = $('#delete_btn').val();

    $.ajax({
            type: 'DELETE',
            url: `/api/posts/${id}`,
            contentType: 'application/json',
            success: function (response) {
                alert(response['responseMessage']);
                window.location.reload();
            },
            error(error, status, request) {
                alert(error['responseJSON']['responseMessage']);
            }
        }
    );
}

//modal 내 close 버튼 클릭 시 새로고침
function win_reload() {
    window.location.reload();
}

//체크박스 클릭시
function updateFinished() {
    let id = $('#update_btn').val();
    let finished = $('#finished').is(":checked");

    $.ajax({
            type: 'PUT',
            url: `/api/posts/${id}/finished/${finished}`,
            contentType: 'application/json',
            success: function (response) {
                alert(response['responseMessage']);
            },
            error(error, status, request) {
                alert(error['responseJSON']['responseMessage']);
                $('#finished').prop("checked", !finished);
            }
        }
    );
}

//댓글 생성
function create_Comment() {
    let post_id = $('#delete_btn').val();
    let contents = $('#comment_text').val().replace("\r\b", "<br>");

    let data = {"contents": contents};

    $.ajax({
            type: 'POST',
            url: `/api/posts/${post_id}/comments`,
            contentType: 'application/json',
            data: JSON.stringify(data),
            success: function (response) {
                alert(response['responseMessage']);
                showComment(post_id);
            },
            error(error, status, request) {
                console.log(error);
                alert(error['responseJSON']['responseMessage']);
            }
        }
    );
}

//할 일내 댓글 조회
function showComment(post_id) {
    $.ajax({
        type: 'GET',
        url: `/api/posts/${post_id}/comments`,
        success: function (response) {
            $('#comment-card').empty();

            for (var i = 0; i < response.length; i++) {
                let comment = response[i];

                let comment_id = comment['id'];
                let createdAt = comment['createdAt'];
                let username = comment['username'];
                let contents = comment['contents'].replaceAll("<br>", "\r\n");

                let tempHTML = `<div class="card mb-4">
                                <div class="card-body">
                                    <div id="${comment_id}-text" class="comment_text">
                                        ${contents}
                                    </div>
                                    <div id="${comment_id}-editarea" class="edit_area">
                                        <textarea id="${comment_id}-textarea" placeholder="댓글 내용" class="edit_textarea" name="" id="" rows="2">${contents}</textarea>
                                    </div>
                                    <div class="d-flex justify-content-between">
                                        <div class="d-flex flex-row align-items-center">                                        
                                            <p class="small mb-0 ms-2">${username}</p>
                                        </div>
                                        <div class="d-flex flex-row align-items-center">
                                            <p class="small text-muted mb-0">${createdAt}</p>
                                        </div>    
                                    </div>
                                    <div class="footer">
                                            <img id="${comment_id}-edit" class="icon-start-edit" src="images/edit.png" alt="" onclick="editComment('${comment_id}')">
                                            <img id="${comment_id}-delete" class="icon-delete" src="images/delete.png" alt="" onclick="delete_Comment('${comment_id}','${post_id}')">
                                            <img id="${comment_id}-submit" class="icon-end-edit" src="images/done.png" alt="" onclick="submitEdit('${comment_id}','${post_id}')">
                                    </div>
                                </div>
                            </div>`;

                $('#comment-card').append(tempHTML);
            }
        }
    })
}

//편집 버튼 누르면 -> 편집 공간 display
function editComment(id) {
    showEdits(id);
}

function showEdits(id) {
    $(`#${id}-editarea`).show();
    $(`#${id}-submit`).show();
    $(`#${id}-delete`).show();

    $(`#${id}-text`).hide();
    $(`#${id}-edit`).hide();
}

//댓글 삭제
function delete_Comment(id, post_id) {
    //삭제 API 호출
    $.ajax({
        type: "DELETE",
        url: `/api/comments/${id}`,
        contentType: "application/json",
        success: function (response) {
            alert(response['responseMessage']);
            showComment(post_id);
        }, error(error, status, request) {
            alert(error['responseJSON']['responseMessage']);
            showComment(post_id);
        }

    });

}

//댓글 수정 제출
function submitEdit(id, post_id) {

    let contents = $(`#${id}-textarea`).val().replaceAll("<br>", "\r\n");

    let data = {'contents': contents};

    $.ajax({
        type: "PUT",
        url: `/api/comments/${id}`,
        contentType: "application/json",
        data: JSON.stringify(data),
        success: function (response) {
            alert(response['responseMessage']);
            showComment(post_id);
        }, error(error, status, request) {
            alert(error['responseJSON']['responseMessage']);
            showComment(post_id);
        }

    });


}