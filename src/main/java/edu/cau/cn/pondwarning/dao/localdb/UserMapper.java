package edu.cau.cn.pondwarning.dao.localdb;


import edu.cau.cn.pondwarning.entity.localdb.User;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;

@Mapper
public interface UserMapper {

    User selectById(String useruuid);
//
    User selectByName(String username);
//
//    User selectByEmail(String email);
//
    int insertUser(User user);

    int selectDevCount(String useruuid);

    int updateUserInfo(User user);
//
//    int updateStatus(int id, int status);
//
//    int updateHeader(int id, String headerUrl);
//
//    int updatePassword(int id, String password);

}
