package trysome.springiocandaop;


import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import trysome.springiocandaop.service.User;
import trysome.springiocandaop.service.UserService;

public class Main {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("application.xml");
        UserService userService = context.getBean(UserService.class);
        User user = userService.login("bob@example.com", "password");
        userService.register("hotttj@163.com","123456","hotg");
        userService.getUserList();
        System.out.println(user.getName());
    }
}
