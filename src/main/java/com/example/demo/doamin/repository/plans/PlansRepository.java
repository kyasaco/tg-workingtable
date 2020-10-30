package com.example.demo.doamin.repository.plans;

import java.sql.Date;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.doamin.model.Plans;

@Repository
public interface PlansRepository extends JpaRepository<Plans, Integer> {

	@Query(value="DELETE FROM Plans WHERE today = ?1",nativeQuery = true)
	@Modifying
	void deleteBythisToday(Date today);

	@Query(value="SELECT *  FROM Plans WHERE today = ?1",nativeQuery = true)
	Optional<Plans> getOneToday(Date today);
}
