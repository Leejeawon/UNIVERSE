<!--   회원가입 정규식     -->
function checks() {
var getMail = RegExp(/^[A-Za-z0-9_.-]+@[A-Za-z0-9-]+.[A-Za-z0-9-]+/);
var getId= RegExp(/^[a-zA-Z0-9]{4,12}$/);
var getPw= RegExp(/^(?=.[a-zA-Z])(?=.[^a-zA-Z0-9]|.*[0-9]).{4,12}$/);
var getName= RegExp(/^[가-힣]+$/);


// 아이디 공백 확인
if($("#inputId").val() == "") {
alert("아이디를 입력해주세요.");
$("#inputId").focus();
return false;
}


// 아이디 유효성검사
if(!getId.test($("#inputId").val())) {
alert("아이디는 4~12자, 영문 대소문자, 숫자만 가능합니다.");
$("#inputId").val("");
$("#inputId").focus();
return false;
}


// 비밀번호 공백 확인
if($("#inputPassword").val() == "") {
alert("비밀번호를 입력해주세요.");
$("#inputPassword").focus();
return false;
}


// 아이디 비밀번호 같음 확인
if($("#inputId").val() == $("#inputPassword").val()) {
alert("아이디와 비밀번호가 같습니다");
$("#inputPassword").val("");
$("#inputPassword").focus();
return false;
}


// 비밀번호 유효성검사
if(!getPw.test($("#inputPassword").val())) {
alert("비밀번호는 4~12자 영문 대소문자, 숫자, 특수문자 혼합해서 사용해야 됩니다.");
$("#inputPassword").val("");
$("#inputPassword").focus();
return false;
}

// 비밀번호 확인란 공백 확인
if($("#inputPasswordConfirm").val() == ""){
alert("비밀번호를 다시 입력해주세요.");
$("#inputPasswordConfirm").focus();
return false;
}


// 비밀번호 확인
if($("#inputPassword").val() != $("#inputPasswordConfirm").val()){
alert("비밀번호가 다릅니다. 다시 입력해주세요.");
$("#inputPassword").val("");
$("#inputPasswordConfirm").val("");
$("#inputPassword").focus();
return false;
}


// 이름 공백 검사
if($("#inputName").val() == ""){
alert("이름을 입력해주세요.");
$("#inputName").focus();
return false;
}


// 이름 유효성 검사
if(!getName.test($("#inputName").val())){
alert("이름은 한글만 입력 가능합니다.")
$("#inputName").val("");
$("#inputName").focus();
return false;
}


// 이메일 공백 확인
if($("#inputEmail").val() == ""){
alert("이메일을 입력해주세요.");
$("#inputEmail").focus();
return false;
}


// 이메일 유효성 검사
if(!getMail.test($("#inputEmail").val())){
alert("이메일 형식에 맞게 입력해주세요.")
$("#inputEmail").val("");
$("#inputEmail").focus();
return false;
}
}