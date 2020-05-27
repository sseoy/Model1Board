package model;

import java.sql.Date;

public class BbsDTO {
	
	/*
	 DTO객체를 만들때 테이블 컬럼의 타입과는 상관없이 대부분의
	 멤버변수는 String형으로 정의하면 된다. JSP에서 산술연산이
	 꼭 필요한 경우에만 int, double과 같이 숫자형으로 정의한다.
	 */
	//멤버변수
	private String num;		//게시판의 일련번호
	private String title;	//제목
	private String content;	//내용
	private String id;		//작성자 아이디(member테이블 참조)
	private Date postDate;//작성일
	private String visitcount;//조회수(오라클에서 계산함  update 테이블명 visitcount = visitcount+1 하면 계산됨)
	//멤버변수 추가 : board와 member테이블의 join을 위해 name컬럼 추가
	private String name;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	//getter,setter
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public java.sql.Date getPostDate() {
		return postDate;
	}
	public void setPostDate(java.sql.Date postDate) {
		this.postDate = postDate;
	}
	public String getVisitcount() {
		return visitcount;
	}
	public void setVisitcount(String visitcount) {
		this.visitcount = visitcount;
	}
}
