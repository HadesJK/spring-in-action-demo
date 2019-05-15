package com.yifan.spring.scope;

import com.yifan.spring.scope.api.Prototype;
import com.yifan.spring.scope.api.Singleton;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * @author yifan
 * @since 2019/5/15 20:12
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ScopeTest.class)
@ComponentScan("com.yifan.spring.scope")
public class ScopeTest {

    @Resource
    private Singleton singleton1;

    @Resource
    private Singleton singleton2;

    @Resource
    private Singleton singleton3;

    @Test
    public void setSingletonEqualsTest() {
        Assert.assertNotNull(singleton1);
        Assert.assertNotNull(singleton2);
        Assert.assertNotNull(singleton3);
        Assert.assertEquals(singleton1, singleton2);
        Assert.assertEquals(singleton2, singleton3);
    }

    @Resource
    private Prototype prototype1;

    @Resource
    private Prototype prototype2;

    @Resource
    private Prototype prototype3;

    @Test
    public void setPrototypeEqualsTest() {
        Assert.assertNotNull(prototype1);
        Assert.assertNotNull(prototype2);
        Assert.assertNotNull(prototype3);
        Assert.assertNotEquals(prototype1, prototype2);
        Assert.assertNotEquals(prototype2, prototype3);
    }
}
