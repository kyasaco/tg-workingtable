package com.example.demo.app.login;


import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.context.annotation.SessionScope;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.doamin.model.DateEntity;
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
	@PostMapping(value="/insert")
	ModelAndView InsertGoForm(
			ModelAndView mav,
			@Validated @ModelAttribute("InsertForm")InsertForm insertForm,
			BindingResult result) {
		mav.setViewName("insertForm");
		if(!result.hasErrors()) {
			mav.setViewName("redirect:/login/loginForm");
			userservice.InsertUser(insertForm);
		}
		return mav;
	}

}
