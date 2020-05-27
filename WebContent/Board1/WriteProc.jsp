<%@page import="model.BbsDAO"%>
<%@page import="model.BbsDTO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="../common/isLogin.jsp"%>
<%
/*
BoardWrite에서 입력한 값을 DB에 넣기위해 DTO객체에 저장해서
DAO로 보내서DB에 넣는다.
*/
request.setCharacterEncoding("UTF-8");

String title = request.getParameter("title");//제목
String content = request.getParameter("content");//내용

BbsDTO dto = new BbsDTO();
dto.setTitle(title);
dto.setContent(content);
dto.setId(session.getAttribute("USER_ID").toString());//세션에 저장되어있는 정보저장

BbsDAO dao = new BbsDAO(application);

int affected = dao.insertWrite(dto);

if(affected==1){
%>
	<!-- script보다 java가 먼저 실행되기 때문에 alert가 안보인다. -->
	<script>
		alert("글쓰기에 성공했습니다.");
	</script>
	
<%
	response.sendRedirect("BoardList.jsp");
	
}else{
%>	

	<script>
		alert("글쓰기에 실패하였습니다.");
	</script>
<%	
}

%>