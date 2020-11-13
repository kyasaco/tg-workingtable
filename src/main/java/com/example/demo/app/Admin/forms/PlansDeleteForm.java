package com.example.demo.app.Admin.forms;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PlansDeleteForm {
	private Integer year = Integer.valueOf(LocalDate.now().getYear());
	private Integer month;
	private Integer day ;

}
