package com.example.demo.doamin.service.DataService;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.app.entry.EntryForm;
import com.example.demo.doamin.model.DateEntity;
import com.example.demo.doamin.model.OutDate;
import com.example.demo.doamin.repository.DateRepository.DateRepository;
import com.example.demo.doamin.service.plans.PlansService;

import ajd4jp.AJD;
import ajd4jp.AJDException;
import ajd4jp.Holiday;
import ajd4jp.Month;
import ajd4jp.Week;
import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class DateService {
	private final DateRepository repository;
	private final PlansService plansservice;

	/**
	 * useridと一致する一件の取得
	 *
	 * @param id
	 * @return useridと一致する一件のDateEntity
	 */
	public Optional<DateEntity> findOne(Integer id) {
		return repository.findById(id);
	}

	/**
	 * useridとtodayに一致する一件の取得
	 *
	 * @param id
	 * @param today
	 * @return
	 */
	public List<DateEntity> findOneTandWID(String workersid,Date today) {

		return repository.findQueryTandWID(workersid, today);
	}

	/**
	 * @param Month
	 * @param id
	 * @return 選択した月と認証しているユーザーIDでデータを取得する
	 */
	public List<DateEntity> findQueryMonth(LocalDate today,String id){
		String month = String.format("%02d", today.getMonthValue());
		String year = String.valueOf(today.getYear());
		return repository.findQueryByMonth(year,month,id);
	}

	/**
	 * @param pageable
	 * @return ページング用。全勤務表データを取得
	 */
	public Page<DateEntity> getAllDate(Pageable pageable){
		return repository.findAll(pageable);
	}

	/**
	 * @param Month
	 * @param id
	 * @param pageable
	 * @return ページング用。選択した月と認証しているユーザーIDでデータを取得する
	 */
	public Page<DateEntity> findQueryMonthForPage(
			LocalDate today,
			String bid,
			Pageable pageable){
		String month = String.format("%02d", today.getMonthValue());
		String year = String.valueOf(today.getYear());
		return repository.findQueryByMonthForPage(year,month, bid, pageable);
	}
	//idで一件削除
	/**
	 * @param id
	 */
	public void DeleteOne(Integer id) {
		repository.deleteById(id);
	}
	//today昇順で全件取得
	/**
	 * @return
	 */
	public List<DateEntity> findAllAsc()
	{
		return repository.findByOrderByWorkersIdAsc();
	}
	//
	/**
	 * 一件登録＆更新 ->日付の重複チェックと開始時間が終了時間より前ということのチェックを追加
	 *
	 * @param entryForm フォームの入力値
	 * @return 実行結果(0=正常終了、1=時間エラー、2=上書き保存)
	 * @throws AJDException
	 */
	public int SaveFlush(EntryForm entryForm) throws AJDException
	{
		int result = 0;
		LocalTime Pasttime = LocalTime.parse(entryForm.getStartTime(), DateTimeFormatter.ISO_LOCAL_TIME);
		LocalTime Futuretime = LocalTime.parse(entryForm.getEndTime(), DateTimeFormatter.ISO_LOCAL_TIME);
		Date today = Date.valueOf(entryForm.getToday());
		LocalDate LDtoday = today.toLocalDate();
		List<Object[]> holidays = plansservice.checkPlansHolidays(LDtoday);

		//始業より終業が前か同じ時刻ならエラー１
		//休日ならエラー３
		//すでにあるなら上書き実行
		if(Pasttime.isAfter(Futuretime) || Pasttime == Futuretime) {
			return 1;
		}else if(LDtoday.getDayOfWeek().getValue() == 6 || LDtoday.getDayOfWeek().getValue() == 7 || checkHolidaysAndPholidays(holidays, LDtoday)) {
			return 3;

		}else if(!findOneTandWID(entryForm.getWorkersId(), today).isEmpty()) {
			result =2;
			repository.deleteBythisId(today);

		}
		DateEntity dateEntity = new DateEntity();
		dateEntity.setToday(entryForm.getDtoday());
		dateEntity.setStartTime(entryForm.getTimeClassAmTime());
		dateEntity.setEndTime(entryForm.getTimeClassPmTime());
		dateEntity.setWorkersId(entryForm.getWorkersId());

		repository.saveAndFlush(dateEntity);
		return result;
	}

	/**
	 * csv用
	 * @param today
	 * @param id
	 * @return
	 */
	public List<OutDate> DateEntityToOutDate(LocalDate today,String bid){
		final String[] week_name = {"日", "月", "火", "水", "木", "金", "土"};
		String year = String.valueOf(today.getYear());
		String month = String.format("%02d", today.getMonthValue());

		Calendar clCalendar = Calendar.getInstance();
		List<OutDate> out_datecsv = new ArrayList<OutDate>();
		List<DateEntity> wk = repository.findQueryByMonth(year,month,bid);
		for(DateEntity s : wk) {
			OutDate hako = new OutDate();
			clCalendar.setTime(s.getToday());

			hako.setToday(clCalendar.get(Calendar.DATE));
			hako.setDayofweek(week_name[clCalendar.get(Calendar.DAY_OF_WEEK)-1]);
			hako.setStartTime(String.valueOf(s.getStartTime().toLocalTime().getMinute()));
			hako.setStarthour(String.valueOf(s.getStartTime().toLocalTime().getHour()));
			hako.setEndhour(String.valueOf(s.getEndTime().toLocalTime().getHour()));
			hako.setEndTime(String.valueOf(s.getEndTime().toLocalTime().getMinute()));
			out_datecsv.add(hako);
		}

		return out_datecsv;
	}

	//勤務表の検索処理：入力された値により検索処理を分岐
	public List<DateEntity> VfindUIDandTDY(String userid,String today){
		if((userid == "" && today == "")||(userid == null && today ==null)) {
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
			return findOneTandWID(userid, Date.valueOf(today));
		}
	}

	/**
	 *
	 *
	 * @param today
	 * @param id
	 * @return
	 */
	public double getSTETMinus(LocalDate today,String id) {
		String month = String.format("%02d", today.getMonthValue());
		List<Object[]> stet_list = repository.findQueryBySTET(String.valueOf(today.getYear()),
				month,
				id);
		double  sum_time = 0.0;
		for(Object[] stet : stet_list) {
			LocalTime st = ((Time) stet[0]).toLocalTime();
			LocalTime et = ((Time) stet[1]).toLocalTime();
			sum_time += (double)(et.getHour() - st.getHour());
			int stMin = st.getMinute();
			int etMin = et.getMinute();
			if(etMin+ stMin == 60) {
				sum_time += 1.0;
			}else if(etMin != stMin) {
				sum_time += 0.5;
			}

		}
		return sum_time - ((double)stet_list.size());
	}
	//todayで検索
	public List<DateEntity> findByToday(Date today){
		return repository.findByToday(today);
	}
	//workers_idで検索
	public List<DateEntity> findByWorkersId(String userid){
		return repository.findByWorkersIdOrderByToday(userid);
	}

	/**
	 * @param today
	 * @return
	 * @throws AJDException
	 */
	public int getWeekDays(LocalDate today) throws AJDException {

		int weekday_sum = 0;
	    Month  mon = new Month(today.getYear(), today.getMonthValue());
	    List<Object[]> holidays = plansservice.checkPlansHolidays(today);

	    for(AJD day: mon.getDays()) {
	      int   d = day.getDay();
	      Week  w = day.getWeek();
	      Holiday h = Holiday.getHoliday(day);
	      String YMD = String.format("%d-%02d-%02d", mon.getYear(), mon.getMonth(),day.getDay());
	      if (h == null && !w.getShortName().equals("Sun") && !w.getShortName().equals("Sat") ) {
	    	  if(!checkHolidays(holidays, YMD)) {
	    		  weekday_sum++;
	    	  }
	      }
	    }
	    return weekday_sum*8;
	}

	/**
	 *予定表内の休日と日付を比較して予定表内にあるか、国民の祝日ならばtrue、それ以外はfalseを返す
	 * @param holidays
	 * @param YMD
	 * @return
	 * @throws AJDException
	 */
	public boolean checkHolidaysAndPholidays(List<Object[]> holidays , LocalDate today) throws AJDException  {
		Month  mon = new Month(today.getYear(), today.getMonthValue());
		AJD day = mon.getDays()[today.getDayOfMonth()-1];
	    String YMD = String.format("%d-%02d-%02d", mon.getYear(), mon.getMonth(),day.getDay());
	    Holiday h = Holiday.getHoliday(day);
	      for(Object[] obj : holidays) {
	    	  if( ((Date)obj[0]).toString().equals(YMD) && (Boolean)obj[1] == true || h != null) {
	    		  	return true;
	    	  }
	      }
	      return false;
	}

	public boolean checkHolidays(List<Object[]> holidays , String YMD) {
	      for(Object[] obj : holidays) {
	    	  if( ((Date)obj[0]).toString().equals(YMD) && (Boolean)obj[1] == true ) {
	    		  	return true;
	    	  }
	      }
	      return false;
	}
 }
