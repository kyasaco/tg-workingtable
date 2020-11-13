package com.example.demo.doamin.service.user.authentication;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.demo.doamin.model.User;


/*
 * UserDetailsを実装したクラス：コアユーザー(サービスを利用するユーザーの中でも特にメイン(固定)で利用するユーザー)情報を設定
 * 各メソッドの詳細は非nullでなくてはいけない
 * メソッドの名前は固定
 * ここではセキュリティに関連する情報のみ宣言する(電話番号、住所などは書かない)
 * */

public class WorkersUserDetails  implements UserDetails{
	//認証に使うユーザー情報のフィールド
	private final User user;

	public WorkersUserDetails(User user)
	{
		this.user = user;
	}
	public User getUser() {
		return user;
	}

	/*
	 *Collection<? extends GrantedAuthority>：ユーザーに付与された権限を返す
	 *GrantedAuthorityインターフェースはAuthentication(認証)オブジェクトに付与された権限を表す
	 * */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// RoleName型のenumをGrantedAuthorityに変換。ROLE_みたいなのが必要
		//AuthorityUtilsクラスはコレクションなどを操作するためのメソッド
		//createAuthorityList(authorities(権限))でGrantedAuthorityオブジェクトに変換
		return AuthorityUtils.createAuthorityList("ROLE_"+this.user.getRolename().name());
	}

	//ユーザー認証に使用されるパスワードを返す
	@Override
	public String getPassword() {
		return this.user.getPassword();
	}

	//ユーザー認証に使用されるユーザー名を返す
	@Override
	public String getUsername() {
		return this.user.getUserid();
	}

	@Override
	public boolean isAccountNonExpired() {
		// アカウントの期限切れdefoult:false;
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// アカウントロックdefoult:false;
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// パスワードの有効時間切れdefoult:false;
		return true;
	}

	@Override
	public boolean isEnabled() {
		// アカウントの無効化defoult:false;
		return true;
	}

	public boolean equals(Object rhs) {
		if (rhs instanceof WorkersUserDetails) {
			return this.user.getUserid().equals(((WorkersUserDetails) rhs).user.getUserid());
		}
		return false;
	}

	/**
	 * Returns the hashcode of the {@code username}.
	 */
	public int hashCode() {
		return this.user.getUserid().hashCode();
	}


}
