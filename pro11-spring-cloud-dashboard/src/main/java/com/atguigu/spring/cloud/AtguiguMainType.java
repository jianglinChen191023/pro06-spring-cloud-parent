package com.atguigu.spring.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

/**
 * `@EnableHystrixDashboard` 启动 Hystrix 仪表盘功能
 * @author chenjianglin
 * @date 2022/8/16 20:56
 */
@EnableHystrixDashboard
@SpringBootApplication
public class AtguiguMainType {

    public static void main(String[] args) {
        SpringApplication.run(AtguiguMainType.class, args);
    }

}