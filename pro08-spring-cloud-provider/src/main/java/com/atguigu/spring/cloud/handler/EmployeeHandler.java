package com.atguigu.spring.cloud.handler;

import com.atguigu.spring.cloud.entity.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class EmployeeHandler {

    private Logger logger = LoggerFactory.getLogger(EmployeeHandler.class);

    @RequestMapping("/provider/get/employee/remote")
    public Employee getEmployeeRemote() {
        return new Employee(555, "tom555 ", 555.55);
    }

    /*    @RequestMapping("/provider/get/employee/remote")
    public Employee getEmployeeRemote(HttpServletRequest request) {
        // 获取当前 Web 应用的端口号
        int serverPort = request.getServerPort();
        return new Employee(555, "tom555 " + serverPort, 555.55);
    }*/

    @RequestMapping("/provider/get/emp/list/remote")
    public List<Employee> getEmpListRemote(@RequestParam("keyword") String keyword) {

        logger.info("keyword=" + keyword);

        List<Employee> empList = new ArrayList<>();
        empList.add(new Employee(33, "empName33", 333.33));
        empList.add(new Employee(44, "empName44", 444.44));
        empList.add(new Employee(55, "empName55", 555.55));

        return empList;
    }

}