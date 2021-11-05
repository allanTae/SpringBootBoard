<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>


<!DOCTYPE html> <html lang="kr"> 
<head>
<meta charset="utf-8">

<!-- jQuery -->
<script src="https://code.jquery.com/jquery-1.12.4.min.js"></script>
</script>

<!-- Bootstrap CSS -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"></script>


<!-- Custom styles for this template -->
<link rel="stylesheet" type="text/css"
	  href="<c:url value='/resources/common/css/login.css'/>" >

<!-- common CSS -->
<link rel="stylesheet"
	  href="<c:url value='/resources/common/css/common.css'/>" >

<!-- Ajax jquery -->
<script src="http://code.jquery.com/jquery-latest.min.js"></script>

<!-- SWAL -->
<script src="https://unpkg.com/sweetalert/dist/sweetalert.min.js"></script>

<style>
	body{padding : 0px}
	#tile_body { width:100%; float:left; }
</style>
	
</head> 
<body class="text-center">
    <div id="tile_common">
        <tiles:insertAttribute name="tile_common" />
    </div>
	<div id="tile_body">
		<tiles:insertAttribute name="tile_modal_find-pwd" />
		<tiles:insertAttribute name="tile_body" />
	</div>
</body>
</html>

