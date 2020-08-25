package org.rrx.setcd.commons.clients;

import org.rrx.setcd.commons.config.EtcdConfigProperties;
import org.springframework.util.StringUtils;

/**
 * @Auther: kevin3046@163.com
 * @Date: 2020/8/23 16:19
 * @Description:
 */
public class SetcdClientBuilder {

    private String namespace;
    private String[] serverAddr;
    private String username = "";
    private String password = "";

    public SetcdClientBuilder namespace(String namespace) {
        this.namespace = namespace;
        return this;
    }

    public SetcdClientBuilder serverAddr(String serverAddr) {
        if (!StringUtils.isEmpty(serverAddr)) {
            this.serverAddr = serverAddr.split(",");
        }
        return this;
    }

    public SetcdClientBuilder username(String username) {
        this.username(username);
        return this;
    }

    public SetcdClientBuilder password(String password) {
        this.username(password);
        return this;
    }


    public static SetcdClientBuilder builder() {
        SetcdClientBuilder builder = new SetcdClientBuilder();
        return builder;
    }

    public SetcdClients build() {
        EtcdConfigProperties etcdConfigProperties = new EtcdConfigProperties();
        etcdConfigProperties.setNamespace(namespace);
        etcdConfigProperties.setServerAddr(serverAddr);
        etcdConfigProperties.setUsername(username);
        etcdConfigProperties.setPassword(password);
        SetcdClients setcdClients = new SetcdClients(etcdConfigProperties);
        setcdClients.initClients();
        return setcdClients;
    }
}
