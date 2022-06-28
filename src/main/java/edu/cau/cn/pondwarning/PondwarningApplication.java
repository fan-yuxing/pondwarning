package edu.cau.cn.pondwarning;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

//@SpringBootApplication
//@EnableScheduling // 开启定时任务功能
//@EnableTransactionManagement//开启事务的管理支持
//public class PondwarningApplication extends SpringBootServletInitializer {
//
//    public static void main(String[] args) {
//        SpringApplication.run(PondwarningApplication.class, args);
//    }
//
//    @Override
//    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
//        return builder.sources(PondwarningApplication.class);
//    }
//}

@SpringBootApplication
@EnableScheduling // 开启定时任务功能
@EnableTransactionManagement//开启事务的管理支持
public class PondwarningApplication{

    public static void main(String[] args) {
        SpringApplication.run(PondwarningApplication.class, args);
    }

}
