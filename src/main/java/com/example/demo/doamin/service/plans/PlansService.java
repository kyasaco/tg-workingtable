package com.example.demo.doamin.service.plans;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.example.demo.app.Admin.PlansForm;
import com.example.demo.doamin.model.Plans;
import com.example.demo.doamin.repository.plans.PlansRepository;

import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class PlansService {
	private final PlansRepository repository;

	/**
	 * 予定表全件を取得する
	 *
	 * @return 予定表全件
	 */
	public List<Plans> getAllPlans(){
		return repository.findAll();
	}

	/**
	 * 予定表一件を取得する
	 *
	 * @param id 予定表id
	 * @return 予定表一件/null
	 */
	public Optional<Plans> getOnePlan(String today){
		return repository.getOneToday(Date.valueOf(today));
	}

	public void deleteOnePlanByToday(String today) {
		repository.deleteBythisToday(Date.valueOf(today));
	}


	public int SaveAndFlushPlans(PlansForm plansform) {
		String ontoday =  plansform.getToday();
		int result = 0;
		if(!getOnePlan(ontoday).isEmpty()) {
			deleteOnePlanByToday(ontoday);
			result = 2;
		}
		Plans plans = new Plans();
		plans.setTitle(plansform.getTitle());
		plans.setToday(Date.valueOf(plansform.getToday()));
		plans.setPlans(plansform.getPlans());
		plans.setHoliday(Boolean.valueOf(plansform.getHoliday()));

		repository.saveAndFlush(plans);

		return result;
	}
}
