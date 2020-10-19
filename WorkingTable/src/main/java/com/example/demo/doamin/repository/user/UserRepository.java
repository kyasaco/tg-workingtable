package com.example.demo.doamin.repository.user;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.doamin.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, String>{
	List<User> findByUserid(String num);
	List<User> findByOrderByUseridAsc();
}
