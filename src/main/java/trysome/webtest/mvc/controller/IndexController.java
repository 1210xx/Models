package trysome.webtest.mvc.controller;

import trysome.webtest.mvc.bean.User;
import trysome.webtest.mvc.framework.GetMapping;
import trysome.webtest.mvc.framework.ModelAndView;
import trysome.webtest.mvc.framework.PostMapping;

import javax.servlet.http.HttpSession;

public class IndexController {

    @GetMapping("/")
    public ModelAndView index(HttpSession session){
        User user = (User) session.getAttribute("user");
        return new ModelAndView("index.html","user", user);
    }
    @GetMapping("/hello")
    public ModelAndView hello(String name){
        if (name == null){
            name = "World";
        }
        return new ModelAndView("/hello.html","name", name);
    }

}
