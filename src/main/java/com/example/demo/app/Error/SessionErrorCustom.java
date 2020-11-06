package com.example.demo.app.Error;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.stereotype.Component;


public class SessionErrorCustom extends LoginUrlAuthenticationEntryPoint{

	public SessionErrorCustom(String loginFormUrl) {
		super(loginFormUrl);
		// TODO 自動生成されたコンストラクター・スタブ
	}
	/**
	 *セッションが無効状態のときデフォルトのloginURLにtimeoutパラメータをつける
	 */
	@Override
    protected String buildRedirectUrlToLoginPage(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) {

        String redirectUrl = super.buildRedirectUrlToLoginPage(request, response, authException);
        if (isRequestedSessionInvalid(request)) {
            redirectUrl += redirectUrl.contains("?") ? "&" : "?";
            redirectUrl += "timeout";
        }
        return redirectUrl;
    }

    private boolean isRequestedSessionInvalid(HttpServletRequest request) {
        return request.getRequestedSessionId() != null && !request.isRequestedSessionIdValid();
    }
}
