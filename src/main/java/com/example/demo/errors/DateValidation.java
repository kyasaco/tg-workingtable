package com.example.demo.errors;

import org.springframework.stereotype.Component;

@Component
public class DateValidation {

	public String ErrorSwitching(int num) {
		String message = "";
		switch (num) {
		case 0:
			message = "登録成功！";
			break;
		case 1:
			message =  "時間エラー";
			break;
		case 2:
			message = "上書きされました";
			break;
		default:
			message = "不明なエラー";
			break;
		}
		return message;
	}


}
