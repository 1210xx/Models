package trysome.webtest.mvc.controller;


import trysome.webtest.mvc.bean.SinInBean;
import trysome.webtest.mvc.bean.User;
import trysome.webtest.mvc.framework.ModelAndView;
import trysome.webtest.mvc.framework.GetMapping;
import trysome.webtest.mvc.framework.PostMapping;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class UserController {
    private Map<String, User> userDataBase = new HashMap<String, User>() {
        {
            //也可以用List.of java9
            List<User> users = Arrays.asList(new User("bob@example.com", "bob123", "Bob", "This is bob."),
                    new User("tom@example.com", "tomcat", "Tom", "This is tom."));
//    users.forEach(user -> {
//        put(user.email, user);
//    });
            for (User u :
                    users) {
                put(u.email, u);
            }
        }
    };

    @GetMapping("/signin")
    public ModelAndView signin(){
        return new ModelAndView("/signin.html");
    }

    @PostMapping("/signin")
    public ModelAndView doSignin(SinInBean bean, HttpServletResponse response, HttpSession session) throws IOException {
        System.out.println(
                "========================="
        );
        User user = userDataBase.get(bean.email);
        if (user == null || !user.password.equals(bean.password)){
            response.setContentType("application/json");
            PrintWriter pw = response.getWriter();
            pw.write("{\"error\":\"Bad email or password\"}");
            pw.flush();
        }else{
            session.setAttribute("user", user);
            response.setContentType("application/json");
            PrintWriter pw = response.getWriter();
            pw.write("{\"result\":true}");
            pw.flush();
        }
        return null;
    }

    @GetMapping("/signout")
    public ModelAndView signout(HttpSession session){
        session.removeAttribute("user");
        return new ModelAndView("redirect:/");
    }

    @GetMapping("/user/profile")
    public ModelAndView profile(HttpSession session){
        User user = (User) session.getAttribute("user");
        if (user == null){
            return new ModelAndView("redirect:/signin");
        }
        return new ModelAndView("/profile.html","user",user);
    }

}
