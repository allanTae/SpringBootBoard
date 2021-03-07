<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="com.allan.springBootBoard.web.board.domain.model.BoardForm"%>

	<!-- 게시물 내용 홀 따옴표 이스케이프 -->
    <% BoardForm boardForm = (BoardForm)request.getAttribute("boardForm");
       if(boardForm.getContent() != null){
    	   boardForm.setContent(boardForm.getContent().replaceAll("\'", "\\\\'").replaceAll("\"", "\\\\\""));
           
           request.setAttribute("boardForm", boardForm);
       }
    %>

<script>

	ClassicEditor.create( document.querySelector( '#content' ) )
	.then( editor => {
	     console.log( editor );
	     editor.setData('${boardForm.content}');
	     console.log(editor.getData());
	 } )
	.catch( error => {
	     console.error( error );
	 } );
</script>