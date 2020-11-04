package com.example.demo.doamin.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="usrs3")
@Getter
@Setter
@EqualsAndHashCode
public class User implements Serializable {
	@Id
	private String userid;
	private String password;
	private String firstname;
	private String lastname;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private RoleName rolename;

//	@OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
//	private List<DateEntity> dateEntities;

}
