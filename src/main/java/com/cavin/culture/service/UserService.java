package com.cavin.culture.service;


import com.cavin.culture.dao.UserDao;
import com.cavin.culture.model.User;
import com.cavin.culture.util.SHAUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    @Resource
    private UserDao userDao;

    public static final String del="0";
    public static final String rank="0";

    public User getUserByName(String userName) {
        return userDao.getUserByName(userName);
    }


    public String getPasswordByName(String userName) {
        return userDao.getPasswordByName(userName);
    }


    public Integer insertUser(User user) {
        String password = user.getUserPassword();
        String sha256Password = SHAUtil.getSHA256(password);
        user.setUserPassword(sha256Password);
        user.setIsDel(del);
        if(user.getLevel()==null){
            user.setLevel(rank);
        }
        if(user.getRegisterDate()==null){
            user.setRegisterDate((new Date()).toString());
        }
        Integer insertNum = userDao.insertUser(user);
//        String id= UniqueIdUtil
        return insertNum;
    }


    public List<User> getAll(Integer currPage,Integer pageSize) {
        Map<String, Object> data = new HashMap<>();
        data.put("currIndex", (currPage-1)*pageSize);
        data.put("pageSize", pageSize);
        return userDao.getAll(data);
    }


    public User getUserById(Long id) {
        return userDao.getUserById(id);
    }


    public void updateUser(User user) {
        String password = user.getUserPassword();
        String sha256Password = SHAUtil.getSHA256(password);
        user.setUserPassword(sha256Password);
        userDao.updateUserById(user);
    }


    public void delUserById(Long id) {
        userDao.delUserById(id);
    }

    public int getUserCount(){
        return userDao.getUserCount();
    }

/*

    public void updateUserInfo(User user) {
        adminDao.updateUserInfo(user);
    }
*/


}
