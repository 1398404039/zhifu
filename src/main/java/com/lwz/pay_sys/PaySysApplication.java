package com.lwz.pay_sys;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource(value = "classpath:config/appkey.properties")
public class PaySysApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaySysApplication.class, args);
    }

//    @Bean
//    public static PropertySourcesPlaceholderConfigurer properties() {
//        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
//        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
//        yaml.setResources(new FileSystemResource("classpath:config/appkey.config"));//File引入
//        configurer.setProperties(yaml.getObject());
//        return configurer;
//    }
}
