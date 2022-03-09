package trysome.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import trysome.spring.service.User;
import trysome.spring.service.UserService;

@Configuration
@ComponentScan
public class AppConfig {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
        UserService userService = applicationContext.getBean(UserService.class);
        User user = userService.login("bob@example.com", "password");
        userService.register("hotttj@163.com","123456","hotg");
        userService.getUserList();
        System.out.println(user.getName());
    }
}
