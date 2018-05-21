package com.zhihui.sbs.mq.kafla.test;

import com.zhihui.mq.kafka.listener.IKafkaConsumerListener;
import com.zhihui.sbs.mq.kafka.listener.IKafkaConsumerListener;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.KafkaException;
import org.springframework.stereotype.Component;

/**
 * @author jj02 jiangjun
 * @create 2017/11/23
 */
@Component
public class SimpleKafkaConsumerListener implements IKafkaConsumerListener {

    private final Logger logger = LoggerFactory.getLogger(SimpleKafkaConsumerListener.class);

    @Autowired
    private SpringTestBean testBean;

    @Override
    public void listen(ConsumerRecord<?, ?> record) throws KafkaException {
        logger.info("接收到kafka消息！record:{}", record.toString());
        testBean.doSomething();
    }
}
