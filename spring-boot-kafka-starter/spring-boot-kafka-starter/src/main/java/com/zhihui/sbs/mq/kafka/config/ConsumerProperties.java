package com.zhihui.sbs.mq.kafka.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import java.util.List;

/**
 * kafka high level consumer config
 *
 * @author jiangjun
 * @create 2017/11/22
 */
@ConfigurationProperties(prefix = "zhihui.mq.kafka.consumer")
public class ConsumerProperties {

    /**
     * kafka servers
     * 格式：host1:port1,host2:port2
     */
    private String servers;

    /**
     * key 反序列化类
     * 默认为StringDeserializer
     */
    private String keyDeserializer;

    /**
     * value 反序列化类
     * 默认为StringDeserializer
     */
    private String valueDeserializer;

    /**
     * consumer group
     * 默认为 consumer_group
     */
    private String groupId;

    /**
     * If true the consumer's offset will be periodically committed in the background.
     * 默认为true
     */
    private Boolean enableAutoCommit;

    /**
     *  there is no initial offset in Kafka or if the current offset does not exist any more on the server.
     *  earliest: automatically reset the offset to the earliest offset
        latest: automatically reset the offset to the latest offset
        none: throw exception to the consumer if no previous offset is found for the consumer's group
     *  默认为 latest
     */
    private String autoOffsetReset;

    /**
     * An id string to pass to the server when making requests
     * 默认为 空
     */
    private String clientId;

    /**
     * A list of classes to use as interceptors.
     * Implementing the {@link org.apache.kafka.clients.consumer.ConsumerInterceptor} interface
     */
    private List interceptorClasses;

    /**
     * the number of consumers to create.
     */
    private Integer consumerConcurrency;

    /**
     * time to block in the consumer waiting for records.
     * default 1000.
     */
    private Long pollTimeout;

    /**
     * consumer订阅的topic
     */
    private List<String> topics;

    @Override
    public String toString() {
        return "ConsumerProperties{" +
                "servers='" + servers + '\'' +
                ", keyDeserializer='" + keyDeserializer + '\'' +
                ", valueDeserializer='" + valueDeserializer + '\'' +
                ", groupId='" + groupId + '\'' +
                ", enableAutoCommit=" + enableAutoCommit +
                ", autoOffsetReset='" + autoOffsetReset + '\'' +
                ", clientId='" + clientId + '\'' +
                ", interceptorClasses=" + interceptorClasses +
                ", consumerConcurrency=" + consumerConcurrency +
                ", pollTimeout=" + pollTimeout +
                ", topics=" + topics +
                '}';
    }

    public List<String> getTopics() {
        return topics;
    }

    public void setTopics(List<String> topics) {
        this.topics = topics;
    }

    public Long getPollTimeout() {
        return pollTimeout;
    }

    public void setPollTimeout(Long pollTimeout) {
        this.pollTimeout = pollTimeout;
    }

    public Integer getConsumerConcurrency() {
        return consumerConcurrency;
    }

    public void setConsumerConcurrency(Integer consumerConcurrency) {
        this.consumerConcurrency = consumerConcurrency;
    }

    public String getServers() {
        return servers;
    }

    public void setServers(String servers) {
        this.servers = servers;
    }

    public String getKeyDeserializer() {
        return keyDeserializer;
    }

    public void setKeyDeserializer(String keyDeserializer) {
        this.keyDeserializer = keyDeserializer;
    }

    public String getValueDeserializer() {
        return valueDeserializer;
    }

    public void setValueDeserializer(String valueDeserializer) {
        this.valueDeserializer = valueDeserializer;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public Boolean getEnableAutoCommit() {
        return enableAutoCommit;
    }

    public void setEnableAutoCommit(Boolean enableAutoCommit) {
        this.enableAutoCommit = enableAutoCommit;
    }

    public String getAutoOffsetReset() {
        return autoOffsetReset;
    }

    public void setAutoOffsetReset(String autoOffsetReset) {
        this.autoOffsetReset = autoOffsetReset;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public List getInterceptorClasses() {
        return interceptorClasses;
    }

    public void setInterceptorClasses(List interceptorClasses) {
        this.interceptorClasses = interceptorClasses;
    }
}
