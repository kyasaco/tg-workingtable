package com.example.demo.doamin.service.user;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.app.Admin.UserConfForm;
import com.example.demo.app.login.InsertForm;
import com.example.demo.doamin.model.RoleName;
import com.example.demo.doamin.model.User;
import com.example.demo.doamin.model.UserEcpertPass;
import com.example.demo.doamin.repository.user.UserRepository;

import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class UserService {
	private final UserRepository repository;
	private final PasswordEncoder passwordEncoder;

	/**
	 * 現在の従業員IDを重複なしで取得
	 * @return
	 */
	public List<String> getNowUserIds(){
		return repository.findByUserIdDistinct();
	}

	/**
	 * @param ucf 従業員ID、苗字、名前、ロールを持ったクラス
	 * @param dcheck 削除する従業員IDが入ったリスト
	 * @return
	 */
	public int updateUsers(UserConfForm ucf,List<String> dcheck) {
		List<User> expass = repository.findByOrderByUseridAsc();
		if(dcheck != null) {
			repository.deleteCheck(dcheck);
			repository.saveAll(expass);
		}
		List<String> cnt = new ArrayList<String>();
		List<User> before_list = new ArrayList<User>();
		for(int i = 0; i<(expass.size());i++) {
			User uep = new User();
			if(!ucf.getUserid().get(i).equals(expass.get(i).getUserid())) {
				cnt.add(expass.get(i).getUserid());
			}
			uep.setUserid(ucf.getUserid().get(i));
			uep.setFirstname(ucf.getFirstname().get(i));
			uep.setPassword(expass.get(i).getPassword());
			uep.setLastname(ucf.getLastname().get(i));
			uep.setRolename(RoleName.valueOf(ucf.getRolename().get(i)));
			before_list.add(uep);
		}
		List<User> after_list = subtract(before_list, expass);
		if(after_list != null) {
				if(cnt != null) {
					repository.deleteCheck(cnt);
				}
			repository.saveAll(after_list);
		}
		return 0;
	}


	public void InsertUser(InsertForm insertForm)
	{
		User user= new User();
		user.setUserid(insertForm.getUserid());
		user.setPassword(passwordEncoder.encode(insertForm.getPassword()));
		user.setFirstname(insertForm.getFirstname());
		user.setLastname(insertForm.getLastname());
		user.setRolename(insertForm.getRolename());
		repository.saveAndFlush(user);
	}

	public List<User> UserIdSet(String num)
	{
		return repository.findByUserid(num);
	}

	public List<User> findAllAsc(){
		return repository.findByOrderByUseridAsc();
	}

	public List<RoleName> findDistinByRolenames(){
		return repository.findByRolenamesDistinct();
	}


	/**
	 * list2に含まれる要素をlis1から削除する
	 * @param list1
	 * @param list2
	 * @return
	 */
	public static List<User> subtract(List<User> list1, List<User> list2) {
	    final HashSet<User> list2Set = new HashSet<>(list2);
	    final List<User> resultList = list1.stream()
	            .filter(p -> {
	                return (! list2Set.contains(p));
	            })
	            .collect(Collectors.toList());
	    return resultList;
	}
}
