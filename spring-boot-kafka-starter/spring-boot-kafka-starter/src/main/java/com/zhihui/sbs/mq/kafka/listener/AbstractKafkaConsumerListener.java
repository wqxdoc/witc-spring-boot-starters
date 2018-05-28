package com.zhihui.sbs.mq.kafka.listener;

import com.zhihui.sbs.mq.kafka.consumer.ConsumerAutoConfiguration;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.springframework.kafka.KafkaException;

/**
 * kafka consumer listener抽象基类
 *
 * @author jiangjun
 * @create 2017/11/22
 */
@Deprecated
public abstract class AbstractKafkaConsumerListener<T> {

    protected Logger logger = null;

    public AbstractKafkaConsumerListener(){
//        try {
//            ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
//            Class<T> clazz = (Class<T>) pt.getActualTypeArguments()[0];
//            logger = LoggerFactory.getLogger(clazz);
//        }catch (Exception e){
//            throw new KafkaException("AbstractKafkaConsumerListener获取子类类型异常！");
//        }
    }

    /**
     * 子类监听器必须要实现该抽象方法
     * 子类需要在该方法上加入注解：
     *  @KafkaListener(topics = {"test"})。
     *  默认采用{@link ConsumerAutoConfiguration}自动装配的 KafkaListenerContainerFactory bean
     * @param record
     */
    public abstract void listen(ConsumerRecord<?,?> record) throws KafkaException;

}
