- [十四 SpringCloud](#十四-springcloud)
  - [1. SpringCloud 组件](#1-springcloud-组件)
  - [2. SpringCloud 版本说明](#2-springcloud-版本说明)
  - [3. 目标1: 准备基础测试环境](#3-目标1-准备基础测试环境)
    - [3.1 结构](#31-结构)
    - [3.2 创建父工程](#32-创建父工程)
      - [3.2.1 `pom.xml` 文件配置](#321-pomxml-文件配置)
    - [3.3 创建通用工程 - 子](#33-创建通用工程---子)
    - [3.4 创建提供者工程 - 子](#34-创建提供者工程---子)
    - [3.5 创建消费者工程 - 子](#35-创建消费者工程---子)
  - [4. 目标2: 创建`Eureka`注册中心](#4-目标2-创建eureka注册中心)
    - [4.1 子目标1: 创建`Eureka`注册中心工程 - 子](#41-子目标1-创建eureka注册中心工程---子)
    - [4.2 子目标2: 将`provider`注册到`Eureka`](#42-子目标2-将provider注册到eureka)
  - [5. 目标3: `consumer` 访问 `provider` 时使用微服务名称代替 `localhost:1000`](#5-目标3-consumer-访问-provider-时使用微服务名称代替-localhost1000)
    - [5.1 分析](#51-分析)
    - [5.2 操作](#52-操作)

# 十四 SpringCloud

> SpringCloud 核心 -
>- 基于 `HTTP` 协议, 这是它和 `Dubbo` 最本质的区别。`Dubbo` 的核心是基于 `RPC`

## 1. SpringCloud 组件

![img](https://cdn.nlark.com/yuque/0/2022/png/12811585/1660006450364-5c2c76f9-333e-4f01-8382-e5babaa5fc12.png)

- 注册中心: `Eureka`
- 客户端负载均衡: `Ribbon`
- 声明式远程方法调用: `Feign`
- 服务降级、熔断: `Hystrix`
- 网关: `Zuul`

![img](https://cdn.nlark.com/yuque/0/2022/png/12811585/1660005438467-419ac26f-7ed5-4f38-9e15-d546b456a7ff.png)

![img](https://cdn.nlark.com/yuque/0/2022/png/12811585/1660005906416-b8d13c3e-5659-4ea8-aba6-7341ff10fdc4.png)

- `Feign` 是以 `Ribbon` 为基础的（导了 `Feign` 会自动加载 `Ribbon`）

![img](https://cdn.nlark.com/yuque/0/2022/png/12811585/1660006005060-1f674eeb-cd8c-4b5d-8380-7586d26a2b19.png)

![img](https://cdn.nlark.com/yuque/0/2022/png/12811585/1660006040099-0a58c5cd-ce4e-4799-acc5-7ebe598302f3.png)

- `Hystrix`

![img](https://cdn.nlark.com/yuque/0/2022/png/12811585/1660006287609-639355c6-ea16-4068-9ab2-0cf17ea5094b.png)

- `Zuul`统一的一个入口

![img](https://cdn.nlark.com/yuque/0/2022/png/12811585/1660006320992-c5dffbcb-596c-4806-bb21-0d201d584dce.png)

## 2. SpringCloud 版本说明

![img](https://cdn.nlark.com/yuque/0/2022/png/12811585/1660006464040-f7003c92-1162-4a8a-ad9d-60d9a6d76f9a.png)

- 20220809

  - 2021.0.3 最低支持 `SpringBoot` **2.6.8 版本**

![img](https://cdn.nlark.com/yuque/0/2022/png/12811585/1660007197186-787ae2c9-7265-4b5f-bc14-450eaa1d5ce7.png)

## 3. 目标1: 准备基础测试环境

`git checkout -b 14.0.0_spring_cloud`

### 3.1 结构

![img](https://cdn.nlark.com/yuque/0/2022/png/12811585/1660032190626-e8e7c6c1-cc13-4bb5-b294-88d521f8131a.png)

### 3.2 创建父工程

- `Group Id`: `com.atguigu.spring.cloud`
- `Artifact Id`: `pro06-spring-cloud-parent`
- `Packaging`: `pom`

![img](https://cdn.nlark.com/yuque/0/2022/png/12811585/1660033359172-6835a303-10bb-45f3-8938-36478ee290b8.png)

![img](https://cdn.nlark.com/yuque/0/2022/png/12811585/1660229479558-93921972-395c-4088-9c6e-fac2245a14a1.png)

#### 3.2.1 `pom.xml` 文件配置

```xml

<dependencyManagement>
    <dependencies>
        <!-- 导入 SpringCloud 需要使用的依赖信息 -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-dependencies</artifactId>
            <version>Greenwich.SR2</version>
            <type>pom</type>
            <!-- import 依赖范围表示将 spring-cloud-dependencies 包中的依赖信息导入 -->
            <scope>import</scope>
        </dependency>
        <!-- 导入 SpringBoot 需要使用的依赖信息 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-dependencies</artifactId>
            <version>2.1.6.RELEASE</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

### 3.3 创建通用工程 - 子

![img](https://cdn.nlark.com/yuque/0/2022/png/12811585/1660222053517-fd5fff5e-2b7c-432e-a0d6-fe6cd53b0b49.png)

- `Artifact Id`: `pro07-spring-cloud-common`

![img](https://cdn.nlark.com/yuque/0/2022/png/12811585/1660218121599-65f212bd-09ba-4ff2-875c-4832c9014ac5.png)

```java
package com.atguigu.spring.cloud.entity;

public class Employee {

    private Integer empId;
    private String empName;
    private Double empSalary;

    public Employee() {
    }

    public Employee(Integer empId, String empName, Double empSalary) {
        this.empId = empId;
        this.empName = empName;
        this.empSalary = empSalary;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "empId=" + empId +
                ", empName='" + empName + '\'' +
                ", empSalary=" + empSalary +
                '}';
    }

    public Integer getEmpId() {
        return empId;
    }

    public void setEmpId(Integer empId) {
        this.empId = empId;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public Double getEmpSalary() {
        return empSalary;
    }

    public void setEmpSalary(Double empSalary) {
        this.empSalary = empSalary;
    }

}
```

### 3.4 创建提供者工程 - 子

![img](https://cdn.nlark.com/yuque/0/2022/png/12811585/1660222032565-65e55157-6b22-4b60-bb68-f67fad6d8749.png)

![img](https://cdn.nlark.com/yuque/0/2022/png/12811585/1660222528633-844b478b-d02e-4d41-af8a-a4543d513c51.png)

------

- `Artifact Id`: `pro08-spring-cloud-provider`

![img](https://cdn.nlark.com/yuque/0/2022/png/12811585/1660218385887-665bced9-a62f-44d5-8930-5694bc7959e8.png)

- `pom.xml`

```xml

<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>com.atguigu.spring.cloud</groupId>
        <artifactId>pro07-spring-cloud-common</artifactId>
        <version>1.0-SNAPSHOT</version>
    </dependency>
</dependencies>
```

- 创建主启动类

```java
package com.atguigu.spring.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AtguiguMainType {

    public static void main(String[] args) {
        SpringApplication.run(AtguiguMainType.class, args);
    }

}
```

- 创建 `application.yml` 配置文件

```yaml
server:
  port: 1000
```

- 创建 `handler` 类和方法

```java
package com.atguigu.spring.cloud.handler;

import com.atguigu.spring.cloud.entity.Employee;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmployeeHandler {

    @RequestMapping("/provider/get/employee/remote")
    public Employee getEmployeeRemote() {
        return new Employee(555, "tom555", 555.55);
    }

}
```

### 3.5 创建消费者工程 - 子

![img](https://cdn.nlark.com/yuque/0/2022/png/12811585/1660221999435-ce6d2813-8941-4501-ba62-e2ebfff5c2c8.png)

![img](https://cdn.nlark.com/yuque/0/2022/png/12811585/1660222636496-270bc4be-9b9f-4170-9384-a3317cb5e3c4.png)

------

- `Artifact Id`: `pro09-spring-cloud-consumer`

![img](https://cdn.nlark.com/yuque/0/2022/png/12811585/1660219603109-2bf93a0b-bd95-4c09-bce8-6daa4aa75f5b.png)

- `pom.xml`

```xml

<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>com.atguigu.spring.cloud</groupId>
        <artifactId>pro07-spring-cloud-common</artifactId>
        <version>1.0-SNAPSHOT</version>
    </dependency>
</dependencies>
```

- 创建主启动类

```java
package com.atguigu.spring.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AtguiguMainType {

    public static void main(String[] args) {
        SpringApplication.run(AtguiguMainType.class, args);
    }
}
```

- 创建配置类提供 `RestTemplate`

```java
package com.atguigu.spring.cloud.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AtguiguSpringCloudConfig {

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

}
```

- 创建 `handler` 类

```java
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
```

- 创建 `application.yml`

```yaml
server:
  port: 4000
```

## 4. 目标2: 创建`Eureka`注册中心

### 4.1 子目标1: 创建`Eureka`注册中心工程 - 子

![img](https://cdn.nlark.com/yuque/0/2022/png/12811585/1660223802061-1d75ed71-6fd9-42d1-9987-8898e3166844.png)

![img](https://cdn.nlark.com/yuque/0/2022/png/12811585/1660224553003-0da8f73d-b5f7-4b9f-af21-4a01329be157.png)

------

- `Artifact Id`: `pro10-spring-cloud-eureka`

![img](https://cdn.nlark.com/yuque/0/2022/png/12811585/1660313092409-d434ba93-6cd2-46c0-a198-349a45865547.png)

- 加入依赖信息

```xml

<dependencies>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
    </dependency>
</dependencies>
```

- 创建主启动类

```java
package com.atguigu.spring.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

// 启用 Eureka 服务器功能
@EnableEurekaServer
@SpringBootApplication
public class AtguiguMainType {

    public static void main(String[] args) {
        SpringApplication.run(AtguiguMainType.class, args);
    }

}
```

- 创建 `application.yml`

```yaml
server:
  port: 5000

eureka:
  instance:
    hostname: location
  client:
    # 自己就是注册中心, 所以自己不注册自己
    register-with-eureka: false
    # 自己就是注册中心, 所以不需要"从注册中心取回信息"
    fetch-registry: false
    # 客户端访问 Eureka 时使用的地址
    service-url:
      defaultZone: http://${eureka.instance.hostname}:${server.port}/eureka
```

### 4.2 子目标2: 将`provider`注册到`Eureka`

![img](https://cdn.nlark.com/yuque/0/2022/png/12811585/1660225306101-05ca8475-73bb-486a-b7eb-e38e09fbd89d.png)

![img](https://cdn.nlark.com/yuque/0/2022/png/12811585/1660226837746-600feb6e-3c57-467a-aadd-f1d16df23f6f.png)



------

- 加入依赖

```xml

<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```

- `application.yml`追加配置

```yaml
eureka:
  client:
    service-url:
      # 配置当前微服务作为 Eureka 客户端访问 Eureka 服务器端时使用的地址
      defaultZone: http://localhost:5000/eureka

spring:
  application:
    # 指定当前微服务的名称, 以便将来通过微服务名称调用当前微服务时能够找到
    name: atguigu-provider
```

## 5. 目标3: `consumer` 访问 `provider` 时使用微服务名称代替 `localhost:1000`

![img](https://cdn.nlark.com/yuque/0/2022/png/12811585/1660227535977-f8efc026-73a9-4620-ae4c-06e34c318bb6.png)

```java
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
        // String host = "http://localhost:1000";

        // 声明远程微服务调用地址从 `IP地址 + 端口号` 改成 `服务器名称`
        String host = "http://atguigu-provider";
        
        // 声明具体要调用的功能的 Url 地址
        String url = "/provider/get/employee/remote";

        // 通过 RestTemplate 调用远程微服务
        return restTemplate.getForObject(host + url, Employee.class);
    }

}
```

![img](https://cdn.nlark.com/yuque/0/2022/png/12811585/1660227984063-d4621a5a-f2e0-497d-b11e-8111db7f1839.png)

------

### 5.1 分析

![img](https://cdn.nlark.com/yuque/0/2022/png/12811585/1660217224462-8c9d0474-0818-4404-b27b-fe429aa869e0.png)



### 5.2 操作

- 在 `consumer` 工程加入依赖

```xml
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-netflix-ribbon</artifactId>
</dependency>
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```



-  `application.yml` 追加配置

```yaml
spring:
  application:
    name: atguigu-consumer

eureka:
  client:
    service-url:
      defaultZone: http://localhost:5000/eureka
```



- 在 `RestTemplate` 的配置方法处使用 `@LoadBalanced` 注解

```java
package com.atguigu.spring.cloud.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AtguiguSpringCloudConfig {

    // 这个注解让 RestTemplate 有负载均衡功能, 通过调用 Ribbon 访问 Provider 集群
    @LoadBalanced
    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

}
```