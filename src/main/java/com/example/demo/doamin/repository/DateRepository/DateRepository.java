package com.example.demo.doamin.repository.DateRepository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.doamin.model.DateEntity;

/**
 * @author kasuda
 * @param aaaa
 *
 */
@Repository
public interface DateRepository extends JpaRepository<DateEntity,Integer>{
	List<DateEntity> findByOrderByTodayAsc();
	List<DateEntity> findByWorkersIdOrderByToday(String workersId);
	List<DateEntity> findByToday(Date today);

	//page
	Page<DateEntity> findAll(Pageable pageable);
	@Query(value="SELECT * FROM Date7 WHERE workers_id=?1 and today=?2"
			,nativeQuery = true)
	List<DateEntity> findQueryTandWID(Integer id,Date today);


	/**
	 * @param Month
	 * @param id
	 * @return 引数の月とユーザーIDが一致したデータリスト
	 */
	@Query(value="SELECT * FROM Date7 WHERE today LIKE \'2020-\' + ?1 + \'-%\' AND workers_id = ?2",nativeQuery=true)
	List<DateEntity> findQueryByMonth(String Month,Integer id);

	/*page用*/
	@Query(value="SELECT * FROM Date7 WHERE today LIKE \'2020-\' + ?1 + \'-%\' AND workers_id = ?2",nativeQuery=true)
	Page<DateEntity> findQueryByMonthForPage(String Month,Integer id,Pageable pageable);

	@Query(value="SELECT * FROM Date7 WHERE today LIKE ?1 AND workers_id = ?2",nativeQuery=true)
	List<DateEntity> findQueryByMonth_2(String Month,Integer id);
}
