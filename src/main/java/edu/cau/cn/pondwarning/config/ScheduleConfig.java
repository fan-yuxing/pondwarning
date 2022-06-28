package edu.cau.cn.pondwarning.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.Executors;


/**
 * springboot定时任务默认是单线程，配置这个类
 * 重新定义一个类,实现 SchedulingConfigurer,在重写的方法中,
 * 调用定时器任务注册器scheduledTaskRegistrar,setScheduler() 传入一个新的线程池,自己可以定义线程池的size.
 * 默认的线城池为SingleThreadScheduledExecutor,所以为单线程
 * 这样重新启动项目,我们的定时任务就会多线程并行执行了
 * 原文链接：https://blog.csdn.net/NokeNoke/article/details/81362257
 */
@Configuration
//定时任务调用一个线程池中的线程。
public class ScheduleConfig implements SchedulingConfigurer {
    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        //参数传入一个size为10的线程池
        scheduledTaskRegistrar.setScheduler(Executors.newScheduledThreadPool(10));
    }
}
