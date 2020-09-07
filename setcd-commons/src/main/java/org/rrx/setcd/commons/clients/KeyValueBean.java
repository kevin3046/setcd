package org.rrx.setcd.commons.clients;

/**
 * @Auther: kevin3046@163.com
 * @Date: 2020/9/2 10:59
 * @Description:
 */
public class KeyValueBean {

    private String key;

    private String value;

    public KeyValueBean(String key, String value) {
        this.key = key;
        this.value = value;
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
}
