package com.example.demo.app.Calendar;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/calendar")
public class CalenderView {

	@GetMapping
	public ModelAndView calenderView(ModelAndView  mav) {
		mav.setViewName("calendar");
		return mav;
	}
}
