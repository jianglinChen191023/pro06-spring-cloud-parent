package com.atguigu.spring.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;

/**
 * ## 1.下面两个注解功能大致相同:
 * ### 1.1 @EnableDiscoveryClient
 * 启用发现服务功能, 不局限于 Eureka 注册中心
 * ### 1.2 @EnableEurekaClient
 * 启用 Eureka 客户端功能, 必须是 Eureka 注册中心
 *
 * `@EnableCircuitBreaker` 注解开启断路器功能
 */
@EnableCircuitBreaker
@SpringBootApplication
public class AtguiguMainType {

    public static void main(String[] args) {
        SpringApplication.run(AtguiguMainType.class, args);
    }

}
