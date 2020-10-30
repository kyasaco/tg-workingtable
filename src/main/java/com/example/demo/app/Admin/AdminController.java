package com.example.demo.app.Admin;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

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

import com.example.demo.doamin.model.DateEntity;
import com.example.demo.doamin.model.Plans;
import com.example.demo.doamin.model.User;
import com.example.demo.doamin.service.DataService.DateService;
import com.example.demo.doamin.service.plans.PlansService;
import com.example.demo.doamin.service.user.UserService;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
@Secured("ROLE_ADMIN")
@RequestMapping("/Admin")
public class AdminController {
	private final DateService dateservice;
	private final UserService userservice;
	private final PlansService plansrservice;

	@GetMapping
	public ModelAndView getConf(
			HttpServletResponse httpServletResponse,
			@RequestParam("today")String today,
			ModelAndView mav) {
		mav.setViewName("Admin/AdminConfForm");
		mav.addObject("today", today);
		return mav;
	}

/**Dateマッピング********************************************************************/

	//勤務レコードを削除
	@GetMapping({"/DeleteDate/{id}","/DeleteDate"})
	@Transactional
	public ModelAndView deleteDate(
		@RequestParam("today")
		@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate today,
		@PathVariable(name="id",required = false) Integer id,
		ModelAndView mav) {
		mav.addObject("today", today);
		if(id != null) {
			if(dateservice.findOne(id).isPresent()) {
				dateservice.DeleteOne(id);
			}
			else {
				mav.addObject("NullError", "データが存在しません");
			}
		}
		List<DateEntity> data = dateservice.findAllAsc();
		mav.addObject("date_data", data);
		mav.setViewName("Admin/AdminDateDelete");

		return mav;
	}
	//変更
	//検索マッピング********************************************************************/
	@PostMapping({"/DeleteDate/search","/DeleteDate/search/{id}"})
	public ModelAndView searchDate(
			@RequestParam(name = "userid",required = false)String userid,
			@RequestParam(name = "today",required = false)String today,
			@RequestParam(name = "d_delete",required = false)Integer id,
			ModelAndView mav) {

		if(id != null) {
			if(dateservice.findOne(id).isPresent()) {
				dateservice.DeleteOne(id);
			}
			else {
				mav.addObject("NullError", "データが存在しません");
			}
		}

		List<DateEntity> udata = dateservice.VfindUIDandTDY(userid, today);
		mav.addObject("date_data",udata);
		mav.addObject("userid",userid);
		mav.addObject("today",today);
		mav.setViewName("Admin/AdminDateDelete");
		return mav;
	}


	/**Userマッピング********************************************************************/
	@GetMapping("/ConfUser")
	public ModelAndView getUserConf(
		@RequestParam("today")
		@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate today,
		ModelAndView mav) {
		List<User> data = userservice.findAllAsc();
		mav.addObject("user_data", data);
		mav.setViewName("Admin/AdminUserConfgure");
		mav.addObject("today", today);

		return mav;
	}

	/**Planマッピング********************************************************************/
	@GetMapping("/Plans")
	public ModelAndView getPlans(
		@RequestParam("today")
		@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate today,
		ModelAndView mav) {

		List<Plans> plans_data = plansrservice.getAllPlans();

		mav.setViewName("Admin/AdminPlans");
		mav.addObject("today", today);
		mav.addObject("plans_data", plans_data);
		return mav;
	}
	@GetMapping("/Plans/Insert")
	public ModelAndView getPlansInsert(
			@ModelAttribute("plans_form")PlansForm plansform,
			@RequestParam("today")
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate today,
			ModelAndView mav) {
		mav.addObject("today", today);
		mav.setViewName("Admin/AdminInsertPlans");
		return mav;
	}

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


}
