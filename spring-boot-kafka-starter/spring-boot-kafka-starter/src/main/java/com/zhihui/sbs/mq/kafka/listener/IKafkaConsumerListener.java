package com.zhihui.sbs.mq.kafka.listener;

import com.zhihui.mq.kafka.consumer.ConsumerAutoConfiguration;
import com.zhihui.sbs.mq.kafka.consumer.ConsumerAutoConfiguration;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.KafkaException;

/**
 * kafka consumer listener interface
 *
 * @author jiangjun
 * @create 2017/11/22
 */
public interface IKafkaConsumerListener {

    /**
     * consumer监听器必须要实现该接口，并将类纳入spring容器进行管理。即可接收kafka订阅的消息
     * 子类需要在该方法上加入注解：
     *  @KafkaListener(topics = {"test"})。
     *  默认采用{@link ConsumerAutoConfiguration}自动装配的 KafkaListenerContainerFactory bean
     * @param record
     */
    void listen(ConsumerRecord<?,?> record) throws KafkaException;
}
