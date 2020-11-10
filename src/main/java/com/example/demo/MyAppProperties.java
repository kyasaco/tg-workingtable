package com.example.demo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.example.demo.doamin.model.RoleName;

import lombok.Getter;
import lombok.Setter;

/**
 * 定数設定クラス
 *
 * @author kasuda
 *
 */
@Component
@Setter
@Getter
public class MyAppProperties {

	/**
	 * 現在時刻の定数
	 */
	private final LocalDate TODAY_NOW = LocalDate.now();

	/**
	 * 開始時間と終了時間のSELECTタグ用(LinkedHashMapは挿入された順番を保持する)定数
	 */
	@SuppressWarnings("serial")
	final Map<String,String> SELECT_TIME = Collections.unmodifiableMap(new LinkedHashMap<String, String>(){
		{
			for(Integer i = 0; i < 24; i++) {
				if(i>9){
					put(i.toString()+":00", i.toString()+":00");
					put(i.toString()+":30", i.toString()+":30");
				}else{
					put("0"+i.toString()+":00", "0"+i.toString()+":00");
					put("0"+i.toString()+":30", "0"+i.toString()+":30");
				}
			}
		}
	});

	/**
	 *
	 */
	@SuppressWarnings("serial")
	private final List<RoleName> SELECT_ROLE = new ArrayList<RoleName>() {
		{
			for(RoleName str : RoleName.values()) {
				add(str);
			}
		}
	};

	@SuppressWarnings("serial")
	private final Map<Integer,Integer> SELECT_YEAR = Collections.unmodifiableMap(new LinkedHashMap<Integer, Integer>(){
		{
			Integer year = 2000;
			for(Integer i = 0; i < 200; i++) {
				put(year+i, year+i);
			}
		}
	});

	@SuppressWarnings("serial")
	private final Map<Integer,Integer> SELECT_MONTH = Collections.unmodifiableMap(new LinkedHashMap<Integer, Integer>(){
		{
			Integer month = 1;
			for(Integer i = 0; i < 12; i++) {
				put(month+i, month+i);
			}
		}
	});

	@SuppressWarnings("serial")
	private final Map<Integer,Integer> SELECT_DAYS = Collections.unmodifiableMap(new LinkedHashMap<Integer, Integer>(){
		{
			Integer day = 1;
			for(Integer i = 0; i < 31; i++) {
				put(day+i, day+i);
			}
		}
	});

}
