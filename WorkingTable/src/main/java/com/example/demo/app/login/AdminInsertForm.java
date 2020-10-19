package com.example.demo.app.login;

import javax.validation.constraints.NotNull;

import com.example.demo.doamin.model.RoleName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminInsertForm {
	private String userid;
	private String password;
	private String firstname;
	private String lastname;
	@NotNull(message = "必須項目です")
	private RoleName roleno;
}
