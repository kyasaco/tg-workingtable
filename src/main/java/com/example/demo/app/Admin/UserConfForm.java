package com.example.demo.app.Admin;

import java.util.List;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.example.demo.doamin.model.RoleName;
import com.example.demo.doamin.model.User;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class UserConfForm {
	private List<String> userid;
	private  List<String> lastname;
	private List<String> firstname;
	private  List<String> rolename;
}
