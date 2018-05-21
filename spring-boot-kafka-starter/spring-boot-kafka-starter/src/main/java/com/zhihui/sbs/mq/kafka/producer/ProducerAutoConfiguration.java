package com.zhihui.sbs.mq.kafka.producer;

import com.zhihui.mq.kafka.config.ProducerProperties;
import com.zhihui.mq.kafka.constants.KafkaProducerConstants;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import java.util.HashMap;
import java.util.Map;

/**
 * kafka producer自动装配
 *
 * @author jiangjun
 * @create 2017/11/21
 */
@Configuration
@ConditionalOnClass({KafkaTemplate.class})
@ConditionalOnProperty(name = "zhihui.mq.kafka.producer.servers",matchIfMissing = false)
@EnableConfigurationProperties(ProducerProperties.class)
@EnableKafka
public class ProducerAutoConfiguration {

    public Map<String, Object> producerProperties(ProducerProperties properties) {
        Map<String, Object> producerProperties = new HashMap<>();
        if (StringUtils.isBlank(properties.getServers())) {
            throw new KafkaException("kafka broker servers is null, init kafka producer failed!");
        }
        producerProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, properties.getServers());
        // 默认为StringSerializer
        if (StringUtils.isBlank(properties.getKeySerializer())) {
            producerProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, KafkaProducerConstants.DEFAULT_KEY_SERIALIZER_CLASS);
        } else {
            producerProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, properties.getKeySerializer());
        }
        // 默认为StringSerializer
        if (StringUtils.isBlank(properties.getValueSerializer())) {
            producerProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaProducerConstants.DEFAULT_VALUE_SERIALIZER_CLASS);
        } else {
            producerProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, properties.getValueSerializer());
        }
        // 默认为 1
        if (StringUtils.isBlank(properties.getAcks())) {
            producerProperties.put(ProducerConfig.ACKS_CONFIG, KafkaProducerConstants.DEFAULT_PRODUCER_ACKS);
        } else {
            producerProperties.put(ProducerConfig.ACKS_CONFIG, properties.getAcks());
        }
        // 默认为 0
        if (properties.getRetries() != null) {
            producerProperties.put(ProducerConfig.RETRIES_CONFIG, KafkaProducerConstants.DEFAULT_PRODUCER_RETIRES);
        }
        // 默认为 16384
        if (properties.getBatchSize() != null) {
            producerProperties.put(ProducerConfig.BATCH_SIZE_CONFIG, properties.getBatchSize());
        }
        // 默认 0
        if (properties.getLingerMs() != null) {
            producerProperties.put(ProducerConfig.LINGER_MS_CONFIG, properties.getLingerMs());
        }
        // broker分区策略类（全限定名）
        // 默认为 DefaultPartitioner
        if (StringUtils.isBlank(properties.getPartitionerClass())) {
            producerProperties.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, KafkaProducerConstants.DEFAULT_PRODUCER_PARTITIONER_CLASS);
        }
        return producerProperties;
    }

    public ProducerFactory<String, String> producerFactory(ProducerProperties producerProperties) {
        return new DefaultKafkaProducerFactory<>(producerProperties(producerProperties));
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate(ProducerProperties producerProperties) {
        KafkaTemplate<String, String> kafkaTemplate = new KafkaTemplate<>(producerFactory(producerProperties));
        // 设置producer listener
        kafkaTemplate.setProducerListener(new SimpleProducerListener());
        return kafkaTemplate;
    }
}
