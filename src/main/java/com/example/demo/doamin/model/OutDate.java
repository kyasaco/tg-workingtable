package com.example.demo.doamin.model;

import java.sql.Date;
import java.sql.Time;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Getter;
import lombok.Setter;

@JsonPropertyOrder({"id","workersId","today","startTime","endTime"})
@Setter
@Getter
public class OutDate {
	@JsonProperty("id")
	private Integer id;

	@JsonProperty("workersId")
	private String workersId;

	@JsonProperty("today")
	@JsonFormat(pattern = "yyyy-MM-dd",timezone = "Asia/Tokyo")
	private Date today;

	@JsonProperty("startTime")
	@JsonFormat(pattern = "HH:mm:ss")
	private Time startTime;

	@JsonProperty("endTime")
	@JsonFormat(pattern = "HH:mm:ss")
	private Time endTime;

	public OutDate() {}

	public OutDate(Integer id,String workersId,Date today,Time startTime,Time endTime) {
		this.id = id;
		this.workersId = workersId;
		this.today = today;
		this.startTime = startTime;
		this.endTime = endTime;
	}

}
