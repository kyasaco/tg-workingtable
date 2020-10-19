package com.example.demo.doamin.model;

import java.sql.Time;
import java.sql.Date ;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Date7")
@Getter
@Setter
public class DateEntity {
	/*
	 * Entityクラスのフィールドの命名規則はHibernateに準ずる
	 * キャメルケースは大文字を境にスネークケースになる
	 */	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@NotNull(message = "NULLは不可")
	private String workersId;
	@NotNull(message = "NULLは不可")
	@Column(nullable=false)
	private Date today;	
	@NotNull(message = "NULLは不可")
	@Column(nullable=false)
	private Time startTime;
	@NotNull(message = "NULLは不可")
	@Column(nullable=false)
	private Time endTime;
	
//	@ManyToOne
//	@JoinColumn(name="workersId",insertable = false,updatable = false)
//	private User user;
}
