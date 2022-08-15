package com.atguigu.spring.cloud.handler;

import com.atguigu.spring.cloud.api.EmployeeRemoteService;
import com.atguigu.spring.cloud.entity.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author chenjianglin
 * @date 2022/8/15 10:13
 */
@RestController
public class FeignHumanResourceHandler {

    /**
     * 转配调用远程微服务的接口, 后面向调用本地方法一样直接使用
     */
    @Autowired
    private EmployeeRemoteService employeeRemoteService;

    @RequestMapping("/feign/consumer/get/emp")
    public Employee getEmployeeRemote() {
        return employeeRemoteService.getEmployeeRemote();
    }

    @RequestMapping("/feign/consumer/search")
    public List<Employee> getEmpListRemote(String keyword) {
        return employeeRemoteService.getEmpListRemote(keyword);
    }

}