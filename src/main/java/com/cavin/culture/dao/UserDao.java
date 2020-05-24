package com.cavin.culture.dao;


import com.cavin.culture.model.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface UserDao {

    User getUserByName(String userName);

    String getPasswordByName(String userName);

    Integer insertUser(User user);

    //查询所有人员信息
    List<User> getAll(Map<String,Object> data);

    //查询单个人员信息
    User getUserById(Long id);

    //修改人员信息
    void updateUserById(User user);

    //删除人员信息
    void delUserById(Long id);

    //查询用户总数
    Integer getUserCount();



}
