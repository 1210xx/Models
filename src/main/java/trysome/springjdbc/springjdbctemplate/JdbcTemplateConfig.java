package trysome.springjdbc.springjdbctemplate;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;
import trysome.springjdbc.springjdbctemplate.service.User;
import trysome.springjdbc.springjdbctemplate.service.UserService;

import javax.sql.DataSource;

/**
 * <head>使用JDBC虽然简单，但代码比较繁琐。Spring为了简化数据库访问，主要做了以下几点工作：</head>
 *
 * <ul>
 *  <li>
 *      提供了简化的访问JDBC的模板类，不必手动释放资源；
 *  </li>
 *  <li>
 *      提供了一个统一的DAO类以实现Data Access Object模式；
 *  </li>
 *  <li>
 *      把SQLException封装为DataAccessException，这个异常是一个RuntimeException，
 *      并且让我们能区分SQL异常的原因，例如，DuplicateKeyException表示违反了一个唯一约束；
 *   </li>
 *  <li>
 *      能方便地集成Hibernate、JPA和MyBatis这些数据库访问框架。
 *  </li>
 * </ul>
 *
 * <head>在本次配置中</head>
 * <ol>
 *     <li>
 *         通过@PropertySource("jdbc.properties")读取数据库配置文件；
 *     </li>
 *     <li>
 *         通过@Value("${jdbc.url}")注入配置文件的相关配置；
 *     </li>
 *
 *    <li>
 *         创建一个DataSource实例，它的实际类型是HikariDataSource，创建时需要用到注入的配置；
 *    </li>
 *
 *    <li>
 *         创建一个JdbcTemplate实例，它需要注入DataSource，这是通过方法参数完成注入的。
 *    </li>
 * </ol>
 *
 */

//@configuration 表示是一个配置类，创建ApplicationContext时，
// 使用了ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
@Configuration
@ComponentScan
@PropertySource("jdbc.properties")
public class JdbcTemplateConfig {
    @Value("${jdbc.url}")
    String jdbcUrl;
    @Value("${jdbc.username}")
    String jdbcUsername;
    @Value("${jdbc.password}")
    String jdbcPassword;

    @Bean
    DataSource createDataSource(){
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(jdbcUsername);
        config.setPassword(jdbcPassword);
        config.addDataSourceProperty("autoCommit","true");
        config.addDataSourceProperty("connectionTimeout","5");
        config.addDataSourceProperty("idleTimeout", "60");
        return new HikariDataSource(config);
    }

    @Bean
    JdbcTemplate createJdbcTemplate(@Autowired DataSource dataSource){
        return new JdbcTemplate(dataSource);
    }

    public static void main(String[] args) {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(JdbcTemplateConfig.class);
        UserService userService = applicationContext.getBean(UserService.class);
        userService.register("bob@example.com", "password1", "Bob");
        userService.register("alice@example.com", "password2", "Alice");
        User bob = userService.getUserByName("Bob");
        System.out.println(bob);
        User tom = userService.register("tom@example.com", "password3", "Tom");
        System.out.println(tom);
        System.out.println("Total: " + userService.getUsers());
        for (User user : userService.getUsers(1)){
            System.out.println(user);
        }

        ((ConfigurableApplicationContext) applicationContext).close();

    }
}
