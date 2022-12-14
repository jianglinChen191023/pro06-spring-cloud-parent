package com.atguigu.spring.cloud.handler;

import com.atguigu.spring.cloud.entity.Employee;
import com.atguigu.spring.cloud.util.ResultEntity;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
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

    /**
     * `@HystrixCommand` 注解通过 fallbackMethod 属性指定断路情况下要调用的备份方法
     * `@HystrixCommand` 注解指定当前方法出问题时调用的备份方法（使用 fallbackMethod 属性指定）
     *
     * @param signal
     * @return
     */
    @HystrixCommand(fallbackMethod="getEmpWithCircuitBreakerBackup")
    @RequestMapping("/provider/get/emp/with/circuit/breaker")
    public ResultEntity<Employee> getEmpWithCircuitBreaker(@RequestParam("signal") String signal) throws InterruptedException {

        if("quick-bang".equals(signal)) {
            throw new RuntimeException();
        }

        if("slow-bang".equals(signal)) {
            Thread.sleep(5000);
        }

        return ResultEntity.successWithData(new Employee(666, "empName666", 666.66));
    }

    public ResultEntity<Employee> getEmpWithCircuitBreakerBackup(@RequestParam("signal") String signal) {
        return ResultEntity.failed("方法执行出现问题, 执行断路 signal: " + signal);
    }

}