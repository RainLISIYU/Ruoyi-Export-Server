## SpringCloud 基本原理
### 服务注册销毁
1. Registration，服务实例，服务名称、端口、ip等信息。
2. ServiceRegistry，提供服务注册（register）与注销接口（deregister）。
3. AbstractAutoServiceRegistration，实现自动注册与注销，引用ApplicationListener<WebServletInitializedEvent>，Spring启动时触发onApplicationEvent，执行注册操作。通过使用@PreDestory注解，在容器销毁时执行注销操作。
4. 定义自动配置类，分别自动实例化上面3个实例。
### 服务发现
1. 实现接口DiscoveryClient，getInstances(serviceId)获取对应id的服务实例，getServices()获取注册的所有服务名称。
### 集成ribbon

