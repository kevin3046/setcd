package org.rrx.setcd.commons.config.spring.annotation;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @Auther: kevin3046@163.com
 * @Date: 2020/8/23 12:40
 * @Description:
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Import(EtcdConfigConfigurationRegistrar.class)
public @interface EnableEtcdConfig {

}
