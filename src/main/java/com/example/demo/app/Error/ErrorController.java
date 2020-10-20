package com.example.demo.app.Error;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/ErrorPage")
public class ErrorController {

	@GetMapping
	public ModelAndView goError(ModelAndView mav) {
		mav.setViewName("Error404");
		return mav;
	}
}
