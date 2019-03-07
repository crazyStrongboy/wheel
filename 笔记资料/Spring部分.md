## 什么是IOC和DI？
IOC：控制反转，把对象的掌控权利交给Spring这个容器进行掌管，并且会在容器内形成一一映射关系，方便后面的DI操作。

DI：依赖注入，简单的来说就是去被依赖注入的一方去IOC容器中获取自己想要的bean，并在初始化实例的时候设置给自身。

## IOC的注册流程

定位 --> 读取 --->解析 -- > 注册

1. 利用ResourceLoader进行资源的定位。它可以通过类路径，文件系统，URL等方式获取资源位置。
2. 读取出来变成Resource供后面的解析工作。
3. 利用BeanDefinitionReader来完成定义信息的解析。
4. 利用BeanDefinitionParserDelegate去向IOC容器中进行注册。是通过BeanDefinitionRegistry接口来实现的。IOC内部维护一个HashMap来保存解析好的BeanDefinition。后面对bean的操作都围绕着这个HashMap来实现的。

## DI注入的发生点
1. 用户第一次调用getBean方法向IOC容器索取Bean时。
2. 用户在bean中配置lazy-init = false时，这个配置会让容器在解析BeanDefinition时就会进行预实例化。

## 循环依赖
> spring帮我们解决了set注入的循环依赖问题，但是构造器循环依赖的问题并没有解决。在getBean的链路中，spring会在init实例后（此时尚未进行属性的注入动作），将其引用加入到一个earlySingletonObjects这样一个集合中（提前将地址暴露出去供其他bean引用），供后期解决set循环注入的问题。

## FactoryBean和BeanFactory的区别
>**BeanFactory:**指的是IOC容器的抽象编程，ApplicationContext,XmlBeanFactory等等是它的具体实现，可以说BeanFactory是用来存储bean的。

>**FactoryBean:**是可以在IOC容器中一个被管理的bean，可以利用它得到各个bean的实现，是用来创建bean的。

## Spring的bean的生命周期
1. 根据配置进行实例化bean
2. 进行一些属性的依赖注入
3. 若实现了BeanNameAware，BeanClassLoaderAware，BeanFactoryAware，则会进行相关属性的注入
4. 自身实现了前置处理器，则执行其方法
5. 执行init-method
6. 自身实现了后置处理器，则执行其方法
7. 实现了DisposableBean接口,则spring容器关闭时，执行destory方法 。



 ![](https://i.imgur.com/bq2thqN.png)

## Spring bean的5中scope
1. singleton:每次从容器中获取的都是同一个实例。
2. prototype:每次调用getBean都会生成一个新的实例。
3. request:每次http请求都会产生一个新的实例，仅在该http请求中有效。
4. session:每次http请求你都会产生一个新的实例，仅在该会话中有效。
5. global-session:如果你在 web 中使用 global session 作用域来标识 bean，那么 web 会自动当成 session 类型来使用。

## Spring AOP的实现
在通过getBean去获取bean的过程中，在creatBean方法里面有这样一个代码段：

`

// Give BeanPostProcessors a chance to return a proxy instead of the target bean instance.
			
Object bean = resolveBeforeInstantiation(beanName, mbdToUse);

`

如果这个类是一个需要增强的类，则会通过动态代理的方式去生成一个加强类。有接口实现则用jdk默认的代理实现，没有实现接口则用Cglib去实现，Cglib实际上就是生成了一个被实现类的子类，然后调用父类方法。


## SpringMVC 实现原理
![](https://i.imgur.com/dt2fH5U.png)

在第四步过程中，还包括参数的解析过程，并通过反射调用method方法。