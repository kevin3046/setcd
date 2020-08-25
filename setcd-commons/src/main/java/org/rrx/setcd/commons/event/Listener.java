package org.rrx.setcd.commons.event;


public interface Listener {

    /**
     * 配置内容变更通知
     *
     * @param eventBean
     */
    void receiveConfigInfo(SetcdEventBean eventBean);
}