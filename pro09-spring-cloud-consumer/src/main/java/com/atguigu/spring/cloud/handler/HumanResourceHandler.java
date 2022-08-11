package com.atguigu.spring.cloud.handler;

import com.atguigu.spring.cloud.entity.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class HumanResourceHandler {

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping("/consumer/get/employee")
    public Employee getEmployeeRemote() {
        // 声明远程微服务的主机地址 + 端口号
        String host = "http://localhost:1000";

        // 声明具体要调用的功能的 Url 地址
        String url = "/provider/get/employee/remote";

        // 通过 RestTemplate 调用远程微服务
        return restTemplate.getForObject(host + url, Employee.class);
    }

}