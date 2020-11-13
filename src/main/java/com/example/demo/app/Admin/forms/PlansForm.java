package com.example.demo.app.Admin.forms;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlansForm {

	@Size(min = 0,max=20,message = "20字以内です")
	@NotBlank
	private String title;

	@NotBlank
	private String today;

	@Size(min=0,max=1000,message = "1000字以内です")
	private String plans;

	@Pattern(regexp = "\s|TRUE",message="無効な入力値です")
	private String holiday;

}
