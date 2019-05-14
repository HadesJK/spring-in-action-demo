package com.yifan.spring.conditional;

import com.yifan.spring.conditional.api.IConditional;
import com.yifan.spring.conditional.config.ConditionalConfig;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author 金奇樑(hzjinqiliang)
 * @since 2019/5/14 21:02
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ConditionalConfig.class})
public class Conditional2Test {

    @Autowired(required = false)
    private IConditional conditional;

    @Test
    public void sayHelloTest() {
        Assert.assertNull(conditional);
    }
}
