package org.rrx.setcd.tests.annotation.service;

import org.rrx.setcd.commons.config.spring.annotation.EtcdConfigListener;
import org.rrx.setcd.commons.event.SetcdEventBean;
import org.springframework.stereotype.Component;


/**
 * @Auther: kevin3046@163.com
 * @Date: 2020/8/22 18:52
 * @Description:
 */
@Component
public class EtcdListener {


    public EtcdListener() {
        System.out.println("EtcdListener init");
    }

    /**
     * 当节点dataId变化时，触发回调
     *
     * @param eventBean
     */
    @EtcdConfigListener(dataId = "/test/foo1")
    public void onChange(SetcdEventBean eventBean) {
        System.out.println("onChange发生了变化-------1" + eventBean.toString());
    }

    /**
     * 可以对同一个dataId,定义多个监听器
     *
     * @param eventBean
     */
    @EtcdConfigListener(dataId = "/test/foo2")
    public void onChange2(SetcdEventBean eventBean) {
        System.out.println("onChange2发生了变化-------2" + eventBean.toString());
    }

    /**
     * 监听所有/test前缀开头的key
     * @param eventBean
     */
    @EtcdConfigListener(dataId = "/test",prefix = true)
    public void onChange3(SetcdEventBean eventBean) {
        System.out.println("onChange3发生了变化-------3" + eventBean.toString());
    }

}
