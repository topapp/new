import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.fileupload.*;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import java.io.File;
import java.util.*;

@WebServlet("/Upload")
public class Upload extends HttpServlet{
	public Upload()
	{
		super();
	}
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{

	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		HttpSession session=request.getSession();
		String message="Upload success!";
		String address="";
		if(ServletFileUpload.isMultipartContent(request))
		{
			DiskFileItemFactory factory=new DiskFileItemFactory();
			factory.setSizeThreshold(20*1024);
			factory.setRepository(factory.getRepository());
			ServletFileUpload upload=new ServletFileUpload(factory);
			int size=2*1024*1024;
			List formlists=null;
			try{
				formlists=upload.parseRequest(request);
			}catch(FileUploadException e){
				e.printStackTrace();
			}
			Iterator iter=formlists.iterator();
			while(iter.hasNext())
			{
				FileItem formitem=(FileItem)iter.next();
				if(!formitem.isFormField())
				{
					String name=formitem.getName();
					if(formitem.getSize()>size)
					{
						message="Failed to upload!\nfile more than 2M!";
						break;
					}
					String adjunctsize=new Long(formitem.getSize()).toString();
					if(name==null||adjunctsize.equals("0"))
						continue;
					String adjuntname=name.substring(name.lastIndexOf("\\")+1,name.length());
					File saveFile=new File(address+adjuntname);
					try{
						formitem.write(saveFile);
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}
		}
		session.setAttribute("result",message);
		response.sendRedirect("Index.jsp");
	}
}