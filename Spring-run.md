## springboot 启动流程
### 流程概述
1. SpringApplication初始化
    * 获取项目类型webApplicationType（NONE）
    * 获取bootstrapRegistryInitializers，注册bootstrapContext
    * 设置ApplicationContextInitializer
    * 设置ApplicationListener
    * 推断main方法所在类
2. SpringApplication.run()
   * **方法createBootstrapContext()** : 创建bootstrapContext。遍历每个BootstrapRegistryInitializer（cloud中的RefreshBootstrapRegistryInitializer和TextEncryptorConfigBootstrapper），执行initialize方法。
     * RefreshBootstrapRegistryInitializer添加closeListener。
     * TextEncryptorConfigBootstrapper文本加密配置初始化。
   * **方法getRunListeners()** : 从META-INF/spring.factories中获取配置的springApplicationRunListener(boot中的EventPublishingRunListener)，初始化EventPublishRunListener等，使用listeners初始化SpringApplicationRunListeners。
   * 启动监听器，执行SpringApplicationRunListener的staring方法，启动ApplicationListener（执行onApplicationEvent）。
   * 初始化ApplicationArguments
   * 打印banner
   * 根据项目类型创建ApplicationContext，设置ApplicationStartup
   * spring容器前置处理，设置环境，执行ApplicationContext的initialize，加载BootstrapImportSelectorConfiguration到beanDefinition（自动装配用到）,通知监听器容器就绪
   * 刷新容器
     * 刷新前准备  - 设置环境
     * 获取BeanFactory
     * 扩展BeanFactory功能
     * postProcessBeanFactory - 空方法
     * 激活BeanFactoryPostProcessors，执行BeanDefinitionRegistryPostProcessor的postProcessBeanDefinitionRegistry，执行BeanFactoryPostProcessor中的postProcessBeanFactory。
     * 注册beanPostProcessors，调佣beanFactory.addBeanPostProcessor(s)注册
     * beanPostProcess.end - 空方法
     * 初始化Message资源 
     * 初始化事件广播器
     * onRefresh - 空方法
     * 注册监听器，将初始化中获取的监听器添加到事件广播器中，获取beanFactory中的监听器并添加到事件监听器
     * 实例化非懒加载单例类 - 实例化后会执行InitializingBean接口方法和处理PostConstruct
     * 完成刷新过程，清空资源缓存，初始化生命周期处理器，调用生命处理器的onFresh，发布最后事件
   * 容器后置处理 - afterRefresh 空方法
   * 结束执行事件
   * 执行Runners - ApplicationRunner和CommandLineRunner
### 事件处理逻辑
1. 三个组件
    * ApplicationEvent：事件类
    * ApplicationListener：事件监听器，发布事件后执行onApplicationEvent
    * ApplicationEventPublisher/ApplicationEventMulticaster：事件发布者，继承接口通过publish发布事件
2. 示例
    * src/test/java/com.ruoyi.business.test.RunTest.java
### 启动时执行方法
1. ApplicationRunner和CommandLineRunner - 启动最后一步执行。
2. InitializingBean接口 - 刷新容器过程中实例化bean时若继承接口，则执行afterPropertiesSet方法。
3. @PostConstruct - 实例化时将注解方法设置为bean的init-method，实例化时通过反射调用，与上面接口处于一个方法中。
### 自动装配原理
1. refresh和自动配置大体流程 - 自动配置处理逻辑主要在ConfigurationClassParser.parse中
   <img src="./2020041121222049.png">
2. @Configuration解析流程 - ConfigurationClassPostProcessor处理逻辑
   <img src="./20200401210855192.png"/>