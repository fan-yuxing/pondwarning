package edu.cau.cn.pondwarning.controller.interceptor;

import com.alibaba.fastjson.JSON;
import edu.cau.cn.pondwarning.util.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class AuthInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(AuthInterceptor.class);


    @Autowired
    private JwtUtils jwtUtils;

    //该方法将在Controller处理之前进行调用
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        //ajax复杂请求会先发送一个OPTIONS头进行探测，并不包含数据，因此要放行，不然会出现跨域
        if("OPTIONS".equals(request.getMethod().toUpperCase())) {
            return true;
        }
        String origin  = request.getHeader(HttpHeaders.ORIGIN);
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Origin",origin);
        if (origin != null) {
            //这里设置允许的自定义header参数,否则自定义token会出现跨域问题
            response.setHeader("Access-Control-Allow-Headers", "Content-Type, token");
            response.setHeader("Access-Control-Allow-Methods", "POST, GET, PATCH, DELETE, PUT, OPTIONS");
        }
        String token = TokenUtil.getRequestToken(request);
        logger.info(">>>拦截到api相关请求，origin："+origin+"<<<");
        //如果token为空
        if (StringUtils.isBlank(token)) {
            setReturn(response, CommonConstant.RE_LOGIN,"用户未登录，请先登录");
            return false;
        }
        //2. 验证token是否失效
        try{
            // 未失效，解析token，得到token中的信息,并刷新token
            String newToken = jwtUtils.refreshToken(token);
            //这里采用每次请求对比是否达到时间阈值（10分钟），达到即刷新token的策略，比较耗时，以后再优化，前端从http header中取最新的token
            if(!newToken.equals("")){
                response.setHeader(CommonConstant.TOKEN_KEY, newToken);
                ThreadLocalUtil.set(CommonConstant.TOKEN_KEY,newToken);
            }
            ThreadLocalUtil.set(CommonConstant.TOKEN_KEY,token);
        }catch (Exception e){
            //token 失效,用户重新登录
            setReturn(response,CommonConstant.RE_LOGIN,"用户未登录，请先登录");
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        ThreadLocalUtil.remove(CommonConstant.TOKEN_KEY);
        logger.info(">>>移除token<<<");
    }
    //返回错误信息
    private static void setReturn(HttpServletResponse response, int status, String msg) throws IOException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
        httpResponse.setHeader("Access-Control-Allow-Origin", HttpContextUtil.getOrigin());
        //UTF-8编码
        httpResponse.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=utf-8");
        Result build = Result.build(status, msg);
        String json = JSON.toJSONString(build);
        httpResponse.getWriter().print(json);
    }

}
