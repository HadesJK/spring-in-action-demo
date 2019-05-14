package com.yifan.spring.conditional.config;

import com.yifan.spring.conditional.impl.ConditionalBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @author yifan
 * @since 2019/5/13 13:25
 */
@Configuration("com.yifan.spring.conditional")
public class ConditionalConfig implements Condition {

    @Bean
    @Conditional(ConditionalConfig.class)
    public ConditionalBean conditionalBean() {
        return new ConditionalBean();
    }

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        Environment env = context.getEnvironment();
        String value = env.getProperty("conditional");
        return "true".equals(value);
    }
}
