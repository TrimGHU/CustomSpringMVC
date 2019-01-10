package com.hugui.springmvc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Copyright © 2018 hugui. All rights reserved.
 * 
 * @Title: MyRequestMapping.java
 * @Prject: custom-springmvc
 * @Package: com.hugui.springmvc.annotation
 * @Description: mapping注解类
 * @author: HuGui
 * @date: 2019年1月9日 下午5:53:46
 * @version: V1.0
 */

@Target({ ElementType.TYPE, ElementType.METHOD })
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface MyRequestMapping {

	String value();

}
