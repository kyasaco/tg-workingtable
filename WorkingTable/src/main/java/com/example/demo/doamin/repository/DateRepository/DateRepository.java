package com.example.demo.doamin.repository.DateRepository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.doamin.model.DateEntity;

@Repository
public interface DateRepository extends JpaRepository<DateEntity,Integer>{
	List<DateEntity> findByOrderByTodayAsc();
	List<DateEntity> findByWorkersId(String workersId);
	List<DateEntity> findByToday(Date today);
}
