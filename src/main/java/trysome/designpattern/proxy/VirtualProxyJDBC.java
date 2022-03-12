package trysome.designpattern.proxy;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * <head>编写代码的原则有：</head>
 * <ol>
 *     <li/> 职责清晰：一个类只负责一件事；
 *     <li/> 易于测试：一次只测一个功能。
 * </ol>
 *
 * <p/> 用Proxy实现这个权限检查，我们可以获得更清晰、更简洁的代码：
 *  <ul>
 *      <li/> A接口：只定义接口；
 *      <li/> ABusiness类：只实现A接口的业务逻辑；
 *      <li/> APermissionProxy类：只实现A接口的权限检查代理。
 *  </ul>
 *
 * <p>Proxy还广泛应用在：</p>
 *  <ol>
 *      <li>
 *          远程代理
 *          <br>
 *          远程代理即Remote Proxy，本地的调用者持有的接口实际上是一个代理，这个代理负责把对接口的方法访问转换成远程调用，
 *          然后返回结果。Java内置的RMI机制就是一个完整的远程代理模式。
 *      </li>
 *      <li>
 *         虚代理
 *         <br>
 *         虚代理即Virtual Proxy，它让调用者先持有一个代理对象，但真正的对象尚未创建。
 *         如果没有必要，这个真正的对象是不会被创建的，直到客户端需要真的必须调用时，才创建真正的对象。
 *         <br>
 *         JDBC的连接池返回的JDBC连接（Connection对象）就可以是一个虚代理，即获取连接时根本没有任何实际的数据库连接，
 *         直到第一次执行JDBC查询或更新操作时，才真正创建实际的JDBC连接。
 *      </li>
 *      <li>
 *          保护代理
 *          <br>
 *          保护代理即Protection Proxy，它用代理对象控制对原始对象的访问，常用于鉴权。
 *      </li>
 *      <li>
 *          智能引用
 *          <br>
 *          智能引用即Smart Reference，它也是一种代理对象，如果有很多客户端对它进行访问，
 *          通过内部的计数器可以在外部调用者都不使用后自动释放它。
 *      </li>
 *  </ol>
 */
public class VirtualProxyJDBC {
    static String JDBC_URL = "jdbc:mysql://localhost:3306/learnjdbc?useSSL=false&characterEncoding=utf8&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    static String JDBC_USER = "root";
    static String JDBC_PASSWORD = "password";
    public static void main(String[] args) throws SQLException{
        ProxyJDBC();
    }
    public static void ProxyJDBC() throws SQLException {
        DataSource lazyDataSource = new LazyDataSource(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
        System.out.println("get lazy connection....");

        try (Connection connection = lazyDataSource.getConnection()){

        }
        System.out.println("get lazy connection.....");
        try (Connection connection2 = lazyDataSource.getConnection()){
            try (PreparedStatement ps = connection2.prepareStatement("SELECT * FROM students")){
                try (ResultSet rs = ps.executeQuery()){
                    while (rs.next()){
                        System.out.println(rs.getString("name"));
                    }
                }
            }
        }

        DataSource pooledDataSource = new PooledDataSource(JDBC_URL,JDBC_USER,JDBC_PASSWORD);
        try (Connection conn = pooledDataSource.getConnection()){

        }
        try (Connection conn = pooledDataSource.getConnection()){

        }
        try (Connection conn = pooledDataSource.getConnection()){

        }
    }

}
