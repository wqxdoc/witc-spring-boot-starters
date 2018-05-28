package com.zhihui.sbs.mq.kafka.listener;

import com.zhihui.sbs.mq.kafka.config.ConsumerProperties;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.ArrayMemberValue;
import javassist.bytecode.annotation.MemberValue;
import javassist.bytecode.annotation.StringMemberValue;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.annotation.KafkaListener;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * kafka consumer listener bean post processor
 *
 * @author jiangjun
 * @create 2017/11/22
 */
public class ListenerBeanPostProcessor implements BeanPostProcessor, ApplicationContextAware {

    private static final String LISTEN_METHOD_NAME = "listen";

    private static final String KAFKA_LISTENER_ATTR_TOPICS = "topics";

    private final Logger logger = LoggerFactory.getLogger(ListenerBeanPostProcessor.class);

    @Autowired
    private ConsumerProperties consumerProperties;

    private ApplicationContext applicationContext;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof IKafkaConsumerListener) {
            try {
                IKafkaConsumerListener wrapperedListener = setKafkaListenerAnnotation((IKafkaConsumerListener) bean);
                wrapperedListener = setAutowiredProperty(wrapperedListener);
                return wrapperedListener;
            } catch (Exception e) {
                throw new KafkaException("ListenerBeanPostProcessor error! " + bean + " not have listen method!", e);
            }
        }
        return bean;
    }

    /**
     * 为实现了{@link IKafkaConsumerListener}接口的监听器，动态运行时新增{@link KafkaListener}注解，并设置订阅的topics
     *
     * @param consumerListener
     * @return
     */
    private IKafkaConsumerListener setKafkaListenerAnnotation(IKafkaConsumerListener consumerListener) {
        //pool creation
        ClassPool pool = ClassPool.getDefault();
        //extracting the class
        CtClass cc = null;
        try {
            cc = pool.get(consumerListener.getClass().getName());
            ClassFile classFile = cc.getClassFile();
            ConstPool constPool = classFile.getConstPool();
            // create the annotation
            AnnotationsAttribute annotationsAttribute = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
            Annotation annotation = new Annotation(KafkaListener.class.getName(), constPool);
            List<StringMemberValue> values = new ArrayList<>();
            consumerProperties.getTopics().forEach(topic -> {
                values.add(new StringMemberValue(topic, classFile.getConstPool()));

            });
            ArrayMemberValue arrayMemberValue = new ArrayMemberValue(classFile.getConstPool());
            MemberValue[] memberValues = new MemberValue[values.size()];
            arrayMemberValue.setValue(values.toArray(memberValues));
            annotation.addMemberValue(KAFKA_LISTENER_ATTR_TOPICS, arrayMemberValue);
            annotationsAttribute.addAnnotation(annotation);

            //looking for the method to apply the annotation on
            CtMethod listenMethodDescriptor = cc.getDeclaredMethod(LISTEN_METHOD_NAME);
            // add the annotation to the method descriptor
            listenMethodDescriptor.getMethodInfo().addAttribute(annotationsAttribute);
            // transform the ctClass to java class
            Class dynamicBeanClass = cc.toClass(new ClassLoader() {
                @Override
                public Class<?> loadClass(String name) throws ClassNotFoundException {
                    return super.loadClass(name);
                }
            }, null);

            //instanciating the updated class
            IKafkaConsumerListener wrapperdListener = (IKafkaConsumerListener) dynamicBeanClass.newInstance();
            return wrapperdListener;
        } catch (Exception e) {
            logger.error("setKafkaListenerAnnotation add annotation error! error:{}", e);
            throw new KafkaException("setKafkaListenerAnnotation add annotation error!", e);
        }
    }

    /**
     * 初始化包装后的监听器，并设置spring容器注入的属性
     *
     * @param wrapperedListener
     * @return
     * @throws IllegalAccessException
     */
    private IKafkaConsumerListener setAutowiredProperty(IKafkaConsumerListener wrapperedListener) throws IllegalAccessException {
        Field[] fields = wrapperedListener.getClass().getFields();
        Field[] declaredFields = wrapperedListener.getClass().getDeclaredFields();
        Field[] allFields = ArrayUtils.addAll(fields, declaredFields);
        for (Field field : allFields) {
            if (field.getAnnotation(Autowired.class) != null) {
                Object fieldBean = applicationContext.getBean(field.getType());
                field.setAccessible(true);
                field.set(wrapperedListener, fieldBean);
            }
        }
        return wrapperedListener;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
