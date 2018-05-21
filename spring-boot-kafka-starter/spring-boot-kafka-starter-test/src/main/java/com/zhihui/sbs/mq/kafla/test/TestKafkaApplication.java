package com.zhihui.sbs.mq.kafla.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

/**
 * @author jiangjun
 * @create 2017/11/23
 */
@SpringBootApplication(exclude = {HibernateJpaAutoConfiguration.class })
public class TestKafkaApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestKafkaApplication.class,args);
    }

}
