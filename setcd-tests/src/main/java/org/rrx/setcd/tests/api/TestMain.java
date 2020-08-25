package org.rrx.setcd.tests.api;

import org.rrx.setcd.commons.clients.SetcdClientBuilder;
import org.rrx.setcd.commons.clients.SetcdClients;
import org.rrx.setcd.commons.event.AbstractNotifyListener;
import org.rrx.setcd.commons.event.SetcdEventBean;
import org.rrx.setcd.commons.utils.SetcdClientUtils;

/**
 * @Auther: kevin3046@163.com
 * @Date: 2020/8/23 16:32
 * @Description:
 */
public class TestMain {

    public static void main(String[] args) throws Exception {
        SetcdClients clients = SetcdClientBuilder.builder()
                .serverAddr("http://etcd.dev-test.com:2379")
                .build();

        String dataId = "/foo";
        clients.addListener(dataId, new AbstractNotifyListener(dataId, 3000) {
            @Override
            protected void onReceived(SetcdEventBean eventBean) {
                System.out.println("onReceived" + eventBean.toString());
            }
        });

        System.out.println("setcdClients.getValue====>" + clients.getValue(dataId));

        String ret = SetcdClientUtils.getValue(dataId);
        System.out.println("getValue."+dataId+"=>" + ret);

        for (int i = 0; i < 10; i++) {
            Long v = SetcdClientUtils.putValue(dataId, "test-"+i);
            System.out.println("putValue."+dataId+"===================>" + v);
            Thread.sleep(3000L);
        }
    }
}
