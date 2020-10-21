package com.example.demo.app.login;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.doamin.service.user.UserService;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class LoginController {
	private final UserService userservice;

	@ModelAttribute("InsertForm")
	InsertForm setUpInsertform() {
		return new InsertForm();
	}

	@GetMapping("loginForm")
	String loginForm() {
		return "login/loginForm";
	}

	@GetMapping("insertForm")
	ModelAndView InsertForm(ModelAndView mav) {
		mav.setViewName("insertForm");
		return mav;
	}
	@RequestMapping(value="insert",method = RequestMethod.POST)
	ModelAndView InsertGoForm(
			ModelAndView mav,
			@ModelAttribute("InsertForm")InsertForm insertForm) {
		mav.setViewName("login/loginForm");
		userservice.InsertUser(insertForm);
		return mav;
	}

}
