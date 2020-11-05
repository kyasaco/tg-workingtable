package com.example.demo.app.entry;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.hibernate.query.criteria.internal.expression.function.SubstringFunction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.expression.Lists;

import com.example.demo.doamin.model.DateEntity;
import com.example.demo.doamin.model.OutDate;
import com.example.demo.doamin.model.Plans;
import com.example.demo.doamin.model.User;
import com.example.demo.doamin.service.DataService.DateService;
import com.example.demo.doamin.service.plans.PlansService;
import com.example.demo.doamin.service.user.UserService;
import com.example.demo.doamin.service.user.WorkersUserDetails;
import com.example.demo.errors.DateValidation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import ajd4jp.AJDException;

import com.fasterxml.jackson.dataformat.csv.CsvGenerator;

import io.github.classgraph.AnnotationEnumValue;
import lombok.AllArgsConstructor;
import net.bytebuddy.asm.Advice.OffsetMapping.Sort;

/*メインコントローラー：ログインに成功したユーザーの勤務表を出力するメイン画面*/
@RequestMapping("/")
@AllArgsConstructor
@Controller
public class EntryController {

	/*DIするサービスクラスを宣言*/
	private final DateService dateservice;
	private final DateValidation dateValidation;
	private final PlansService plansservice;
	private final DownloadHelper downloadHelper;
	private final LocalDate TODAY_NOW = LocalDate.now();
	/*定数*/
	/*開始時間と終了時間のSELECTタグ用(LinkedHashMapは挿入された順番を保持する)*/
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

	/*データをCSV化*/
	public String getCsvText(WorkersUserDetails workersUserDetails,LocalDate today) throws JsonProcessingException{
		CsvMapper mapper = new CsvMapper();
		/*
		 * ObjectMapper(CsvMapper)はDateをシリアル値化するときデフォルトではTimestampで変換するので一度.disableで解除
		 * setDateFormat(StdDateFormat()にてDateで変換するように)
		 */
		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		mapper.setDateFormat(new StdDateFormat().withColonInTimeZone(true));
		mapper.configure(CsvGenerator.Feature.ALWAYS_QUOTE_STRINGS, true);
		//ヘッダをつける
		CsvSchema schema = mapper.schemaFor(OutDate.class).withHeader();
		List<OutDate> datecsv = dateservice.DateEntityToOutDate(today,workersUserDetails.getUsername());
		return  mapper.writer(schema).writeValueAsString(datecsv);

	}

	/*Formの初期化と宣言*/
	@ModelAttribute("EntryForm")
	EntryForm setUpForm() {
		EntryForm entryForm = new EntryForm();
		return entryForm;
	}
	//マッピング処理---------------------------------------------------------------------------------------------------------------------------------------
	/*初期表示。パラメータの日付を受け取る*/
	@GetMapping({"/","/{today}","/calendar/{today}"})
	public ModelAndView EntryView(
	@AuthenticationPrincipal WorkersUserDetails workersUserDetails,
	@PathVariable(name="today",required = false)
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate today,
	@PageableDefault(page = 0,size = 31,sort = {"today"},direction =Direction.ASC)Pageable pageable,
	ModelAndView  mav) throws AJDException
	{
		if(today==null ){
			today=LocalDate.now();
		}

		mav.addObject("select_a",SELECT_TIME);
		mav.addObject("today",today);
		mav.setViewName("index");
		List<Plans> plan = plansservice.getOneSomeDayPlan(TODAY_NOW.toString());
		Page<DateEntity> datedata = dateservice.findQueryMonthForPage(
			today,
			workersUserDetails.getUsername(),
			pageable);
		int weekdays_sum = dateservice.getWeekDays(today);
		double sum_time = dateservice.getSTETMinus(today, workersUserDetails.getUsername());
		mav.addObject("weekdays_sum", weekdays_sum);
		mav.addObject("sum_time", sum_time);
		mav.addObject("TODAY_NOW",Date.valueOf(TODAY_NOW));
		mav.addObject("plan_data", plan);
		mav.addObject("DateTableData", datedata.getContent());
		mav.addObject("Luser", workersUserDetails.getUser());

		return mav;
	}

	/*勤務表レコード登録。エラーでなければ登録*/
	@PostMapping
	@Transactional
	public ModelAndView Entry(
			@Validated EntryForm entryForm,
			BindingResult result,
			ModelAndView  mav,
			@AuthenticationPrincipal WorkersUserDetails workersUserDetails,
			@PageableDefault(page = 0,size = 25,sort = {"today"},direction =Direction.ASC)Pageable pageable)
	{
		mav.addObject("select_a",SELECT_TIME);
		mav.addObject("today", entryForm.getLDtoday());
		mav.setViewName("index");
		mav.addObject("Luser", workersUserDetails.getUser());
		if(!result.hasErrors()){
			String errormessage = dateValidation.ErrorSwitching(dateservice.SaveFlush(entryForm));
			mav.addObject("errormsg", errormessage);
		}
		List<Plans> plan = plansservice.getOneSomeDayPlan(TODAY_NOW.toString());
		Page<DateEntity> datedata = dateservice.findQueryMonthForPage(
			entryForm.getLDtoday(),
			workersUserDetails.getUsername(),
			pageable);
		double sum_time = dateservice.getSTETMinus(entryForm.getLDtoday(), workersUserDetails.getUsername());
		mav.addObject("sum_time", sum_time);
		mav.addObject("TODAY_NOW",Date.valueOf(TODAY_NOW));
		mav.addObject("plan_data", plan);

		mav.addObject("DateTableData", datedata.getContent());
		return mav;
	}

	/*勤務表csvダウンロードのマッピングを行う*/
	@PostMapping("/download")
	@Transactional
	public ResponseEntity<byte[]> download(
			@AuthenticationPrincipal WorkersUserDetails workersUserDetails,
			@RequestParam("filename")String filename,
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
			@RequestParam("D_today")LocalDate today) throws IOException{
		if(filename == "") {
			filename= today.getMonthValue()+"月のユーザー"+workersUserDetails.getUsername()+"の勤務表";
		}
		HttpHeaders headers = new HttpHeaders();
		downloadHelper.addContentDisposition(headers, filename+".csv");
		return new ResponseEntity<>(getCsvText(workersUserDetails,today).getBytes("MS932"),headers,HttpStatus.OK);
	}

}
