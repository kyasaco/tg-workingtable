package com.example.demo.app.Calendar;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.doamin.model.Plans;
import com.example.demo.doamin.service.plans.PlansService;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
@RequestMapping("/calendar")
public class CalenderView {
	private final PlansService plansrservice;

	@GetMapping
	public ModelAndView calenderView(ModelAndView  mav) {
		List<Plans> plans_data = plansrservice.getAllPlans();
		mav.setViewName("calendar");
		mav.addObject("plans_data", plans_data);
		return mav;
	}
}
