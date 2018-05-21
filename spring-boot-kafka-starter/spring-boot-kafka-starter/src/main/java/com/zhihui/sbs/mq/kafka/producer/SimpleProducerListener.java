package com.zhihui.sbs.mq.kafka.producer;

import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.util.ObjectUtils;

/**
 * simple implements interface {@link ProducerListener} to logging producer send result info
 *
 * @author jiangjun
 * @create 2017/11/24
 */
public class SimpleProducerListener implements ProducerListener<String,String> {

    private static final Logger logger = LoggerFactory.getLogger(SimpleProducerListener.class);

    private int maxContentLogged = 500;

    /**
     * Invoked after the successful send of a message (that is, after it has been acknowledged by the broker).
     * @param topic the destination topic
     * @param partition the destination partition (could be null)
     * @param key the key of the outbound message
     * @param value the payload of the outbound message
     * @param recordMetadata the result of the successful send operation
     */
    @Override
    public void onSuccess(String topic, Integer partition, String key, String value, RecordMetadata recordMetadata) {
        StringBuffer logOutput = new StringBuffer();
        logOutput.append("sending a message success");
        logOutput.append(" with key=【"
                + toDisplayString(ObjectUtils.nullSafeToString(key), this.maxContentLogged) + "】");
        logOutput.append(" and value=【"
                + toDisplayString(ObjectUtils.nullSafeToString(value), this.maxContentLogged) + "】");
        logOutput.append(" to topic 【" + topic + "】");
        String[] resultArr = recordMetadata.toString().split("@");
        logOutput.append(" send result: topicPartition【" + resultArr[0] + "】 offset 【" + resultArr[1] + "】");
        logger.info(logOutput.toString());
    }

    /**
     * Invoked after an attempt to send a message has failed.
     * @param topic the destination topic
     * @param partition the destination partition (could be null)
     * @param key the key of the outbound message
     * @param value the payload of the outbound message
     * @param exception the exception thrown
     */
    @Override
    public void onError(String topic, Integer partition, String key, String value, Exception exception) {
        StringBuffer logOutput = new StringBuffer();
        logOutput.append("Exception thrown when sending a message");
        logOutput.append(" with key=【"
                + toDisplayString(ObjectUtils.nullSafeToString(key), this.maxContentLogged) + "】");
        logOutput.append(" and value=【"
                + toDisplayString(ObjectUtils.nullSafeToString(value), this.maxContentLogged) + "】");
        logOutput.append(" to topic 【" + topic + "】");
        if (partition != null) {
            logOutput.append(" and partition 【" + partition + "】");
        }
        logOutput.append(":");
        logger.error(logOutput.toString(), exception);
    }

    /**
     * Return true if this listener is interested in success as well as failure.
     * @return true to express interest in successful sends.
     */
    @Override
    public boolean isInterestedInSuccess() {
        return true;
    }

    private String toDisplayString(String original, int maxCharacters) {
        if (original.length() <= maxCharacters) {
            return original;
        }
        return original.substring(0, maxCharacters) + "...";
    }
}
