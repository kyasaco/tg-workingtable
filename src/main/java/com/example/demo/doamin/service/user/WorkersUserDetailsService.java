package com.example.demo.doamin.service.user;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.doamin.model.User;
import com.example.demo.doamin.repository.user.UserRepository;

import lombok.AllArgsConstructor;


@Service
@AllArgsConstructor
public class WorkersUserDetailsService implements UserDetailsService {

	private final UserRepository userrepository;

	/*
	 * UserDetailsServiceインターフェースのメソッド
	 * loadUserByUsernaeme(string)をオーバーライドしてユーザーの検索処理を実装する
	 * ex)このユーザーはダメとかパスワードの設定権限の設定etc...
	 * */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO 自動生成されたメソッド・スタブ
		Optional<User> user = userrepository.findById(username);
		if(user.isEmpty())
		{
			throw new UsernameNotFoundException(username+"is not found.");
		}
		//Optional<T>型はnullかもしれないデータをいれられる .get()でvalueを取得
		return new WorkersUserDetails(user.get());
	}

}
