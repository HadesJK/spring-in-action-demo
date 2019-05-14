package com.yifan.spring.profile;

import javax.annotation.Resource;

import com.yifan.spring.profile.api.IProfile;
import com.yifan.spring.profile.config.ProfileConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author yifan
 * @since 2019/5/13 13:24
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles({"profile2", "profile3"})
@ContextConfiguration(classes = ProfileConfig.class)
public class AppProfile3Test {

    @Resource
    private IProfile profile;

    @Test
    public void sayHello3Test() {
        profile.sayHello("yifan");
    }
}
