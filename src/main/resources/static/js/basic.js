const host = 'http://' + window.location.host;
let targetId;
let login_user;

$(document).ready(function () {
    const auth = getToken();

    if (auth !== undefined && auth !== '') {
        $.ajaxPrefilter(function (options, originalOptions, jqXHR) {
            jqXHR.setRequestHeader('Authorization', auth);
        });
    } else {
        window.location.href = host + '/api/user/login-page';
        return;
    }

    $.ajax({
        type: 'GET',
        url: `/api/user-info`,
        contentType: 'application/json',
    })
        .done(function (res, status, xhr) {
            const username = res;

            if (!username) {
                window.location.href = '/api/user/login-page';
                return;
            }

            $('#username').text(username);
            $('#login_username').text(username);
            login_user = username;
        })
        .fail(function (jqXHR, textStatus) {
            logout();
        });

    addMemberCard();
})

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


function saveTodo(value) {
    let title = $('#' + value + '-title').val();
    let contents = $('#' + value + '-contents').val().replace("\r\b", "<br>");


    let data = {
        'title': title,
        'contents': contents
    };

    $.ajax({
            type: 'POST',
            url: '/api/post',
            contentType: 'application/json',
            data: JSON.stringify(data),
            success: function (response) {
                alert('할일이 추가되었습니다.');
                window.location.reload();
            },
            error(error, status, request) {
                alert(error['responseText']);
            }
        }
    );
}

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
                        <button id="addBtn" class="btn btn-primary"  value="${username}" onclick="saveTodo(this.value)">추가</button>
                   </div>              
                    <button id="${username}-add_btn" style="border: transparent" value="${username}" onclick="toggle_control(this.value)">+ Add TO DO</button>                 
                </div></div>`;

                $('#card').append(temp);

                if (login_user !== username) {
                    let btn_id = '#' + username + '-add_btn';
                    $(btn_id).attr("disabled", true);
                }
                addTodoList(username, todolist);
            }
        },
        error(error, status, request) {
            console.log(error);
        }
    });
}

function toggle_control(value) {
    let box_id = '#' + value + '-box';
    $(box_id).toggle();
}


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
                    style="text-decoration-line: line-through">${title}</li>`;
        }

        $(list_name).append(tempHtml);
    }
}


function showDetails(id) {
    $.ajax({
        type: 'GET',
        url: `/api/post/${id}`,
        success: function (response) {
            let id = response['id'];
            let title = response['title'];
            let username = response['username'];
            let contents = response['contents'];
            let createdAt = response['createdAt'];
            //let modifiedAt = response['modifiedAt'];
            var finished = response['finished'];

            contents = contents.replaceAll("<br>", "\r\n");

            $('#response_title').text(title);
            $('#response_contents').text(contents);
            $('#response_username').text(username);
            $('#response_modifiedAt').text(createdAt);

            $('#delete_btn').val(id);
            $('#update_btn').val(id);

            $('#edit_title').val(title);
            $('#edit_contents').val(contents);

            $('#finished').prop('checked', finished);
        }
    })

    showComment(id);
}

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
            url: `/api/post/${id}`,
            contentType: 'application/json',
            data: JSON.stringify(data),
            success: function (response) {
                alert('성공적으로 수정되었습니다.');
                window.location.reload();
            },
            error(error, status, request) {
                alert(error['responseText']);
            }
        }
    );
}

function deleteTodo() {
    let id = $('#delete_btn').val();

    $.ajax({
            type: 'DELETE',
            url: `/api/post/${id}`,
            contentType: 'application/json',
            success: function (response) {
                alert('할일이 삭제되었습니다.');
                window.location.reload();
            },
            error(error, status, request) {
                alert(error['responseText']);
            }
        }
    );
}

function win_reload() {
    window.location.reload();
}

function updatefinished() {
    let id = $('#update_btn').val();
    let finished = $('#finished').is(":checked");

    $.ajax({
            type: 'PUT',
            url: `/api/post/${id}/${finished}`,
            contentType: 'application/json',
            success: function (response) {
            },
            error(error, status, request) {
                alert(error['responseText']);
                $('#finished').prop("checked", !finished);
            }
        }
    );
}

function create_Comment() {

    let id = $('#delete_btn').val();

    let contents = $('#comment_text').val().replace("\r\b", "<br>");

    let data = {"contents": contents};

    $.ajax({
            type: 'POST',
            url: `/api/comment/${id}`,
            contentType: 'application/json',
            data: JSON.stringify(data),
            success: function (response) {
                alert("댓글이 작성되었습니다.");
                showComment(id);
            },
            error(error, status, request) {
                alert(error['responseText']);
            }
        }
    );
}

function showComment(id) {
    $.ajax({
        type: 'GET',
        url: `/api/comment/${id}`,
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
                                            <img id="${comment_id}-delete" class="icon-delete" src="images/delete.png" alt="" onclick="delete_Comment('${comment_id}','${id}')">
                                            <img id="${comment_id}-submit" class="icon-end-edit" src="images/done.png" alt="" onclick="submitEdit('${comment_id}','${id}')">
                                    </div>
                                </div>
                            </div>`;

                $('#comment-card').append(tempHTML);
            }
        }
    })
}

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

function delete_Comment(id, post_id) {
    //삭제 API 호출

    $.ajax({
        type: "DELETE",
        url: `/api/comment/${id}`,
        contentType: "application/json",
        success: function (response) {
            alert('댓글 삭제 완료!')
            showComment(post_id);
        }, error(error, status, request) {
            alert(error['responseText']);
        }

    });

}

function submitEdit(id, post_id) {

    let contents = $(`#${id}-textarea`).val().replaceAll("<br>", "\r\n");

    let data = {'contents': contents};

    $.ajax({
        type: "PUT",
        url: `/api/comment/${id}`,
        contentType: "application/json",
        data: JSON.stringify(data),
        success: function (response) {
            alert('댓글 수정 완료!')
            showComment(post_id);
        }, error(error, status, request) {
            alert(error['responseText']);
        }

    });


}