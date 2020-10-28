package com.example.demo.app.entry;

import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EntryForm {
	@NotNull(message = "NULLは不可")
	private String workersId;
	@NotBlank(message = "空白は不可")
	private String startTime ;
	@NotEmpty(message = "空白は不可")
	private  String endTime ;
	@NotNull(message = "NULLは不可")
	private String today;

	//String=>LocalTime=>Time
	public Time getTimeClassAmTime()
	{
		LocalTime time = LocalTime.parse(this.startTime, DateTimeFormatter.ofPattern("HH:mm"));
		Time sqlTime = Time.valueOf(time);
		return sqlTime;
	}

	public Time getTimeClassPmTime()
	{
		LocalTime time = LocalTime.parse(this.endTime, DateTimeFormatter.ofPattern("HH:mm"));
		Time sqlTime = Time.valueOf(time);
		return sqlTime;
	}

	//String=>LocalDate=>Date
	public Date getDtoday()
	{
		LocalDate date= LocalDate.parse(this.today, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		Date sqlDate = Date.valueOf(date);
		return sqlDate;
	}
	//String=>LocalDate
	public LocalDate getLDtoday()
	{
		LocalDate date= LocalDate.parse(this.today);
		return date;
	}

//	public String getResult() {
//		String amTTime = getAmTime();
//		String pmTTime = getPmTime();
//		String result2 = "00:00";
//		SimpleDateFormat sdf= new SimpleDateFormat("HH:mm");
//		try {
//			Date date1 = sdf.parse(amTTime);
//			Date date2 = sdf.parse(pmTTime);
//			long calTTime = (date2.getTime())-(date1.getTime());
//			//calHour
//			int calHour= (int)(calTTime/1000/60/60);
//			int calMin= (int)(calTTime/1000/60%60);
//			result2 =calHour+ "時間"+calMin + "分";
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		}
//
//		return this.result = result2;
//	}

}
