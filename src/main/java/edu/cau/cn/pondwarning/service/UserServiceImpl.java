package edu.cau.cn.pondwarning.service;

import com.alibaba.fastjson.JSON;
import edu.cau.cn.pondwarning.dao.localdb.PondMapper;
import edu.cau.cn.pondwarning.dao.localdb.UserMapper;
import edu.cau.cn.pondwarning.entity.localdb.User;
import edu.cau.cn.pondwarning.util.CommonConstant;
import edu.cau.cn.pondwarning.util.CommonUtil;
import edu.cau.cn.pondwarning.util.JwtUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService{
    @Value("${user.root}")
    private String root;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private PondMapper pondMapper;
    /**
     * 用户注册
     * @param user
     * @return
     */
    @Override
    public Map<String, Object> register(User user) {
        Map<String, Object> map = new HashMap<>();

        // 空值处理
        if (user == null) {
            map.put(CommonConstant.STATUS_KEY, CommonConstant.REGISTER_FAIL);
            return map;
        }
        if (StringUtils.isBlank(user.getUsername())) {
            map.put(CommonConstant.STATUS_KEY,CommonConstant.REGISTER_FAIL);
            return map;
        }
        if (StringUtils.isBlank(user.getPassword())) {
            map.put(CommonConstant.STATUS_KEY,CommonConstant.REGISTER_FAIL);
            return map;
        }
        //检查用户名是否已经存在
        String username=user.getUsername();
        User u = userMapper.selectByName(username);
        if (u != null) {
            //注册失败
            map.put(CommonConstant.STATUS_KEY,CommonConstant.REGISTER_FAIL);
            return map;
        }
        //用户不存在则插入
        String useruuid=CommonUtil.generateUUID();
        user.setUseruuid(useruuid);
        String salt=CommonUtil.generateUUID().replace("-","");
        user.setPassword(CommonUtil.md5(user.getPassword()+salt));
        user.setSalt(salt);
        user.setUsertype(CommonConstant.USER_TYPE_ORIDINARY);
        user.setCreateTime(new Date());
        userMapper.insertUser(user);
        //注册成功
        map.put(CommonConstant.STATUS_KEY,CommonConstant.REGISTER_SUCCESS);
        //创建用户目录
        String rootpath=root+useruuid.replace("-","");
        File dir = new File(rootpath);
        if (!dir.exists()) {// 判断目录是否存在
            dir.mkdir();
        }
        return map;
    }

    /**
     * 用户登录
     * @param user
     * @return
     */
    @Override
    public Map<String, Object> login(User user) {
        Map<String, Object> map = new HashMap<>();
        // 空值处理
        if (user == null) {
            map.put(CommonConstant.STATUS_KEY,CommonConstant.LOGIN_FAIL);
            return map;
        }
        if (StringUtils.isBlank(user.getUsername())) {
            map.put(CommonConstant.STATUS_KEY,CommonConstant.LOGIN_FAIL);
            return map;
        }
        if (StringUtils.isBlank(user.getPassword())) {
            map.put(CommonConstant.STATUS_KEY,CommonConstant.LOGIN_FAIL);
            return map;
        }

        //检查用户名是否存在
        User u = userMapper.selectByName(user.getUsername());
        if (u == null) {
            //用户不存在，登录失败
            map.put(CommonConstant.STATUS_KEY,CommonConstant.LOGIN_FAIL);
            return map;
        }
        // 验证密码
        String password = CommonUtil.md5(user.getPassword() + u.getSalt());
        if (!u.getPassword().equals(password)) {
            //密码错误，登录失败
            map.put(CommonConstant.STATUS_KEY,CommonConstant.LOGIN_FAIL);
            return map;
        }
        //验证通过，生成Token登录凭证
        String token=jwtUtils.createToken(u);

        map.put(CommonConstant.STATUS_KEY,CommonConstant.LOGIN_SUCCESS);
        map.put(CommonConstant.TOKEN_KEY ,token);

        return map;
    }

    /**
     * 获取用户个人信息
     * @return
     */
    @Override
    public Map<String, Object> userInfo(){
        String useruuid = jwtUtils.getUseruuid();
        User user=userMapper.selectById(useruuid);
        int pondcount = pondMapper.selectTotalPond(useruuid);
        BigDecimal pondarea = pondMapper.selectTotalAcreage(useruuid);
        int devcount = userMapper.selectDevCount(useruuid);
        // 将 实体类 转换为 Map
        Map<String,Object> map = JSON.parseObject(JSON.toJSONString(user), Map.class);
        map.put("pondcount",pondcount);
        map.put("pondarea",pondarea);
        map.put("devcount",devcount);
        return map;
    }

    /**
     * 更新用户个人信息
     * @return
     */
    @Override
    public Map<String, Object> updateUserInfo(User user){
        Map<String, Object> map = new HashMap<>();
        String useruuid = jwtUtils.getUseruuid();
        user.setUseruuid(useruuid);
        int rows = userMapper.updateUserInfo(user);
        if(rows==0){
            map.put(CommonConstant.STATUS_KEY,CommonConstant.UPDATE_USER_FAIL);
            return map;
        }
        map.put(CommonConstant.STATUS_KEY,CommonConstant.UPDATE_USER_SUCCESS);
        return map;
    }

}
