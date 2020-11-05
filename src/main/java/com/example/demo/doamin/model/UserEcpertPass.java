package com.example.demo.doamin.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class UserEcpertPass {

	@Id
	private String userid;
	private String firstname;
	private String lastname;

	private String rolename;

}
