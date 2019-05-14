package com.yifan.spring.profile.config;

import com.yifan.spring.profile.api.IProfile;
import com.yifan.spring.profile.impl.ProfileBean1;
import com.yifan.spring.profile.impl.ProfileBean2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @author yifan
 * @since 2019/5/13 13:25
 */
@Configuration
public class ProfileConfig {

    @Bean("profile")
    @Profile("profile1")
    public IProfile profile1() {
        return new ProfileBean1();
    }

    @Bean("profile")
    @Profile({"profile2", "profile3", "profile4"})
    public IProfile profile2() {
        return new ProfileBean2();
    }
}
