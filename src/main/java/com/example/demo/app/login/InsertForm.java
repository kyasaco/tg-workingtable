package com.example.demo.app.login;

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

	@NotBlank(message = "必須です")
	private String userid;

	@Length(min = 6 , message = "長さは6文字以上必要です")
	@Pattern(regexp = "\\d+",message = "使えるのは数字のみです")
	private String password;

	@NotBlank(message = "必須です")
	private String firstname;

	@NotBlank(message = "必須です")
	private String lastname;

	@NotNull(message = "必須です")
	private RoleName rolename = RoleName.システム部;
}
