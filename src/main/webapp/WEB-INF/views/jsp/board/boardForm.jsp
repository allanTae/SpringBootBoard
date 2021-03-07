<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri = "http://www.springframework.org/tags/form" %> 

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">

<title>board</title>
	<script src="https://cdn.ckeditor.com/ckeditor5/23.1.0/classic/ckeditor.js"></script>
	<script>
		$(document).on('click', '#btnSave', function(e){
			e.preventDefault();
			$("#form").submit();
		});
		
		$(document).on('click', '#btnList', function(e){
			e.preventDefault();
			location.href="${pageContext.request.contextPath}/board/getBoardList";
		});
		
		$(document).ready(function(){
			// '새로 글쓰기' 와 '글 수정' 기능을 나누기 위한 일종의 토글
			var mode = '<c:out value="${mode}"/>';
			if ( mode == 'edit'){
				//입력 폼 셋팅
				$("input:hidden[name='mode']").val('<c:out value="${mode}"/>');
			}

		});
	
	</script>
</head>
<body>
	<article>
		<div class="container" role="main">
			<h2>board Form</h2>
			<form:form name="form" id="form" role="form" modelAttribute="boardForm" method="post" action="${pageContext.request.contextPath}/board/saveBoard">

					<form:hidden path="boardId" />
					<input type="hidden" name="mode" />
					
					<div class="mb-3">
						<label for="title">제목</label>
						<form:input path="title" id="title" class="form-control" placeholder="제목을 입력해 주세요" />
					</div>
					
					<div class="mb-3">
						<label for="registerId">작성자</label>
						<form:input path="registerId" id="registerId" class="form-control" readonly="true"  />
					</div>
					
					<div class="mb-3">
						<label for="content">내용</label>
						<form:textarea path="content" id="content" class="form-control" rows="5" placeholder="내용을 입력해 주세요" />	
					</div>
					
					<div class="mb-3">
						<label for="tag">TAG</label>
						<form:input path="tag" id="tag" class="form-control" placeholder="태그를 입력해 주세요" />	
					</div>
			</form:form>
			<div >
				<button type="button" class="btn btn-sm btn-primary" id="btnSave">저장</button>
				<button type="button" class="btn btn-sm btn-primary" id="btnList">목록</button>
			</div>
		</div>
	</article>
</body>
<%@ include file="/WEB-INF/views/jsp/common/ckeditor.js.jsp"%>
</html>
