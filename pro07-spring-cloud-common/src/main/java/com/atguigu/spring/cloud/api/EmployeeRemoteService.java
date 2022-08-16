package com.atguigu.spring.cloud.api;

import com.atguigu.spring.cloud.entity.Employee;
import com.atguigu.spring.cloud.factory.MyFallBackFactory;
import com.atguigu.spring.cloud.util.ResultEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * `@FeignClient` 注解表示当前接口和一个 Provider 对应,
 *      注解中 value 属性指定要调用的 Provider 的微服务名称
 *      注解中 fallbackFactory 属性指定 Provider 不可用时提供备用方案的工厂类型
 * @author chenjianglin
 * @date 2022/8/15 09:00
 */
@FeignClient(value = "atguigu-provider", fallbackFactory = MyFallBackFactory.class)
public interface EmployeeRemoteService {

    /**
     * 远程调用的接口方法
     * 要求 `@RequestMapping` 注解映射的地址一致
     * 要求方法声明一致
     * 用来获取请求参数、`@RequestParam`、`@PathVariable`、`@RequestBody` 不能省略, 两边一致
     *
     * @return
     */
    @RequestMapping("/provider/get/employee/remote")
    Employee getEmployeeRemote();

    @RequestMapping("/provider/get/emp/list/remote")
    List<Employee> getEmpListRemote(@RequestParam("keyword") String keyword);

    @RequestMapping("/provider/get/emp/with/circuit/breaker")
    ResultEntity<Employee> getEmpWithCircuitBreaker(@RequestParam("signal") String signal);

}