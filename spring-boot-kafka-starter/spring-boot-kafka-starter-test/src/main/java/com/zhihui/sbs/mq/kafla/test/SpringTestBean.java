package com.zhihui.sbs.mq.kafla.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 模拟listener调用sprig容器的bean
 *
 * @author jiangjun
 * @create 2017/11/23
 */
@Component
public class SpringTestBean {

    private final Logger logger = LoggerFactory.getLogger(SpringTestBean.class);

    public void doSomething(){
        logger.info("listener invoke spring bean success...");
    }

}
