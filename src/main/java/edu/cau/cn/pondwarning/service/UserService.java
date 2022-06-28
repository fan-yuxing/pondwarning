package edu.cau.cn.pondwarning.service;

import edu.cau.cn.pondwarning.entity.localdb.User;

import java.util.Map;

public interface UserService {
    Map<String, Object> register(User user);

    Map<String, Object> login(User user);

    Map<String, Object> userInfo();

    Map<String, Object> updateUserInfo(User user);


}
