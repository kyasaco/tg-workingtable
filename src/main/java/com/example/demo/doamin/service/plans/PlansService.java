package com.example.demo.doamin.service.plans;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.example.demo.app.Admin.PlansDeleteForm;
import com.example.demo.app.Admin.PlansForm;
import com.example.demo.doamin.model.Plans;
import com.example.demo.doamin.repository.plans.PlansRepository;
import com.example.demo.errors.DateValidation;

import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class PlansService {
	private final PlansRepository repository;


	public int deletePlan(LocalDate today) {
		Date Dtoday = Date.valueOf(today);
		if(repository.getOneToday(Dtoday).isPresent()) {
			repository.deleteBythisToday(Dtoday);
			return 0;
		}
		return 999;
	}

	public void deletePlansYMD(PlansDeleteForm pdf) {
		String today = String.format("%04d-%02d-%02d",pdf.getYear(),pdf.getMonth(),pdf.getDay());
		String re_today = today.replaceAll("[a-zA-Z]+", "%");
		repository.deleteBythisVYMD(re_today);
	}

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
		Optional<Plans> plan =Optional.ofNullable(new Plans());
		if(repository.getOneToday(Date.valueOf(today)).isEmpty()) {
			plan.get().setInitData();
		}
		else {
			plan=repository.getOneToday(Date.valueOf(today));
		}

		return plan;
	}

	/**
	 * 今日を基準に今日の予定があれば「今日の予定＋その次の予定のリスト」を取得
	 * 今日の予定がなければ「予定なしデータを入れた予定＋その次の予定リスト」を取得
	 *
	 * @param today
	 * @return
	 */
	public List<Plans> getOneSomeDayPlan(String today){
		Date onToday = Date.valueOf(today);
		Plans plan =new Plans();
		plan.setInitData();
		List<Plans> planslist = new ArrayList<Plans>();
		if(repository.getOneToday(onToday).isEmpty()) {
			//今日ないなら今日以上の昇順データから一件
			planslist.add(plan);
		}
		else {
			//今日あるならいれる
			planslist.add(repository.getOneToday(onToday).get());
		}

		if(repository.getByTosayGreaterAsc(onToday).isEmpty()) {
			planslist.add(plan);
		}else {
			planslist.add( repository.getByTosayGreaterAsc(onToday).get(0));
		}
		return planslist;
	}
//	public List<Plans> getOnePlanVali(String today){
//		 if(repository.getOneToday(Date.valueOf(today)).isEmpty()){
//			 Plans plan =
//		 };
//	}

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

	public List<Object[]> checkPlansHolidays(LocalDate today) {
		String Month = String.valueOf(today.getMonthValue());
		String Year = String.valueOf(today.getYear());
		if(today.getMonthValue() < 10) {
			Month = "0" + Month;
		}
		List<Object[]> a = repository.getHolidayOneMonth(Year, Month);
		return a;

	}
}
