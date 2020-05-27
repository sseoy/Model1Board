

package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.ServletContext;

public class BbsDAO {
	Connection con;
	PreparedStatement psmt;
	ResultSet rs;
	
	public BbsDAO(String driver,String url) {
	      try {
	      Class.forName(driver);
	      String id = "kosmo";
	      String pw = "1234";
	      con = DriverManager.getConnection(url,id,pw);
	      System.out.println("DB연결성공");
	      }
	      catch(Exception e) {
	         e.printStackTrace();
	      }
	   }
	
	
	//application으로 받을때 필요하다.
	public BbsDAO(ServletContext ctx) {
	      try {
	      Class.forName(ctx.getInitParameter("JDBCDriver"));
	      String id = "kosmo";
	      String pw = "1234";
	      con = DriverManager.getConnection(
	    		  ctx.getInitParameter("ConnectionURL"),id,pw);
	      System.out.println("DB연결성공");
	      }
	      catch(Exception e) {
	    	 System.out.println("DB 연결실패ㅜㅜ;");
	         e.printStackTrace();
	      }
	   }
	
	//조회를 해서 행의 갯수
	public int getTotalRecordCount(Map<String, Object> map) {
		int totalCount = 0;
		
		String query = "SELECT COUNT(*) FROM board";
	      //JSP페이지에서 검색어를 입력한 경우 where절이 동적으로 추가됨.
		  //MAP은 가져올때 get을 쓴다.
	    if(map.get("Word") != null) {
	        query += " WHERE "+ map.get("Column")+" "+" LIKE '%"+map.get("Word")+"%'";
	    }
	    System.out.println("query="+query);
	   
	    try {
	       psmt = con.prepareStatement(query);
	       rs = psmt.executeQuery();
	       rs.next();
	       
	       //반환한 결과값(레코드수)을 저장
	       //getInt(1) : 1은 select문장에서 첫번째 항목을 가져오라는 의미
	       totalCount = rs.getInt(1);
	    }
	    catch(Exception e) {}
	      
	    return totalCount;
	}
	
	//select해서 결과값들을 list에 넣기
	public List<BbsDTO> selectList(Map<String, Object> map){
		 /*
		  List 컬렉션
		   - 객체를(데이터)를 저장하면 인덱스가 자동으로 부여되고 부여된 인덱스를 
		   	통해 데이터의 검색 및 삭제가 가능하다.(종류 : ArrayList, Vector, LinkedList)
		   	
		  
		  (1) List<E>객체명 = new ArrayList<E>([초기 저장용량]); 
		  	- 초기저장 용량을 생략하면 기본적으로 10의 저장용량을 갖는다.
		  	- E는 제네릭타입을 의미, 생략하면 Object타입이 된다.
		  	- 검색 및 맨 마지막에 객체 추가 등에서 좋은 성능을 발휘함.
		  	
		  (2) List<E> list = new Vector<e>([초기용량,</e> 증가용량]);
			- ArrayList와 동일한 내부 구조를 가지고 있다. 
			 Vector클래스는 동기화된 메서드로 구성, 멀티 스레드 환경에서 안전하게 객체를 추가, ㅅ학제 할수 있다.
			 동기화 되어있기 때문에 ArrayList보다는 객체를 추가, 삭제하는과정은 느릴수 밖에 없다.
		  */
	      List<BbsDTO> bbs = new Vector<BbsDTO>();
	      //기본쿼리문
	      String query = "SELECT * FROM board ";
	      
	      //검색어가 있는 경우 조건절 동적 추가
	      if(map.get("Word") != null) {
	         query += " WHERE "+ map.get("Column")+" "+" LIKE '%"+map.get("Word")+"%'";
	      }
	      
	      //최근게시물의 항상 위로 노출되야 하므로 작성된 순서의 역순으로 정렬한다.
	      query +=" ORDER BY num DESC";
	      try {
	         psmt = con.prepareStatement(query);
	         rs = psmt.executeQuery();
	         //오라클이 반환해준 ResultSet의 갯수만큼 반복한다.
	         while(rs.next()) {
	        	 //하나의 레코드를 DTO객체에 저장하기 위해 새로운 객체생성
	            BbsDTO dto = new BbsDTO();
	            
	            dto.setNum(rs.getString(1));
	            dto.setTitle(rs.getString("title"));
	            dto.setContent(rs.getString(3));
	            dto.setPostDate(rs.getDate("postdate"));
	            dto.setId(rs.getString("id"));
	            dto.setVisitcount(rs.getString(6));
	            
	            //저장된 DTO객체를 List컬렉션에 추가
	            bbs.add(dto);
	         }
	      }
	      catch(Exception e) {
	         System.out.println("Select시 예외발생");
	         e.printStackTrace();
	      }
	      return bbs;
	   }
	 
	 //조회수
	 public void updateVisitCount(String num) {

		  String query = "UPDATE board SET "
				   + " visitcount = visitcount+1 "
				   + " WHERE num=?";
		   System.out.println("조회수 증가 :"+query);
			try {
	   			psmt = con.prepareStatement(query);
	   			psmt.setString(1, num);//?에 받아온 num넣기
	   			psmt.executeQuery();
	   		}
	   		catch(Exception e) {
	   			System.out.println("조회수 증가시 예외발생");
	   			e.printStackTrace();
	   		}
	 }
	 
	
	 
	 //게시물 가져와서 DTO객체로 반환하기때문에 BbsDTO형으로 한다.
	 public BbsDTO selectView(String num) {
		   BbsDTO dto =  new BbsDTO();
		 
		   /*변경된쿼리문 : member테이블과 join하여 사용자 이름 가져옴.
				 (DTO에 name생성해야함)*/
		   //innerJoin
		  /* String query = "SELECT B.*, M.name " + 
		   		" FROM member M INNER JOIN board B " + 
		   		"    ON  M.id=B.id" + 
		   		" WHERE num = ?";*/
		   
		   /*변경된쿼리문 : member테이블과 join하여 사용자 이름 가져옴.
			 (DTO에 name생성해야함)*/
		   //join
		   String query = "SELECT B.*, M.name " + 
	   		" FROM member M, board B " + 
	   		" WHERE M.id = B.id AND num = ?";
		   try {
			   psmt = con.prepareStatement(query);
			   psmt.setString(1, num);
			   rs = psmt.executeQuery();
			   if(rs.next()) {
				   dto.setNum(rs.getString(1));
				   dto.setTitle(rs.getString(2));
				   dto.setContent(rs.getString("content"));
				   dto.setPostDate(rs.getDate("postdate"));
				   dto.setId(rs.getString("id"));
				   dto.setVisitcount(rs.getString(6));
				   //테이블join으로 컬럼추가
				   dto.setName(rs.getString("name"));
			   }
		   }catch(Exception e) {
	  			System.out.println("상세보기시 예외발생");
	  			e.printStackTrace();
	  		}
		   return dto;
	 }
	 //삽입
	 public int insertWrite(BbsDTO dto) {
		 int affected =0;
		 try {
			 String query = " INSERT INTO BOARD( num, title, content, id) "
			 		+ " VALUES (seq_board_num.NEXTVAL, ?, ?, ?)";
			 
			 psmt = con.prepareStatement(query);
			 psmt.setString(1,  dto.getTitle());
			 psmt.setString(2,  dto.getContent());
			 psmt.setString(3, dto.getId());
			 
			 affected = psmt.executeUpdate();
		 }catch(Exception e) {
			 System.out.println("insert하는중 예외");
			 e.printStackTrace();
		 }
		 return affected;
		 
	 }
	 //수정
	 public int updateEdit(BbsDTO dto) {
		 int affected =0;
		 try {
			 String query = " UPDATE board SET "
			 		+ " title=?, content=? "
			 		+ " WHERE num=?";
			 
			 psmt = con.prepareStatement(query);
			 psmt.setString(1, dto.getTitle());
			 psmt.setString(2, dto.getContent());
			 psmt.setString(3, dto.getNum());
			 
			 affected = psmt.executeUpdate();
		 }catch(Exception e) {
			 e.printStackTrace();
		 }
		 return affected;
	 }
	 //삭제
	 public int delete(BbsDTO dto) {
		   int affected = 0;
		   try {
			   String query = "DELETE FROM board WHERE num=?";
			   
			   psmt = con.prepareStatement(query);
			   psmt.setString(1, dto.getNum());
			   
			   affected = psmt.executeUpdate();
		   }
		   catch(Exception e) {
			   System.out.println("delete중 예외발생");
			   e.printStackTrace();
		   }
		   return affected;
	   }
	 
	 //DB자원해제
	 public void close() {
	      try {
	         if(rs != null) rs.close();
	         if(psmt != null) psmt.close();
	         if(con != null) con.close();
	      }
	      catch(Exception e) {
	         System.out.println("자원반납시 예외발생");
	      }
	   }
}
