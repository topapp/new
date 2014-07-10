import java.io.File;  
import java.io.IOException;  
import java.util.*;  
import java.text.*;
  
import javax.servlet.ServletException;  
import javax.servlet.http.HttpServlet; 
import javax.servlet.annotation.WebServlet; 
import javax.servlet.http.HttpServletRequest;  
import javax.servlet.http.HttpServletResponse;  
import java.sql.*;  
import net.sf.json.*; 
  
@WebServlet("/DownloadDiary")


public class DownloadDiary extends HttpServlet {  
  
  
    public void doGet(HttpServletRequest request, HttpServletResponse response)  
            throws ServletException, IOException {  
        this.doPost(request, response);  
    }  
  
    public void doPost(HttpServletRequest request, HttpServletResponse response)  
            throws ServletException, IOException {  
        request.setCharacterEncoding("utf-8");   
        Connection conn=null;
        Statement stmt=null;
        ResultSet rs=null;
        String id=request.getParameter("id");
        int page=Integer.parseInt(request.getParameter("page"));
        int pagesize=Integer.parseInt(request.getParameter("pagesize"));
        JSONObject json=new JSONObject();
        List list=new ArrayList();
        String url="http://192.168.23.1:8080/apache-tomcat-7.0.47/webapps/test/clients/";
        try{
            String driverClass="com.mysql.jdbc.Driver";
            String ur1="jdbc:mysql://localhost:3306/davidlee";
            Class.forName(driverClass);                                                          //--------连接 mysql---------
            conn=DriverManager.getConnection(ur1,"root","201231000713");                         
            stmt=conn.createStatement();
            rs=stmt.executeQuery("select * from diary where id = "+id+" order by time desc");
            }
        catch(ClassNotFoundException e){
            System.out.println(e.toString());
        }
        catch(SQLException e){
            System.out.println(e.toString());
        }
        try{                                                                                    //查找匹配账号密码
            while(rs.next())
            {
                String url1,url2,url3;
                int picturenum;
                Timestamp date=rs.getTimestamp("time");
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String strTime = dateFormat.format(date);
                url1=url+rs.getSring("pic1");
                url2=url+rs.getSring("pic2");
                url3=url+rs.getSring("pic3");
                picturenum=rs.getInt("picturenum");
                list.add(new Diary(url1,url2,url3,picturenum,strTime));
            }
        }
        catch(SQLException e){
            System.out.println(e.toString());
        }

        for(int i=0;i<pagesize;i++)
        {
            if(i+(page-1)*pagesize>=list.size())
                break;
            else
            {

            }
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
public class Diary{
    public String url1;
    public String url2;
    public String url3;
    public int picturenum;
    public String time;
    public void(String url1,String url2,String url3,int picturenum,String time)
    {
        this.url1=url1;
        this.url2=url2;
        this.url3=url3;
        this.picturenum=picturenum;
        this.time=time;
    }
}