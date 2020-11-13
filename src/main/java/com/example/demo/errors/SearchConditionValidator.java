package com.example.demo.errors;

import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.example.demo.app.Admin.forms.AdminDeleteForm;
import com.example.demo.doamin.service.DataService.DateService;
import com.example.demo.doamin.service.user.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SearchConditionValidator implements ConstraintValidator<WorkerIdValid, AdminDeleteForm> {
	private WorkerIdValid wiv;
	private final UserService usersevice;

	@Override
	public void initialize(WorkerIdValid annotaion) {
		this.wiv = annotaion;
	}

	@Override
	public boolean isValid(AdminDeleteForm value, ConstraintValidatorContext context) {
		// TODO 自動生成されたメソッド・スタブ
		List<String> userid_list = usersevice.getNowUserIds();
		if(value.getUserid() != "") {
			if(userid_list.indexOf(value.getUserid()) == -1) {
				return false;
			}
		}
		return true;
	}

}
