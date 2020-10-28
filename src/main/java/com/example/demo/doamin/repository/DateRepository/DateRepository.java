package com.example.demo.doamin.repository.DateRepository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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
	List<DateEntity> findByOrderByWorkersIdAsc();
	List<DateEntity> findByWorkersIdOrderByToday(String workersId);
	List<DateEntity> findByToday(Date today);

	//page
	Page<DateEntity> findAll(Pageable pageable);
	@Query(value="SELECT * FROM Date7 WHERE workers_id=?1 and today=?2"
			,nativeQuery = true)
	List<DateEntity> findQueryTandWID(Integer workersid,Date today);

	/**
	 * @param id
	 */
	@Query(value="DELETE FROM Date7 WHERE today = ?1",nativeQuery = true)
	@Modifying
	void deleteBythisId(Date today);


	/**
	 * 月とユーザーIDが一致したデータリストを取得するクエリ
	 *
	 * @param Month 月
	 * @param id ユーザーID
	 * @return Monthとidが一致したデータリスト
	 */
	@Query(value="SELECT * FROM Date7 WHERE today LIKE ?1 + \'-\' + ?2 + \'-%\' AND workers_id = ?3 ORDER BY today ASC",nativeQuery=true)
	List<DateEntity> findQueryByMonth(String Year,String Month,Integer id);

	/*page用*/
	@Query(value="SELECT * FROM Date7 WHERE today LIKE ?1 + \'-\' + ?2 + \'-%\' AND workers_id = ?3",nativeQuery=true)
	Page<DateEntity> findQueryByMonthForPage(String Year,String Month,Integer id,Pageable pageable);

}
