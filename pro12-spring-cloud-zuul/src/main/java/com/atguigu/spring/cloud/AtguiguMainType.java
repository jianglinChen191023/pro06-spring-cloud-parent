package com.atguigu.spring.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * `@EnableZuulProxy` 启用 Zuul 代理功能
 *
 * @author chenjianglin
 * @date 2022/8/17 02:03
 */
@EnableZuulProxy
@SpringBootApplication
public class AtguiguMainType {

    public static void main(String[] args) {
        SpringApplication.run(AtguiguMainType.class, args);
    }

}