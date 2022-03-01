package trysome.webtest;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * <p>
 *      <br>
 *      一个Servlet总是继承自HttpServlet，然后覆写doGet()或doPost()方法。
 *      <br>
 *      注意到doGet()方法传入了HttpServletRequest和HttpServletResponse两个对象，分别代表HTTP请求和响应。
 *      <br>
 *      我们使用Servlet API时，并不直接与底层TCP交互，也不需要解析HTTP协议，
 *      因为HttpServletRequest和HttpServletResponse就已经封装好了请求和响应。
 *      <br>
 *      以发送响应为例，我们只需要设置正确的响应类型，然后获取PrintWriter，写入响应即可。
 * </p>
 *
 * <p>
 *      <br>
 *      实际上，类似Tomcat这样的服务器也是Java编写的，启动Tomcat服务器实际上是启动Java虚拟机，执行Tomcat的main()方法，
 *      然后由Tomcat负责加载我们的.war文件，并创建一个ServeletTest实例，最后以多线程的模式来处理HTTP请求。
 * </p>
 * <p>
 *      <br>
 *      如果Tomcat服务器收到的请求路径是/（假定部署文件为ROOT.war），就转发到ServeletTest并传入HttpServletRequest和HttpServletResponse两个对象。
 * </p>
 *
 * <p>
 *      因为我们编写的Servlet并不是直接运行，而是由Web服务器(Tomcat)加载后创建实例运行，所以，类似Tomcat这样的Web服务器也称为Servlet容器。
 * </p>
 *
 *
 *  <p>
 *      在Servlet容器中运行的Servlet具有如下特点：
 *  </p>
 *
 * <p>
 *      无法在代码中直接通过new创建Servlet实例，必须由Servlet容器(Tomcat)自动创建Servlet实例；
 *      <br>
 *      Servlet容器(Tomcat)只会给每个<b>Servlet类</b>创建唯一实例；
 *      <br>
 *      Servlet容器会使用多线程执行doGet()或doPost()方法。
 * </p>
 */

//WebServlet注解表示这是一个Servlet，并映射到地址/:
@WebServlet(urlPatterns = "/")
public class ServeletTest extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //获取参数map
        Map<String, String[]> parameterMap = req.getParameterMap();
        //
        //String[] name = parameterMap.get("name");
        //
        String name = req.getParameter("name");
        //设置响应类型
        resp.setContentType("text/html");
        //获取输出流
        PrintWriter printWriter = resp.getWriter();
        //boolean flag = name != null && name.length >0;
        if (name == null){
            name = "world";
        }
        //写入响应
        //printWriter.write("<h1> hello, "+(flag?name[0]:"") + " </h1>");
        printWriter.write("<h1> hello, "+ name + "</h1>");
        //刷新输出
        printWriter.flush();
    }
}
