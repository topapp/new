import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.*;
import javax.servlet.http.Cookie;
import net.sf.json.*;

@WebServlet("/dealregister")
public class dealregister extends HttpServlet{
	public dealregister()
	{
		super();
	}
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
    doPost(request,response);
  }
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
  {
    Connection conn=null;
    PreparedStatement pStmt=null;
    ResultSet rs=null;
    Statement stmt=null;
    
    //connect database
    try{                                                                        
        String driverClass="com.mysql.jdbc.Driver";
        String ur1="jdbc:mysql://localhost:3306/davidlee";
        Class.forName(driverClass);            
        conn=DriverManager.getConnection(ur1,"root","201231000713");
        stmt=conn.createStatement();
        rs=stmt.executeQuery("select * from myclients");
      }
    catch(ClassNotFoundException e){
        System.out.println(e.toString());
    }
    catch(SQLException e){
        System.out.println(e.toString());
    }
    String id=new String(request.getParameter("id").getBytes("ISO-8859-1"),"utf-8");
    String password=new String(request.getParameter("password").getBytes("ISO-8859-1"),"utf-8");
    String email=new String(request.getParameter("email").getBytes("ISO-8859-1"),"utf-8");
    String sex=new String(request.getParameter("sex").getBytes("ISO-8859-1"),"utf-8");
    String name=new String(request.getParameter("name").getBytes("ISO-8859-1"),"utf-8");

    JSONObject json=new JSONObject();
    
    try{                                          //confirm is the id has been register
      while(rs.next())
      {
        if(id.equals(rs.getString("id")))
        {
          json.put("information","id has been registered");
          response.setContentType("application/json");
          response.getWriter().write(json.toString());  
          response.getWriter().close();
          return;
        }
      }
    }
    catch(SQLException e){
      System.out.println(e.toString());
    }

                                                  //insert the user into the database
    try{
        pStmt=conn.prepareStatement("insert into myclients (name,password,sex,id,email) values(?,?,?,?,?)"); 
        pStmt.setString(1,name);
        pStmt.setString(2,password);
        pStmt.setString(3,sex);
        pStmt.setString(4,id);
        pStmt.setString(5,email);
        int rtn=pStmt.executeUpdate();
    }
    catch(SQLException e){
      System.out.println(e.toString());
    }

      //return the information to the clients
    json.put("information","success");
    response.setContentType("application/json");
    response.getWriter().write(json.toString());  
    response.getWriter().close();
      return;
  }
}

