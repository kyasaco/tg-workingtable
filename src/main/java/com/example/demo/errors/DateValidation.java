package com.example.demo.errors;

import org.springframework.stereotype.Component;

@Component
public class DateValidation {

	public String ErrorSwitching(int num) {
		String message = "";
		switch (num) {
		case 0:
			message = "正常に実行されました";
			break;
		case 1:
			message =  "時間エラー";
			break;
		case 2:
			message = "上書きされました";
			break;
		case 3:
			message = "休日です";
			break;
		case 4:
			message = "データが存在しません";
			break;
		case 5:
			message = "未入力です";
			break;
		default:
			break;
		}
		return message;
	}


}
