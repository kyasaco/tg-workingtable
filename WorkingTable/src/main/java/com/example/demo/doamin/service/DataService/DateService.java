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

	public Optional<DateEntity> findOne(Integer id) {
		return repository.findById(id);
	}
	public void DeleteOne(Integer id) {
		repository.deleteById(id);
	}
	public List<DateEntity> findAllAsc()
	{
		return repository.findByOrderByTodayAsc();
	}

	public void SaveFlush(EntryForm entryForm)
	{

		DateEntity dateEntity = new DateEntity();
		dateEntity.setToday(entryForm.getDtoday());
		dateEntity.setStartTime(entryForm.getTimeClassAmTime());
		dateEntity.setEndTime(entryForm.getTimeClassPmTime());
		dateEntity.setWorkersId(entryForm.getWorkersId());

		repository.saveAndFlush(dateEntity);
	}

	public List<DateEntity> findByToday(Date today){
		return repository.findByToday(today);
	}

	public List<DateEntity> findByWorkersId(String userid){
		return repository.findByWorkersId(userid);
	}
 }
