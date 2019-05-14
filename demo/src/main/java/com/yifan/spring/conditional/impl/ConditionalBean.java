package com.yifan.spring.conditional.impl;

import com.yifan.spring.conditional.api.IConditional;

/**
 * @author yifan
 * @since 2019/5/14 20:52
 */
public class ConditionalBean implements IConditional {
    @Override
    public void sayHello(String name) {
        System.out.println("This is conditional bean: hello " + name);
    }
}
