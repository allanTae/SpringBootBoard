<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<script>
    var errorMessage = "${errorMessage}";
    if(errorMessage){
        alert(errorMessage);
    }

    $(document).on('click', '#signUp', function(e){
        location.href ="${pageContext.request.contextPath}/member/signupForm";
        e.preventDefault();
    });

</script>

<!-- login form {s} -->
<form:form class="form-signin" name="form" id="form" role="form"
		   modelAttribute="loginForm" method="post" action="${pageContext.request.contextPath}/user/login">
	<div class="text-center mb-4">
		<h1 class="h3 mb-3 font-weight-normal">FAKEUSER.COM</h1>
	</div>
	<div class="form-label-group">
		<form:input path="userId" id="id" class="form-control" placeholder="User ID" required="" autofocus="" />
		<form:errors path="userId"/>
		<label for="id" class="sr-only">User ID</label>
	</div>
	<div class="form-label-group">
		<form:password path="userPwd" id="pwd" class="form-control" placeholder="User Password" required="" />
		<form:errors path="userPwd"/>
		<label for="pwd" class="sr-only">User Password</label>
	</div>
	<div class="checkbox">
        <label>
            <form:checkbox path="useCookie"/>아이디 기억
        </label>
    </div>
	<button class="btn btn-lg btn-primary btn-block" type="submit">Sign in</button>
	<a href="${pageContext.request.contextPath}/oauth2/authorization/google" class="btn btn-success active" role="button">Google Login</a>
	<a href="${pageContext.request.contextPath}/oauth2/authorization/naver" class="btn btn-secondary active" role="button">Naver Login</a>
	<a href="${pageContext.request.contextPath}/oauth2/authorization/kakao" class="btn btn-secondary" role="button">Kakao Login</a>
	<span style="font-size:11pt;">
		<a href="#" id="signUp">Sign up</a>
	</span>

</form:form>
<!-- login form {e} -->