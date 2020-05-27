<%@page import="model.BbsDAO"%>
<%@page import="model.BbsDTO"%>
<%@page import="util.JavascriptUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%--삭제 Deleteproc--%>
<%@include file="../common/isLogin.jsp" %>
<%
String num = request.getParameter("num");

BbsDTO dto = new BbsDTO();
BbsDAO dao = new BbsDAO(application);

dto = dao.selectView(num);
int affected = dao.delete(dto);


if(affected==1){
	JavascriptUtil.jsAlertLocation("삭제되었습니다", 
		"BoardList.jsp", out);	
}
else{
	out.println(JavascriptUtil.jsAlertBack("삭제실패하였습니다"));
}
%>