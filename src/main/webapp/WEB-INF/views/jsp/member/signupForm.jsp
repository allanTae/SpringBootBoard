<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<script>
    // 스프링 시큐리티, ajax 처리를 위한 csrf 토큰 정보와 토큰을 서버에 전달하기 위한 헤더 이름.
    var token = '${_csrf.token}';
    var headerName = '${_csrf.headerName}';

    var idCheck = false; // 중복 아이디 체크 여부 default value = false

    // 회원가입 버튼 이벤트
	$(document).on('click', '#btnSignup', function(e){
		e.preventDefault();
		if(!checkValidation(e)){
		    return;
		}
		$("#form").submit();
	});

    // 취소 버튼 이벤트
	$(document).on('click', '#btnCancle', function(e){
		e.preventDefault(); 
		//$('#uid').val('');
		//$('#name').val('');
		//$('#pwd1').val('');
		//$('#pwd2').val('');
		//$('#email').val('');
		
		location.href="${pageContext.request.contextPath}/board/getBoardList";
	});

	// 중복 아이디 검사 버튼 이벤트
	$(document).on('click', '#btnIdCheck', function(e){
		e.preventDefault();
		var memberId = $('#memberId').val();
        var paramData = {"memberId": memberId};

		var headers = {"Content-Type" : "application/x-www-form-urlencoded; charset=UTF-8;"
				, "X-HTTP-Method-Override" : "POST"};
		$.ajax({
			url: "${pageContext.request.contextPath}/member/checkId"
			, headers : headers
			, data : paramData
			, type : 'POST'
			, dataType : 'text'
            , beforeSend: function(xhr){
                   xhr.setRequestHeader(headerName, token);
            }
			, success: function(result){
				var htmls = '';
				var checkId = JSON.parse(result);
				if(checkId.status === "in not use"){
	                htmls = htmls + '<div style="color: green; font-size: 10pt">' + checkId.message + '</div>';
	                idCheck = true;
				}else{
					htmls = htmls + '<div style="color: red; font-size: 10pt">' + checkId.message + '</div>';
				}
				
				$("#checkId").html(htmls);
			}
			, error:function(request,status,error){
                alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);}
		});
	});

    // 유효성 체크 메소드
    // return true => 유효성 검사 통과
    // return false => 유효성 검사 실패
    function RegTest(text, reg) {
        return reg.test(text);
    }

    // 아이디 유효성 체크 이벤트
    $(document).on('input', '#memberId', function(e){
        // 영소문자, 숫자만 허용하는 10~16자리 문자열
        var reg = /^[a-zA-Z0-9]{10,16}$/g;
        var id = e.target.value;
        var result = RegTest($.trim(e.target.value), reg);
        var html = '';
        if(result === false){
            html += '<div style="color: red; font-size: 10pt">아이디는 영대소문자, 숫자 10~16자리만 허용됩니다.</div>';
        }
        $("#checkId").html(html);
    });

    // 폼내 사용자 입력 값의 양쪽 공백 제거
    function textTrim(){
        var inputTexts = $("#form input[type=text]");
        for(let i=0; i<inputTexts.length; i++){
            if(inputTexts[i].value !== null && inputTexts[i].value !== ''){
                inputTexts[i].value = $.trim(inputTexts[i].value);
            }
        }
    }

    /**
     * 폼 데이터 유효성 검사 수행.
     * @param event object
     * @return boolean
     */
    function checkValidation(e){
        if(!idCheck){
            alert("아이디 중복체크를 확인 해 주세요.");
            return false;
        }

        // 공백 제거
        textTrim();

        // 이름 유효성 검사 정규식
        var regName = /^[가-힣]{2,16}$/;
        if(!RegTest($("#name").val(), regName)){
            alert("이름은 한글로 2자~16자이 사이로 입력하셔야 합니다.");
            e.preventDefault();
            return false;
        }

        // 비밀번호 유효성 검사 정규식
        // 조건1) 영문자, 숫자, 특수문자가 모두 들어가야 한다.
        // 조건2) 공백문자 들어가서는 안된다.
        var regPwd = /^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*()\-_=+~₩|\\:;"',.<>/?]{10,16}$/;
        if(!RegTest($("#pwd").val(), regPwd)){
            alert("비밀번호는 영문자, 숫자, 특수문자가 모두 들어가 10자~16자로 입력하셔야 합니다.");
            e.preventDefault();
            return false;
        }
        return true;
    }

</script>
<article>
	<div class="container col-md-6" role="main">
		<div class="card">
			<div class="card-header">Register</div>
			<div class="card-body">
				<form:form name="form" id="form" class="form-signup" role="form" modelAttribute="memberForm" method="post" action="${pageContext.request.contextPath}/member">

					<div class="form-group row">
						<label for="memberId" class="col-md-3 col-form-label text-md-right">아이디</label>
						<div class="col-md-5">
							<form:input path="memberId" id="memberId" class="form-control" placeholder="아이디을 입력해 주세요" />
							<div id="checkId"></div>
						</div>
						<div class="col-md-3">
							<button type="button" name="btnIdCheck" class="btn btn-sm btn-primary" id="btnIdCheck" 
									style="width=50px;">아이디 중복체크</button>
						</div>
					</div>
					<div class="row">
						<label for="memberIdError" class="col-md-3" text-md-right></label>
						<form:errors path="memberId" id="memberIdError" class="col-md-6 form-group text-left" style="font-size:15px; color:red; width=100px;"/>
					</div>

					<div class="form-group row">
						<label for="name" class="col-md-3 col-form-label text-md-right">이름</label>
						<div class="col-md-5">
							<form:input path="name" id="name" class="form-control" placeholder="이름을 입력해 주세요" />
						</div>
					</div>
					<div class="row">
						<label for="nameError" class="col-md-3" text-md-right></label>
						<form:errors path="name" id="nameError" class="col-md-6 form-group text-left" style="font-size:15px; color:red; width=100px;"/>
					</div>

					<div class="form-group row">
						<label for="pwd" class="col-md-3 col-form-label text-md-right">비밀번호</label>
						<div class="col-md-5">
							<form:password path="pwd" id="pwd" class="form-control" placeholder="비밀번호를 입력해 주세요" />
						</div>
					</div>
					<div class="row">
						<label for="pwdError" class="col-md-3" text-md-right></label>
						<form:errors path="pwd" id="pwdError" class="col-md-6 form-group text-left" style="font-size:15px; color:red; width=100px;"/>
					</div>

					<div class="form-group row">
						<label for="re_pwd" class="col-md-3 col-form-label text-md-right">비밀번호 확인</label>
						<div class="col-md-5">
							<form:password path="re_pwd" id="re_pwd" class="form-control" placeholder="입력하신 비밀번호와 동일하게 입력해 주세요" />
						</div>
					</div>
					<div class="row">
						<label for="re_pwdError" class="col-md-3" text-md-right></label>
						<form:errors path="re_pwd" id="re_pwdError" class="col-md-6 form-group text-left" style="font-size:15px; color:red; width=100px;"/>
					</div>

					<div class="form-group row">
						<label for="street" class="col-md-3 col-form-label text-md-right">거리명</label>
						<div class="input-group col-md-7">
							<div class="input-group-prepend">
								<span class="input-group-text">@</span>
							</div>
							<form:input path="street" id="street" class="form-control" placeholder="거리명을 입력해 주세요" />
						</div>
					</div>
					<div class="row">
						<label for="streetError" class="col-md-3" text-md-right></label>
						<form:errors path="street" id="streetError" class="col-md-6 form-group text-left" style="font-size:15px; color:red; width=100px;"/>
					</div>

					<div class="form-group row">
						<label for="zipcode" class="col-md-3 col-form-label text-md-right">우편번호</label>
						<div class="input-group col-md-7">
							<div class="input-group-prepend">
								<span class="input-group-text">@</span>
							</div>
							<form:input path="zipcode" id="zipcode" class="form-control" placeholder="우편번호를 입력해 주세요" />
						</div>
					</div>
					<div class="row">
						<label for="zipcodeError" class="col-md-3" text-md-right></label>
						<form:errors path="zipcode" id="zipcodeError" class="col-md-6 form-group text-left" style="font-size:15px; color:red; width=100px;"/>
					</div>

					<div class="form-group row">
						<label for="city" class="col-md-3 col-form-label text-md-right">도시</label>
						<div class="input-group col-md-7">
							<div class="input-group-prepend">
								<span class="input-group-text">@</span>
							</div>
							<form:input path="city" id="city" class="form-control" placeholder="도시를 입력해 주세요" />
						</div>
					</div>
					<div class="row">
						<label for="cityError" class="col-md-3" text-md-right></label>
						<form:errors path="city" id="cityError" class="col-md-6 form-group text-left" style="font-size:15px; color:red; width=100px;"/>
					</div>

					<div class="form-group row">
						<label for="phone" class="col-md-3 col-form-label text-md-right">전화번호</label>
						<div class="input-group col-md-7">
							<div class="input-group-prepend">
								<span class="input-group-text">@</span>
							</div>
							<form:input path="phone" id="phone" class="form-control" placeholder="전화번호를 입력해 주세요" />
						</div>
					</div>
					<div class="row">
						<label for="phoneError" class="col-md-3" text-md-right></label>
						<form:errors path="phone" id="phoneError" class="col-md-6 form-group text-left" style="font-size:15px; color:red; width=100px;"/>
					</div>

					<div class="form-group row">
						<label for="age" class="col-md-3 col-form-label text-md-right">나이</label>
						<div class="input-group col-md-7">
							<div class="input-group-prepend">
								<span class="input-group-text">@</span>
							</div>
							<form:input path="age" id="age" class="form-control" placeholder="나이를 입력해 주세요" />
						</div>
					</div>
					<div class="row">
						<label for="ageError" class="col-md-3" text-md-right></label>
						<form:errors path="age" id="ageError" class="col-md-6 form-group text-left" style="font-size:15px; color:red; width=100px;"/>
					</div>

					<div class="form-group row">
					    <label for="gender" class="col-md-3 col-form-label text-md-right">성별</label>
						<div class="input-group col-md-7">
							<div class="input-group-prepend">
								<span class="input-group-text">@</span>
							</div>
							<form:radiobuttons path="gender" id="gender" items="${genders}" itemLabel="description" itemValue="id" class="form-control" />
						</div>
					</div>
					<div class="row">
						<label for="genderError" class="col-md-3" text-md-right></label>
						<form:errors path="gender" id="genderError" class="col-md-6 form-group text-left" style="font-size:15px; color:red; width=100px;"/>
					</div>
				</form:form>
			</div>
		</div>
		<div style="margin-top:10px">
			<button type="button" class="btn btn-sm btn-primary" id="btnSignup">회원가입</button>
			<button type="button" class="btn btn-sm btn-primary" id="btnCancle">취소</button>
		</div>
	</div>
</article>

