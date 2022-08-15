package com.atguigu.spring.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * `@EnableFeignClients` 启用 Feign 客户端功能
 *
 * @author chenjianglin
 * @date 2022/8/15 10:11
 */
@EnableFeignClients
@SpringBootApplication
public class AtguiguMainType {

    public static void main(String[] args) {
        SpringApplication.run(AtguiguMainType.class, args);
    }

}
