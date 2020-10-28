package com.example.demo;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

public class MySpringSecurityInitializer extends AbstractSecurityWebApplicationInitializer{
	public MySpringSecurityInitializer() {
		super(MySpringSecurityInitializer.class);
	}

	@Override
	 protected boolean enableHttpSessionEventPublisher() {
        return true;
    }
}
