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
                alert('할일이 성공적으로 작성되었습니다.');
                console.log(response);
                window.location.reload();
            },
            error(error, status, request) {
                alert(request['responseText']);
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
            console.log(keys.length);

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
                         <textarea class="form-control" id="${username}-contents" placeholder="내용" rows="3"></textarea>
                        </div>
                        <button class="btn btn-primary"  value="${username}" onclick="saveTodo(this.value)">추가</button>
                   </div>              
                    <button id="${username}-add_btn" value="${username}" onclick="toggle_control(this.value)">+ Add TO DO</button>                 
                </div></div>`;

                $('#card').append(temp);

                if (login_user != username) {
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
            // let createdAt = response['createdAt'];
            let modifiedAt = response['modifiedAt'];
            var finished = response['finished'];

            contents = contents.replaceAll("<br>", "\r\n");

            $('#response_title').text(title);
            $('#response_contents').text(contents);
            $('#response_username').text(username);
            $('#response_modifiedAt').text(modifiedAt);

            $('#delete_btn').val(id);
            $('#update_btn').val(id);

            $('#edit_title').val(title);
            $('#edit_contents').val(contents);

            $('#finished').prop('checked', finished);
        }
    })
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
                win_reload();
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
                alert('성공적으로 삭제되었습니다.');
                win_reload();
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
                $('#finished').prop("checked",!finished);
            }
        }
    );
}