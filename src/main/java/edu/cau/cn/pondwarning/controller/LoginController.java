package edu.cau.cn.pondwarning.controller;


import edu.cau.cn.pondwarning.entity.localdb.User;
import edu.cau.cn.pondwarning.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class  LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/register",method = RequestMethod.POST )
    public Map<String,Object> register(@RequestBody User user){
        logger.info(">>>访问注册接口<<<");
        Map<String, Object> map = userService.register(user);
        return map;
    }

    @RequestMapping(value = "/login",method = RequestMethod.POST )
    public Map<String,Object> login(@RequestBody User user){
        logger.info(">>>访问登录接口<<<");
        Map<String, Object> map = userService.login(user);
        return map;
    }

    @RequestMapping(value = "/info",method = RequestMethod.GET )
    public Map<String,Object> userInfo(){
        logger.info(">>>访问查询用户信息接口<<<");
        return userService.userInfo();
    }

    @RequestMapping(value = "/info",method = RequestMethod.PUT )
    public Map<String,Object> updateUserInfo(@RequestBody User user){
        logger.info(">>>访问更新用户信息接口<<<");
        return userService.updateUserInfo(user);
    }

    @RequestMapping(value = "/hello",method = RequestMethod.GET )
    public String hello(HttpServletRequest request){
        logger.info(">>>访问hello接口<<<");
        System.out.println(request.getHeader("token"));
        return "token";
    }
//
//    @RequestMapping(value = "/hello2",method = RequestMethod.GET )
//    public Map<String, Object> hello2(String token){
//        Map<String, Object> dataMap = new HashMap<>();
//        try{
//            dataMap.put("result",jwtUtils.parseJwt(token));
//        }catch (Exception e){
//            System.out.println("token过期");
//        }
//
//        return dataMap;
//    }

}
