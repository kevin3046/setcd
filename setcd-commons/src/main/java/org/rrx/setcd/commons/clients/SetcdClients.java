package org.rrx.setcd.commons.clients;

import io.etcd.jetcd.*;
import io.etcd.jetcd.kv.GetResponse;
import io.etcd.jetcd.kv.PutResponse;
import io.etcd.jetcd.options.GetOption;
import io.etcd.jetcd.options.WatchOption;
import io.etcd.jetcd.watch.WatchEvent;
import io.etcd.jetcd.watch.WatchResponse;
import org.rrx.setcd.commons.config.EtcdConfigProperties;
import org.rrx.setcd.commons.event.Listener;
import org.rrx.setcd.commons.event.SetcdEventBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

/**
 * @Auther: kevin3046@163.com
 * @Date: 2020/8/23 12:55
 * @Description:
 */
public class SetcdClients implements ApplicationContextAware, ApplicationListener, InitializingBean {

    private ApplicationContext applicationContext;

    private static SetcdClients instance;

    private EtcdConfigProperties etcdConfigProperties;

    private final AtomicReference<Map<String, CopyOnWriteArrayList<Listener>>> cacheMap = new AtomicReference<>(new HashMap<>());

    private Client client;

    public SetcdClients(EtcdConfigProperties etcdConfigProperties) {
        this.etcdConfigProperties = etcdConfigProperties;
    }


    public String getValue(String key) throws Exception {
        String value = "";
        KV kvClient = client.getKVClient();
        ByteSequence byteSequence = ByteSequence.from(key, StandardCharsets.UTF_8);
        GetResponse getResponse = kvClient.get(byteSequence).get();
        if (getResponse.getKvs().size() > 0) {
            KeyValue keyValue = getResponse.getKvs().get(0);
            value = Optional.ofNullable(keyValue.getValue()).map(v -> v.toString(StandardCharsets.UTF_8)).orElse("");
        }
        return value;
    }

    public Long putValue(String key, String value) throws Exception {
        KV kvClient = client.getKVClient();
        ByteSequence byteSequenceKey = ByteSequence.from(key, StandardCharsets.UTF_8);
        ByteSequence byteSequenceValue = ByteSequence.from(value, StandardCharsets.UTF_8);

        PutResponse putResponse = kvClient.put(byteSequenceKey, byteSequenceValue).get();

        //@TODO kevin
        return putResponse.getPrevKv().getModRevision();
    }

    public void watch(String key, Consumer<SetcdEventBean> consumer) {

        Watch watchClient = client.getWatchClient();
        ByteSequence byteSequence = ByteSequence.from(key, StandardCharsets.UTF_8);
        //watch一次即可
        watchClient.watch(byteSequence,watchResponse -> {
            for (WatchEvent event : watchResponse.getEvents()) {
                consumer.accept(buildEventBean(event));
            }
        });

    }

    public void watch(String key, Consumer<SetcdEventBean> consumer,boolean prefix) {

        Watch watchClient = client.getWatchClient();
        ByteSequence byteSequence = ByteSequence.from(key, StandardCharsets.UTF_8);
        //watch一次即可
        if(prefix) {
            WatchOption watchOption = WatchOption.newBuilder().withPrefix(ByteSequence.from(key, StandardCharsets.UTF_8)).build();
            watchClient.watch(byteSequence, watchOption,watchResponse -> {
                for (WatchEvent event : watchResponse.getEvents()) {
                    consumer.accept(buildEventBean(event));
                }
            });
        }else{
            watchClient.watch(byteSequence,watchResponse -> {
                for (WatchEvent event : watchResponse.getEvents()) {
                    consumer.accept(buildEventBean(event));
                }
            });
        }
    }

    private SetcdEventBean buildEventBean(WatchEvent event){
        WatchEvent.EventType eventType = event.getEventType();
        SetcdEventBean eventBean = new SetcdEventBean();
        eventBean.setEventType(eventType);
        eventBean.setKey(Optional.ofNullable(event.getKeyValue().getKey()).map(bs -> bs.toString(StandardCharsets.UTF_8)).orElse(""));
        eventBean.setValue(Optional.ofNullable(event.getKeyValue().getValue()).map(bs -> bs.toString(StandardCharsets.UTF_8)).orElse(""));
        System.out.println("收到watch回调消息:" + eventBean.toString());
        return eventBean;
    }

    public void initClients() {

        if (client == null) {
            synchronized (SetcdClients.class) {
                if (client == null) {
                    ClientBuilder clientBuilder = Client.builder().endpoints(etcdConfigProperties.getServerAddr());
                    if (!StringUtils.isEmpty(etcdConfigProperties.getUsername())) {
                        clientBuilder.user(ByteSequence.from(etcdConfigProperties.getUsername(), StandardCharsets.UTF_8));
                    }
                    if (!StringUtils.isEmpty(etcdConfigProperties.getPassword())) {
                        clientBuilder.password(ByteSequence.from(etcdConfigProperties.getPassword(), StandardCharsets.UTF_8));
                    }
                    if (!StringUtils.isEmpty(etcdConfigProperties.getNamespace())) {
                        clientBuilder.namespace(ByteSequence.from(etcdConfigProperties.getNamespace(), StandardCharsets.UTF_8));
                    }
                    clientBuilder.loadBalancerPolicy("round_robin");
                    this.client = clientBuilder.build();
                    if (instance == null) {
                        instance = this;
                    }
                }
            }
        }

    }

    public static SetcdClients getInstance() {
        return instance;
    }


    public void addListener(String dataId, Listener listener,boolean prefix) {

        CopyOnWriteArrayList<Listener> listeners = cacheMap.get().get(dataId);
        if (listeners == null) {
            synchronized (cacheMap) {
                listeners = cacheMap.get().get(dataId);
                if (listeners == null) {
                    listeners = new CopyOnWriteArrayList<>();
                    Map<String, CopyOnWriteArrayList<Listener>> copy = cacheMap.get();
                    copy.put(dataId, listeners);
                    cacheMap.set(copy);

                    //注册etcd watch
                    watch(dataId, new Consumer<SetcdEventBean>() {
                        @Override
                        public void accept(SetcdEventBean eventBean) {
                            invokeEtcdListener(dataId, eventBean);
                        }
                    },prefix);
                }
            }
        }
        listeners.add(listener);

    }

    public void addListener(String dataId, Listener listener) {

        CopyOnWriteArrayList<Listener> listeners = cacheMap.get().get(dataId);
        if (listeners == null) {
            synchronized (cacheMap) {
                listeners = cacheMap.get().get(dataId);
                if (listeners == null) {
                    listeners = new CopyOnWriteArrayList<>();
                    Map<String, CopyOnWriteArrayList<Listener>> copy = cacheMap.get();
                    copy.put(dataId, listeners);
                    cacheMap.set(copy);

                    //注册etcd watch
                    watch(dataId, new Consumer<SetcdEventBean>() {
                        @Override
                        public void accept(SetcdEventBean eventBean) {
                            invokeEtcdListener(dataId, eventBean);
                        }
                    });
                }
            }
        }
        listeners.add(listener);

    }

    private void invokeEtcdListener(String dataId, SetcdEventBean eventBean) {

        try {
            CopyOnWriteArrayList<Listener> listeners = cacheMap.get().get(dataId);
            if (listeners.size() == 0) {
                return;
            }
            invoke(listeners, eventBean);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void invoke(List<Listener> listeners, SetcdEventBean eventBean) {
        for (Listener listener : listeners) {
            listener.receiveConfigInfo(eventBean);
        }
    }

    public void close() {
        this.client.close();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {

        if (applicationEvent instanceof ContextRefreshedEvent) {
            init();
        } else if (applicationEvent instanceof ContextStoppedEvent) {
            destroy();
        }
    }

    private void init() {

    }

    private void destroy() {
        SetcdClients setcdClients = SetcdClients.getInstance();
        if (setcdClients != null) {
            setcdClients.close();
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (this.etcdConfigProperties == null) {
            this.etcdConfigProperties = (EtcdConfigProperties) applicationContext.getBean(EtcdConfigProperties.class.getName());
        }
        initClients();
    }
}
