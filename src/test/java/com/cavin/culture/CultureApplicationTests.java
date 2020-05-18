package com.cavin.culture;

import com.cavin.culture.model.User;
import com.cavin.culture.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
class CultureApplicationTests {
    @Resource
    private UserService userService;
    @Test
    void contextLoads() {

    }

    //测试逻辑删除用户 信息
  /*  @Test
    public void delUser(){
        Long i=1L;
        userService.delUserById(i);
        User user=userService.getUserById(i);
        System.out.println(user);
    }*/
    //测试修改用户信息
  /*  @Test
    public void updateUser(){
        Long id=1L;
        String name="test01";
        String password="123456";
        String level="1";
        String pwd= SHAUtil.getSHA256(password);
        User user=new User();
        user.setId(id);
        user.setUserName(name);
        user.setUserPassword(pwd);
        user.setLevel(level);
        userService.updateUser(user);
        System.out.println(userService.getUserById(id));
    }*/
    @Test
    public void testId(){
//        Long id= UniquelUtil.genId();
//        long randomNum = System.currentTimeMillis();
//        String idd=UUID.randomUUID().toString();
//        System.out.println(idd);
        List<User> users= userService.getAll();
//        User name=userService.getUserByName("test02");
        System.out.println(users.toString());

    }
}
