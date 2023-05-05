package com.solvd.userservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@ConfigurationPropertiesScan
public class UserServiceApplication {

    /**
     * The main method.
     *
     * @param args the args
     */
    public static void main(final String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }

}
