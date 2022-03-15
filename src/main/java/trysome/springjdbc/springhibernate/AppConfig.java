package trysome.springjdbc.springhibernate;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import trysome.springjdbc.springhibernate.entity.User;
import trysome.springjdbc.springhibernate.service.UserService;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * LocalSessionFactoryBean是一个FactoryBean，它会再自动创建一个SessionFactory，
 * 在Hibernate中，Session是封装了一个JDBC Connection的实例，
 * 而SessionFactory是封装了JDBC DataSource的实例，即SessionFactory持有连接池，
 * 每次需要操作数据库的时候，SessionFactory创建一个新的Session，相当于从连接池获取到一个新的Connection。
 * SessionFactory 就是 Hibernate 提供的最核心的一个对象，
 * 但 LocalSessionFactoryBean 是Spring提供的为了让我们方便创建SessionFactory的类。
 */
@Configurable
@ComponentScan
@EnableTransactionManagement
@PropertySource("jdbc.properties")
public class AppConfig {
    @Bean
    LocalSessionFactoryBean createSessionFactory(@Autowired DataSource dataSource){
        Properties props = new Properties();
        //hibernate.hbm2ddl.auto=update：表示自动创建数据库的表结构，注意不要在生产环境中启用；
        props.setProperty("hibernate.hbm2ddl.auto","update");// 生产环境不要使用
        //hibernate.dialect=org.hibernate.dialect.HSQLDialect：
        // 指示Hibernate使用的数据库是HSQLDB。Hibernate使用一种HQL的查询语句，
        // 它和SQL类似，但真正在“翻译”成SQL时，会根据设定的数据库“方言”来生成针对数据库优化的SQL；
        props.setProperty("hibernate.dialect","org.hibernate.dialect.HSQLDialect");
        //hibernate.show_sql=true：让Hibernate打印执行的SQL
        //这对于调试非常有用，我们可以方便地看到Hibernate生成的SQL语句是否符合我们的预期。
        props.setProperty("hibernate.show_sql","true");
        LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource);
        // 扫描指定的package获取所有entity class:
        sessionFactoryBean.setPackagesToScan("trysome/springjdbc/springhibernate/entity");
        sessionFactoryBean.setHibernateProperties(props);
        return sessionFactoryBean;
    }

    //Spring为了便于我们使用Hibernate提供的工具类，不是非用不可，但推荐使用以简化代码。
    @Bean
    HibernateTemplate createHibernateTemplate(@Autowired SessionFactory sessionFactory){
        return new HibernateTemplate(sessionFactory);
    }

    //配合Hibernate使用声明式事务所必须的
    @Bean
    PlatformTransactionManager createTxManager(@Autowired SessionFactory sessionFactory){
        return new HibernateTransactionManager(sessionFactory);
    }

    @Bean
    DataSource createDataSource(@Value("${jdbc.url}") String jdbcUrl, @Value("${jdbc.username}") String jdbcUsername, @Value("${jdbc.password}") String password){
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(jdbcUsername);
        config.setPassword(password);
        config.addDataSourceProperty("autoCommit","false");
        config.addDataSourceProperty("connectionTimeout","5");
        config.addDataSourceProperty("idleTimeout","60");
        return new HikariDataSource(config);
    }

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        UserService userService  = context.getBean(UserService.class);
        if (userService.fetchUserByEmail("bob@example.com") == null){
            User bob = userService.register("bob@example.com","bob123","bob");
            System.out.println("Register OK: " + bob);
        }

        if (userService.fetchUserByEmail("alice@example.com") == null) {
            User alice = userService.register("alice@example.com", "helloalice", "Bob");
            System.out.println("Registered ok: " + alice);
        }

        for (User u : userService.getUsers(1)){
            System.out.println(u);
        }

        User bob =userService.login("bob@example.com","bob123");
        System.out.println(bob);
        ((ConfigurableApplicationContext)context).close();
    }
}
