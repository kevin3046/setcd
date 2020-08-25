package org.rrx.setcd.commons.config.spring.annotation;

import org.rrx.setcd.commons.clients.SetcdClients;
import org.rrx.setcd.commons.config.EtcdConfigProperties;
import org.rrx.setcd.commons.utils.PropertySourcesUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * @Auther: kevin3046@163.com
 * @Date: 2020/8/23 12:42
 * @Description:
 */
public class EtcdConfigConfigurationRegistrar implements ImportBeanDefinitionRegistrar, EnvironmentAware {

    private ConfigurableEnvironment environment;

    public static String prefix = "etcd.config.";


    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {

        registerConfig(registry);
        registerClient(registry);
        registerListener(registry);
    }

    private void registerClient(BeanDefinitionRegistry registry) {
        BeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClassName(SetcdClients.class.getName());
        registry.registerBeanDefinition(SetcdClients.class.getName(), beanDefinition);
    }

    private void registerListener(BeanDefinitionRegistry registry) {
        BeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClassName(EtcdConfigListenerMethodProcessor.class.getName());
        registry.registerBeanDefinition(EtcdConfigListenerMethodProcessor.class.getName(), beanDefinition);
    }

    private void registerConfig(BeanDefinitionRegistry registry) {
        BeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClassName(EtcdConfigProperties.class.getName());
        Map<String, Object> configurationProperties = PropertySourcesUtils.getSubProperties(environment.getPropertySources(), environment, prefix);
        System.out.println(configurationProperties);
        for (String key : configurationProperties.keySet()) {
            String name = key.substring(key.lastIndexOf(".") + 1, key.length());
            if (name.equals("serverAddr")) {
                String[] serverAddr = String.valueOf(configurationProperties.get(key)).split(",");
                beanDefinition.getPropertyValues().addPropertyValue(name, serverAddr);
            } else {
                beanDefinition.getPropertyValues().addPropertyValue(name, configurationProperties.get(key));
            }
        }
        registry.registerBeanDefinition(EtcdConfigProperties.class.getName(), beanDefinition);
    }

    @Override
    public void setEnvironment(Environment environment) {
        Assert.isInstanceOf(ConfigurableEnvironment.class, environment);
        this.environment = (ConfigurableEnvironment) environment;
    }
}
