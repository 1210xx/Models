package trysome.springjdbc.springhibernate;


import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

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
}
