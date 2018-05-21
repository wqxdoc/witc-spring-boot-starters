package com.zhihui.sbs.mq.kafka.config;

import org.apache.kafka.clients.producer.internals.DefaultPartitioner;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * kafka broker配置
 *
 * @author jj02 jiangjun
 * @create 2017/11/21
 */
@ConfigurationProperties(prefix = "zhihui.mq.kafka.producer")
public class ProducerProperties {

    /**
     * kafka servers
     * 格式：host1:port1,host2:port2
     */
    private String servers;

    /**
     * key 序列化类
     * 默认为StringSerializer
     */
    private String keySerializer;

    /**
     * value 序列化类
     * 默认为StringSerializer
     */
    private String valueSerializer;

    /**
     *  producer 确认策略
     *  默认为 1
     */
    private String acks;

    /**
     * 默认为 0
     */
    private Integer retries;

    /**
     * 默认为 16384
     */
    private Integer batchSize;

    /**
     * 默认 0
     */
    private Long lingerMs;

    /**
     * broker分区策略类（全限定名）
     * 默认为 {@link DefaultPartitioner}
     */
    private String partitionerClass;

    @Override
    public String toString() {
        return "ProducerProperties{" +
                "servers='" + servers + '\'' +
                ", keySerializer='" + keySerializer + '\'' +
                ", valueSerializer='" + valueSerializer + '\'' +
                ", acks='" + acks + '\'' +
                ", retries=" + retries +
                ", batchSize=" + batchSize +
                ", lingerMs=" + lingerMs +
                ", partitionerClass='" + partitionerClass + '\'' +
                '}';
    }

    public String getServers() {
        return servers;
    }

    public void setServers(String servers) {
        this.servers = servers;
    }

    public String getKeySerializer() {
        return keySerializer;
    }

    public void setKeySerializer(String keySerializer) {
        this.keySerializer = keySerializer;
    }

    public String getValueSerializer() {
        return valueSerializer;
    }

    public void setValueSerializer(String valueSerializer) {
        this.valueSerializer = valueSerializer;
    }

    public String getAcks() {
        return acks;
    }

    public void setAcks(String acks) {
        this.acks = acks;
    }

    public Integer getRetries() {
        return retries;
    }

    public void setRetries(Integer retries) {
        this.retries = retries;
    }

    public Integer getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(Integer batchSize) {
        this.batchSize = batchSize;
    }

    public Long getLingerMs() {
        return lingerMs;
    }

    public void setLingerMs(Long lingerMs) {
        this.lingerMs = lingerMs;
    }

    public String getPartitionerClass() {
        return partitionerClass;
    }

    public void setPartitionerClass(String partitionerClass) {
        this.partitionerClass = partitionerClass;
    }
}
