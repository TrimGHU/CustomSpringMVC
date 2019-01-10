package com.hugui.springmvc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Copyright © 2018 hugui. All rights reserved.
 * 
 * @Title: MyController.java
 * @Prject: custom-springmvc
 * @Package: com.hugui.springmvc.annotation
 * @Description: 控制器注解类
 * @author: HuGui
 * @date: 2019年1月8日 下午5:16:30
 * @version: V1.0
 */

@Target(ElementType.TYPE)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface MyController {

	String value() default "";

}
