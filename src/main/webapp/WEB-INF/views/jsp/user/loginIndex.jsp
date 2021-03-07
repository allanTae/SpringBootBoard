<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
 
<c:catch>
    <c:choose>
        <c:when test="${empty authInfo }">
            <li>
                 <a href="/account/login"><i class="fa fa-sign-in"></i> 로그인</a>
             </li>
             <li>
                 <a href="/account/register"><i class="fa fa-user"></i> 회원가입</a>
             </li>
        </c:when>
        <c:otherwise>
            <c:choose>
                <c:when test="${authInfo.grade.getGrade() eq 'manager' }">
                    <li>
                       <p>관리자 ${authInfo.name }님, 환영합니다.</p>
                   </li>
                   <li>
                       <a href="/account/logout"><i class="fa fa-sign-out"></i> 로그아웃</a>
                   </li>
                </c:when>
                <c:otherwise>
                    <li>
                       <p>${authInfo.name }님, 반갑습니다!</p>
                   </li>
                   <li>
                       <a href="/account/logout"><i class="fa fa-sign-out"></i> 로그아웃</a>
                   </li>
                </c:otherwise>
            </c:choose>
        </c:otherwise>
    </c:choose>
</c:catch>
