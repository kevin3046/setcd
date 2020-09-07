package org.rrx.setcd.tests.annotation;

import org.rrx.setcd.commons.clients.GetOptionBuilder;
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
public class Application {

    private static final Log log = LogFactory.getLog(Application.class);

    public static void main(String[] args) throws IOException, InterruptedException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
        context.start();

        EtcdConfigProperties etcdConfigProperties = (EtcdConfigProperties) context.getBean(EtcdConfigProperties.class.getName());

        System.out.println(etcdConfigProperties.getServerAddr()[0]);

//        Thread.sleep(3000L);
//        SetcdClientUtils.putValue("/test/foo1","1");
//        log.info("===========1");
//        SetcdClientUtils.putValue("/test/foo2","2");
//        log.info("===========2");
//        SetcdClientUtils.putValue("/test/foo3","3");
//        log.info("===========3");
//
//        SetcdClientUtils.getValue("/test",new GetOptionBuilder().withPrefix("/tests"));

        String dir = "/jcache/jcache_client_demo/hots_dir";
        String str = "/jcache/jcache_client_demo/hots_dir/test-1";

        String key = str.substring(dir.length()+1,str.length());


//        SetcdClientUtils.election("/roo/lock", 1L, new ElectionCandidate() {
//            @Override
//            public void startLeadership() throws Exception {
//                while (true){
//                    System.err.println("我是主服务-----1");
//                    TimeUnit.SECONDS.sleep(1);
//                }
//            }
//
//            @Override
//            public void onError(Throwable t) {
//
//            }
//
//            @Override
//            public void onCompleted() {
//
//            }
//        });

        System.in.read();
    }
}
