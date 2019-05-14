package com.yifan.spring.profile.impl;

import com.yifan.spring.profile.api.IProfile;

/**
 * @author yifan
 * @since 2019/5/13 13:23
 */
public class ProfileBean2 implements IProfile {
    @Override
    public void sayHello(String name) {
        System.out.println("=============bean 2============");
        System.out.println("This is profile bean 2: hello " + name);
        System.out.println("=============bean 2============");
    }
}
