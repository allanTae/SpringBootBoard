<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE html>

<html>
<head>
<meta charset="UTF-8">
<c:url var="getBoardList" value="/myBoard/getBoardList">
</c:url>
<style>
	#paginationBox {
		margin-top: 10px;
	}
	#con-left {
		padding-right: 250px;
	}

	#con-right{
		width: 220px;
		height: 400px;
		background: rgb(0 0 0 / 8%);
		border=1px solid #ddd;
		position:absolute;
		top:46px;
		right:10px;
		text-align: center;
		color:#212529;
	}

	article > .container:nth-child(1) {
		max-width: 1300px;
		position:relative;
		//padding:20px;
	}

	article > .container:nth-child(2) {
	    max-width: 1300px;
	}


</style>
<script>
	$(document).on('click', '#btnWriteForm', function(e){
		e.preventDefault();
		location.href = "${pageContext.request.contextPath}/board/boardForm";
	});
	function fn_contentView(boardId){
		var url= "${pageContext.request.contextPath}/board/boardContent";
		url = url + "?boardId=" +boardId;
		location.href = url;
	}
	//이전 버튼 이벤트
	function fn_prev(page, range, rangeSize) {
		var page = ((range - 2) * rangeSize) + 1;
		var range = range - 1;
		var url = "${getBoardList}";
		url = url + "?page=" + page;
		url = url + "&range=" + range;
		url = url + "&searchType=" + "${pagination.searchType}";
		url = url + "&keyword=" + "${pagination.keyword}";
		location.href = url;
	}
	//페이지 번호 클릭
	function fn_pagination(page, range, rangeSize) {
		var url = "${getBoardList}";
		url = url + "?page=" + page;
		url = url + "&range=" + range;
		url = url + "&searchType=" + "${pagination.searchType}";
		url = url + "&keyword=" + "${pagination.keyword}";
		location.href = url;
	}
	//다음 버튼 이벤트
	function fn_next(page, range, rangeSize) {
		var page = (parseInt(range) * rangeSize) + 1;
		var range = parseInt(range) + 1;
		var url = "${getBoardList}";
		url = url + "?page=" + page;
		url = url + "&range=" + range;
		url = url + "&searchType=" + "${pagination.searchType}";
		url = url + "&keyword=" + "${pagination.keyword}";
		location.href = url;
	}

	// 검색 버튼 이벤트
    	$(document).on('click', '#btnSearch', function(e){
    		e.preventDefault();
    		var url = "${getBoardList}";
    		url = url + "?searchType=" + $('#searchType').val();
    		url = url + "&keyword=" + $('#keyword').val();
    		location.href = url;
    	});

	// 로그인 버튼 이벤트
	$(document).on('click', '#btnLogin', function(e){
		e.preventDefault();
		location.href = "${pageContext.request.contextPath}/user/login";
	});

	// 로그아웃 버튼 이벤트
	$(document).on('click', '#btnLogout', function(e){
        e.preventDefault();
		location.href = "${pageContext.request.contextPath}/user/logout";
	});

	// 사용자 관리 페이지 버튼 이벤트
	$(document).on('click', '#btnUserPage', function(e){
		e.preventDefault();
		location.href = "${pageContext.request.contextPath}/user/getUserList";
	});
</script>



<title>board</title>

</head>

<body>


<article>

	<!-- container {s} -->
	<div class="container">
		<h2>board</h2>

		<!-- content-left {s} -->
		<div class="table-responsive" id="con-left">
			<table class="table table-striped table-sm">
				<colgroup>
					<col style="width:5%;" />
					<col style="width:auto;" />
					<col style="width:15%;" />
					<col style="width:10%;" />
					<col style="width:10%;" />
				</colgroup>
				<thead>
					<tr>
						<th>NO</th>
						<th>글제목</th>
						<th>작성자</th>
						<th>조회수</th>
						<th>작성일</th>
					</tr>
				</thead>
				<tbody>
					<c:choose>
						<c:when test="${empty boardList }" >
							<tr><td colspan="5" align="center">데이터가 없습니다.</td></tr>
						</c:when>
						<c:when test="${!empty boardList}">
							<c:forEach var="list" items="${boardList}">
								<tr>
									<td><c:out value="${list.boardId}"/></td>
									<td>
										<a href="#" onclick="fn_contentView(<c:out value="${list.boardId}" />)">
											<c:out value="${list.title}" />
										</a>
									</td>
									<td><c:out value="${list.createdBy}"/></td>
									<td><c:out value="${list.viewCnt}"/></td>
									<td><c:out value="${list.createdDate}"/></td>
								</tr>
							</c:forEach>
						</c:when>
					</c:choose>
				</tbody>
			</table>
		</div>
		<!-- content-left {e} -->

		<!-- content-right {s} -->
		<div id="con-right">
			<div style=" margin-bottom: 20px; display: inline-block; padding:10px;">
			    <sec:authorize access="isAuthenticated()">
                    <div>
                        <p>${user_name}님</p>
                        <p>환영합니다.</p>
                    </div>
                    <form>
                        <div>
                            <button style="width:80px; display:inline;" class="btn btn-sm btn-primary" type="button" name="btnLogout" id="btnLogout">로그아웃</button>
                            <sec:authorize access="hasRole('ROLE_ADMIN')" >
                                <button style="width:100px; display:inline;" class="btn btn-sm btn-primary" type="button" name="btnUserPage" id="btnUserPage">관리자 페이지</button>
                            </sec:authorize>
			            </div>
			        </form>
                </sec:authorize>
                <sec:authorize access="!isAuthenticated()">
                    <div>
                        <form>
                            <button style="width:120px;" class="btn btn-sm btn-primary" type="button" name="btnLogin" id="btnLogin">로그인 하러 가기</button>
                        </form>
                    </div>
                </sec:authorize>
			</div>
		</div>
		<!-- content-right {e} -->
	</div>
	<!-- container{e} -->

	<!-- bottom-cotainer{s} -->
	<div id="bottomContainer" class="container">
		<!-- botton-area {s} -->
		<div id="bottomButtonAreaWrap">
			<button type="button" class="btn btn-sm btn-primary" id="btnWriteForm">글쓰기</button>
		</div>
		<!-- botton-area {e} -->

		<!-- pagination{s} -->
		<div id="paginationBoxWrap">
			<ul class="pagination" id="paginationBox">
				<c:if test="${pagination.prev}">
					<li class="page-item">
						<a class="page-link" href="#" onClick="fn_prev('${pagination.page}', '${pagination.range}', '${pagination.rangeSize}')">
							Previous
						</a>
					</li>
				</c:if>

				<c:forEach begin="${pagination.startPage}" end="${pagination.endPage}" var="idx">
					<li class="page-item <c:out value="${pagination.page == idx ? 'active' : ''}"/> ">
						<a class="page-link" href="#" onClick="fn_pagination('${idx}', '${pagination.range}', '${pagination.rangeSize}')">
							${idx}
						</a>
					</li>
				</c:forEach>

				<c:if test="${pagination.next}">
					<li class="page-item">
						<a class="page-link" href="#" onClick="fn_next('${pagination.range}', '${pagination.range}', '${pagination.rangeSize}')" >
							Next
						</a>
					</li>
				</c:if>
			</ul>
		</div>
		<!-- pagination{e} -->

		<!-- search{s} -->
        <div class="form-group row justify-content-center">
            <div class="w100" style="padding-right:10px">
                <select class="form-control form-control-sm" name="searchType" id="searchType">
                    <option value="title">제목</option>
                    <option value="content">본문</option>
                </select>
            </div>
            <div class="w300" style="padding-right:10px">
                <input type="text" class="form-control form-control-sm" name="keyword" id="keyword" value="${pagination.keyword}">
            </div>
            <div>
                <button class="btn btn-sm btn-primary" name="btnSearch" id="btnSearch">검색</button>
            </div>
        </div>
        <!-- search{e} -->
    </div>
    <!-- bottom-cotainer{e} -->


	</div>
	<!-- bottom-cotainer{e} -->

</article>

</body>

</html>