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
        response.setCharacterEncoding("utf-8");   
        Connection conn=null;
        Statement stmt=null;
        ResultSet rs=null;
        String id=request.getParameter("id");
        int page=Integer.parseInt(request.getParameter("page"));
        int pagesize=Integer.parseInt(request.getParameter("pagesize"));
        System.out.println(page);
        System.out.println(pagesize);
        System.out.println(id);
        JSONObject json=new JSONObject();
        List<Diary> list=new ArrayList<Diary>();
        String url="http://192.168.23.1:8080/test/clients/";
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
        try{                                                                                    //
            while(rs.next())
            {
                String url1,url2,url3,diary;
                int picturenum;
                Timestamp date=rs.getTimestamp("time");
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String strTime = dateFormat.format(date);
                url1=url+rs.getString("pic1");
                url2=url+rs.getString("pic2");
                url3=url+rs.getString("pic3");
                picturenum=rs.getInt("picturenum");
                diary=rs.getString("diary");
                list.add(new Diary(url1,url2,url3,picturenum,strTime,diary));
            }
        }
        catch(SQLException e){
            System.out.println(e.toString());
        }
        
        int i;
        for(i=0;i<pagesize;i++)
        {
            if(i+(page-1)*pagesize>=list.size())
                break;
            else
            {
                Diary diary=(Diary)list.get(i+(page-1)*pagesize);
                json.put("diary"+(i+1)+"url1",diary.url1);
                json.put("diary"+(i+1)+"url2",diary.url2);
                json.put("diary"+(i+1)+"url3",diary.url3);
                json.put("diary"+(i+1),diary.diary);
                json.put("picturenum"+(i+1),""+diary.picturenum);
                json.put("time"+(i+1),diary.time);
            }
        }
        json.put("diarynum",""+i);
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
} /* 
class Diary{
    public String url1;
    public String url2;
    public String url3;
    public int picturenum;
    public String time;
    public String diary;
    public Diary(String url1,String url2,String url3,int picturenum,String time,String diary)
    {
        this.url1=url1;
        this.url2=url2;
        this.url3=url3;
        this.picturenum=picturenum;
        this.time=time;
        this.diary=diary;
    }
}*/