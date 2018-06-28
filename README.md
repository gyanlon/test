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
com.xyz.demo.web.DemoUserController的queryPageData方法

### <a name="exceptionhandler">统一异常处理</a>
异常处理方法：
* 创建系统自己的异常类:

参照DemoException

* 异常处理：只需要在合适的地方抛出自定义业务异常，框架统一处理。

参照 com.xyz.demo.web.DemoUserController.getUser
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

参照：com.xyz.demo.mapper.entity. DemoUser
    @NotBlank(message = "demo.usermgnt.userisblank")
    private String id;
其中demo.usermgnt.userisblank定义在属性文件
demo-service\src\main\resources\messages\ exception.properties中，以支持国际化。

* controller中使用@Valid注解

参照: com.xyz.demo.web.DemoUserController的createUser方法

### <a name="API的请求头">API的请求头</a>
* sourceSysId : 框架根据application name配置统一赋值
* targetSysId : 框架根据url统一赋值
* businessId ： 需要调用component-sequence接口DistributeSequenceService.getSequence(String sysCode)得到系统序列号，其中sysCode定义在共享目录：
* 扩展属性extAttributes.  

用法参照：com.xyz.demo.client.impl. DemoUserClientImpl的main方法

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
			<groupId>com.xyz.component</groupId>
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
    
    #默认为false。true：自动创建topic的exchange,用户需要开通权限。false：不创建exchange，需要保证mq上已有名称
