package edu.cau.cn.pondwarning.util;

import edu.cau.cn.pondwarning.entity.localdb.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: Mr.Yang
 * @create: 2020-02-13 21:19
 **/
@EnableConfigurationProperties({JwtUtils.class})
@ConfigurationProperties("jwt.config")
@Configuration("jwt.config")
public class JwtUtils {
    //签名私钥
    private String key;
    //签名失效时间
    private Long failureTime;
    //阈值
    private Long thresh;

    public Long getThresh() {
        return thresh;
    }

    public void setThresh(Long thresh) {
        this.thresh = thresh;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Long getFailureTime() {
        return failureTime;
    }

    public void setFailureTime(Long failureTime) {
        this.failureTime = failureTime;
    }

    /**
     * 设置认证token
     *
     * @param id      用户登录ID
     * @param subject 用户登录名
     * @param map     其他私有数据
     * @return
     */
    public String createJwt(String id, String subject, Map<String, Object> map) {

        //1、设置失效时间啊
        long now = System.currentTimeMillis();  //毫秒
        long exp = now + failureTime;

        //2、创建JwtBuilder
        JwtBuilder jwtBuilder = Jwts.builder().setId(id).setSubject(subject)
                .setIssuedAt(new Date())
                //设置签名防止篡改
                .signWith(SignatureAlgorithm.HS256, key);

        //3、根据map设置claims
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            jwtBuilder.claim(entry.getKey(), entry.getValue());
        }
        jwtBuilder.setExpiration(new Date(exp));

        //4、创建token
        String token = jwtBuilder.compact();
        return token;
    }

    /**
     * 解析token
     *
     * @param token
     * @return
     */
    public Claims parseJwt(String token) {
        Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
        return claims;
    }

    /**
     * 配置jwt
     *
     * @return
     */
    @Bean
    public JwtUtils jwtUtils() {
        return new JwtUtils();
    }

    public String createToken(User user) {

        //生成Token登录凭证
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("createTime",user.getCreateTime());
        String token = createJwt(user.getUseruuid(),user.getUsername(),dataMap);
        return token;
    }

    /**
     * claims结构：{
     *         "jti": "10acb560-d1e1-4868-ad4a-ddeb2635187f",编号
     *         "sub": "fan",主题
     *         "iat": 1643340114,签发时间
     *         "createTime": 1643296960000,自定义内容
     *         "exp": 1643340414，过期时间
     *     }
     * @param token
     * @return
     */

    public String refreshToken(String token) {
        Claims claims =  parseJwt(token);
        Date exp=claims.getExpiration();
        Date now=new Date();
        int result=CommonUtil.compareDate(now,exp,thresh);
        if(result==2){ //达到刷新阈值即刷新
            User user =new User();
            user.setUseruuid(claims.getId());
            user.setUsername(claims.getSubject());
            user.setCreateTime(new Date((Long)claims.get("createTime")));
            return createToken(user);
        }
       return "";
    }

    /**
     * 根据token获取用户id
     * @return
     */
    public String getUseruuid(){
        Claims claims =  parseJwt(ThreadLocalUtil.get(CommonConstant.TOKEN_KEY));
        return claims.getId();
    }


}
