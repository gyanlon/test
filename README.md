如果需要浏览最新代码，请确保使用dev分支
=======================================

框架功能使用说明
----------------
<a href="#pagination">分页功能</a>  |  <a href="#exceptionhandler">统一异常处理</a> | <a href="#validation">后端注解验证机制</a> | 
<a href="#API的请求头">API的请求头</a> | <a href="#MQ组件">MQ组件</a> | <a href="#redis">缓存组件</a> | <a href="#unittest">单元测试</a> | 
<a href="#Swagger UI">Swagger UI</a> | <a href="#URL设置">URL设置</a> | <a href="#Log file 配置">Log file 配置</a> | 
<a href="#pack">打包配置</a> | <a href="#scheduler">定时任务</a> | <a href="#mail">邮件发送<a>

### <a name="pagination">分页功能</a>
具体写法参考：
com.eg.egsc.scp.demo.web.DemoUserController的queryPageData方法

### <a name="exceptionhandler">统一异常处理</a>
异常处理方法：
* 创建系统自己的异常类:

参照DemoException

* 异常处理：只需要在合适的地方抛出自定义业务异常，框架统一处理。

参照 com.eg.egsc.scp.demo.web.DemoUserController.getUser
if (!isValid(id)) {
      throw new DemoException("demo.usermgnt.userisblank");
    }
其中demo.usermgnt.userisblank定义在属性文件
demo-service\src\main\resources\messages\ exception.properties中，以支持国际化。

* 错误消息定义：

demo.usermgnt.userisblank=00001:用户名不能为空
其中00001:为错误代码，共五位，前两位为系统编号， 00表示公共框架。
后三位为具体错误编号。

### <a name="validation">后端注解验证机制</a>
后端验证方法：
* bean中使用validation annotation:

参照：com.eg.egsc.scp.demo.mapper.entity. DemoUser
    @NotBlank(message = "demo.usermgnt.userisblank")
    private String id;
其中demo.usermgnt.userisblank定义在属性文件
demo-service\src\main\resources\messages\ exception.properties中，以支持国际化。

* controller中使用@Valid注解

参照: com.eg.egsc.scp.demo.web.DemoUserController的createUser方法

### <a name="API的请求头">API的请求头</a>
* sourceSysId : 框架根据application name配置统一赋值
* targetSysId : 框架根据url统一赋值
* businessId ： 需要调用component-sequence接口DistributeSequenceService.getSequence(String sysCode)得到系统序列号，其中sysCode定义在共享目录：\\zcfs224vw\恒大智慧社区\1-公共区\08.新人指南\EGSC_公共平台_应用与组件命名清单_v1.0_20171208
* 扩展属性extAttributes.  

用法参照：com.eg.egsc.scp.demo.client.impl. DemoUserClientImpl的main方法

### <a name="MQ组件">MQ组件</a>
用法参照工程developer-demo\demo\demo-service
* 1、pom.xml中加入依赖jar包，从私服下载相关jar包

        <dependency>
			<groupId>org.springframework.amqp</groupId>
			<artifactId>spring-rabbit</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.amqp</groupId>
			<artifactId>spring-amqp</artifactId>
		</dependency>	
		<dependency>
			<groupId>com.eg.egsc.component</groupId>
			<artifactId>component-rabbitmq</artifactId>
			<version>0.0.3-SNAPSHOT</version>
		</dependency>
* 2、application.properties中定义mq的配置信息，框架支持同时使用2个mq，如果不使用mq或只用其中1个则删除相关配置

    #default mq 使用时放开
    
    spring.rabbitmq.host=localhost
    
    spring.rabbitmq.port=5672
    
    spring.rabbitmq.username=rabbitmq
    
    spring.rabbitmq.password=rabbitmq
    
    spring.rabbitmq.virtualHost=test/test
    
    spring.rabbitmq.queues=Q_DEMO_002,Q_TEST_002 #需要定义的queue名称，如果不需要定义，可以注释此行
    
    #topic
    
    spring.rabbitmq.topics=DEFAULT_TOPIC_MESSAGE_01,DEFAULT_TOPIC_MESSAGE_02,DEFAULT_TOPIC_TEST_03
    
    #路由规则，多个路由规则使用&分割, *匹配一个word，#匹配多个word
    
    spring.rabbitmq.topic.routing=message.test.*:DEFAULT_TOPIC_MESSAGE_01,DEFAULT_TOPIC_MESSAGE_02,DEFAULT_TOPIC_TEST_03 & #.topic:DEFAULT_TOPIC_TEST_03,DEFAULT_TOPIC_MESSAGE_02
    
    #默认为false。true：自动创建topic的exchange,用户需要开通权限。false：不创建exchange，需要保证mq上已有名称为"TOPIC.EXCHANGE"的exchange
    
    spring.rabbitmq.topic.exchange=true 

    
    #物联网总线mq 使用时放开
    
    iotbus.rabbitmq.host=192.168.0.239
    
    iotbus.rabbitmq.port=5672
    
    iotbus.rabbitmq.username=rabbitmq
    
    iotbus.rabbitmq.password=rabbitmq
    
    iotbus.rabbitmq.virtualHost=egsc
    
    iotbus.rabbitmq.queues=Q_DEMO_001,Q_TEST_001 #需要定义的queue名称，如果不需要定义，可以注释此行
    
    iotbus.rabbitmq.topics=IOTBUS_TOPIC_MESSAGE_01,IOTBUS_TOPIC_MESSAGE_02,IOTBUS_TOPIC_TEST_03
    
    iotbus.rabbitmq.topic.routing=message.test.*:IOTBUS_TOPIC_MESSAGE_01,IOTBUS_TOPIC_MESSAGE_02 & message.*.topic:IOTBUS_TOPIC_TEST_03
    
    iotbus.rabbitmq.topic.exchange=true

* 3、发布消息，继承BaseDefaultMqSender类或BaseIotbusMqSender类，调用父类的sendMessage方法

参考SenderDefaultDemo.java，SenderIotbusDemo.java

启用default mq，类继承com.eg.egsc.framework.client.mq.BaseDefaultMqSender类

启用物联网总线mq，类继承com.eg.egsc.framework.client.mq.BaseIotbusMqSender类

* 4、发布topic，继承BaseDefaultMqSender类或BaseIotbusMqSender类，调用父类的sendTopic方法

参考SenderTopicDefaultDemo.java，SenderTopicIotbusDemo.java

启用default mq，类继承com.eg.egsc.framework.client.mq.BaseDefaultMqSender类

启用物联网总线mq，类继承com.eg.egsc.framework.client.mq.BaseIotbusMqSender类

* 5、消费消息，注解中containerFactory的值需要根据application.properties中配置的mq对应

参考ReceiverListenerDefaultDemo.java，ReceiverListenerIotbusDemo.java，ReceiverTopicDefaultDemo.java，ReceiverTopicIotbusDemo.java

启用default mq，使用@RabbitListener(queues = "queuename", containerFactory="rlcFactory")

启用物联网总线mq，使用@RabbitListener(queues = "queuename", containerFactory="iotbusFactory")

* 6、注意：使用MQ发送和接收的消息的数据类型必须是com.eg.egsc.framework.client.dto.BaseBusinessDto的子类，参考com.eg.egsc.scp.demo.dto.UserDto

### <a name="redis">缓存组件</a>
参考demo-service工程
* 1、pom.xml中加入依赖jar包，从私服下载相关jar包

        <dependency>
			<groupId>com.eg.egsc.component</groupId>
			<artifactId>component-redis</artifactId>
		</dependency>

* 2、添加配置文件application-rabbit-dev.properties

* 3、application.properties中添加redis，spring.profiles.active = redis-dev

* 4、redis访问参考com.eg.egsc.scp.demo.redis.RedisTest

### <a name="unittest">单元测试</a>
API用法参照文件： \demo-service\src\test\java\com\eg\egsc\scp\demo\api\TestDemoApi.java

Controller用法参照文件： \demo-service\src\test\java\com\eg\egsc\scp\demo\api\TestDemoWebController.java

DemoService用法参照文件： \demo-service\src\test\java\com\eg\egsc\scp\demo\service\TestDemoService.java

### <a name="Swagger UI">Swagger UI</a>
* 注解Api信息，参照： DemoUserApi的selectUser方法
* 访问Swagger UI, http://<ip>:<port>/swagger-ui.html, 比如本地服务： http://localhost:9082/swagger-ui.html
* 配置Swagger,application.properties中的 api.swagger.*, 参考demo-service中的配置
* 集成到eureka, 参照demo-service下的bootstrap.yml配置： statusPageUrlPath: /swagger-ui.html。注册此服务后，别人就可以在eureka上看到服务的API信息

### <a name="URL设置">URL设置</a>
为保证开发环境和测试环境中的URL一致性， URL设置需遵循如下规范：
* Web control URL规范： /\<module name\>/\<action\>/\<parameters\>
* API URL规范： /api/\<module name\>/\<action\>/\<parameters\>
* Client URL规范， client中提供给其他系统访问的URL与服务中API中定义对应， 但是需要前面加上系统英文服务名用于服务网关路由: /\<service name\>/api/\<module name\>/\<action\>/\<parameters\>
比如： API中为 /api/user/lists  对应于Client中的URL为 /scp-usermgmtcomponent/api/user/lists


* dev环境下的配置，需要在dev目录的bootstrap.yml中配置context-path：/\<service name\>， 比如context-path: /scp-usermgmtcomponent, 请参照demo-service项目中dev目录下的配置

* pom.xml更新， 参照demo-service下pom.xml将 build和profiles部分copy到自己的service项目下的pom.xml中

* 本地联调时，client端的IP设置， 有两种方式：
 > 1， 使用构造方法， Client实现类 加入两个构造方法（参考demo-client）  
2， 使用setServiceUrl方法， 参考 ： ((BaseApiClient)demoUserClientImpl).setServiceUrl("http://localhost:8082");

### <a name="Log file 配置">Log file 配置</a>
* 请根据应用或组件名修改log file name, 参照 demo-service\src\main\resources\test\logback-spring.xml, 请注意dev, test, product, pre目录里的logback-spring.xml都要修改。
* 因为服务器权限限制， log文件目录已经更新为/home/hd/logs/
* 删除\<include resource="org/springframework/boot/logging/logback/base.xml" /\>

### <a name="pack">打包配置</a>
* 根目录下POM文件加入如下配置(具体参考demo/pom.xml)：
> \<distributionManagement\>  
		\<repository\>  
			\<id\>nexus-releases\</id\>  
			\<url\>http://192.168.0.237:8081/nexus/content/repositories/releases\</url\>  
		\</repository\>  
		\<snapshotRepository\>  
			\<id\>nexus-snapshots\</id\>  
			\<url\>http://192.168.0.237:8081/nexus/content/repositories/snapshots\</url\>  
		\</snapshotRepository\>  
	\</distributionManagement\>  

* 确保只有根目录POM有上述配置， 子工程下pom如果有此配置，必须清除。

* xxx-dao的pom文件加入如下配置(具体参考demo-dao)： 
>	\<build\>  
		\<resources\>  
			\<resource\>  
				\<directory\>src/main/java\</directory\>  
				\<includes\>  
					\<include\>\*\*/\*.xml\</include\>  
				\</includes\>  
			\</resource\>  
			\<resource\>  
				\<directory\>src/main/resources/${profiles.active}\</directory\>  
			\</resource\>  
		\</resources\>  
	\</build\>  

### <a name="scheduler">定时任务</a>
* 创建自己的JOB类：参考DemoJob
* 配置job调度器：参考jobs-dev.properties， 将注释去掉，启动demo service，可以看到demo job 运行信息(demo job is runing ...).

>> jobs-dev.properties中5个属性为必须：  
1) serviceName : job类的bean name, 参考 DemoJob  
2) methodName : job 类的运行方法  
3) cronExpression： cron定时设置  
4) ips： job运行所在机器的IP  
5) status: 1为生效， 0 为失效 

* 扩展说明：框架提供了统一的任务配置接口IJobConfigService, 来实现定制和扩展. Demo提供了一个默认实现(PropertyJobConfigService)，从properties文件中读取配置，可以直接使用，也可以自行扩展自己的实现。扩展步骤如下：

> 1, 创建IJobConfigService接口的实现类，并将实现类bean name命名为jobConfig(此名必须为jobConfig,且需保证唯一)， 参考PropertyJobConfigService ）  
2, 实现IJobConfigService接口的getAllJobConfigs()方法，返回job配置对象(BusinessJobConfig)列表。
BusinessJobConfig为POJO对象，记录job配置信息，各字段参照上面jobs-dev.properties属性的解释。  
3， 此时就可以创建自己的JOB类，配置Job，并运行。

### <a name="mail">邮件发送<a>
* 参考 demo-service\src\test\java\com\eg\egsc\mail\TestMail.java