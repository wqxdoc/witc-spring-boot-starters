package com.zhihui.sbs.mq.kafla.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 自动生产并发送消息服务
 *
 * @author jiangjun
 * @create 2017/11/23
 */
@Component
public class AutoSendMsgTask {

    private final Logger logger = LoggerFactory.getLogger(AutoSendMsgTask.class);

    private ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(3);

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Autowired
    private SpringTestBean springTestBean;

    @PostConstruct
    public void doSendMsgTask(){
        scheduledExecutorService.scheduleAtFixedRate((() -> {
            doSendMsg();
        }), 5, 30, TimeUnit.SECONDS);
    }

    private void doSendMsg(){
        try {
            springTestBean.doSomething();
            kafkaTemplate.send("test_topic", "test", "test");
        } catch (Exception e) {
            logger.error("发送失败！失败信息：{}",e);
        }
    }
}
