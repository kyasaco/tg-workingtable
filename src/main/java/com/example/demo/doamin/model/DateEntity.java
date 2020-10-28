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

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Getter;
import lombok.Setter;


@JsonPropertyOrder({"id","ユーザーID","日付","始業","終業"})
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
	@JsonProperty("id")
	private Integer id;

	@JsonProperty("ユーザーID")
	@NotNull(message = "NULLは不可")
	private String workersId;

	@JsonProperty("日付")
	@JsonFormat(pattern = "yyyy-MM-dd",timezone = "Asia/Tokyo")
	@NotNull(message = "NULLは不可")
	@Column(nullable=false)
	private Date today;

	@JsonProperty("始業")
	@JsonFormat(pattern = "HH:mm")
	@NotNull(message = "NULLは不可")
	@Column(nullable=false)
	private Time startTime;

	@JsonProperty("終業")
	@JsonFormat(pattern = "HH:mm")
	@NotNull(message = "NULLは不可")
	@Column(nullable=false)
	private Time endTime;

//	@ManyToOne
//	@JoinColumn(name="workersId",insertable = false,updatable = false)
//	private User user;
}
