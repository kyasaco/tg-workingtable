package com.example.demo.doamin.repository.plans;

import java.sql.Date;
import java.util.List;
import java.util.Map;
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

	@Query(value="DELETE FROM Plans WHERE today LIKE ?1",nativeQuery = true)
	@Modifying
	void deleteBythisVYMD(String YMD);

	@Query(value="SELECT *  FROM Plans WHERE today = ?1",nativeQuery = true)
	Optional<Plans> getOneToday(Date today);

	@Query(value="SELECT * FROM Plans WHERE today  > ?1 ORDER BY today ASC",nativeQuery = true)
	List<Plans> getByTosayGreaterAsc(Date today);

	@Query(value="SELECT today,holiday FROM Plans WHERE today LIKE ?1 + \'-\' + ?2 + \'-%\'",nativeQuery = true)
	List<Object[]> getHolidayOneMonth(String Year,String Month);



}
