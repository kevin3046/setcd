package org.rrx.setcd.commons.event;

import io.etcd.jetcd.watch.WatchEvent;

/**
 * @Auther: kevin3046@163.com
 * @Date: 2020/8/23 13:40
 * @Description:
 */
public class SetcdEventBean {

    private WatchEvent.EventType eventType;

    private String key;

    private String value;

    public WatchEvent.EventType getEventType() {
        return eventType;
    }

    public void setEventType(WatchEvent.EventType eventType) {
        this.eventType = eventType;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "SetcdEventBean{" +
                "eventType=" + eventType.toString() +
                ", key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
