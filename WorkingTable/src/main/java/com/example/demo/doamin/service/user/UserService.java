package com.example.demo.doamin.service.user;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.app.login.InsertForm;
import com.example.demo.doamin.model.RoleName;
import com.example.demo.doamin.model.User;
import com.example.demo.doamin.repository.user.UserRepository;

import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class UserService {
	private final UserRepository repository;
	private final PasswordEncoder passwordEncoder;

	public void InsertUser(InsertForm insertForm)
	{
		User user= new User();
		user.setUserid(insertForm.getUserid());
		user.setPassword(passwordEncoder.encode(insertForm.getPassword()));
		user.setFirstname("悠馬");
		user.setLastname("柏田");
		user.setRolename(RoleName.ADMIN);
		repository.saveAndFlush(user);
	}

	public List<User> UserIdSet(String num)
	{
		return repository.findByUserid(num);
	}

	public List<User> findAllAsc(){
		return repository.findByOrderByUseridAsc();
	}
}
