package trysome.springjdbc.springjdbctemplate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

/**
 * Spring提供的JdbcTemplate采用Template模式，提供了一系列以回调为特点的工具方法，目的是避免繁琐的try...catch语句。
 * JdbcTemplate还有许多重载方法，需要强调的是，
 * JdbcTemplate只是对JDBC操作的一个简单封装，它的目的是尽量减少手动编写try(resource) {...}的代码，
 * 对于查询，主要通过RowMapper实现了JDBC结果集到Java对象的转换。
 *
 * JdbcTemplate的用法：
 *
 * 针对简单查询，优选query()和queryForObject()，因为只需提供SQL语句、参数和RowMapper；
 * 针对更新操作，优选update()，因为只需提供SQL语句和参数；
 * 任何复杂的操作，最终也可以通过execute(ConnectionCallback)实现，因为拿到Connection就可以做任何JDBC操作。
 *
 */
@Component
public class UserService {
    //在需要访问数据库的Bean中，注入JdbcTemplate
    @Autowired
    JdbcTemplate jdbcTemplate;

    public User getUserByID(long id) {
        //注意传入的是ConnectionCallback
        return jdbcTemplate.execute((Connection conn) -> {
            //没有jdbcTemplate需要从DataSource获取Connection
            // 可以直接使用conn实例，不要释放它，回调结束后JdbcTemplate自动释放:
            //允许获取Connection，然后做任何基于Connection的操作。
            // 在内部手动创建的PreparedStatement、ResultSet必须用try(...)释放:
            try (PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM users WHERE id = ? ")) {
                preparedStatement.setObject(1, id);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return new User(
                                resultSet.getLong("id"),
                                resultSet.getString("email"),
                                resultSet.getString("password"),
                                resultSet.getString("name"));
                    }
                    throw new RuntimeException("user not found by id. ");
                }
            }
        });
    }

    public User getUserByName(String name) {
        //需要传入SQL语句，以及PreparedStatementCallback:
        return jdbcTemplate.execute("SELECT * FROM users WHERE name = ?", (PreparedStatement ps) -> {
                    ps.setObject(1, name);
                    // PreparedStatement实例已经由JdbcTemplate创建，并在回调后自动释放:
                    try (ResultSet resultSet = ps.executeQuery()) {
                        if (resultSet.next()) {
                            return new User(
                                    resultSet.getLong("id"),
                                    resultSet.getString("email"),
                                    resultSet.getString("password"),
                                    resultSet.getString("name"));
                        }
                        throw new RuntimeException("User not found by name.");
                    }
                }
        );
    }

    /**
     * 在queryForObject()方法中，传入SQL以及SQL参数后，JdbcTemplate会自动创建PreparedStatement，
     * 自动执行查询并返回ResultSet，RowMapper需要做的事情就是把ResultSet的当前行映射成一个JavaBean并返回。
     * 整个过程中，使用Connection、PreparedStatement和ResultSet都不需要我们手动管理。
     * @param email User.email
     * @return User
     */
    public User getUserByEmail(String email) {
        // 传入SQL，参数和RowMapper实例:自动创建PreparedStatement，自动查询并返回ResultSet。
        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email = ?", new Object[]{email},
                //将ResultSet映射成User（Java Bean）并返回
                (ResultSet resultSet, int rowNum) -> {
                    return new User(
                            resultSet.getLong("id"),
                            resultSet.getString("email"),
                            resultSet.getString("password"),
                            resultSet.getString("name")
                    );

                });
    }


    /**
     * RowMapper不一定返回JavaBean，实际上它可以返回任何Java对象
     * @return 返回ID
     */
    public long getUsers() {
        return jdbcTemplate.queryForObject("SELECT count(*) FROM users ", null,
                (ResultSet resultSet, int rowNum) -> {
                    return resultSet.getLong(1);
                });
    }

    /**
     * 期望返回多行记录，而不是一行，可以用query()方法：
     * @param pageIndex 页码
     * @return 返回UserList
     */
    public List<User> getUsers(int pageIndex) {
        int limit = 100;
        int offset = limit * (pageIndex - 1);
        //直接使用Spring提供的BeanPropertyRowMapper。
        // 如果数据库表的结构恰好和JavaBean的属性名称一致，
        // 那么BeanPropertyRowMapper就可以直接把一行记录按列名转换为JavaBean。
        return jdbcTemplate.query("SELECT * FROM users LIMIT ? OFFSET ?", new Object[]{limit, offset},
                new BeanPropertyRowMapper<>(User.class));
    }

    public User login(String email, String password, String name) {
        User user = getUserByEmail(email);
        if (user.getPassword().equals(password)) {
            return user;
        }
        throw new RuntimeException("Login failed");
    }

    /**
     * INSERT操作比较特殊，那就是如果某一列是自增列（例如自增主键），
     * 通常，我们需要获取插入后的自增值。
     * JdbcTemplate提供了一个KeyHolder来简化这一操作：
     * @param email User email
     * @param password User password
     * @param name User name
     * @return register User
     */
    public User register(String email, String password, String name) {
        KeyHolder holder = new GeneratedKeyHolder();
        if (1 != jdbcTemplate.update((conn) -> {
            // 参数1:PreparedStatementCreator
            // 创建PreparedStatement时，必须指定RETURN_GENERATED_KEYS:
            PreparedStatement ps = conn.prepareStatement("INSERT INTO users(email,password,name) VALUES(?,?,?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, email);
            ps.setObject(2, password);
            ps.setObject(3, name);
            return ps;
            // 参数2:KeyHolder
        }, holder)) {
            throw new RuntimeException("Insert failed.");
        }
        // 从KeyHolder中获取返回的自增值:
        return new User(holder.getKey().longValue(), email, password, name);
    }

    public void updateUser(User user) {
        if (1 != jdbcTemplate.update("UPDATE user SET name = ? WHERE id = ?", user.getName(), user.getId())) {
            throw new RuntimeException("User not found by Id.");
        }
    }
}
