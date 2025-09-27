package com.xlei.zerocode;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@MapperScan("com.xlei.zerocode.mapper")
//@EnableAspectJAutoProxy(exposeProxy = true)
public class ZerocodePlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZerocodePlatformApplication.class, args);
    }

}
