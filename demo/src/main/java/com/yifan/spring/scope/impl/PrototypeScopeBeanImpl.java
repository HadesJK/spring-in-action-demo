package com.yifan.spring.scope.impl;

import com.yifan.spring.scope.api.Prototype;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author yifan
 * @since 2019/5/15 20:03
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PrototypeScopeBeanImpl implements Prototype {
}
