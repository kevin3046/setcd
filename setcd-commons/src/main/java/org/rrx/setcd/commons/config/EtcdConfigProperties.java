package org.rrx.setcd.commons.config;

/**
 * @Auther: kevin3046@163.com
 * @Date: 2020/8/23 12:44
 * @Description:
 */
public class EtcdConfigProperties {

    private String namespace = "/setcd-demospace";
    private String[] serverAddr;
    private String username = "";
    private String password = "";

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String[] getServerAddr() {
        return serverAddr;
    }

    public void setServerAddr(String[] serverAddr) {
        this.serverAddr = serverAddr;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
