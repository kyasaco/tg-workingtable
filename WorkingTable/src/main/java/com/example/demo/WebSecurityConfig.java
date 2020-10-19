package com.example.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import com.example.demo.doamin.MySessionInformation;
import com.example.demo.doamin.service.user.WorkersUserDetailsService;
import lombok.AllArgsConstructor;

@EnableWebSecurity
@AllArgsConstructor
public class WebSecurityConfig  extends WebSecurityConfigurerAdapter{
	
	//ユーザー検索処理を書いたUserDetailsServiceインターフェースを実装したクラスを宣言
	private final WorkersUserDetailsService userDetailsService;
	
	//HttpSession生成・破棄イベントのハンドラ(処理要求が発生したときに起動されるプログラムのこと)
	@Bean
	public HttpSessionEventPublisher  httpSessionEventPublisher() {
		return new HttpSessionEventPublisher();
	}
	
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
	
	/*HttpSecurityクラス：特定のhttpリクエストに対してWebベースのセキュリティを構成できる*/
	protected void configure(HttpSecurity http) throws Exception{
		//authorizeRequests:何のリクエストを許可するか
		http.authorizeRequests()
			/*
			antMatcher[s]:指定したantパターンと一致する場合にのみ呼び出されるようにする
			mvcMatacher[s]:指定したSpringMVCパターンと一致する場合にのみ呼び出されるようにする
			permitAll()を付けるとすべてのユーザーがアクセス可能になる
			hasRole("ロール名")を付けると設定されたロールのみアクセス可能
			*/
			.antMatchers("/js/**","/css/**","/insertForm","/insert").permitAll()
			//	anyRequest().authenticated()：全てのURLリクエストは認証されているユーザしかアクセスできない
			.anyRequest().authenticated()
			.and()
			.formLogin()
			//loginPage(URL)：ログイン画面のURL
			.loginPage("/loginForm")
			//loginProcessingUrl(URL)：ログインを処理するURL
			.loginProcessingUrl("/login")
			/*
			 * usernameParameter/passwordParameter：ログイン画面のhtmlのinputのname属性を見に行っている
			 */
			.usernameParameter("username")
			.passwordParameter("password")
			//defaultSuccessUrl("URL",T or F)：第一引数＝ログインに成功したときのURL
			//第二引数＝true：ログイン画面後必ずtopへ飛ばされる
			//	false：(認証されてなくて一度ログイン画面に飛ばされていも)ログインしたら指定したURLに飛んでくれる
			.defaultSuccessUrl("/",true)
			//failureUrl(URL)：ログインに失敗したときのURL。?errorとつけるとThymeleafでの処理が楽
			.failureUrl("/loginForm?error=true").permitAll()
			.and()
			.sessionManagement().maximumSessions(1).expiredUrl("/loginForm").expiredSessionStrategy(new MySessionInformation());
		

				
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
