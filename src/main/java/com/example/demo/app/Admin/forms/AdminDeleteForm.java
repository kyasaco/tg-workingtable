package com.example.demo.app.Admin.forms;

import java.time.LocalDate;

import com.example.demo.errors.WorkerIdValid;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@WorkerIdValid(message = "存在しない従業員IDです")
public class AdminDeleteForm {

	private String userid;
	private Integer year = Integer.valueOf(LocalDate.now().getYear());
	private Integer month;
	private Integer day ;

	public void Init() {
		this.userid = "";
		this.month = null;
		this.day = null;
	}
}
