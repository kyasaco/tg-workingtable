package com.example.demo.errors;

import org.springframework.stereotype.Component;

@Component
public class PlanValidation {

	public String ErrorSwitching(int num) {
		String message = "";
		switch (num) {
		case 0:
			message = "正常に実行されました";
			break;
		case 1:
			message = "未入力です";
			break;
		case 2:
			message = "上書きされました";
			break;
		case ３:
			message = "データが存在しません";
			break;
		default:
			message = "不明なエラー";
			break;
		}
		return message;
	}


}
