package com.atguigu.spring.cloud.factory;

import com.atguigu.spring.cloud.api.EmployeeRemoteService;
import com.atguigu.spring.cloud.entity.Employee;
import com.atguigu.spring.cloud.util.ResultEntity;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 实现 Consumer 端服务降级功能
 * 实现 FallbackFactory 接口时要传入 @FeignClient 接口类型
 * 在 create() 方法中返回 @FeignClient 注解标记的接口类型对象, 当 Provider 调用失败后, 会执行这个对象的对应方法
 * 这个类必须使用 @Component 注解将当前的对象加入 IOC 容器, 当然当前类必须能够被扫描到
 *
 * @author chenjianglin
 * @date 2022/8/16 13:41
 */
@Component
public class MyFallBackFactory implements FallbackFactory<EmployeeRemoteService> {
    // cause 对象是失败原因对于的异常对象
    @Override
    public EmployeeRemoteService create(Throwable cause) {
        return new EmployeeRemoteService() {

            @Override
            public Employee getEmployeeRemote() {
                return null;
            }

            @Override
            public List<Employee> getEmpListRemote(String keyword) {
                return null;
            }

            @Override
            public ResultEntity<Employee> getEmpWithCircuitBreaker(String signal) {
                return ResultEntity.failed("降级机制生效: " + cause.getMessage());
            }
        };
    }
}