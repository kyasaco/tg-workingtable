package com.example.demo.app.Admin.forms;

import java.util.List;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class UserConfForm {

	private List<@NotBlank(message = "空白での登録は許されていません") String> userid;

	private  List<@NotBlank(message = "空白での登録は許されていません") String> lastname;

	private List<@NotBlank(message = "空白での登録は許されていません") String> firstname;


	private  List<@NotBlank(message = "空白での登録は許されていません") String> rolename;
}
