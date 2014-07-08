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

@WebServlet("/deallogin")
public class deallogin extends HttpServlet{
	public deallogin()
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
		Statement stmt=null;
		ResultSet rs=null;
		//HttpSession session=request.getSession();
		try{
			String driverClass="com.mysql.jdbc.Driver";
			String ur1="jdbc:mysql://localhost:3306/davidlee";
			Class.forName(driverClass);                                                          //--------连接 mysql---------
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
		String id=request.getParameter("id");
		String password=request.getParameter("password");
		boolean isFoundId=false;
		JSONObject json=new JSONObject();
		try{																					//查找匹配账号密码
			while(rs.next())
			{
				if(!id.equals(rs.getString("id")))
					continue;
				isFoundId=true;
				if(password.equals(rs.getString("password")))
				{
					json.put("information","success");
					break;
				}
				else
				{
					json.put("information","incorrect password");
					break;
				}
			}
			if(isFoundId==false)
			{
				json.put("information","id not found");
			}
		}
		catch(SQLException e){
			System.out.println(e.toString());
		}
		response.setContentType("application/json");  
        response.getWriter().write(json.toString());  
        response.getWriter().close();
		try{
			if(rs!=null)
			rs.close();
			if(stmt!=null)
			stmt.close();
			if(conn!=null)
			conn.close();
		}
		catch(SQLException e){
			System.out.println(e.toString());
		}
		return;
	}
}