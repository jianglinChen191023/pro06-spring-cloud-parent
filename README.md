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
  - [6. 目标4: `provider`以集群方式启动](#6-目标4-provider以集群方式启动)
    - [6.1 修改`provider`的`handler`方法 (显示端口号)](#61-修改provider的handler方法-显示端口号)
    - [6.2 打包](#62-打包)
    - [6.3 `provider`以集群方式启动](#63-provider以集群方式启动)
    - [6.4 `consumer` 正常访问](#64-consumer-正常访问)
    - [6.5 注意](#65-注意)
  - [扩展](#扩展)
  - [1.下面两个注解功能大致相同:](#1下面两个注解功能大致相同)
    - [1.1 @EnableDiscoveryClient](#11-enablediscoveryclient)
    - [1.2 @EnableEurekaClient](#12-enableeurekaclient)
- [十五 Feign、Hystrix、Zuul](#十五-feignhystrixzuul)
  - [1. `Feign`](#1-feign)
    - [1.1 `Feign` 用法及介绍](#11-feign-用法及介绍)
      - [1.1.1 `Feign` 介绍](#111-feign-介绍)
      - [1.1.2 `Feign` 的用法](#112-feign-的用法)
    - [1.2 使用 `Fengn` 实战](#12-使用-fengn-实战)
    - [1.2.1 `common` 工程](#121-common-工程)
    - [1.2.2 `provider` 工程](#122-provider-工程)
    - [1.2.3 新建一个 `consumer` 工程](#123-新建一个-consumer-工程)
    - [1.2.4 `Feign` 传参中需要注意的地方](#124-feign-传参中需要注意的地方)
      - [1.2.4.1 如果删除 `Common` 中 `@RequestParam("keyword") `](#1241-如果删除-common-中-requestparamkeyword-)
      - [1.2.4.2 如果删除 `Provider` 中 `@RequestParam("keyword")`](#1242-如果删除-provider-中-requestparamkeyword)
      - [1.2.4.3 如果删除 `Commin、Provider` 中 `@RequestParam("keyword")`](#1243-如果删除-comminprovider-中-requestparamkeyword)
  - [2. `Hystrix`](#2-hystrix)
    - [2.1 分布式系统面临的问题](#21-分布式系统面临的问题)
    - [2.2 `Hystrix` 介绍](#22-hystrix-介绍)
      - [2.2.1 `Hystrix` 有什么用](#221-hystrix-有什么用)
    - [2.3 服务熔断机制](#23-服务熔断机制)
      - [2.3.1 依赖信息(`provider`)](#231-依赖信息provider)
      - [2.3.2 主启动类(`provider`)](#232-主启动类provider)
      - [2.3.3 ResultEntity(`common`)](#233-resultentitycommon)
      - [2.3.4 `handler` 方法(`provider`)](#234-handler-方法provider)
    - [2.4 服务降级机制](#24-服务降级机制)
      - [2.4.1 `common` 工程: 依赖](#241-common-工程-依赖)
      - [2.4.2 `common` 工程: `FallbackFactory`](#242-common-工程-fallbackfactory)
      - [2.4.3 `common` 工程: `Feign` 接口](#243-common-工程-feign-接口)
      - [2.4.4 `consumer` 工程: `FeignHumanResourceHandler` 和 `application.yml`](#244-consumer-工程-feignhumanresourcehandler-和-applicationyml)
      - [2.4.5 测试 关闭 `provider` 服务, 测试降级](#245-测试-关闭-provider-服务-测试降级)
    - [2.5 `Hystrix` 监控仪表盘](#25-hystrix-监控仪表盘)
      - [2.5.1 `provider` 工程](#251-provider-工程)
      - [2.5.2 监控工程](#252-监控工程)
      - [2.5.3 查看监控数据](#253-查看监控数据)

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

## 6. 目标4: `provider`以集群方式启动

### 6.1 修改`provider`的`handler`方法 (显示端口号)

```java
package com.atguigu.spring.cloud.handler;

import com.atguigu.spring.cloud.entity.Employee;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class EmployeeHandler {

    @RequestMapping("/provider/get/employee/remote")
    public Employee getEmployeeRemote(HttpServletRequest request) {
        // 获取当前 Web 应用的端口号
        int serverPort = request.getServerPort();
        return new Employee(555, "tom555 " + serverPort, 555.55);
    }

}
```



### 6.2 打包

![img](https://cdn.nlark.com/yuque/0/2022/png/12811585/1660518605808-d72cab49-6bb1-4ef4-96be-727d645c040f.png)

![img](https://cdn.nlark.com/yuque/0/2022/png/12811585/1660519838846-58645523-5290-4e40-b3d5-f0a87d95ed2b.png)

![img](https://cdn.nlark.com/yuque/0/2022/png/12811585/1660519851566-f352cae2-b5e5-4574-9b92-a3e25ac285ef.png)

- pom 追加配置

```xml
<build>
    <plugins>
        <!-- 这个插件将 SpringBoot 应用打包成一个可执行的 jar 包 -->
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
            <executions>
                <execution>
                    <goals>
                        <goal>repackage</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

- 启动: `nohup java -jar xxx.jar > log.out &`



### 6.3 `provider`以集群方式启动

- 安装端口号 1000 启动第一个实例
- 安装端口号 2000 启动第二个实例
- 安装端口号 3000 启动第三个实例



1. 先启动 `pro10-spring-cloud-eureka`
2. 再启动 `pro08-spring-cloud-provider`, 修改 `prot` 后再启动
  1. 1000
  2. 2000
  3. 3000
3. 启动 `pro09-spring-cloud-consumer`

### 6.4 `consumer` 正常访问

- 轮询 `prorider`1000、2000、3000

![img](https://cdn.nlark.com/yuque/0/2022/png/12811585/1660520667653-dac393a4-54bd-484b-ad2e-cf35459cad01.png)



### 6.5 注意

- privider 的微服务名称必须使用**同一个名称**才能构建一个集群, 否则将不会认定为是属于同一个集群



## 扩展

## 1.下面两个注解功能大致相同:

### 1.1 @EnableDiscoveryClient

启用发现服务功能, 不局限于 Eureka 注册中心

### 1.2 @EnableEurekaClient

启用 Eureka 客户端功能, 必须是 Eureka 注册中心


# 十五 Feign、Hystrix、Zuul

```
git checkout -b 15.0.0_fergn_hystrix_zuul
```



![img](https://cdn.nlark.com/yuque/0/2022/png/12811585/1660521409650-deb62b3f-fac4-454d-bbb8-da2914275c75.png)

## 1. `Feign`

### 1.1 `Feign` 用法及介绍

#### 1.1.1 `Feign` 介绍

- `feign`是声明式的`web service`客户端，它让微服务之间的调用变得更简单了，类似`controller`调用`service`。
- `Spring Cloud`集成了`Ribbon`和`Eureka`，可在使用`Feign`时提供负载均衡的`http`客户端。

#### 1.1.2 `Feign` 的用法

![img](https://cdn.nlark.com/yuque/0/2022/png/12811585/1660524601756-c5fbd39c-5f14-4ca6-997c-0f6ef0dc05b7.png)

![img](https://cdn.nlark.com/yuque/0/2022/png/12811585/1660524616245-dcb9b3d0-47c0-4dd6-877a-840480cea487.png)

![img](https://cdn.nlark.com/yuque/0/2022/png/12811585/1660524722041-2f7c4a71-4b95-439e-8a33-7595c4b98877.png)



### 1.2 使用 `Fengn` 实战

![img](https://cdn.nlark.com/yuque/0/2022/png/12811585/1660531700636-b1256df1-b11d-4a16-9db8-7d379e475025.png)

![img](https://cdn.nlark.com/yuque/0/2022/jpeg/12811585/1660532017546-fc0ed3a9-cfe7-4938-8376-9db3e2b3af90.jpeg)



### 1.2.1 `common` 工程

- 导入依赖

```xml
<dependencies>
  <dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-openfeign</artifactId>
  </dependency>
</dependencies>
  
```

- 创建远程调用方法的接口

```java
package com.atguigu.spring.cloud.api;

import com.atguigu.spring.cloud.entity.Employee;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * `@FeignClient` 注解表示当前接口和一个 Provider 对应, 注解中 value 属性指定要调用的 Provider 的微服务名称
 *
 * @author chenjianglin
 * @date 2022/8/15 09:00
 */
@FeignClient("atguigu-provider")
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
    
}
```



### 1.2.2 `provider` 工程

- `EmployeeHandler`

```java
package com.atguigu.spring.cloud.handler;

import com.atguigu.spring.cloud.entity.Employee;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmployeeHandler {

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

}
```



### 1.2.3 新建一个 `consumer` 工程

- `pro09-spring-cloud-feign-consumer`

![img](https://cdn.nlark.com/yuque/0/2022/png/12811585/1660530448195-015198dd-1f79-4b10-ac8a-5291119d2f38.png)

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
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <!-- 这个插件将 SpringBoot 应用打包成一个可执行的 jar 包 -->
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <executions>
                    <execution>
                        <goals>
                            <goal>repackage</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
```

- 主启动类

```java
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
```

- applicatoin.yml

```yaml
server:
  port: 7000
spring:
  application:
    name: atguigu-feign-consumer

eureka:
  client:
    serviceUrl:
      defaultZone: http://localhost:5000/eureka
```



- `FeignHumanResourceHandler.java`

```java
package com.atguigu.spring.cloud.handler;

import com.atguigu.spring.cloud.api.EmployeeRemoteService;
import com.atguigu.spring.cloud.entity.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    
}
```



### 1.2.4 `Feign` 传参中需要注意的地方

- `FeignHumanResourceHandler` - `consumer`

```java
@RequestMapping("/feign/consumer/search")
public List<Employee> getEmpListRemote(String keyword) {
    return employeeRemoteService.getEmpListRemote(keyword);
}
```

- `EmployeeRemoteService` - `common`

```java
@RequestMapping("/provider/get/emp/list/remote")
List<Employee> getEmpListRemote(@RequestParam("keyword") String keyword);
```

- `EmployeeHandler` - `provider`

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

    private Logger logger = LoggerFactory.getLogger(EmployeeHandler.class);
    
    @RequestMapping("/provider/get/emp/list/remote")
    public List<Employee> getEmpListRemote(@RequestParam("keyword") String keyword) {
        
        logger.info("keyword=" + keyword);
        
        List<Employee> empList = new ArrayList<>();
        empList.add(new Employee(33, "empName33", 333.33));
        empList.add(new Employee(44, "empName44", 444.44));
        empList.add(new Employee(55, "empName55", 555.55));
        
        return empList;
    }
```

- 访问

![img](https://cdn.nlark.com/yuque/0/2022/png/12811585/1660535154104-9cf73404-17be-4e3a-9931-4c97ccc63035.png)

- 等待一会就可以了, 服务注册发现需要一点时间

![img](https://cdn.nlark.com/yuque/0/2022/png/12811585/1660534872334-604fb4cf-c39a-4e39-bc84-7b11c709b3c1.png)



##### 1.2.4.1 如果删除 `Common` 中 `@RequestParam("keyword") `

```java
@RequestMapping("/provider/get/emp/list/remote")
List<Employee> getEmpListRemote(String keyword);
```

- 报错信息:

![img](https://cdn.nlark.com/yuque/0/2022/png/12811585/1660535663703-6b7adec8-c00c-4d72-ac7e-6665bcbe7f0c.png)

- 一个 500; 一个400

![img](https://cdn.nlark.com/yuque/0/2022/jpeg/12811585/1660536644061-d45d1aee-7058-455a-9b59-4e4b1efd99f2.jpeg)

- 400 `provider` 的报错信息: 参数问题

  - ![img](https://cdn.nlark.com/yuque/0/2022/png/12811585/1660536227622-9e8dfe61-d71f-4e8b-bb58-af7fc0cd56e2.png)

- 500

  - ![img](https://cdn.nlark.com/yuque/0/2022/png/12811585/1660536516800-2b9e2acf-450c-4ea6-acb1-9d6426c87fc2.png)



##### 1.2.4.2 如果删除 `Provider` 中 `@RequestParam("keyword")`

```java
    @RequestMapping("/provider/get/emp/list/remote")
    public List<Employee> getEmpListRemote(String keyword) {

        logger.info("keyword=" + keyword);

        List<Employee> empList = new ArrayList<>();
        empList.add(new Employee(33, "empName33", 333.33));
        empList.add(new Employee(44, "empName44", 444.44));
        empList.add(new Employee(55, "empName55", 555.55));

        return empList;
    }
```

- 访问成功

![img](https://cdn.nlark.com/yuque/0/2022/png/12811585/1660561304169-a795d4bc-42e2-408b-a468-c141895d0339.png)



##### 1.2.4.3 如果删除 `Commin、Provider` 中 `@RequestParam("keyword")`

- 参数获取失败为 `null`


## 2. `Hystrix`

### 2.1 分布式系统面临的问题

![img](https://cdn.nlark.com/yuque/0/2022/png/12811585/1660561731485-3a38b969-c81a-4685-bf41-a69c582caa45.png)



- 在微服务架构体系下, 服务间调用错综复杂, 交织成一张大网
- 如果其中某个节点突然无法正常工作, 则访问它的众多服务都会被卡住, 进而有更多服务被卡住, 系统中的线程、CPU、内存等资源有可能被迅速耗尽、最终整个服务体系崩溃
- 我们称这样的现象叫`**服务雪崩**`
- CAP: CAP 三个里面不能同时都满足

  - C 一致性
  - A 可用性
  - **P 分区容错性**



### 2.2 `Hystrix` 介绍

- `Hystrix` 是一个用于处理分布式系统的延迟和容错的开源库, 在分布式系统里, 许多依赖不可避免的会调用失败, 比如超时、异常等, `Hystriy` 能够保证在一个依赖出问题的情况下, 不会导致整体服务失败, 避免级联故障, 以提高分布式系统的弹性
- "断路器" 本身是一种开关装置, 当某服务单元发生故障之后, 通过断路器的故障检测（类似于熔断保险丝）, 向调用方法返回一个符合预期的、可处理的备选响应（`FallBack`）, 而不是长时间的等待或者抛出调用无法处理的异常, 这样就保证了服务调用方的线程不会被长时间、不必要地占用, 从而避免故障在分布式系统中的蔓延, 乃至雪崩

#### 2.2.1 `Hystrix` 有什么用

`Hystrix`能够提供服务降级、服务熔断、服务限流、接近实时的监控等方面的功能



### 2.3 服务熔断机制

- 熔断机制是应对雪崩效应的一种微服务链路保护机制
- 当扇出链路的某个微服务不可以或者响应时间太长时, 会进行服务的降级、进而熔断该节点微服务的调用, 快速响应错误信息。

- 当检测到该节点微服务调用响应正常后恢复调用链路
  - 在 `SpringCloud` 框架里熔断机制通过 `Hystrix` 实现
  - `Hystrix` 会监控微服务间调用的状况, 当失败的调用到一定阈值, 缺省是 5 秒内 20 次调用失败就会启动熔断机制

- 熔断机制的注解是 `@HystrixCommand`

![img](https://cdn.nlark.com/yuque/0/2022/png/12811585/1660564449594-926188ba-b098-4fa9-be4d-9af11427fbe8.png)



#### 2.3.1 依赖信息(`provider`)

```xml
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
</dependency>
```



#### 2.3.2 主启动类(`provider`)

```java
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
```



#### 2.3.3 ResultEntity(`common`)

```java
package com.atguigu.spring.cloud.util;

/**
 * 整个项目统一使用这个类型作为 Ajax 请求或远程方法调用返回响应的数据格式
 * 
 * @author chenjianglin
 * @date 2022/8/16 10:14
 */
public class ResultEntity<T> {
    
    private String result;
    private String message;
    private T data;

    public ResultEntity() {

    }

    public ResultEntity(String result, String message, T data) {
        super();
        this.result = result;
        this.message = message;
        this.data = data;
    }

    public static final String SUCCESS = "SUCCESS";
    public static final String FAILED = "FAILED";
    public static final String NO_MESSAGE = "NO_MESSAGE";
    public static final String NO_DATA = "NO_DATA";

    /**
     * 操作成功, 不需要返回数据
     * 
     * @return
     */
    public static ResultEntity<String> successWithoutData() {
        return new ResultEntity<String>(SUCCESS, NO_MESSAGE, NO_DATA);
    }

    /**
     * 操作成功, 需要返回数据
     * 
     * @param data
     * @param <E>
     * @return
     */
    public static <E> ResultEntity<E> successWithData(E data) {
        return new ResultEntity<>(SUCCESS, NO_MESSAGE, data);
    }

    /**
     * 操作失败, 返回错误数据
     * 
     * @param message
     * @param <E>
     * @return
     */
    public static <E> ResultEntity<E> failed(String message) {
        return new ResultEntity<>(FAILED, message, null);
    }

    @Override
    public String toString() {
        return "ResultEntity{" +
                "result='" + result + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
    
}
```



#### 2.3.4 `handler` 方法(`provider`)

```java
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
```

- 正常 `127.0.0.1:1000/provider/get/emp/with/circuit/breaker?signal=22`

![img](https://cdn.nlark.com/yuque/0/2022/png/12811585/1660625534881-47a37806-d2b6-4816-b60a-b4b4034d57c1.png)

- 异常 `127.0.0.1:1000/provider/get/emp/with/circuit/breaker?signal=quick-bang`

![img](https://cdn.nlark.com/yuque/0/2022/png/12811585/1660625606875-ecd42c01-5334-4432-8c54-d7aeb0842f8e.png)

- 超时`127.0.0.1:1000/provider/get/emp/with/circuit/breaker?signal=slow-bang`

![img](https://cdn.nlark.com/yuque/0/2022/png/12811585/1660625824498-ca3dca15-dfed-4c23-84e5-8202483cba0b.png)



### 2.4 服务降级机制

- 服务降级处理是在客户端（`Consumer` 端）实现完成的, 与服务端（`Provider` 端）没有关系
- 当某个 `Consumer` 访问一个 `Provider` 却迟迟等不到响应时执行预先设定好的一个解决方案, 而不是一直等待

![img](https://cdn.nlark.com/yuque/0/2022/jpeg/12811585/1660627763409-40cc9257-29c4-4a4b-8d43-50efad8d2261.jpeg)



#### 2.4.1 `common` 工程: 依赖

```xml
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
</dependency>
```



#### 2.4.2 `common` 工程: `FallbackFactory`

- 请注意自动扫描包的规则
- 比如: `feign-consumer` 工程需要使用 `MyFallBackFactory`,

  - 那么 `MyFallBackFactory` 应该在 `feign-consumer` 工程的主启动类所在包或它的子包下
  - 简单来说: 哪个工程用这个类, 哪个工程必须想办法扫描到这个类

```java
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
```



- `EmployeeRemoteService` 追加 `getEmpWithCircuitBreaker`

```java
package com.atguigu.spring.cloud.api;

import com.atguigu.spring.cloud.entity.Employee;
import com.atguigu.spring.cloud.util.ResultEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * `@FeignClient` 注解表示当前接口和一个 Provider 对应, 注解中 value 属性指定要调用的 Provider 的微服务名称
 *
 * @author chenjianglin
 * @date 2022/8/15 09:00
 */
@FeignClient("atguigu-provider")
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
```



#### 2.4.3 `common` 工程: `Feign` 接口

- 在 `@FeignClient` 注解中增加 `fallbackFactory` 属性

```java
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
```



#### 2.4.4 `consumer` 工程: `FeignHumanResourceHandler` 和 `application.yml`

```java
package com.atguigu.spring.cloud.handler;

import com.atguigu.spring.cloud.api.EmployeeRemoteService;
import com.atguigu.spring.cloud.entity.Employee;
import com.atguigu.spring.cloud.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @RequestMapping("/feign/consumer/test/fallback")
    public ResultEntity<Employee> testFallBack(@RequestParam("signal") String signal) {
        return employeeRemoteService.getEmpWithCircuitBreaker(signal);
    }

}
feign:
  hystrix:
    enabled: true
```



#### 2.4.5 测试 关闭 `provider` 服务, 测试降级

- 访问: `127.0.0.1:7000/feign/consumer/test/fallback?signal=111`

![img](https://cdn.nlark.com/yuque/0/2022/png/12811585/1660631164708-77884776-8b3a-424b-9494-349a5e1ab28b.png)

```plain
{"result":"FAILED","message":"降级机制生效: 拒绝连接 (Connection refused) executing GET http://atguigu-provider/provider/get/emp/with/circuit/breaker?signal=111","data":null}
```



### 2.5 `Hystrix` 监控仪表盘

#### 2.5.1 `provider` 工程

- 依赖

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

- 配置

```yaml
management:
  endpoints:
    web:
      exposure: 
        include: hystrix.stream
```



#### 2.5.2 监控工程

- `pro11-spring-cloud-dashboard`

![img](https://cdn.nlark.com/yuque/0/2022/png/12811585/1660654459477-1a131f37-2799-4f5b-88c6-463a220460aa.png)

- 依赖

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-boot-starter-netflix-hystrix-dashboard</artifactId>
    </dependency>
</dependencies>

<build>
    <plugins>
        <!-- 这个插件将 SpringBoot 应用打包成一个可执行的 jar 包 -->
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
            <executions>
                <execution>
                    <goals>
                        <goal>repackage</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

- 启动 `Hystrix` 仪表盘功能

```java
package com.atguigu.spring.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

/**
 * `@EnableHystrixDashboard` 启动 Hystrix 仪表盘功能
 * @author chenjianglin
 * @date 2022/8/16 20:56
 */
@EnableHystrixDashboard
@SpringBootApplication
public class AtguiguMainType {

    public static void main(String[] args) {
        SpringApplication.run(AtguiguMainType.class, args);
    }

}
```

- 配置

```yaml
server:
  port: 8000
spring:
  application:
    name: atguigu-dashboard
```



#### 2.5.3 查看监控数据

- 直接查看监控数据本身

  - `http://localhost:1000/actuator/hystrix.stream`

    - 说明1: `http://localhost:1000 `访问的是被监控的 `provider` 工程
    - 说明2: `/actuator/hystrix.stream` 是固定格式
    - 说明3: 如果从 `provider` 启动开始它的方法没有被访问过, 那么显示的数据只有 "`ping`:", 要实际访问一个带熔断功能的方法才会有实际数据

  - \1. 访问 `127.0.0.1:1000/provider/get/emp/with/circuit/breaker?signal=111`
  - \2. 访问 `http://localhost:1000/actuator/hystrix.stream`

![img](https://cdn.nlark.com/yuque/0/2022/png/12811585/1660662097985-0e5d2175-380f-4d58-a707-f9cd8aac4f90.png)



- 通过仪表盘工程访问监控数据

  - 第一步: 打开仪表盘工程的首页

    - `http://localhost:8000/hystrix`

  - 第二步: 填入获取监控数据的地址（上面直接查看时使用的地址）

![img](https://cdn.nlark.com/yuque/0/2022/png/12811585/1660659016012-0d10a04b-0f1c-41e5-8320-8e95b4956896.png)

- 填入 `http://localhost:1000/actuator/hystrix.stream`
- 点击按钮 `Monitor Stream`

![img](https://cdn.nlark.com/yuque/0/2022/png/12811585/1660662233486-d2f2c084-a767-4ea7-8adb-dcf4a957cbbc.png)

