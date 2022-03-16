package trysome.springjdbc.springmybatis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import trysome.springjdbc.springmybatis.entity.User;
import trysome.springjdbc.springmybatis.mapper.UserMapper;

import java.time.Instant;
import java.util.List;

/**
 * 业务逻辑主要就是通过XxxMapper定义的数据库方法来访问数据库
 */
@Component
@Transactional
public class UserService {
    @Autowired
    UserMapper userMapper;

    public User getUserById(long id){
        User user = userMapper.getById(id);
        if (user == null){
            throw new RuntimeException("User not found by id!");
        }
        return user;
    }

    public User fetchUserByEmail(String email){
        return userMapper.getByEmail(email);
    }

    public User getUserByEmail(String email){
        User user = fetchUserByEmail(email);
        if (user == null){
            throw new RuntimeException("User not found by email!");
        }
        return user;
    }

    public List<User> getUsers(int pageIndex){
        int pageSize = 100;
        return userMapper.getAll((pageIndex - 1) * pageSize, pageSize);
    }

    public void updateUser(long id,String name){
        User user = getUserById(id);
        user.setName(name);
        user.setCreatedAt(System.currentTimeMillis());
        userMapper.update(user);
    }

    public void deleteUser(Long id){
        userMapper.deleteById(id);
    }

    public User login(String email,String password){
        User user = userMapper.getByEmail(email);
        if (user != null && password.equals(user.getPassword())){
            return user;
        }
        throw new RuntimeException("Login failed!");
    }

    public User register(String email, String name , String password){
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setPassword(password);
        user.setCreatedAt(Instant.now().toEpochMilli());
        userMapper.insert(user);
        return user;
    }

    public void clearTable(){
        userMapper.deletable();
    }
}
