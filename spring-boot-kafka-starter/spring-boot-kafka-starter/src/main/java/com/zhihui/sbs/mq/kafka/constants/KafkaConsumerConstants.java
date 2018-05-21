package com.zhihui.sbs.mq.kafka.constants;

import org.apache.kafka.common.serialization.StringDeserializer;

/**
 * kafka consumer constant utils
 *
 * @author jiangjun
 * @create 2017/11/22
 */
public class KafkaConsumerConstants {

    /**
     * 默认 consumer 反序列化key类
     */
    public static final String DEFAULT_KEY_DESERIALIZER_CLASS = StringDeserializer.class.getName();

    /**
     * 默认 consumer 反序列化value类
     */
    public static final String DEFAULT_VALUE_DESERIALIZER_CLASS = StringDeserializer.class.getName();

    /**
     * 默认consumer group id
     */
    public static final String DEFAULT_CONSUMER_GROUP_ID = "consumer_group";
}
