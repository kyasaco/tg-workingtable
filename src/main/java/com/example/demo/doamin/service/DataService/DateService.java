package com.example.demo.doamin.service.DataService;

import java.sql.Date;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.app.entry.EntryForm;
import com.example.demo.doamin.model.DateEntity;
import com.example.demo.doamin.repository.DateRepository.DateRepository;

import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class DateService {
	private final DateRepository repository;

	/*idで一件取得*/
	public Optional<DateEntity> findOne(Integer id) {
		return repository.findById(id);
	}
	//useridとtodayに一致する一件の取得
	public List<DateEntity> findOneTandWID(Integer id,Date today) {
		return repository.findQueryTandWID(id, today);
	}
	/*指定した月の勤務表一覧表示*/
	public List<DateEntity> findQueryMonth(String Month,Integer id){
		return repository.findQueryByMonth(Month,id);
	}
	/*?????????????*/
	public Page<DateEntity> getAllDate(Pageable pageable){
		return repository.findAll(pageable);
	}
	/*?????????????*/
	public Page<DateEntity> findQueryMonthForPage(String Month,Integer id,Pageable pageable){
		if(Integer.valueOf(Month) < 10) {
			Month = "0" + Month;
		}
		return repository.findQueryByMonthForPage(Month, id, pageable);
	}
	/*指定した月の勤務表一覧表示*/
	public List<DateEntity> findQueryMonth2(String Month,Integer id){
		return repository.findQueryByMonth_2(Month,id);
	}
	//idで一件削除
	public void DeleteOne(Integer id) {
		repository.deleteById(id);
	}
	//today昇順で全件取得
	public List<DateEntity> findAllAsc()
	{
		return repository.findByOrderByTodayAsc();
	}
	//一件登録＆更新 ->日付の重複チェックと開始時間が終了時間より前ということのチェックを追加
	public int SaveFlush(EntryForm entryForm)
	{
		LocalTime Pasttime = LocalTime.parse(entryForm.getStartTime(), DateTimeFormatter.ISO_LOCAL_TIME);
		LocalTime Futuretime = LocalTime.parse(entryForm.getEndTime(), DateTimeFormatter.ISO_LOCAL_TIME);

		if(!findOneTandWID(Integer.valueOf(entryForm.getWorkersId()), Date.valueOf(entryForm.getToday())).isEmpty()) {
			return 2;
		}
		else if(Pasttime.isAfter(Futuretime)) {
			return 1;
		}
		DateEntity dateEntity = new DateEntity();
		dateEntity.setToday(entryForm.getDtoday());
		dateEntity.setStartTime(entryForm.getTimeClassAmTime());
		dateEntity.setEndTime(entryForm.getTimeClassPmTime());
		dateEntity.setWorkersId(entryForm.getWorkersId());

		repository.saveAndFlush(dateEntity);
		return 0;
	}

	//勤務表の検索処理：入力された値により検索処理を分岐
	public List<DateEntity> VfindUIDandTDY(String userid,String today){
		if(userid == "" && today == "") {
			return findAllAsc();
		}

		if(userid == "" && today != "") {
			Date dtoday = Date.valueOf(today);
			return findByToday(dtoday);
		}
		else if (userid != "" && today == "") {
			return findByWorkersId(userid);
		}
		else {
			return findOneTandWID(Integer.valueOf(userid), Date.valueOf(today));
		}
	}

	//todayで検索
	public List<DateEntity> findByToday(Date today){
		return repository.findByToday(today);
	}
	//workers_idで検索
	public List<DateEntity> findByWorkersId(String userid){
		return repository.findByWorkersIdOrderByToday(userid);
	}
 }
