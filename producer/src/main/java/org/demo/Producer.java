package org.demo;


import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@EnableDubbo
@AutoConfiguration
@Configuration
public class Producer {
    public static void main( String[] args )
    {
        SpringApplication.run(Producer.class, args);
    }
}
