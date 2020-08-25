package org.rrx.setcd.commons.config.spring.annotation;

import java.lang.annotation.*;

/**
 * @Auther: kevin3046@163.com
 * @Date: 2020/8/23 12:41
 * @Description:
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface EtcdConfigListener {

    /**
     * 配置集
     *
     * @return
     */
    String dataId();

    /**
     * 最大执行时间，单位毫秒
     *
     * @return
     */
    long timeout() default 5000L;


    /**
     * 启动前缀匹配 等同于 etcdctl 的 --prefix
     * @return
     */
    boolean prefix() default false;

}
