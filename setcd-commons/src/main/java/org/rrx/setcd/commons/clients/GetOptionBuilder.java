package org.rrx.setcd.commons.clients;

import io.etcd.jetcd.ByteSequence;
import io.etcd.jetcd.options.GetOption;

import java.nio.charset.StandardCharsets;

/**
 * @Auther: kevin3046@163.com
 * @Date: 2020/9/2 10:45
 * @Description:
 */
public class GetOptionBuilder {

    private GetOption.Builder builder;

    public GetOptionBuilder(){
        this.builder = GetOption.newBuilder();
    }

    public GetOptionBuilder withPrefix(String key){
        this.builder.withPrefix(ByteSequence.from(key, StandardCharsets.UTF_8));
        return this;
    }

    public GetOption build(){
        return this.builder.build();
    }

}
