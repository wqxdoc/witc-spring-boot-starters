package com.zhihui.sbs.mq.kafka.listener;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ListenerBeanPostProcessor auto configuration
 *
 * @author jj02 jiangjun
 * @create 2017/11/23
 */
@Configuration
public class ListenerAutoConfiguration {

//    @Bean
//    public ListenerBeanPostProcessor2 listenerBeanPostProcessor2(){
//        return new ListenerBeanPostProcessor2();
//    }

    @Bean
    public ListenerBeanPostProcessor listenerBeanPostProcessor(){
        return new ListenerBeanPostProcessor();
    }
}
