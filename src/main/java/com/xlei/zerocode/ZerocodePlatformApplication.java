package com.xlei.zerocode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
//@EnableAspectJAutoProxy(exposeProxy = true)
public class ZerocodePlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZerocodePlatformApplication.class, args);
    }

}
