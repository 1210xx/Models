package trysome.springjdbc.springmybatis.mapper;

import org.apache.ibatis.annotations.*;
import trysome.springjdbc.springmybatis.entity.User;

import java.util.List;

/**
 * MyBatis使用Mapper来实现映射，而且Mapper必须是接口
 */
public interface UserMapper {
    @Select("SELECT * FROM users WHERE id = #{id}")
    User getById(@Param("id") long id);

    @Select("SELECT * FROM users WHERE email = #{email}")
    User getByEmail(@Param("email") String email);

    @Select("SELECT * FROM users LIMIT #{offset} , #{maxResults}")
    List<User> getAll(@Param("offset") int offset, @Param("maxResults") int maxResults);


    //使用自增主键，返回属性主键和表内主键
    @Options(useGeneratedKeys = true,keyProperty = "id",keyColumn = "id")
    @Insert("INSERT INTO users (email, password, name, createdAt) VALUES (#{user.email}, #{user.password}, #{user.name}, #{user.createdAt})")
    void insert(@Param("user") User user);


    @Update("UPDATE users SET name = #{user.name} , createdAt = #{user.createdAt}, WHERE id = #{user.id}")
    void update(@Param("user") User user);

    @Delete("DELETE FROM users WHERE id = #{user.id}")
    void deleteById(@Param("id") long id);

    @Delete("DELETE FROM users where 1=1")
    void deletable();
}
