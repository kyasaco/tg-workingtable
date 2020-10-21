package com.example.demo.app.entry;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.format.annotation.DateTimeFormat;
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

import com.example.demo.app.login.LoginForm;
import com.example.demo.doamin.model.DateEntity;
import com.example.demo.doamin.model.User;
import com.example.demo.doamin.service.DataService.DateService;
import com.example.demo.doamin.service.user.UserService;
import com.example.demo.doamin.service.user.WorkersUserDetails;
import com.example.demo.errors.DateValidation;

import io.github.classgraph.AnnotationEnumValue;
import lombok.AllArgsConstructor;
@RequestMapping("/")
@AllArgsConstructor
@Controller
public class EntryController {

	/*DIするサービスクラスを宣言*/
	private final UserService userservice;
	private final DateService dateservice;
	private final DateValidation dateValidation;

	/*Formの初期化と宣言*/
	@ModelAttribute("EntryForm")
	EntryForm setUpForm() {
		 EntryForm entryForm = new EntryForm();
		return entryForm;
	}

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
		mav.addObject("today",today);
		mav.setViewName("index");
		List<DateEntity> datedata = dateservice.findByWorkersId(workersUserDetails.getUsername());
		mav.addObject("DateTableData", datedata);
		mav.addObject("Luser", workersUserDetails.getUser());

		return mav;
	}

	@PostMapping
	public ModelAndView Entry(
			@Validated EntryForm entryForm,
			BindingResult result,
			ModelAndView  mav,
			@AuthenticationPrincipal WorkersUserDetails workersUserDetails)
	{
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

}
