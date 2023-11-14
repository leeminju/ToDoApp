const host = 'http://' + window.location.host;
let targetId;

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
        })
        .fail(function (jqXHR, textStatus) {
            logout();
        });

    showMyTodoList();
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

function isValid(name, value, min, max) {
    if (value.trim().length < min) {
        alert(name + '을 공백 포함' + min + '자 이상로 입력해주세요');
        return false;
    }
    if (value.trim().length > max) {
        alert(name + '을 공백 포함' + max + '자 이하로 입력해주세요');
        return false;
    }
    return true;
}

function openclose() {
    $('#myinputbox').toggle();
}

function saveTodo() {
    let title = $('#title').val();
    let contents = $('#contents').val().replace("\r\b", "<br>");

    // 2. 작성한 내용이 올바른지 isValidContents 함수를 통해 확인합니다.
    if (!isValid("내용", contents, 1, 100) || !isValid("제목", title, 1, 20)) {
        return;
    }
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
                console.log(error);
            }
        }
    );
}

function showMyTodoList(isAdmin = false) {
    $.ajax({
        type: 'GET',
        url: '/api/posts',
        contentType: 'application/json',
        success: function (response) {
            $('#myToDoList').empty();

            for (let i = 0; i < response.length; i++) {
                let todo = response[i];
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
                    style="text-decoration-line: line-through">
                    ${title}</li>`;
                }
                $('#myToDoList').append(tempHtml);
            }
        },
        error(error, status, request) {
            console.log(error);
        }
    });
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
            $('#edit_title').val(title);
            $('#response_contents').text(contents);
            $('#edit_contents').val(contents);
            $('#response_username').text(username);
            $('#response_modifiedAt').text(modifiedAt);
            $('#update_btn').val(id);

            $('#finished').prop('checked', finished);
        }
    })
}

function updateTodo() {
    let id = $('#update_btn').val();

    let title = $('#edit_title').val();
    let contents = $('#edit_contents').val().replace("\r\b", "<br>");

    // 2. 작성한 내용이 올바른지 isValidContents 함수를 통해 확인합니다.
    if (!isValid("내용", contents, 1, 100) || !isValid("제목", title, 1, 20)) {
        return;
    }
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
                console.log(response);

            },
            error(error, status, request) {
                console.log(error);
            }
        }
    );
}

function win_close() {
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
                console.log(error);
            }
        }
    );
}