<%@page import="model.BbsDTO"%>
<%@page import="model.BbsDAO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%--로그인 되어있는지 다시한번 확인 --%>
<%@ include file="../common/isLogin.jsp" %>
<%
request.setCharacterEncoding("UTF-8");

//폼에서 title, content수정했으니깐 title,content값을 가져온다.
String num = request.getParameter("num");
String title = request.getParameter("title");
String content = request.getParameter("content");

BbsDTO dto = new BbsDTO();
dto.setNum(num);
dto.setTitle(title);
dto.setContent(content);
//DAO객체 생성 및 DB연결
BbsDAO dao = new BbsDAO(application);
int affected = dao.updateEdit(dto); 
if(affected==1){
	//수정 성공이 되면 num의 값을 가지고 있는 상세보기 페이지로 간다
	response.sendRedirect("BoardView.jsp?num="+dto.getNum());
}
else{
	
%>
	<script>
		alert("수정하기에 실패하였습니다.");
		history.go(-1);
	</script>
<%	
}
%>