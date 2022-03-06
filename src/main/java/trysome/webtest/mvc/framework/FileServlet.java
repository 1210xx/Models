package trysome.webtest.mvc.framework;

import jdk.internal.util.xml.impl.Input;
import org.apache.jasper.tagplugins.jstl.core.Out;
import org.apache.tomcat.util.http.fileupload.IOUtils;

import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@WebServlet(urlPatterns = {"/favicon.io","/static/*"})
public class FileServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletContext context = req.getServletContext();

        String urlPath = req.getRequestURI().substring(context.getContextPath().length());

        String filePath = context.getRealPath(urlPath);

        if (filePath == null){
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        Path path = Paths.get(filePath);
        if (!path.toFile().isFile()){
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String mine = Files.probeContentType(path);
        if (mine == null){
            mine = "application/octet-stream";
        }
        resp.setContentType(mine);
        OutputStream outputStream = resp.getOutputStream();
        try(InputStream inputStream = new BufferedInputStream(new FileInputStream(filePath))) {
//            inputStream.transferTo(outputStream);
            IOUtils.copy(inputStream,outputStream);
        }
        outputStream.flush();
    }
}
