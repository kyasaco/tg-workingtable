package com.example.demo.doamin.model;

import java.sql.Date;
import java.sql.Time;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*曜日分時間分割から*/
@JsonPropertyOrder({"id","ユーザーID","日付","始業時間","終業時間"})
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OutDateSub {
	@JsonProperty("id")
	private Integer id;

	@JsonProperty("ユーザーID")
	private String workersId;

	@JsonProperty("日付")
	@JsonFormat(pattern = "yyyy-MM-dd",timezone = "Asia/Tokyo")
	private Date today;
	@JsonProperty("日付")
	private String DoW;
	@JsonProperty("始業時間")
	private String starthour;
	@JsonProperty("始業分")
	private String startTime;

	@JsonProperty("終業時間")
	@JsonFormat(pattern = "HH:mm")
	private Time endTime;


}
