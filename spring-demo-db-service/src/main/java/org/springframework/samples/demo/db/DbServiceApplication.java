package org.springframework.samples.demo.db;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * @author wdongyu
 */
@EnableDiscoveryClient
@SpringBootApplication
public class DbServiceApplication {

    public static void main(String[] args) {
	new SpringApplicationBuilder(DbServiceApplication.class).web(true).run(args);
        //SpringApplication.run(DbServiceApplication.class, args);
    }

}
