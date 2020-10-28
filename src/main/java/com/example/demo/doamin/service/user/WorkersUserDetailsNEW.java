package com.example.demo.doamin.service.user;

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

public class WorkersUserDetailsNEW  extends org.springframework.security.core.userdetails.User{
	//認証に使うユーザー情報のフィールド
	private final User user;

	public WorkersUserDetailsNEW(User user) {
	super(user.getUserid(),user.getPassword(),AuthorityUtils.createAuthorityList("ROLE_"+user.getRolename().name()));
		this.user = user;
	}
	public User getUser() {
		return user;
	}




}
