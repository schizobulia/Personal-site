### 部署文档
```
    1. 下载java环境,并配置好Jdk.
    2. wget http://mirrors.hust.edu.cn/apache/tomcat/tomcat-8/v8.5.37/bin/apache-tomcat-8.5.37.tar.gz
    3. tar zxvf apache-tomcat-8.5.37.tar.gz
    3. 修改tomcat默认端口为8001(tomcat默认端口与spark冲突!)
    4. wget https://www.apache.org/dyn/closer.lua/spark/spark-2.4.0/spark-2.4.0-bin-hadoop2.7.tgz
    5. tar zxvf spark-2.4.0-bin-hadoop2.7.tgz
    6. ./spark-2.4.0-bin-hadoop2.7/sbin/start-master.sh
    7. 使用idea将项目打包为war文件,并上传到服务器的中tomacat的webaaps目录下载.(maven打包与可以,根据情况而定)
    8. 启动tomcat
    9. 访问http://127.0.0.1/personal-0.1/createkey(看到生成的key即可)
```

### 参考
- #### [spark部署文档](https://github.com/schizobulia/Spark-Java)
- #### [jdk环境配置](https://blog.csdn.net/u010837794/article/details/77505989)
- #### [tomcat配置与启动](https://blog.csdn.net/piaocoder/article/details/51636178)

##### 说明: 部署成功之后可测试api文档中的所有接口.