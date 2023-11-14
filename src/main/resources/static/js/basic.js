const host = 'http://' + window.location.host;
let targetId;

$(document).ready(function () {
    showMyTodoList();
})


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
    let contents = $('#contents').val();

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
                let tempHtml = `<li class="list-group-item" onclick="showDetails(${todo['id']})">${todo['title']}</li>`;
                $('#myToDoList').append(tempHtml);
            }
        },
        error(error, status, request) {
            console.log(error);
        }
    });
}


function showDetails(id) {
    console.log("클릭");
    $.ajax({
        type: 'GET',
        url: `/api/post/${id}`,
        success: function (response) {
            let id = response['id'];
            let title = response['title'];
            let username = response['username'];
            let contents = response['contents'];
            let createdAt = response['createdAt'];
            let modifiedAt = response['modifiedAt'];
            console.log(id+" "+title+" "+username+" "+contents+" "+createdAt+" "+modifiedAt);
        }
    })
}

function setMyprice() {
    /**
     * 1. id가 myprice 인 input 태그에서 값을 가져온다.
     * 2. 만약 값을 입력하지 않았으면 alert를 띄우고 중단한다.
     * 3. PUT /api/product/${targetId} 에 data를 전달한다.
     *    주의) contentType: "application/json",
     *         data: JSON.stringify({myprice: myprice}),
     *         빠뜨리지 말 것!
     * 4. 모달을 종료한다. $('#container').removeClass('active');
     * 5, 성공적으로 등록되었음을 알리는 alert를 띄운다.
     * 6. 창을 새로고침한다. window.location.reload();
     */
        // 1. id가 myprice 인 input 태그에서 값을 가져온다.
    let myprice = $('#myprice').val();
    // 2. 만약 값을 입력하지 않았으면 alert를 띄우고 중단한다.
    if (myprice == '') {
        alert('올바른 가격을 입력해주세요');
        return;
    }

    // 3. PUT /api/product/${targetId} 에 data를 전달한다.
    $.ajax({
        type: 'PUT',
        url: `/api/products/${targetId}`,
        contentType: 'application/json',
        data: JSON.stringify({myprice: myprice}),
        success: function (response) {

            // 4. 모달을 종료한다. $('#container').removeClass('active');
            $('#container').removeClass('active');
            // 5. 성공적으로 등록되었음을 알리는 alert를 띄운다.
            alert('성공적으로 등록되었습니다.');
            // 6. 창을 새로고침한다. window.location.reload();
            window.location.reload();
        },
        error(error, status, request) {
            console.error(error);
        }
    })
}