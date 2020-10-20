package com.example.demo.app.login;

import com.example.demo.doamin.model.RoleName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InsertForm {
	private String userid;
	private String password;
	private String firstname;
	private String lastname;
	private RoleName rolename;
}
