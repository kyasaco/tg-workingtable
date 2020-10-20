package com.example.demo.doamin.repository.DateRepository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.doamin.model.DateEntity;

@Repository
public interface DateRepository extends JpaRepository<DateEntity,Integer>{
	List<DateEntity> findByOrderByTodayAsc();
	List<DateEntity> findByWorkersIdOrderByToday(String workersId);
	List<DateEntity> findByToday(Date today);

	@Query(value="SELECT * FROM Date7 WHERE workers_id=?1 and today=?2"
			,nativeQuery = true)
	Optional<DateEntity> findQueryTandWID(Integer id,Date today);
}
