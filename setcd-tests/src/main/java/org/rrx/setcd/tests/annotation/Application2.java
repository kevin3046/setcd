package org.rrx.setcd.tests.annotation;

import org.rrx.setcd.commons.config.EtcdConfigProperties;
import org.rrx.setcd.commons.election.ElectionCandidate;
import org.rrx.setcd.commons.logging.Log;
import org.rrx.setcd.commons.logging.LogFactory;
import org.rrx.setcd.commons.utils.SetcdClientUtils;
import org.rrx.setcd.tests.annotation.config.Config;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: kevin3046@163.com
 * @Date: 2020/8/20 13:45
 * @Description:
 */
public class Application2 {

    private static final Log log = LogFactory.getLog(Application2.class);

    public static void main(String[] args) throws IOException, InterruptedException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
        context.start();

        EtcdConfigProperties etcdConfigProperties = (EtcdConfigProperties) context.getBean(EtcdConfigProperties.class.getName());

        System.out.println(etcdConfigProperties.getServerAddr()[0]);

        Thread.sleep(3000L);
        SetcdClientUtils.putValue("/test/foo1","1");
        log.info("===========1");
        SetcdClientUtils.putValue("/test/foo2","2");
        log.info("===========2");
        SetcdClientUtils.putValue("/test/foo3","3");
        log.info("===========3");

        //SetcdClientUtils.election2();

        SetcdClientUtils.election("/roo/lock", 1L, new ElectionCandidate() {
            @Override
            public void startLeadership() throws Exception {
                while (true){
                    System.err.println("我是备用服务，我开始工作了，估计主服务已经挂了---2");
                    TimeUnit.SECONDS.sleep(1);
                }
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onCompleted() {

            }
        });

        System.in.read();
    }
}
