package com.example.demo;

import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import com.example.demo.doamin.service.user.WorkersUserDetailsService;

import lombok.AllArgsConstructor;

@EnableWebSecurity
@AllArgsConstructor
public class WebSecurityConfig  extends WebSecurityConfigurerAdapter{

	//ユーザー検索処理を書いたUserDetailsServiceインターフェースを実装したクラスを宣言
	private final WorkersUserDetailsService userDetailsService;

//	@Bean
//	public SessionRegistry sessionRegistry() {
//	    SessionRegistry sessionRegistry = new SessionRegistryImpl();
//	    return sessionRegistry;
//	}


	/*パスワードエンコード用*/
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	/*以下でSpring Securityにおけるフィルタ的なことを設定できる
	 * configure(WebSecurity web) throws Exception{
	 * 	/resources以下のSpringSecurity要求を無視
	 * 	ignoring().antMatchers("/resources/**");
	 * }
	 * */
	@Override
	/*HttpSecurityクラス：特定のhttpリクエストに対してWebベースのセキュリティを構成できる*/
	protected void configure(HttpSecurity http) throws Exception{
		//authorizeRequests:何のリクエストを許可するか
		http.authorizeRequests()
				.mvcMatchers("/js/**","/css/**","/insertForm","/insert","/ErrorPage").permitAll()
				.mvcMatchers("/Admin/**").access("hasRole('ADMIN') and isFullyAuthenticated()")
				.anyRequest().authenticated()
				.and()
			.formLogin()
				.loginPage("/loginForm")
				.loginProcessingUrl("/login")
				.usernameParameter("username")
				.passwordParameter("password")
				.defaultSuccessUrl("/",true)
				.failureUrl("/loginForm?error=true").permitAll()
				.and()
			.logout()
				.deleteCookies("JSESSIONID")
				.and()
			.rememberMe()
				.key("REMEMBER")
				.rememberMeCookieName("remembered_working_user")
				.tokenValiditySeconds(604800)
				.and()
			/*Spring SecurityではセッションIDの再発行、CSRF対策はデフォルト*/
			.sessionManagement()
				.maximumSessions(1)
				.expiredUrl("/loginForm?error=true")
//				.sessionRegistry(sessionRegistry())
				.and()
			.invalidSessionUrl("/ErrorPage");

	}


	/*
	 * configure(AuthenticationManagerBuilder auth)メソッドをオーバーライドして使用することで
	 * メモリ内認証、LDAP認証、JDBCベースの認証、UserDetailsServiceの追加、AuthenticationProviderの追加を簡単に構築できる by wiki
	 * */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception{
		//認証するユーザーの設定。passwordEncoder(new PasseordEncoder)で入力値を設定したハッシュ化方法で暗号化しそれで認証する
		//userdetailsService(UserDetailsSeviceを実装したクラス)でその内容に基づいて認証を追加する
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}
}
