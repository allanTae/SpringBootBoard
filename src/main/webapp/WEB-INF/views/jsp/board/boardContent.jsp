<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">

<title>board</title>
<script>
    // 스프링 시큐리티, ajax 처리를 위한 csrf 토큰 정보와 토큰을 서버에 전달하기 위한 헤더 이름.
    var token = '${_csrf.token}';
    var headerName = '${_csrf.headerName}';

	// escape 처리를 위한 객체 및 메소드
	var entityMap = { '&': '&amp;', 
				  '<': '&lt;', 
				  '>': '&gt;', 
				  '"': '&quot;', 
				  "'": '&#39;', 
				  '/': '&#x2F;', 
				  '`': '&#x60;', 
				  '=': '&#x3D;' };
				  
	function escapeHtml (string) { 
		return String(string).replace(/[&<>"'`=\/]/g, function (s) { return entityMap[s]; }); 
		
	}

	// 로그인 아이디, 회원 이름
	//var authId = ${userInfo.authId};

	// 댓글 리스트 갱신
	$(document).ready(function(){
		 showReplyList();
	});

	//목록 버튼 클릭 이벤트
	$(document).on('click', '#btnList', function(){

		location.href = "${pageContext.request.contextPath}/board/getBoardList";

	});

	//수정 버튼 클릭 이벤트
	$(document).on('click', '#btnUpdate', function(){
        var url = "${pageContext.request.contextPath}/board/editForm";
        url = url + "?boardId="+${boardContent
        .boardId};
        url = url + "&mode=edit";

        location.href = url;
	});

	// 삭제 버튼 클릭 이벤트
	$(document).on('click', '#btnDelete', function(){

        var url = "${pageContext.request.contextPath}/board/deleteBoard";
        url = url + "?boardId="+${boardContent.boardId};

        location.href = url;
		
	});
	
	// 댓글 리스트를 위한 AJax 처리
	function showReplyList(){
		var url = "${pageContext.request.contextPath}/boards/${boardContent.boardId}/replies";
		var paramData = {"boardId" : "${boardContent.boardId}"};
		$.ajax({
            type: 'GET',
            url: url,
            data: paramData,
            dataType: 'json',
            beforeSend: function(xhr){
                    xhr.setRequestHeader(headerName, token);
                },
            success: function(result) {
               	var htmls = "";
			if(result.length < 1){
				htmls += "<strong>등록된 댓글이 없습니다.</strong>";
			} else {
                $(result).each(function(){
                	htmls += makeHTML(this);
	        	});	//each end
			}
			$("#replyList").html(htmls);
            },	   // Ajax success end
            error:function(request,status,error){
                alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);}
		});	// Ajax end
	}


    //  댓글 HTML 생성 메소드
    function makeHTML(reply){
        var htmls = "";
        if(!reply.isRemove){
            htmls += '<div class="media text-muted pt-3" id="replyId' + reply.replyId + '">';
            htmls += '<div style="padding-left:' + 50*reply.depth + 'px"></div>'
            htmls += '<svg class="bd-placeholder-img mr-2 rounded" width="32" height="32" xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid slice" focusable="false" role="img" aria-label="Placeholder:32x32">';
            htmls += '<title>Placeholder</title>';
            htmls += '<rect width="100%" height="100%" fill="#007baf"></rect>';
            htmls += '<text x="50%" fill="#007bff" dy=".3em">32x32</text>';
            htmls += '</svg>';
            htmls += '<p class="media-body pb-3 mb-0 small lh-125 border-bottom horder-gray">';
            htmls += '<span class="d-block">';
            htmls += '<strong class="text-gray-dark">' + reply.nickName + '</strong>';
            htmls += '<span style="padding-left: 7px; font-size: 9pt">';
            htmls += '<a href="javascript:void(0)" onclick="fn_editReply(' + reply.replyId + ', \'' + reply.registerId + '\', \'' + escapeHtml(reply.content) + '\', \'' + reply.userName + '\')" style="padding-right:5px">수정</a>';
            htmls += '<a href="javascript:void(0)" onclick="fn_deleteReply(' + reply.replyId + ')" style="padding-right:5px">삭제</a>';
            htmls += '<a href="javascript:void(0)" onclick="fn_replyForm(' + reply.replyGroup + ', ' + reply.replyGroupOrder + ', ' + reply.replyId + ', ' + reply.depth + ' )" >댓글작성</a>';
            htmls += '</span>';
            htmls += '</span>';
            htmls += escapeHtml(reply.content);
            htmls += '</p>';
            htmls += '</div>';
            htmls += '<div id="' + reply.replyId + 'replyId">';
            htmls += '</div>';
        }else{
            htmls += '<div class="media text-muted pt-3" id="replyId' + reply.replyId + '">';
            htmls += '<div style="padding-left:' + 50*reply.depth + 'px"></div>'
            htmls += '<p class="media-body pb-3 mb-0 small lh-125 border-bottom horder-gray">';
            htmls += '<span class="d-block">';
            htmls += '</span>';
            htmls += "<strong>삭제 된 댓글입니다.</strong>";
            htmls += '</p>';
            htmls += '</div>';
            htmls += '<div id="' + reply.replyId + 'replyId">';
            htmls += '</div>';
        }

        return htmls;
    }

	
	// 부모 댓글 저장 버튼 클릭 이벤트
	$(document).on('click', '#btnReplyParentSave', function(e){
	    e.preventDefault();
        var replyContent = $('#content').val();
        var registerId = "${userInfo.authId}";
        var paramData = JSON.stringify({"content": replyContent
                                      , "registerId": registerId
                                      , "boardId":'${boardContent.boardId}'
        });

        var headers = {"Content-Type" : "application/json"
                , "X-HTTP-Method-Override" : "POST"
                , "Accept" : "application/json"};
        $.ajax({
            url: "${pageContext.request.contextPath}/boards/${boardContent.boardId}/replies/parent-reply"
            , headers : headers
            , data : paramData
            , type : 'POST'
            , beforeSend: function(xhr){
                                xhr.setRequestHeader(headerName, token);
                            }
            , dataType : 'text'
            , success: function(result){
                console.log(result);
                showReplyList();
                $('#content').val('');
            }
            , error:function(request,status,error){
                alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);}
        });
	});
	
	// 자식 댓글 저장 버튼 클릭 이벤트
	$(document).on('click', '#btnReplyChildSave', function(){
        var childReplyContent = $('#childContent').val();
        var childRegisterId = $('#childRegisterId').val(); // 나중에 스프링 시큐리티 공부 후 현재 로그인 된 회원 아이디로 변경 필요.
        var parentReplyGroup = $('#parentReplyGroup').val();
        var parentReplyGroupOrder = $('#parentReplyGroupOrder').val();
        var parentDepth = $('#parentDepth').val();
        var paramData = JSON.stringify({"content": childReplyContent
                                      , "registerId": childRegisterId
                                      , "boardId":'${boardContent.boardId}'
                                      , "parentReplyGroup": parentReplyGroup
                                      , "parentReplyGroupOrder": parentReplyGroupOrder
                                      , "parentDepth": parentDepth
        });
        console.log(paramData);
        var headers = {"Content-Type" : "application/json"
                , "X-HTTP-Method-Override" : "POST"};
        $.ajax({
            url: "${pageContext.request.contextPath}/boards/${boardContent.boardId}/replies/child-reply"
            , headers : headers
            , data : paramData
            , type : 'POST'
            , beforeSend: function(xhr){
                xhr.setRequestHeader(headerName, token);
            }
            , dataType : 'text'
            , success: function(result){
                console.log(result);
                if (result == "max_depth"){
                    alert("해당 댓글에 대한 답변 댓글 작성 수가 최대입니다.");
                }
                showReplyList();
            }
            , error:function(request,status,error){
                alert(request.responseText);}
        });
	});
	
	// 댓글 수정 버튼 클릭 메소드
	function fn_editReply(replyId, registerId, content, userName){
	    console.log("registerId: " + registerId);
	    console.log("authId: " + "${userInfo.authId}");
	    console.log("userName: " + userName);
	    if(registerId == "${userInfo.authId}"){
	        var htmls = "";
                    htmls += '<div class="media text-muted pt-3" id="replyId' + replyId + '">';
                    htmls += '<svg class="bd-placeholder-img mr-2 rounded" width="32" height="32" xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid slice" focusable="false" role="img" aria-label="Placeholder:32x32">';
                    htmls += '<title>Placeholder</title>';
                    htmls += '<rect width="100%" height="100%" fill="#007bff"></rect>';
                    htmls += '<text x="50%" fill="#007bff" dy=".3em">32x32</text>';
                    htmls += '</svg>';
                    htmls += '<p class="media-body pb-3 mb-0 small lh-125 border-bottom horder-gray">';
                    htmls += '<span class="d-block">';
                    htmls += '<strong class="text-gray-dark">' + userName + '</strong>';
                    htmls += '<span style="padding-left: 7px; font-size: 9pt">';
                    htmls += '<a href="javascript:void(0)" onclick="fn_updateReply(' + replyId + ', \'' + registerId + '\'' + ')" style="padding-right:5px">저장</a>';
                    htmls += '<a href="javascript:void(0)" onClick="showReplyList()">취소<a>';
                    htmls += '</span>';
                    htmls += '</span>';
                    htmls += '<textarea name="editContent" id="editContent" class="form-control" rows="3">';
                    htmls += content;
                    htmls += '</textarea>';
                    htmls += '</p>';
                    htmls += '</div>';
                    $('#replyId' + replyId).replaceWith(htmls);
                    $('#replyId' + replyId + ' #editContent').focus();
	    }else{
	        alert("직접 작성하신 댓글만 수정이 가능합니다.");
	    }
	}

	// 댓글 수정 후 저장 버튼 클릭 메소드
	function fn_updateReply(replyId, registerId){
	    console.log("fn_updateReply");
	    console.log("replyId: " + replyId);
	    console.log("registerId: " + registerId);
		var replyEditContent = $('#editContent').val();
		var paramData = JSON.stringify({"content": replyEditContent
				                      , "replyId": replyId
				                      , "registerId": registerId
		});
		
		var headers = {"Content-Type" : "application/json"
				, "X-HTTP-Method-Override" : "PUT"};
		$.ajax({
			url: "${pageContext.request.contextPath}/boards/${boardContent.boardId}/replies/" + replyId
			, headers : headers
			, data : paramData
			, type : 'PUT'
			, dataType : 'text'
			, beforeSend: function(xhr){
                            xhr.setRequestHeader(headerName, token);
                        }
			, success: function(result){
                console.log(result);
				showReplyList();
			}
			, error: function(error){
				alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
			}
		});
	}

	// 댓글 삭제 버튼 이벤트 메소드
	function fn_deleteReply(replyId){
        var paramData = JSON.stringify({"replyId": replyId
                                        , "boardId": '${boardContent.boardId}'});
        var headers = {"Content-Type" : "application/json"
            , "X-HTTP-Method-Override" : "DELETE"};
        $.ajax({
            url: "${pageContext.request.contextPath}/boards/${boardContent.boardId}/replies/" + replyId
            , headers : headers
            , data : paramData
            , type : 'DELETE'
            , dataType : 'text'
            , beforeSend: function(xhr){
                            xhr.setRequestHeader(headerName, token);
                        }
            , success: function(result){
                console.log(result);
                showReplyList();
            }
            , error: function(error){
                alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
            }
        });
	}
	
	// 대 댓글 폼 생성 이벤트 메소드 
 	function fn_replyForm(replyGroup, replyGroupOrder, replyId, depth){
 	    console.log("parentReplyGroup: " + replyGroup + ", parentReplyGroupOrder: " + replyGroupOrder + ", parentDepth: " + depth);
        var htmls = "";
        htmls += '<div class="my-3 p-3 bg-white rounded shadow-sm" style="padding-top: 10px">';
        htmls += '<form name="form" id="form" role="form" method="post">';
        htmls += '<div class="row">';
        htmls += '<div class="col-sm-10">';
        htmls += '<textarea id="childContent" class="form-control" rows="3" placeholder="댓글을 입력해 주세요"></textarea>';
        htmls += '</div>';
        htmls += '<div class="col-sm-2">';
        htmls += '<input type=text class="form-control" id="childRegisterId" value="${userInfo.nickname}" readonly="true"></input>';
        htmls += '<input type=hidden id="parentReplyGroup" value=' + replyGroup + ' ></input>';
        htmls += '<input type=hidden id="parentReplyGroupOrder" value=' + replyGroupOrder + ' ></input>';
        htmls += '<input type=hidden id="parentDepth" value=' + depth + ' ></input>';
        htmls += '<button type="button" class="btn btn-sm btn-primary" id="btnReplyChildSave" style="width: 100%; margin-top: 10px"> 저 장 </button>';
        htmls += '<button type="button" class="btn btn-sm btn-primary" onclick="fn_replyFormCancle(' + '\'' + replyId + '\')" style="width: 100%; margin-top: 10px"> 취 소 </button>';
        htmls += '</div>';
        htmls += '</div>';
        htmls += '</form>';
        htmls += '</div>';

        var replyForm = '#' + replyId + 'replyId';
        $(replyForm).html(htmls);
	}
	
	function fn_replyFormCancle(replyId){
		var replyForm = '#' + replyId + 'replyId';
		$(replyForm).html("");
	}

</script>
</head>
<body>
	<article>
		<div class="container" role="main">
			<h2>board Content</h2>
			<div class="bg-white rounded shadow-sm">
				<div class="board_title">
					<c:out value="${boardContent.title}"/>
				</div>
				<div class="board_info_box">
					<span class="board_author">
						<c:out value="${boardContent.nickName}"/>,
					</span>
					<span class="board_date">
						<c:out value="${boardContent.nickName}"/>
					</span>
				</div>
				<div class="board_content">
					${boardContent.content}
				</div>
				<div class="board_tag">
					TAG : <c:out value="${boardContent.tag}"/>
				</div>
			</div>

			<div style="margin-top : 20px">
				<button type="button" class="btn btn-sm btn-primary" id="btnUpdate">수정</button>
				<button type="button" class="btn btn-sm btn-primary" id="btnDelete">삭제</button>
				<button type="button" class="btn btn-sm btn-primary" id="btnList">목록</button>
			</div>
			
			<!-- Reply Form {s} -->
			<div class="my-3 p-3 bg-white rounded shadow-sm" style="padding-top: 10px">
                <form:form name="form" id="form" role="form" modelAttribute="replyDTO" method="post">
                    <div class="row">
                        <div class="col-sm-10">
                            <form:textarea path="content" id="content" class="form-control" rows="3" placeholder="댓글을 입력해 주세요"></form:textarea>
                        </div>
                        <div class="col-sm-2">
                            <p>작성자 : ${userInfo.nickname}</p>
                            <button type="button" class="btn btn-sm btn-primary" id="btnReplyParentSave" style="width: 100%; margin-top: 10px"> 저 장 </button>
                        </div>
                    </div>
                </form:form>
            </div>
			<!-- Reply Form {e} -->

			<!-- Reply List {s}-->
			<div class="my-3 p-3 bg-white rounded shadow-sm" style="padding-top: 10px">
				<h6 class="border-bottom pb-2 mb-0">Reply list</h6>
				<div id="replyList"></div>
			</div> 
			<!-- Reply List {e}-->
		</div>
	</article>
</body>
</html>

