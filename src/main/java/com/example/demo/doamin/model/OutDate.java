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


@JsonPropertyOrder({"日付","曜日","始時","始分","終時","終分"})

@Getter
@Setter
public class OutDate {

//	@JsonProperty("ユーザーID")
//	@NotNull(message = "NULLは不可")
//	private String workersId;

	@JsonProperty("日付")
	@JsonFormat(pattern = "yyyy-MM-dd",timezone = "Asia/Tokyo")
	private int today;

	@JsonProperty("曜日")
	private String Dayofweek;

	@JsonProperty("始時")
	@JsonFormat(pattern = "HH")
	private String starthour;

	@JsonProperty("始分")
	@JsonFormat(pattern = "HH")
	private String startTime;

	@JsonProperty("終時")
	@JsonFormat(pattern = "mm")
	private String endhour;

	@JsonProperty("終分")
	@JsonFormat(pattern = "mm")
	private String endTime;

//	@ManyToOne
//	@JoinColumn(name="workersId",insertable = false,updatable = false)
//	private User user;
}
