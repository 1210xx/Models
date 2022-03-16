package trysome.springjdbc.springmybatis;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import trysome.springjdbc.springmybatis.entity.User;
import trysome.springjdbc.springmybatis.service.UserService;

import javax.sql.DataSource;

@Configurable
@ComponentScan
//有了@MapperScan，就可以让MyBatis自动扫描指定包的所有Mapper并创建实现类。在真正的业务逻辑中，我们可以直接注入
@MapperScan("trysome.springjdbc.springmybatis.mapper")
@EnableTransactionManagement
@PropertySource("jdbc.properties")
public class MyBatisConfig {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(MyBatisConfig.class);
        UserService userService = context.getBean(UserService.class);
        userService.clearTable();
        for (User u : userService.getUsers(1)) {
            System.out.println(u);
        }

        if (userService.fetchUserByEmail("bob@example.com") == null) {
            User bob = userService.register("bob@example.com", "bob123", "Bob");
            System.out.println("Registered ok: " + bob);
        }
        if (userService.fetchUserByEmail("alice@example.com") == null) {
            User alice = userService.register("alice@example.com", "helloalice", "Alice");
            System.out.println("Registered ok: " + alice);
        }
        if (userService.fetchUserByEmail("tom@example.com") == null) {
            User tom = userService.register("tom@example.com", "tomcat", "Alice");
            System.out.println("Registered ok: " + tom);
        }
        // 查询所有用户:
        for (User u : userService.getUsers(1)) {
            System.out.println(u);
        }
        System.out.println("login...");
        User tom = userService.login("tom@example.com", "Alice");
        System.out.println(tom);
        ((ConfigurableApplicationContext) context).close();
    }

    @Bean
    DataSource createDateSource(
            @Value("${jdbc.url}") String jdbcUrl,
            @Value("${jdbc.username}") String jdbcUsername,
            @Value("${jdbc.password}") String password){
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(jdbcUsername);
        config.setPassword(password);
        config.addDataSourceProperty("autoCommit","false");
        config.addDataSourceProperty("connectionTimeout","5");
        config.addDataSourceProperty("idleTimeout","60");
        return new HikariDataSource(config);
    }

    @Bean
    SqlSessionFactoryBean sessionFactoryBean (@Autowired DataSource dataSource){
        SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource);
        return sessionFactoryBean;
    }

    @Bean
    PlatformTransactionManager createTxManage (@Autowired DataSource dataSource){
        return new DataSourceTransactionManager(dataSource);
    }

}
