package com.yifan.spring.profile;

import com.yifan.spring.profile.api.IProfile;
import com.yifan.spring.profile.config.ProfileConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * @author yifan
 * @since 2019/5/13 13:24
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles({"profile2", "profile1"})
@ContextConfiguration(classes = ProfileConfig.class)
public class AppProfile4Test {

    @Resource
    private IProfile profile;

    @Test
    public void sayHello4Test() {
        profile.sayHello("yifan");
    }
}
