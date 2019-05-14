package com.yifan.spring.profile.impl;

import com.yifan.spring.profile.api.IProfile;

/**
 * @author yifan
 * @since 2019/5/13 13:21
 */
public class ProfileBean1 implements IProfile {
    @Override
    public void sayHello(String name) {
        System.out.println("=============bean 1============");
        System.out.println("This is profile bean 1: hello " + name);
        System.out.println("=============bean 1============");
    }
}
