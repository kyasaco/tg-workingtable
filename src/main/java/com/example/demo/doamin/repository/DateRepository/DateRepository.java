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
	List<DateEntity> findQueryTandWID(String workersid,Date today);

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
	List<DateEntity> findQueryByMonth(String Year,String Month,String id);

	/*page用*/
	@Query(value="SELECT * FROM Date7 WHERE today LIKE ?1 + \'-\' + ?2 + \'-%\' AND workers_id = ?3",nativeQuery=true)
	Page<DateEntity> findQueryByMonthForPage(String Year,String Month,String id,Pageable pageable);

	/**
	 * 指定した年月の全ての始業時間と終業時間をリストで取得する
	 * @param Year 年
	 * @param Month 月
	 * @param id ユーザーID
	 * @return
	 */
	@Query(value="SELECT start_time,end_time FROM Date7 WHERE today LIKE ?1 + \'-\' + ?2 + \'-%\' AND workers_id = ?3 ",nativeQuery = true)
	List<Object[]> findQueryBySTET(String Year,String Month,String id);


	/**
	 * 従業員IDが存在する場合の日付検索
	 * @param VYMD 正規表現にて作成された日付フォーマットの文字列
	 * @param id
	 * @return
	 */
	@Query(value="SELECT * FROM Date7 WHERE today LIKE ?1 AND workers_id = ?2 ",nativeQuery = true)
	List<DateEntity> findQueryByYMDWithUID(String VYMD,String id);


	/**
	 * 従業員IDが存在しない場合の日付検索
	 * @param VYMD 正規表現にて作成された日付フォーマットの文字列
	 * @return
	 */
	@Query(value="SELECT * FROM Date7 WHERE today LIKE ?1 ",nativeQuery = true)
	List<DateEntity> findQueryByYMD(String VYMD);


}
