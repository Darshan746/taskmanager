package com.encora.taskmanager;

import com.encora.taskmanager.constant.StringConstants;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@SpringBootApplication
@ComponentScan(basePackages = {StringConstants.BASE_PACKAGE_SCANNER})
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class TaskmanagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskmanagerApplication.class, args);
    }

}
