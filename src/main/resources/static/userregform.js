// 유효성 검사 상태를 저장할 변수들
let isUsernameValid = false;
let isEmailValid = false;
let isPasswordValid = false;
let isNameValid = false;

// 아이디 중복 확인 함수
function checkUsername() {
    const username = $('#username').val().trim(); // 입력 값의 앞뒤 공백 제거
    if (username === "") { // 공백인 경우
        $('#username-result').text('공백은 사용할 수 없습니다.').removeClass('valid').addClass('invalid');
        isUsernameValid = false;
        validateForm();
        return;
    }
    // 서버에 중복 확인 요청
    $.get('/api/users/check-username', { username: username }, function(isTaken) {
        if (isTaken) { // 중복된 경우
            $('#username-result').text('아이디가 이미 사용 중입니다.').removeClass('valid').addClass('invalid');
            isUsernameValid = false;
        } else { // 사용 가능한 경우
            $('#username-result').text('아이디를 사용할 수 있습니다.').removeClass('invalid').addClass('valid');
            isUsernameValid = true;
        }
        validateForm();
    });
}

// 이메일 중복 확인 함수
function checkEmail() {
    const email = $('#email').val().trim(); // 입력 값의 앞뒤 공백 제거
    if (email === "") { // 공백인 경우
        $('#email-result').text('공백은 사용할 수 없습니다.').removeClass('valid').addClass('invalid');
        isEmailValid = false;
        validateForm();
        return;
    }
    // 서버에 중복 확인 요청
    $.get('/api/users/check-email', { email: email }, function(isTaken) {
        if (isTaken) { // 중복된 경우
            $('#email-result').text('이메일이 이미 사용 중입니다.').removeClass('valid').addClass('invalid');
            isEmailValid = false;
        } else { // 사용 가능한 경우
            $('#email-result').text('이메일을 사용할 수 있습니다.').removeClass('invalid').addClass('valid');
            isEmailValid = true;
        }
        validateForm();
    });
}

// 비밀번호 공백 확인 함수
function checkPassword() {
    const password = $('#password').val().trim(); // 입력 값의 앞뒤 공백 제거
    if (password === "") { // 공백인 경우
        $('#password-result').text('공백은 사용할 수 없습니다.').removeClass('valid').addClass('invalid');
        isPasswordValid = false;
    } else { // 공백이 아닌 경우
        $('#password-result').text('').removeClass('invalid').addClass('valid');
        isPasswordValid = true;
    }
    validateForm();
}

// 이름 공백 확인 함수
function checkName() {
    const name = $('#name').val().trim(); // 입력 값의 앞뒤 공백 제거
    if (name === "") { // 공백인 경우
        $('#name-result').text('공백은 사용할 수 없습니다.').removeClass('valid').addClass('invalid');
        isNameValid = false;
    } else { // 공백이 아닌 경우
        $('#name-result').text('').removeClass('invalid').addClass('valid');
        isNameValid = true;
    }
    validateForm();
}

// 폼 유효성 검사 함수
function validateForm() {
    if (isUsernameValid && isEmailValid && isPasswordValid && isNameValid) {
        $('#submitBtn').removeClass('disabled').removeAttr('disabled'); // 모든 필드가 유효한 경우 버튼 활성화
    } else {
        $('#submitBtn').addClass('disabled').attr('disabled', 'disabled'); // 유효하지 않은 필드가 있는 경우 버튼 비활성화
    }
}

// 아이디 입력 필드 초기화 및 포커스 이벤트 처리
$('#username').on('focus', function() {
    $('#username-result').text('').removeClass('valid invalid');
    isUsernameValid = false;
    validateForm();
});

// 이메일 입력 필드 초기화 및 포커스 이벤트 처리
$('#email').on('focus', function() {
    $('#email-result').text('').removeClass('valid invalid');
    isEmailValid = false;
    validateForm();
});

// 비밀번호 입력 필드 초기화, 포커스 및 블러 이벤트 처리
$('#password').on('focus', function() {
    $('#password-result').text('').removeClass('valid invalid');
    isPasswordValid = false;
    validateForm();
}).on('blur', checkPassword); // 포커스가 사라질 때 검사

// 이름 입력 필드 초기화, 포커스 및 블러 이벤트 처리
$('#name').on('focus', function() {
    $('#name-result').text('').removeClass('valid invalid');
    isNameValid = false;
    validateForm();
}).on('blur', checkName); // 포커스가 사라질 때 검사

// 폼 제출 시 최종 유효성 검사 함수
function checkForm() {
    if (!isUsernameValid || !isEmailValid || !isPasswordValid || !isNameValid) {
        alert('모든 필드를 올바르게 입력해 주세요.');
        return false;
    }
    return true;
}
