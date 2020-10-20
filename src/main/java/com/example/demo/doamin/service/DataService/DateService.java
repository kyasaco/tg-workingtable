package com.example.demo.doamin.service.DataService;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

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

	//idで一件取得
	public Optional<DateEntity> findOne(Integer id) {
		return repository.findById(id);
	}

	public Optional<DateEntity> findOneTandWID(Integer id,Date today) {
		return repository.findQueryTandWID(id, today);
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
	//一件登録＆更新
	public void SaveFlush(EntryForm entryForm)
	{

		DateEntity dateEntity = new DateEntity();
		dateEntity.setToday(entryForm.getDtoday());
		dateEntity.setStartTime(entryForm.getTimeClassAmTime());
		dateEntity.setEndTime(entryForm.getTimeClassPmTime());
		dateEntity.setWorkersId(entryForm.getWorkersId());

		repository.saveAndFlush(dateEntity);
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
