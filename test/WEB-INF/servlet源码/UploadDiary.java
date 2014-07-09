import java.io.File;  
import java.io.IOException;  
import java.util.Iterator;  
import java.util.List;  
  
import javax.servlet.ServletException;  
import javax.servlet.http.HttpServlet; 
import javax.servlet.annotation.WebServlet; 
import javax.servlet.http.HttpServletRequest;  
import javax.servlet.http.HttpServletResponse;  
import java.sql.*;
import org.apache.commons.fileupload.FileItem;  
import org.apache.commons.fileupload.disk.DiskFileItemFactory;  
import org.apache.commons.fileupload.servlet.ServletFileUpload;  
  
@WebServlet("/UploadDiary")


public class UploadDiary extends HttpServlet {  
  
  
    public void doGet(HttpServletRequest request, HttpServletResponse response)  
            throws ServletException, IOException {  
        this.doPost(request, response);  
    }  
  
    public void doPost(HttpServletRequest request, HttpServletResponse response)  
            throws ServletException, IOException {  
        request.setCharacterEncoding("utf-8");   
        String address ="d:\\apache-tomcat-7.0.47\\webapps\\test\\clients\\";
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        String id;
        String diary;
        int picturenum;
        java.util.Date  date=new java.util.Date();                                  //current time
        java.sql.Date  time=new java.sql.Date(date.getTime());


        Connection conn=null;                                                       //connect database
        PreparedStatement pStmt=null;
        try{                                                                        
        String driverClass="com.mysql.jdbc.Driver";
        String ur1="jdbc:mysql://localhost:3306/davidlee";
        Class.forName(driverClass);            
        conn=DriverManager.getConnection(ur1,"root","201231000713");
        }
        catch(ClassNotFoundException e){
        System.out.println(e.toString());
        }
        catch(SQLException e){
        System.out.println(e.toString());
        }



        if(isMultipart){  
            DiskFileItemFactory factory = new DiskFileItemFactory();  
            ServletFileUpload upload = new ServletFileUpload(factory);  
            try {  
                List<FileItem> items = upload.parseRequest(request);  
                   
                for(FileItem item : items){                                   //receive the photos or file in the diary
                    if(!item.isFormField()){ 
                        String name =item.getName();  
                        item.write(new File(address + name.substring(name.lastIndexOf("/")+1,name.length())));  
                    }
                    else
                    {                                                        //key=value  
                        String name = item.getFieldName();  
                        String value = item.getString();  
                        if(name.equals("diary"))
                        {
                            diary=value;
                        }
                        else if(name.equals("id"))
                        {
                            id=value;
                        }
                        else if(name.equals("picturenum"))
                        {
                            picturenum=Integer.parseInt(value);
                        }
                    }
                }
                  
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
        }
        try{
            pStmt=conn.prepareStatement("insert into diary (id,diary,picturenum,time) values(?,?,?,?)"); 
            pStmt.setString(1,id);
            pStmt.setString(2,diary);
            pStmt.setInt(3,picturenum);
            pStmt.setDateTime(4,time);
            int rtn=pStmt.executeUpdate();
        }
        catch(SQLException e){
          System.out.println(e.toString());
        }
    }  
}  