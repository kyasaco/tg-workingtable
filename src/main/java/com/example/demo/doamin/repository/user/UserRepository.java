package com.example.demo.doamin.repository.user;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

import com.example.demo.doamin.model.DateEntity;
import com.example.demo.doamin.model.RoleName;
import com.example.demo.doamin.model.User;
import com.example.demo.doamin.model.UserEcpertPass;

@Repository
public interface UserRepository extends JpaRepository<User, String>{
	List<User> findByUserid(String num);
	List<User> findByOrderByUseridAsc();

	@Query(value="UPDATE usrs3"
			+ "SET userid = ?1,firstname =?2,lastname = ?3,rolename = ?4"
			+ "WHERE userid =  ?5",nativeQuery = true)
	@Modifying
	void updateAllexpass(String userid,String firstname,
			String lastname,String rolename,String ex_userid);

	@Query(value="SELECT userid,firstname,lastname ,rolename FROM usrs3",nativeQuery = true)
	List<Object[]> findAllexpass();

	@Modifying
	@Query(value="DELETE FROM usrs3 WHERE userid IN(:values) ",nativeQuery = true)
	void deleteCheck(@Param("values")List<String> dcheck);


	@Query(value="SELECT DISTINCT rolename FROM usrs3",nativeQuery = true)
	List<RoleName> findByRolenamesDistinct();
}
