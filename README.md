# spring in action demo

最近打算写一点 spring cache 相关的东西，先复习下 spring，那就从 srping in action 下手吧

这里主要针对 spring in action 做点笔记，记录一些注解的作用和基本原理

# spring jar
spring 框架有非常多 jar，看下框图

![spring 框架](./img/spring_框架图.png)

这里主要学习核心容器

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



