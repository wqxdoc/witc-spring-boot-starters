package com.zhihui.sbs.mq.kafka.listener;

import com.zhihui.mq.kafka.config.ConsumerProperties;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.ArrayList;
import java.util.List;

/**
 * kafka consumer listener bean post processor
 *
 * @author jiangjun
 * @create 2017/11/22
 */
@Deprecated
public class ListenerBeanPostProcessor2 implements BeanPostProcessor{

    private final String LISTEN_METHOD_NAME = "listen";

    private final Logger logger = LoggerFactory.getLogger(ListenerBeanPostProcessor2.class);

    @Autowired
    private ConsumerProperties consumerProperties;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if(bean instanceof AbstractKafkaConsumerListener){
            try {
                return addKafkaListener((AbstractKafkaConsumerListener) bean);
            } catch (Exception e) {
                throw new KafkaException("ListenerBeanPostProcessor error! " + bean + " not have listen method!");
            }
        }
        return bean;
    }

    private AbstractKafkaConsumerListener addKafkaListener(AbstractKafkaConsumerListener consumerListener){
        //pool creation
        ClassPool pool = ClassPool.getDefault();
        //extracting the class
        CtClass cc = null;
        try {
            cc = pool.get(consumerListener.getClass().getName());
//            cc = pool.makeClass(consumerListener.getClass().getName() + "$proxy");
//            cc.setSuperclass(pool.getCtClass(consumerListener.getClass().getName()));

            ClassFile classFile = cc.getClassFile();
            ConstPool constPool = classFile.getConstPool();
            // create the annotation
            AnnotationsAttribute annotationsAttribute = new AnnotationsAttribute(constPool,AnnotationsAttribute.visibleTag);
            Annotation annotation = new Annotation(KafkaListener.class.getName(),constPool);
            List<StringMemberValue> values = new ArrayList<>();
            consumerProperties.getTopics().forEach(topic -> {
                values.add(new StringMemberValue(topic,classFile.getConstPool()));

            });
//            values.add(new StringMemberValue("test_topic",classFile.getConstPool()));
//            values.add(new StringMemberValue("weixin_sync_notify",classFile.getConstPool()));
            ArrayMemberValue arrayMemberValue = new ArrayMemberValue(classFile.getConstPool());
            MemberValue[] memberValues = new MemberValue[values.size()];
            arrayMemberValue.setValue(values.toArray(memberValues));
            annotation.addMemberValue("topics",arrayMemberValue);
            annotationsAttribute.addAnnotation(annotation);
            //classFile.addAttribute(annotationsAttribute);

            //looking for the method to apply the annotation on
            CtMethod listenMethodDescriptor = cc.getDeclaredMethod(LISTEN_METHOD_NAME);
            //CtMethod listenMethodDescriptor = cc.getDeclaredMethod("sayHelloTo");

//            ClassFile ccFile = cc.getClassFile();
//            ConstPool constpool = ccFile.getConstPool();
//            AnnotationsAttribute attr = new AnnotationsAttribute(constpool, AnnotationsAttribute.visibleTag);
//
//            attr.addAnnotation(annotation);
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
            AbstractKafkaConsumerListener  wrapperdListener = (AbstractKafkaConsumerListener) dynamicBeanClass.newInstance();
            /*try{
                Method listen = wrapperdListener.getClass().getDeclaredMethod("listen", ConsumerRecord.class);
                //getting the annotation
                KafkaListener personneName = (KafkaListener) listen.getAnnotation(KafkaListener.class);
                System.out.println(personneName.topics()[0] );
                System.out.println(personneName.topics()[1] );
            }
            catch(Exception e){
                e.printStackTrace();
            }*/
            return wrapperdListener;
        } catch (Exception e) {
            logger.error("addKafkaListener add annotation error! error:{}",e);
            throw new KafkaException("addKafkaListener add annotation error!",e);
        }
    }

    /*public static void main(String[] args) {
        AbstractKafkaConsumerListener listener = new TestLisnter();
        Object object = addKafkaListener(listener);
       // Object object = addKafkaListener();
        *//*try {
            Method listen = object.getClass().getDeclaredMethod("sayHelloTo");
            KafkaListener annotation = listen.getAnnotation(KafkaListener.class);
            System.out.println(annotation.topics());
        }catch (Exception e){
            e.printStackTrace();
        }*//*
    }*/

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
