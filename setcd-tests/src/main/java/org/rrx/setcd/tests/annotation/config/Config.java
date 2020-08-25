package org.rrx.setcd.tests.annotation.config;

import org.rrx.setcd.commons.config.spring.annotation.EnableEtcd;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @Auther: kevin3046@163.com
 * @Date: 2020/8/20 13:48
 * @Description:
 */
@Configuration
@EnableEtcd
@PropertySource("classpath:etcd.properties")
@ComponentScan(value = {"org.rrx.setcd.tests.annotation.service"})
public class Config {

}
