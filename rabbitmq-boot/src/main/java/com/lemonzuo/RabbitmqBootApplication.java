package com.lemonzuo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.oas.annotations.EnableOpenApi;

/**
 * @author LemonZuo
 */
@EnableOpenApi
@SpringBootApplication
public class RabbitmqBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(RabbitmqBootApplication.class, args);
    }

}
