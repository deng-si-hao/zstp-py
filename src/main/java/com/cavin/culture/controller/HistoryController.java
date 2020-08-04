package com.cavin.culture.controller;

import com.auth0.jwt.interfaces.Claim;
import com.cavin.culture.model.History;
import com.cavin.culture.model.JsonMessage;
import com.cavin.culture.model.User;
import com.cavin.culture.service.HistoryService;
import com.cavin.culture.service.UserService;
import com.cavin.culture.util.JWTMEUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class HistoryController {

    @Resource
    private HistoryService historyService;
    @Resource
    private UserService userService;


    @RequestMapping(value = "/getHistoriesByType", method = RequestMethod.GET)
    @ResponseBody
    public JsonMessage getHistoriesByType(@RequestParam(value = "type")String type, HttpServletRequest request) {
        String token = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("access_token")) {
                token = cookie.getValue();
            }
        }
        String username = null;
        try {
            Map<String, Claim> tokenRes = JWTMEUtil.verifyToken(token);
            username = tokenRes.get("userName").asString();
        } catch (Exception e) {
            e.printStackTrace();
        }


        List<History> histories = historyService.getHistoriesByNameAndType(username, type);
        return JsonMessage.success().addData("histories", histories);
    }

    @RequestMapping(value = "/addHistory", method = RequestMethod.POST)
    @ResponseBody
    public JsonMessage addHistory(@RequestBody History history, HttpServletRequest request) {
        String token = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("access_token")) {
                token = cookie.getValue();
            }
        }
        String username = null;
        try {
            Map<String, Claim> tokenRes = JWTMEUtil.verifyToken(token);
            username = tokenRes.get("userName").asString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        history.setUserName(username);
        User user = userService.getUserByName(username);
        history.setUserId(user.getId());
        Integer insertNum = historyService.insertHistory(history);
        return JsonMessage.success().addData("insertNum", insertNum);
    }

    @RequestMapping(value = "/deleteHistoryById/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public JsonMessage deleteHistoryById(@PathVariable long id) {
        Integer deleteNum = historyService.deleteHistoryById(id);
        return JsonMessage.success().addData("deleteNum", deleteNum);
    }
    //获取输入建议
    @RequestMapping(value = "/getInputSuggestion", method = RequestMethod.GET)
    @ResponseBody
    public JsonMessage getInputSuggestion(@RequestParam(value = "queryString")String queryString,HttpServletRequest request) {
        String token = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("access_token")) {
                token = cookie.getValue();
            }
        }
        String username = null;
        try {
            Map<String, Claim> tokenRes = JWTMEUtil.verifyToken(token);
            username = tokenRes.get("userName").asString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<String> suggestion = historyService.getInputSuggestion(queryString,username);
        return JsonMessage.success().addData("suggestion", suggestion);
    }
    //搜索
    @RequestMapping(value = "/queryForKnowledge", method = RequestMethod.GET)
    @ResponseBody
    public JsonMessage queryForKnowledge(@RequestParam(value = "individualName")String individualName,
                                         @RequestParam(value = "scope")String scope) {
//        List<Map<String, String>> nodes = ontInstanceService.getResourceObjectWithCate(individualName);
//        List<Map<String, String>> queryResults = ontInstanceService.queryForKnowledge(individualName, scope);
        //TODO:测试数据11
        List<Map<String, String>> nodes = new ArrayList<Map<String,String>>();
        Map map = new HashMap();
        map.put("name",individualName);
        map.put("category","历史事件");
        nodes.add(map);
        List<Map<String, String>> queryResults =  new ArrayList<Map<String,String>>();
        return JsonMessage.success().addData("nodes", nodes).addData("queryResults", queryResults);
    }
}
