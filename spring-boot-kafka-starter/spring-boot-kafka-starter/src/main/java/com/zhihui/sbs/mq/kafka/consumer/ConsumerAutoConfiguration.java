package com.zhihui.sbs.mq.kafka.consumer;

import com.zhihui.mq.kafka.config.ConsumerProperties;
import com.zhihui.mq.kafka.constants.KafkaConsumerConstants;
import com.zhihui.sbs.mq.kafka.config.ConsumerProperties;
import com.zhihui.sbs.mq.kafka.constants.KafkaConsumerConstants;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.util.CollectionUtils;
import java.util.HashMap;
import java.util.Map;

/**
 * kafka high level consumer auto configuration
 *
 * 相关类的用法，请查看{@link EnableKafka}
 *
 * @author  jiangjun
 * @create 2017/11/22
 */
@Configuration
@ConditionalOnProperty(name = {"zhihui.mq.kafka.consumer.servers"},matchIfMissing = false)
@EnableConfigurationProperties(ConsumerProperties.class)
@EnableKafka
public class ConsumerAutoConfiguration {

    private Logger logger = LoggerFactory.getLogger(ConsumerAutoConfiguration.class);

    public Map<String,Object> consumerProperties(ConsumerProperties properties){
        Map<String,Object> consumerProperties = new HashMap<>();
        if(StringUtils.isBlank(properties.getServers())){
            throw new KafkaException("kafka broker servers is null, init kafka consumer failed!");
        }
        if(CollectionUtils.isEmpty(properties.getTopics())){
            throw new KafkaException("kafka consumer described topic is null, init kafka consumer failed!");
        }
        consumerProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, properties.getServers());
        // 默认为StringDeserializer
        if(StringUtils.isBlank(properties.getKeyDeserializer())){
            consumerProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, KafkaConsumerConstants.DEFAULT_KEY_DESERIALIZER_CLASS);
        }else {
            consumerProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, properties.getKeyDeserializer());
        }
        // 默认为StringDeserializer
        if(StringUtils.isBlank(properties.getValueDeserializer())){
            consumerProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaConsumerConstants.DEFAULT_VALUE_DESERIALIZER_CLASS);
        }else {
            consumerProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, properties.getValueDeserializer());
        }
        // 设置consumer group
        if(StringUtils.isBlank(properties.getGroupId())){
            consumerProperties.put(ConsumerConfig.GROUP_ID_CONFIG,KafkaConsumerConstants.DEFAULT_CONSUMER_GROUP_ID);
        }else {
            consumerProperties.put(ConsumerConfig.GROUP_ID_CONFIG,properties.getGroupId());
        }
        // 设置enable.auto.commit。默认为true
        if(properties.getEnableAutoCommit() != null){
            consumerProperties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,properties.getEnableAutoCommit());
        }
        // 设置 auto.offset.reset 策略
        if(StringUtils.isNotBlank(properties.getAutoOffsetReset())){
            consumerProperties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG,properties.getAutoOffsetReset());
        }
        // 设置client.id
        if(StringUtils.isNotBlank(properties.getClientId())){
            consumerProperties.put(ConsumerConfig.CLIENT_ID_CONFIG,properties.getClientId());
        }
        // 设置consumer 过滤器
        if(!CollectionUtils.isEmpty(properties.getInterceptorClasses())){
            consumerProperties.put(ConsumerConfig.INTERCEPTOR_CLASSES_CONFIG,properties.getInterceptorClasses());
        }
        return consumerProperties;
    }

    public ConsumerFactory<String,String> consumerFactory(ConsumerProperties properties){
        return new DefaultKafkaConsumerFactory<String, String>(consumerProperties(properties));
    }

    /**
     * The bean name of the {@link org.springframework.kafka.config.KafkaListenerContainerFactory}
     * to use to create the message listener container responsible to serve this endpoint.
     * <p>If not specified, the default container factory is used, if any.
     * @return the container factory bean name.
     */
    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String,String>> kafkaListenerContainerFactory(ConsumerProperties properties){
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        try {
            factory.setConsumerFactory(consumerFactory(properties));
            factory.setConcurrency(properties.getConsumerConcurrency());
            if(properties.getPollTimeout() != null && properties.getPollTimeout() != 0L) {
                factory.getContainerProperties().setPollTimeout(properties.getPollTimeout());
            }
            return factory;
        }catch (Exception e){
            logger.error("init KafkaListenerContainerFactory error! error info:{}",e);
            throw new KafkaException("init KafkaListenerContainerFactory error!",e);
        }
    }

}
