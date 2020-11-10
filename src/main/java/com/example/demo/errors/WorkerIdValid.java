package com.example.demo.errors;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ElementType.TYPE,ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy= SearchConditionValidator.class)
@Documented
public @interface WorkerIdValid {
	String message() default "入力値を入力してください"; //エラーメッセージ
    Class<?>[] groups() default {};            //引数に使う肩を宣言
    Class<? extends Payload>[] payload() default {}; //Payload荷重
}
