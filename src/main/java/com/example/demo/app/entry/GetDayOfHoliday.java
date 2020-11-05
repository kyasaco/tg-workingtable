package com.example.demo.app.entry;

import java.time.LocalDate;

import ajd4jp.AJD;
import ajd4jp.AJDException;
import ajd4jp.Holiday;
import ajd4jp.Month;
import ajd4jp.Week;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class GetDayOfHoliday {

	public void getWeekDays(LocalDate today) throws AJDException {

	    // 2009/5 を設定。
	    Month  mon = new Month(today.getYear(), today.getMonthValue());

	    for(AJD day: mon.getDays()) {
	      int   d = day.getDay();
	      Week  w = day.getWeek();
	      Holiday h = Holiday.getHoliday(day);
	      String  note = "";
	      if (h != null ) {
	        note = h.getName(day);
	      }
	      String  line = String.format("%2d(%s): %s", d, w.getJpName(), note);
	      System.out.println(line);
	    }

	}

}
