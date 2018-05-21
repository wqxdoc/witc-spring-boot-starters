package com.zhihui.sbs.mq.kafka.constants;

import org.apache.kafka.clients.producer.internals.DefaultPartitioner;
import org.apache.kafka.common.serialization.StringSerializer;

/**
 * kafka producer constant utils
 *
 * @author jiangjun
 * @create 2017/11/22
 */
public class KafkaProducerConstants {

    /**
     * 默认 producer 序列化key类
     */
    public static final String DEFAULT_KEY_SERIALIZER_CLASS = StringSerializer.class.getName();

    /**
     * 默认 producer 序列化value类
     */
    public static final String DEFAULT_VALUE_SERIALIZER_CLASS = StringSerializer.class.getName();

    /**
     * producer发送消息确认策略。
     * 0：不会等待来自服务器的应答。
     * 1：只等待leader的应答，leader把消息写入日志文件后会发送一个确认
     * all：等待所有in-sync的副本应答
     */
    public static final String DEFAULT_PRODUCER_ACKS = "1";

    /**
     * 发送消息失败，重试次数。
     * 0：禁止重试
     */
    public static final Integer DEFAULT_PRODUCER_RETIRES = 0;

    /**
     * producer 发送消息默认分区策略类
     */
    public static final String DEFAULT_PRODUCER_PARTITIONER_CLASS = DefaultPartitioner.class.getName();
}
