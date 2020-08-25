### 1.单机etcd安装

#### 1.1下载包
wget https://github.com/etcd-io/etcd/releases/download/v3.4.12/etcd-v3.4.12-linux-amd64.tar.gz

参考文章：https://www.jianshu.com/p/2966b6ef5d10

#### 1.2启动etcd

##### 1.2.1 启动v2
nohup ./etcd --enable-v2 --listen-client-urls http://0.0.0.0:2379 --advertise-client-urls http://0.0.0.0:2379 --listen-peer-urls http://0.0.0.0:2380 > /tmp/etcd.log 2>&1 &

##### 1.2.2 默认启动v3
nohup ./etcd --listen-client-urls http://0.0.0.0:2379 --advertise-client-urls http://0.0.0.0:2379 --listen-peer-urls http://0.0.0.0:2380 > /tmp/etcd.log 2>&1 &

#### 1.3 操作命令

参考文章：https://segmentfault.com/a/1190000017408481

##### 1.3.1 查看版本
curl http://127.0.0.1:2379/version 


##### 1.3.2 put一个key（v3版本api）
curl -L http://127.0.0.1:2379/v3/kv/put -X POST -d '{"key":"Zm9v","value":"MTExMTEx"}'

##### 1.3.3 get一个key（v3版本api）
curl -L http://127.0.0.1:2379/v3/kv/range -X POST -d '{"key":"Zm9v"}'
