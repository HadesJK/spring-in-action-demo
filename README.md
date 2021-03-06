# spring in action demo

最近打算写一点 spring cache 相关的东西，先复习下 spring，那就从 srping in action 下手吧

**文章抄袭自《spring 实战》第四版**

这里主要针对 spring in action 做点笔记，记录一些注解的作用和基本原理

# spring jar
spring 框架有非常多 jar，看下框图

![spring 框架](./img/spring_框架图.png)

主要学习核心容器

## spring 依赖关系

```java
spring-core
   spring-jcl
spring-beans
   spring-core
spring-expression
   spring-core
spring-context
   spring-aop
   spring-beans
   spring-expression
spring-context-support
   spring-beans
   spring-context
   spring-aop
   spring-expression
   spring-core
```

因为经常用到aop，看下spring aop的依赖关系

```
spring-aop
   spring-beans
   spring-core
```

# bean 生命周期

![](./img/spring_bean_life_cycle.png)

1. spring对bean实例化
2. spring将引用注入到bean的属性中
3. 如果实现BeanNameAware接口，则执行其setBeanName()方法，入参是bean的id
4. 如果实现BeanFactoryAware接口，spring调用setBeanFactory()，传入BeanFactory实例
5. 如果实现了ApplicationContextAware接口，spring调用setApplicationContext()方法，传入应用的上下文的引用
6. 如果实现了BeanPostProcessor接口，spring调用postProcessBeforeInitialization()方法
7. 如果实现了InitializingBean接口，spring调用afterPropertiesSet()方法
8. 如果指定了自定义的初始化方法，spring调用该方法
9. 如果实现了BeanPostProcessor接口，spring调用postProcessAfterInitialization()方法

现在bean可以使用了，当容器关闭时：

1. DisposableBean的destroy()，在容器关闭时，如果Bean类实现了该接口，则执行它的destroy()方法
2. Bean定义文件中定义destroy-method，在容器关闭时，可以在Bean定义文件中使用“destory-method”定义的方法

# 用法

主要是注解的用法

## @Profile
根据不同环境指定的profile生成bean

激活Profile需要依赖两个独立的属性：

- spring.profiles.active
- spring.profiles.default

如果设置了 spring.profiles.active 属性的话， 那么它的值就会用来确定哪个profile是激活的。 
但如果没有设置spring.profiles.active属性的话， 那Spring将会查找spring.profiles.default的值。 
如果spring.profiles.active和spring.profiles.default均没有设置的话， 那就没有激活的profile， 因此只会创建那些没有定义在profile中的bean。

https://github.com/HadesJK/spring-in-action-demo/blob/master/Profile.md

## @Conditional

当满足某个条件时才去创建bean，这时候需要用到@Conditional注解

@Conditional注解需要指定Condition

https://github.com/HadesJK/spring-in-action-demo/blob/master/Conditional.md

## @Scope

spring 默认创建的bean都是单例，但是可以用这个注解进行修改，有以下可选项：

- ConfigurableBeanFactory#SCOPE_SINGLETON
即字符串： singleton，单例，                                                                            整个应用中只创建一个bean，默认方式
- ConfigurableBeanFactory#SCOPE_PROTOTYPE
即字符串：prototype，原型，每次注入或者通过spring上下文获取的时候，都会创建一个bean
- org.springframework.web.context.WebApplicationContext#SCOPE_REQUEST
                      在web应用中，为每个会话创建一个bean
- org.springframework.web.context.WebApplicationContext#SCOPE_SESSION
在web应用中，为每个请求创建一个bean


