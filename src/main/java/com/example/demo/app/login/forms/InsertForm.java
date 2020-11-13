package com.example.demo.app.login.forms;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import com.example.demo.doamin.model.RoleName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InsertForm {

	@Length(min = 4,max = 4,message = "4文字のみです")
	@NotBlank(message = "入力必須項目です")
	private String userid;

	@Length(min = 6 , message = "長さは6文字以上必要です")
	@Pattern(regexp = "\\d+",message = "使えるのは数字のみです")
	private String password;

	@NotBlank(message = "入力必須項目です")
	private String firstname;

	@NotBlank(message = "入力必須項目です")
	private String lastname;

	@NotNull(message = "入力必須項目です")
	private RoleName rolename = RoleName.システム部;
}
