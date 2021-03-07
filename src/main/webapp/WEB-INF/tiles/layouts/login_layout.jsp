<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %> 

<!DOCTYPE html> <html lang="kr"> 
<head> 
<meta charset="utf-8"> 

<!-- jQuery --> 
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js">
</script> 

<!-- Bootstrap CSS --> 
<link rel="stylesheet" 
	  href="https://stackpath.bootstrapcdn.com/bootstrap/4.2.1/css/bootstrap.min.css" 
	  integrity="sha384-GJzZqFGwb1QTTN6wy59ffF1BuGJpLSa9DkKMp0DgiMDm4iYMj70gZWKYbI706tWS" 
	  crossorigin="anonymous"> 

<!-- Custom styles for this template -->
<link rel="stylesheet" type="text/css"
	  href="<c:url value='/resources/common/css/login.css'/>" >

<!-- common CSS --> 
<link rel="stylesheet" 
	  href="<c:url value='/resources/common/css/common.css'/>" > 
	  
<!-- Ajax jquery -->
<script src="http://code.jquery.com/jquery-latest.min.js"></script>
	  
<style> 
	body{padding : 0px} 
	#tile_body { width:100%; float:left; } 
</style> 
	
</head> 
<body class="text-center"> 
	<div id="tile_body">
		<tiles:insertAttribute name="tile_body" />
	</div>  
	<div id="tile_common">
		<tiles:insertAttribute name="tile_common" />
	</div>
</body> 
</html>

