package com.yifan.spring.scope.impl;

import com.yifan.spring.scope.api.Singleton;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author yifan
 * @since 2019/5/15 20:10
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class SingletonScopeBeanImpl implements Singleton {
}
