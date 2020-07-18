package com.cavin.culture.controller;

import com.cavin.culture.model.JsonMessage;
import com.cavin.culture.model.User;
import com.cavin.culture.service.UserService;
import com.cavin.culture.util.JWTUtil;
import com.cavin.culture.util.SHAUtil;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.List;

@RestController
@CrossOrigin(origins = "*",allowCredentials="true",allowedHeaders = "",methods = {})
@RequestMapping("/sys")
public class UserController {

    @Resource
    private UserService userService;

    @RequestMapping(value = "/user/isLogin", method = RequestMethod.GET)
    @ResponseBody
    public JsonMessage isLogin(HttpServletRequest request) {
        Cookie[] cookies = null;
        cookies = request.getCookies();

        if (cookies != null) {
            boolean isLogin = false;
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("access_token")) {
                    isLogin = true;
                }
            }
            if (isLogin) {
                // 有access_token
                return JsonMessage.success();
            } else {
                // 无access_token
                return JsonMessage.error(401, "请先登录，好吗！");
            }
        } else {
            // 无access_token
            return JsonMessage.error(401, "请先登录！");
        }
    }
/**
* 注册
* */
    @RequestMapping(value = "/user/register", method = RequestMethod.POST)
    public JsonMessage login(@RequestBody User user) {
        User checkUser = userService.getUserByName(user.getUserName());
        if (checkUser == null) {
//            Long id= UniqueIdUtil.genId();
            //shiro的加密方法
            //Object salt = ByteSource.Util.bytes(user.getUserName());
            //SimpleHash simpleHash=new SimpleHash("MD5", user.getUserPassword(), salt, 1);
            Integer insertNum = userService.insertUser(user);
            return JsonMessage.success().addData("insertNum", insertNum);
        } else {
            return JsonMessage.error(400, "该用户名已注册");
        }
    }
/**
* 登录
* */
    @RequestMapping(value = "/user/login")
    public JsonMessage login(@RequestBody User user, HttpServletResponse response) {
        String username = user.getUserName();
        String password = user.getUserPassword();
        User checkUser = userService.getUserByName(username);
        if (checkUser != null) {
            if(checkUser.getIsDel().equals("0")){
                String checkPassword = SHAUtil.getSHA256(password);
                String storedPassword = userService.getPasswordByName(username);
                if (checkPassword.equals(storedPassword)) {
                    // 登陆成功
                    String token = JWTUtil.getJwtToken(checkUser.getUserName());
                    Cookie cookie = new Cookie("access_token", token);
                    Cookie cookieUserId = new Cookie("userId",(String.valueOf(checkUser.getId())));
                    cookie.setDomain("localhost");
                    cookie.setPath("/");
                    cookie.setHttpOnly(true);
                    cookie.setMaxAge(3*24*60*60);
                    response.addCookie(cookie);
                    response.addCookie(cookieUserId);
                    return JsonMessage.adminLogin(checkUser.getLevel());
            }else {
                    return JsonMessage.error(400, "密码错误！");
                }
            } else {
                return JsonMessage.error(400,"用户被锁定，请联系管理员！");
//                return JsonMessage.error(400, "密码错误！");
            }
        } else {
            return JsonMessage.error(400, "用户名不存在！");
        }
    }
/**
* 注销
* */
    @RequestMapping(value = "/user/logout", method = RequestMethod.POST)
    @ResponseBody
    public JsonMessage logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("access_token")) {
                    cookie.setValue(null);
                    cookie.setMaxAge(0);
                    cookie.setPath("/");
                    response.addCookie(cookie);
                }
            }
            return JsonMessage.success();
        } else {
            return JsonMessage.error(401, "请先登录！");
        }
    }
    /**
    * 查询所有用户信息
    * */
    @RequestMapping(value = "/admin/getAllUser",method = RequestMethod.POST)
    @ResponseBody
    public JsonMessage getAllUserInfo(int currPage, HttpServletResponse response){
        int currPageInt=0;
        int pageSizeInt=8;
        if(currPage==0){
            currPageInt=1;
        }else {
            currPageInt=currPage;
        }
        List<User> info= userService.getAll(currPageInt,pageSizeInt);
        int total=userService.getUserCount();
        return JsonMessage.success().addData("userInfo",info).addData("total",total);
    }

    /**
    * 修改用户信息
    * */
    @RequestMapping(value = "/admin/updateUserInfo",method = RequestMethod.POST)
    @ResponseBody
    public JsonMessage updateUserInfo(@RequestBody User user, HttpServletResponse response){
        try {
            userService.updateUser(user);
            return JsonMessage.success();
        } catch (Exception e) {
            e.printStackTrace();
            return JsonMessage.error(400,"删除失败");
        }

    }
    /**
    * 查询单个用户信息
    * */
    @RequestMapping(value = "/user/queryById",method = RequestMethod.POST)
    @ResponseBody
    public JsonMessage queryById(Long id, HttpServletResponse response){
            User user= userService.getUserById(id);

        System.out.println(user.toString());
        return JsonMessage.success().addData("user",user);
    }
    /**
    * 删除用户信息id
    * */
    @RequestMapping(value = "/admin/delUser",method = RequestMethod.POST)
    @ResponseBody
    public JsonMessage delUser(Long id){
        try {
            userService.delUserById(id);
            return JsonMessage.success();
        } catch (Exception e) {
            e.printStackTrace();
            return JsonMessage.error(400,"删除失败！");
        }
    }
}
