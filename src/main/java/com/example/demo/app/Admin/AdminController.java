package com.example.demo.app.Admin;

import java.sql.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.doamin.model.DateEntity;
import com.example.demo.doamin.model.User;
import com.example.demo.doamin.service.DataService.DateService;
import com.example.demo.doamin.service.user.UserService;
import com.sun.net.httpserver.Authenticator.Result;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
@RequestMapping("/Admin")
public class AdminController {
	private final DateService dateservice;
	private final UserService userservice;

	@GetMapping
	public ModelAndView getConf(ModelAndView mav) {
		mav.setViewName("Admin/AdminConfForm");
		return mav;
	}
	//Date画面へリダイレクト
	@PostMapping(params = "date")
	public ModelAndView showDate(ModelAndView mav) {
		mav.setViewName("redirect:/Admin/DeleteDate");
		return mav;
	}

	//User画面へリダイレクト
	@PostMapping(params = "user")
	public ModelAndView showUser(ModelAndView mav) {
		mav.setViewName("redirect:/Admin/ConfUser");
		return mav;
	}

/**Dateマッピング********************************************************************/

	//勤務レコードを削除
	@GetMapping({"/DeleteDate/{id}","/DeleteDate"})
	@Transactional
	public ModelAndView deleteDate(@PathVariable(name="id",required = false) Integer id,
			ModelAndView mav) {
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
	public ModelAndView getUserConf(ModelAndView mav) {
		List<User> data = userservice.findAllAsc();
		mav.addObject("user_data", data);
		mav.setViewName("Admin/AdminUserConfgure");
		return mav;
	}


}
