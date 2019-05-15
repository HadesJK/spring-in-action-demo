package com.yifan.spring.conditional;

import com.yifan.spring.conditional.api.IConditional;
import com.yifan.spring.conditional.config.ConditionalConfig;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * @author yifan
 * @since 2019/5/14 21:02
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ConditionalConfig.class, Properties.class})
public class Conditional1Test {

    @Resource
    private IConditional conditional;

    @Test
    public void sayHelloTest() {
        Assert.assertNotNull(conditional);
        conditional.sayHello("yifan");
    }
}
