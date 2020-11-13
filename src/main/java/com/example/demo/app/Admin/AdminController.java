package com.example.demo.app.Admin;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.security.access.annotation.Secured;
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
import com.example.demo.app.Admin.forms.AdminDeleteForm;
import com.example.demo.app.Admin.forms.PlansDeleteForm;
import com.example.demo.app.Admin.forms.PlansForm;
import com.example.demo.app.Admin.forms.UserConfForm;
import com.example.demo.doamin.model.DateEntity;
import com.example.demo.doamin.model.Plans;
import com.example.demo.doamin.model.RoleName;
import com.example.demo.doamin.model.User;
import com.example.demo.doamin.service.DataService.DateService;
import com.example.demo.doamin.service.plans.PlansService;
import com.example.demo.doamin.service.user.UserService;
import com.example.demo.errors.DateValidation;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
@RequestMapping("/Admin")
public class AdminController {
	private final DateService dateservice;
	private final UserService userservice ;
	private final PlansService plansrservice;
	private final DateValidation dateValidation;
	private final MyAppProperties myAppProperties;

	@GetMapping
	public ModelAndView getConf(
			HttpServletResponse httpServletResponse,
			ModelAndView mav) {
		mav.setViewName("Admin/AdminConfForm");
		return mav;
	}
/**Dateマッピング********************************************************************/

	//勤務レコードを削除
	@GetMapping("/DeleteDate/search")
	public ModelAndView  GsearchDate(
			@ModelAttribute("AdminDeleteForm")AdminDeleteForm adf,
			@PageableDefault(page = 0,size = 20,sort = {"workers_id"},direction =Direction.ASC)Pageable pageable,
			ModelAndView mav
			) {
		Page<DateEntity> udata = dateservice.getPageVYMD(adf, pageable);
		mav.addObject("date_data",udata);
		mav.addObject("content",udata.getContent());
		mav.addObject("SELECT_YEAR",myAppProperties.getSELECT_YEAR());
		mav.addObject("SELECT_MONTH",myAppProperties.getSELECT_MONTH());
		mav.addObject("SELECT_DAYS",myAppProperties.getSELECT_DAYS());


		mav.setViewName("Admin/AdminDateDelete");

		return  mav;
	}
	//変更
	//検索マッピング********************************************************************/
	@PostMapping("/DeleteDate/search")
	public ModelAndView PsearchDate(
			@Validated @ModelAttribute("AdminDeleteForm")AdminDeleteForm adf,
			BindingResult bindingResult,
			@RequestParam(name = "d_delete",required = false)Integer id,
			@PageableDefault(page = 0,size = 20,sort = {"workers_id"},direction =Direction.ASC)Pageable pageable,
			ModelAndView mav
			) {
		String error_message = dateValidation.ErrorSwitching(dateservice.DeleteOne(id));
		mav.addObject("error_message",error_message);

		if(bindingResult.hasErrors()) {
			adf.Init();
		}
		Page<DateEntity> udata = dateservice.getPageVYMD(adf, pageable);
		mav.addObject("date_data",udata);
		mav.addObject("content",udata.getContent());
		mav.addObject("SELECT_YEAR",myAppProperties.getSELECT_YEAR());
		mav.addObject("SELECT_MONTH",myAppProperties.getSELECT_MONTH());
		mav.addObject("SELECT_DAYS",myAppProperties.getSELECT_DAYS());

		mav.setViewName("Admin/AdminDateDelete");

		return  mav;
	}


	/**Userマッピング********************************************************************/
	@GetMapping("/ConfUser")
	public ModelAndView getUserConf(
		@ModelAttribute("user_form")UserConfForm ucf,
		ModelAndView mav) {
		List<User> data = userservice.findAllAsc();
		mav.addObject("SELECT_ROLE", myAppProperties.getSELECT_ROLE());
		mav.addObject("user_data", data);
		mav.setViewName("Admin/AdminUserConfgure");

		return mav;
	}

	@PostMapping("/ConfUser")
	@Transactional
	public ModelAndView postUserConf(
			@Validated @ModelAttribute("user_form")UserConfForm ucf,
			BindingResult bindingResult,
			@RequestParam(value="dcheck",required = false)List<String> dcheck,
			ModelAndView mav) {
		if(!bindingResult.hasErrors()) {
			userservice.updateUsers(ucf, dcheck);
		}

		List<User> data = userservice.findAllAsc();
		mav.addObject("SELECT_ROLE", myAppProperties.getSELECT_ROLE());
		mav.addObject("user_data", data);
		mav.setViewName("Admin/AdminUserConfgure");

		return mav;
	}

	/**Planマッピング********************************************************************/
	/**
	 * @param today
	 * @param mav
	 * @return
	 */
	@GetMapping("/Plans")
	public ModelAndView getPlans(
		@ModelAttribute("PlansDeleteForm")PlansDeleteForm pdf,
		@RequestParam("today")
		@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate today,
		ModelAndView mav) {

		List<Plans> plans_data = plansrservice.getAllPlans();
		mav.addObject("SELECT_YEAR",myAppProperties.getSELECT_YEAR());
		mav.addObject("SELECT_MONTH",myAppProperties.getSELECT_MONTH());
		mav.addObject("SELECT_DAYS",myAppProperties.getSELECT_DAYS());

		mav.setViewName("Admin/AdminPlans");
		mav.addObject("today", today);
		mav.addObject("plans_data", plans_data);
		return mav;
	}
	/**
	 * @param pdf
	 * @param today
	 * @param mav
	 * @return
	 */
	@PostMapping(value="/Plans",params = "form_plan")
	public ModelAndView postDPlans(
		@ModelAttribute("PlansDeleteForm")PlansDeleteForm pdf,
		ModelAndView mav
		) {
		plansrservice.deletePlansYMD(pdf);
		List<Plans> plans_data = plansrservice.getAllPlans();
		mav.addObject("SELECT_YEAR",myAppProperties.getSELECT_YEAR());
		mav.addObject("SELECT_MONTH",myAppProperties.getSELECT_MONTH());
		mav.addObject("SELECT_DAYS",myAppProperties.getSELECT_DAYS());

		mav.setViewName("Admin/AdminPlans");
		mav.addObject("today",LocalDate.now());
		mav.addObject("plans_data", plans_data);
		return mav;
	}

	/**
	 * @param plansform
	 * @param today
	 * @param mav
	 * @return
	 */
	@GetMapping("/Plans/Insert")
	public ModelAndView getPlansInsert(
			@ModelAttribute("plans_form")PlansForm plansform,
			@RequestParam("today")
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate today,
			ModelAndView mav) {
		Optional<Plans> plan = plansrservice.getOnePlan(today.toString());

		mav.addObject("plans_form",plan.get());
		mav.addObject("today", today);
		mav.setViewName("Admin/AdminInsertPlans");
		return mav;
	}

	/**
	 * @param plansform
	 * @param bindingResult
	 * @param today
	 * @param mav
	 * @return
	 */
	@PostMapping("/Plans/Insert")
	@Transactional
	public ModelAndView postPlansInsert(
			@Validated @ModelAttribute("plans_form")PlansForm plansform,
			BindingResult bindingResult,
			@RequestParam(name="today",required = false)
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate today,
			ModelAndView mav) {

		mav.addObject("today", today);
		mav.setViewName("Admin/AdminInsertPlans");
		if(!bindingResult.hasErrors()) {
			plansrservice.SaveAndFlushPlans(plansform);
		}
		return mav;
	}

	/**
	 * @param today
	 * @param mav
	 * @return
	 */
	@PostMapping("/Plans/CtrlDelete")
	@Transactional
	public ModelAndView postPlansCtrlDelete(
			@RequestParam(name="plan_name",required = false)
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate today,
			ModelAndView mav) {
		String error_msg = dateValidation.ErrorSwitching(plansrservice.deletePlan(today));

		return new ModelAndView("redirect:/Admin/Plans?today="+today.toString()+"");
	}


}
