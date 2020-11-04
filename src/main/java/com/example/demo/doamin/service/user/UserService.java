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

	public int updateUsers(UserConfForm ucf,List<String> dcheck) {
		if(dcheck!=null) {
			repository.deleteCheck(dcheck);
		}
		List<User> expass = repository.findAllexpass();
		List<UserEcpertPass> before_list = new ArrayList<UserEcpertPass>();
		for(int i = 0; i<(ucf.getUserid().size());i++) {
			UserEcpertPass uep = new UserEcpertPass();
			uep.setUserid(ucf.getUserid().get(i));
			uep.setFirstname(ucf.getFirstname().get(i));
			uep.setLastname(ucf.getLastname().get(i));
			uep.setRolename(ucf.getRolename().get(i));
			before_list.add(uep);
		}
//		List<UserEcpertPass> after_list = subtract(before_list, expass);
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
	public static List<UserEcpertPass> subtract(List<UserEcpertPass> list1, List<UserEcpertPass> list2) {
	    final HashSet<UserEcpertPass> list2Set = new HashSet<>(list2);
	    final List<UserEcpertPass> resultList = list1.stream()
	            .filter(p -> {
	                return (! list2Set.contains(p));
	            })
	            .collect(Collectors.toList());
	    return resultList;
	}
}
