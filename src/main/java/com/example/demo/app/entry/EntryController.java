package com.example.demo.app.entry;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
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

import com.example.demo.app.login.LoginForm;
import com.example.demo.doamin.model.DateEntity;
import com.example.demo.doamin.model.OutDate;
import com.example.demo.doamin.model.User;
import com.example.demo.doamin.service.DataService.DateService;
import com.example.demo.doamin.service.user.UserService;
import com.example.demo.doamin.service.user.WorkersUserDetails;
import com.example.demo.errors.DateValidation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.dataformat.csv.CsvGenerator;

import io.github.classgraph.AnnotationEnumValue;
import lombok.AllArgsConstructor;

/*メインコントローラー：ログインに成功したユーザーの勤務表を出力するメイン画面*/
@RequestMapping("/")
@AllArgsConstructor
@Controller
public class EntryController {

	/*DIするサービスクラスを宣言*/
	private final UserService userservice;
	private final DateService dateservice;
	private final DateValidation dateValidation;
	private final DownloadHelper downloadHelper;

	/*開始時間と終了時間のSELECTタグ用(LinkedHashMapは挿入された順番を保持する)*/
	final Map<String,String> SELECT_TIME = Collections.unmodifiableMap(new LinkedHashMap<String, String>(){
		{
			for(Integer i = 0; i < 24; i++) {
				if(i>9) {
					put(i.toString()+":00", i.toString()+":00");
					put(i.toString()+":30", i.toString()+":30");
				}
				else {
					put("0"+i.toString()+":00", "0"+i.toString()+":00");
					put("0"+i.toString()+":30", "0"+i.toString()+":30");
				}
			}
		}
	});
	/*データをCSV化*/
	public String getCsvText(WorkersUserDetails workersUserDetails) throws JsonProcessingException{
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

		List<DateEntity> datecsv = dateservice.findByWorkersId(workersUserDetails.getUsername());
		String a = mapper.writer(schema).writeValueAsString(datecsv);
		return  a;
	}

	/*Formの初期化と宣言*/
	@ModelAttribute("EntryForm")
	EntryForm setUpForm() {
		 EntryForm entryForm = new EntryForm();
		return entryForm;
	}

	/*初期表示。パラメータの日付を受け取る*/
	@GetMapping({"/","/{today}"})
	public ModelAndView EntryView(
	@PathVariable(name="today",required = false)
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate today,
	ModelAndView  mav,
	@AuthenticationPrincipal WorkersUserDetails workersUserDetails)
	{
		if(today==null)
		{
			today=LocalDate.now();
		}
		mav.addObject("select_a",SELECT_TIME);
		mav.addObject("today",today);
		mav.setViewName("index");
		List<DateEntity> datedata = dateservice.findByWorkersId(workersUserDetails.getUsername());
		mav.addObject("DateTableData", datedata);
		mav.addObject("Luser", workersUserDetails.getUser());

		return mav;
	}

	/*勤務表レコード登録。エラーでなければ登録*/
	@PostMapping
	public ModelAndView Entry(
			@Validated EntryForm entryForm,
			BindingResult result,
			ModelAndView  mav,
			@AuthenticationPrincipal WorkersUserDetails workersUserDetails)
	{
		mav.addObject("select_a",SELECT_TIME);
		mav.addObject("today", entryForm.getDtoday());
		mav.setViewName("index");
		mav.addObject("Luser", workersUserDetails.getUser());
		if(!result.hasErrors()){
			String errormessage = dateValidation.ErrorSwitching(dateservice.SaveFlush(entryForm));
			mav.addObject("errormsg", errormessage);
		}
		List<DateEntity> datedata = dateservice.findByWorkersId(workersUserDetails.getUsername());
		mav.addObject("DateTableData", datedata);
		return mav;
	}

	@PostMapping("/download")
	public ResponseEntity<byte[]> download(
			@AuthenticationPrincipal WorkersUserDetails workersUserDetails) throws IOException{
		HttpHeaders headers = new HttpHeaders();
		downloadHelper.addContentDisposition(headers, "テスト.csv");
		return new ResponseEntity<>(getCsvText(workersUserDetails).getBytes("MS932"),headers,HttpStatus.OK);
	}

}
