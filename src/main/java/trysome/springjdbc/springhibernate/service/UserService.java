package trysome.springjdbc.springhibernate.service;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Component;
import trysome.springjdbc.springhibernate.entity.User;

import javax.transaction.Transactional;
import java.util.List;


@Component
@Transactional
public class UserService {
    @Autowired
    HibernateTemplate hibernateTemplate;

    public User register(String email, String password, String name){
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setName(name);
        hibernateTemplate.save(user);
        System.out.println(user.getId());
        return user;
    }

    //通过主键删除记录时，一个常见的用法是先根据主键加载该记录，再删除。
    // load()和get()都可以根据主键加载记录，它们的区别在于，当记录不存在时，get()返回null，而load()抛出异常。
    public boolean deleteUser(Long id){
        User user = hibernateTemplate.get(User.class,id);
        if (user != null){
            hibernateTemplate.delete(user);
            return true;
        }
        return false;
    }

    public void updateUser(Long id,String name){
        User user = hibernateTemplate.load(User.class,id);
        user.setName(name);
        hibernateTemplate.update(user);
    }

    // 使用findByExample()时，注意基本类型字段总是会加入到WHERE条件！
    //example实例只有email和password两个属性为非null，所以最终生成的WHERE语句就是WHERE email = ? AND password = ?。
    //如果我们把User的createdAt的类型从Long改为long，findByExample()的查询将出问题，
    // 原因在于example实例的long类型字段有了默认值0，
    // 导致Hibernate最终生成的WHERE语句意外变成了WHERE email = ? AND password = ? AND createdAt = 0。
    // 显然，额外的查询条件将导致错误的查询结果。
    public User login(String email, String password){
        User example = new User();
        example.setEmail(email);
        example.setPassword(password);
        List<User> list = hibernateTemplate.findByExample(example);
        return list.isEmpty()?null:list.get(0);
    }

    public User login2(String email, String password){
        DetachedCriteria criteria =DetachedCriteria.forClass(User.class);
        criteria.add(Restrictions.eq("email",email)).add(Restrictions.eq("password",password));
        List<User> list = (List<User>) hibernateTemplate.findByCriteria(criteria);
        return list.isEmpty()?null:list.get(0);
    }

    public User login3(String email,String password){
        @SuppressWarnings("deprecation")
        List<User> list = (List<User>) hibernateTemplate.findByNamedQuery("login",email,password);
        return list.isEmpty()?null:list.get(0);
    }

    public User getUserById(Long id){
        return hibernateTemplate.load(User.class,id);
    }

    public User fetchUserByEmail(String email){
        User example = new User();
        example.setEmail(email);
        List<User> list = hibernateTemplate.findByExample(example);
        return list.isEmpty()?null:list.get(0);
    }

    public User getUserByEmail(String email){
        User user = fetchUserByEmail(email);
        if (user == null){
            throw new RuntimeException("User not found by email:" + email);
        }
        return user;
    }

    public List<User> getUsers(int pageIndex){
        int pageSize = 100;
        return (List<User>) hibernateTemplate.findByCriteria(DetachedCriteria.forClass(User.class),(pageIndex-1)*pageSize,pageSize);
    }


    public User signin(String email, String password){
        @SuppressWarnings({ "deprecation", "unchecked" })
        List<User> list = (List<User>) hibernateTemplate.find("FROM User WHERE email = ? AND password=?",email,password);
        return list.isEmpty()?null:list.get(0);
    }
}

