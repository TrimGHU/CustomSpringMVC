package com.hugui.springmvc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Copyright © 2018 hugui. All rights reserved.
 * 
 * @Title: MyRequestParam.java
 * @Prject: custom-springmvc
 * @Package: com.hugui.springmvc.annotation
 * @Description: 参数注解类
 * @author: HuGui
 * @date: 2019年1月8日 下午5:18:01
 * @version: V1.0
 */

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyRequestParam {

	String value();

}
