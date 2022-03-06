package trysome.webtest;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/user")
public class userServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        super.doGet(req, resp);
        School school = new School("No.1 Middle School ", "101 South Street");
        User user = new User(123, "Bob", school);
        req.setAttribute("user", user);
        req.getRequestDispatcher("/WEB-INF/user.jsp").forward(req,resp);
    }
}
