package com.example.demo.app.entry;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

import com.example.demo.MyAppProperties;
import com.example.demo.app.entry.forms.EntryForm;
import com.example.demo.doamin.model.DateEntity;
import com.example.demo.doamin.model.OutDate;
import com.example.demo.doamin.model.Plans;
import com.example.demo.doamin.service.DataService.DateService;
import com.example.demo.doamin.service.plans.PlansService;
import com.example.demo.doamin.service.user.authentication.WorkersUserDetails;
import com.example.demo.errors.DateValidation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.dataformat.csv.CsvGenerator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import ajd4jp.AJDException;
import lombok.AllArgsConstructor;

/*メインコントローラー：ログインに成功したユーザーの勤務表を出力するメイン画面*/
@RequestMapping("/")
@AllArgsConstructor
@Controller
public class EntryController {

	/**
	 * DIするサービスクラスを宣言
	 */
	private final DateService dateservice;
	private final DateValidation dateValidation;
	private final PlansService plansservice;
	private final DownloadHelper downloadHelper;
	private final MyAppProperties myAppProperties;




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

	/**
	 * 勤務表入力フォームの初期化と宣言
	 *
	 * @return 初期化されたEntryForm
	 */
	@ModelAttribute("EntryForm")
	EntryForm setUpForm() {
		EntryForm entryForm = new EntryForm();
		return entryForm;
	}
	//マッピング処理---------------------------------------------------------------------------------------------------------------------------------------

	/**
	 * 初期表示。パラメータの日付を受け取る
	 *
	 * @param workersUserDetails 認証されたユーザー情報
	 * @param today 開かれている勤務表の日付
	 * @param pageable ページング情報保持変数
	 * @param mav モデル＆ビュー
	 * @return メイン画面(予定一覧、勤務表、勤務表入力欄)の表示
	 * @throws AJDException
	 */
	@GetMapping({"/","/{today}","/calendar/{today}"})
	public ModelAndView EntryView(
	@AuthenticationPrincipal WorkersUserDetails workersUserDetails,
	@PathVariable(name="today",required = false)
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate today,
	@PageableDefault(page = 0,size = 31,sort = {"today"},direction =Direction.ASC)Pageable pageable,
	ModelAndView  mav) throws AJDException
	{
		if(today==null){
			today=LocalDate.now();
		}

		List<Plans> plan = plansservice.getOneSomeDayPlan(myAppProperties.getTODAY_NOW().toString());
		Page<DateEntity> datedata = dateservice.findQueryMonthForPage(
			today,
			workersUserDetails.getUsername(),
			pageable);
		int weekdays_sum = dateservice.getWeekDays(today);
		double sum_time = dateservice.getSTETMinus(today, workersUserDetails.getUsername());

		mav.setViewName("index");

		mav.addObject("select_a",myAppProperties.getSELECT_TIME());
		mav.addObject("today",today);
		mav.addObject("weekdays_sum", weekdays_sum);
		mav.addObject("sum_time", sum_time);
		mav.addObject("TODAY_NOW",Date.valueOf(myAppProperties.getTODAY_NOW()));
		mav.addObject("plan_data", plan);
		mav.addObject("DateTableData", datedata.getContent());
		mav.addObject("Luser", workersUserDetails.getUser());

		return mav;
	}


	/**
	 * 勤務表レコード登録。エラーでなければ登録
	 *
	 * @param entryForm フォーム入力クラス
	 * @param result バリデーションエラー検知
	 * @param mav モデル＆ビュー
	 * @param workersUserDetails 認証されたユーザー情報
	 * @param pageable ページング情報保持変数
	 * @return メイン画面情報に登録結果を付与させたモデル
	 * @throws AJDException
	 */
	@PostMapping
	@Transactional
	public ModelAndView InsertWorkingTables(
			@Validated EntryForm entryForm,
			BindingResult result,
			ModelAndView  mav,
			@AuthenticationPrincipal WorkersUserDetails workersUserDetails,
			@PageableDefault(page = 0,size = 31,sort = {"today"},direction =Direction.ASC)Pageable pageable) throws AJDException
	{
		if(!result.hasErrors()){
			String errormessage = dateValidation.ErrorSwitching(dateservice.SaveFlush(entryForm));
			mav.addObject("errormsg", errormessage);
		}
		List<Plans> plan = plansservice.getOneSomeDayPlan(myAppProperties.getTODAY_NOW().toString());
		Page<DateEntity> datedata = dateservice.findQueryMonthForPage(
			entryForm.getLDtoday(),
			workersUserDetails.getUsername(),
			pageable);

		int weekdays_sum = dateservice.getWeekDays(entryForm.getLDtoday());
		double sum_time = dateservice.getSTETMinus(entryForm.getLDtoday(), workersUserDetails.getUsername());

		mav.setViewName("index");

		mav.addObject("select_a",myAppProperties.getSELECT_TIME());
		mav.addObject("today", entryForm.getLDtoday());
		mav.addObject("Luser", workersUserDetails.getUser());
		mav.addObject("sum_time", sum_time);
		mav.addObject("TODAY_NOW",Date.valueOf(myAppProperties.getTODAY_NOW()));
		mav.addObject("plan_data", plan);
		mav.addObject("weekdays_sum", weekdays_sum);
		mav.addObject("DateTableData", datedata.getContent());
		return mav;
	}

	/**
	 * 勤務表csvダウンロードのマッピングを行う
	 *
	 * @param workersUserDetails 認証されたユーザー情報
	 * @param filename 入力されたファイル名
	 * @param today 開かれていた勤務表の日付
	 * @return ファイル名が付いたcsvファイルをダウンロード
	 * @throws IOException
	 */
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
