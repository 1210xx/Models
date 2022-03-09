package trysome.spring;


import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import trysome.spring.service.User;
import trysome.spring.service.UserService;

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
