package com.hugui.springmvc.contoller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hugui.springmvc.annotation.MyController;
import com.hugui.springmvc.annotation.MyRequestMapping;

/**
 * Copyright © 2018 hugui. All rights reserved.
 * 
 * @Title: TestController.java
 * @Prject: custom-springmvc
 * @Package: com.hugui.springmvc.contoller
 * @Description: 测试控制器类
 * @author: HuGui
 * @date: 2019年1月10日 下午2:01:49
 * @version: V1.0
 */

@MyController
@MyRequestMapping("/test")
public class TestController {

	@MyRequestMapping("/sayhi")
	public void sayhi(HttpServletRequest req, HttpServletResponse resp) {
		try {
			resp.getWriter().print("this is test!!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
