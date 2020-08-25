package org.rrx.setcd.tests.annotation;

import org.rrx.setcd.commons.config.EtcdConfigProperties;
import org.rrx.setcd.commons.utils.SetcdClientUtils;
import org.rrx.setcd.tests.annotation.config.Config;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;

/**
 * @Auther: kevin3046@163.com
 * @Date: 2020/8/20 13:45
 * @Description:
 */
public class Application {


    public static void main(String[] args) throws IOException, InterruptedException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
        context.start();

        EtcdConfigProperties etcdConfigProperties = (EtcdConfigProperties) context.getBean(EtcdConfigProperties.class.getName());

        System.out.println(etcdConfigProperties.getServerAddr()[0]);

        Thread.sleep(3000L);
        SetcdClientUtils.putValue("/test/foo1","1");
        System.out.println("===========1");
        SetcdClientUtils.putValue("/test/foo2","2");
        System.out.println("===========2");
        SetcdClientUtils.putValue("/test/foo3","3");
        System.out.println("===========3");

        System.in.read();
    }
}
