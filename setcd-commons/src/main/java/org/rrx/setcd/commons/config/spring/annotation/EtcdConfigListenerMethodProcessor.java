package org.rrx.setcd.commons.config.spring.annotation;

import org.rrx.setcd.commons.clients.SetcdClients;
import org.rrx.setcd.commons.event.AbstractNotifyListener;
import org.rrx.setcd.commons.event.SetcdEventBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

import static java.lang.reflect.Modifier.*;

/**
 * @Auther: kevin3046@163.com
 * @Date: 2020/8/23 13:16
 * @Description:
 */
public class EtcdConfigListenerMethodProcessor implements BeanPostProcessor, ApplicationContextAware {


    private SetcdClients setcdClients;

    private final Class<EtcdConfigListener> annotationType = EtcdConfigListener.class;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {

        processBean(beanName, bean, bean.getClass());
        return bean;
    }

    private void processBean(String beanName, Object bean, Class<?> beanClass) {

        ReflectionUtils.doWithMethods(beanClass, new ReflectionUtils.MethodCallback() {
            @Override
            public void doWith(Method method)
                    throws IllegalArgumentException {
                EtcdConfigListener annotation = AnnotationUtils.getAnnotation(method, annotationType);
                if (annotation != null) {

                    checkCandidateMethod(method, annotation);
                    processListenerMethod(bean, method, annotation);
                }
            }

        }, new ReflectionUtils.MethodFilter() {
            @Override
            public boolean matches(Method method) {
                return isListenerMethod(method);
            }
        });

    }

    private void checkCandidateMethod(Method method, EtcdConfigListener annotation) {
        Class<?>[] parameterTypes = method.getParameterTypes();

        //验证参数个数
        if (parameterTypes.length != 1) {
            throw new RuntimeException("@EtcdConfigListener method " + method + " parameters count must be one");
        }

        Class<?> targetType = parameterTypes[0];

        //参数类型必须是SetcdEventBean
        if (!targetType.equals(SetcdEventBean.class)) {
            throw new RuntimeException("@EtcdConfigListener method " + method + " parameter type must be SetcdEventBean");
        }

        String dataId = annotation.dataId();
        if (StringUtils.isEmpty(dataId)) {
            throw new RuntimeException("@EtcdConfigListener method " + method + " dataId is null");
        }
    }

    private boolean isListenerMethod(Method method) {

        int modifiers = method.getModifiers();

        Class<?> returnType = method.getReturnType();

        return isPublic(modifiers) && !isStatic(modifiers) && !isNative(modifiers)
                && !isAbstract(modifiers) && void.class.equals(returnType);
    }


    private void processListenerMethod(Object bean, Method method, EtcdConfigListener listener) {

        String dataId = listener.dataId();
        boolean prefix = listener.prefix();
        //注册用户自定义监听
        setcdClients.addListener(dataId, new AbstractNotifyListener(dataId, listener.timeout()) {
            @Override
            protected void onReceived(SetcdEventBean eventBean) {
                ReflectionUtils.invokeMethod(method, bean, eventBean);
            }
        },prefix);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.setcdClients = (SetcdClients) applicationContext.getBean(SetcdClients.class.getName());
    }
}
