package com.example.demo.doamin;

import java.io.IOException;

import javax.servlet.ServletException;

import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

public class MySessionInformation implements SessionInformationExpiredStrategy {

	@Override
	public void onExpiredSessionDetected(SessionInformationExpiredEvent event) throws IOException, ServletException {
		// TODO 自動生成されたメソッド・スタブ
		DefaultRedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
		redirectStrategy.sendRedirect(event.getRequest(), event.getResponse(), "/login/loginForm");
	}

}
