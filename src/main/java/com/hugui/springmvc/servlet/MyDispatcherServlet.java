package com.hugui.springmvc.servlet;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hugui.springmvc.annotation.MyController;
import com.hugui.springmvc.annotation.MyRequestMapping;

/**
 * Copyright © 2018 hugui. All rights reserved.
 * 
 * @Title: MyDispatcherServlet.java
 * @Prject: custom-springmvc
 * @Package: com.hugui.springmvc.servlet
 * @Description: 核心控制类
 * @author: HuGui
 * @date: 2019年1月8日 下午5:19:37
 * @version: V1.0
 */

@SuppressWarnings("serial")
public class MyDispatcherServlet extends HttpServlet {

	private static final Logger log = Logger.getLogger("MyDispatcherServlet");

	// 存放配置参数
	private static Properties properties = new Properties();

	// 模拟IOC容器
	private ConcurrentHashMap<String, Object> ioc = new ConcurrentHashMap<String, Object>();

	// 存放扫描的controller类和方法
	private ConcurrentHashMap<String, Method> handleMapping = new ConcurrentHashMap<String, Method>();
	private ConcurrentHashMap<String, Object> controllerMap = new ConcurrentHashMap<String, Object>();

	@Override
	public void init(ServletConfig config) throws ServletException {
		// ①获取properties配置文件
		String propertiesPath = config.getInitParameter("contextConfigLocation");
		try {
			properties.load(this.getClass().getClassLoader().getResourceAsStream(propertiesPath));
		} catch (IOException e) {
			log.severe(e.getMessage());
		}

		// ②获取配置文件中扫描的基本包地址
		String packagePath = properties.getProperty("scanPackage");

		// ③开启扫描
		doScanner(packagePath);

		// ④填充mapping信息
		setting();

	}

	private void setting() {
		if (ioc.isEmpty()) {
			return;
		}

		for (Entry<String, Object> entry : ioc.entrySet()) {
			Class<? extends Object> clazz = entry.getValue().getClass();
			if (clazz.isAnnotationPresent(MyRequestMapping.class)) {
				MyRequestMapping annotation = clazz.getAnnotation(MyRequestMapping.class);
				String controllerUrl = annotation.value();

				Method[] methods = clazz.getMethods();
				for (Method method : methods) {
					if (method.isAnnotationPresent(MyRequestMapping.class)) {
						MyRequestMapping methodAnnotation = method.getAnnotation(MyRequestMapping.class);
						String methodUrl = methodAnnotation.value();
						String requestUrl = (controllerUrl + "/" + methodUrl).replaceAll("/+", "/");

						try {
							controllerMap.put(requestUrl, clazz.newInstance());
						} catch (InstantiationException | IllegalAccessException e) {
							e.printStackTrace();
						}
						handleMapping.put(requestUrl, method);
						System.out.println(requestUrl + " -------- " + methodUrl);
					}
				}
			}
		}
	}

	private void doScanner(String packagePath) {
		URL url = this.getClass().getClassLoader().getResource("/" + packagePath.replaceAll("\\.", "/"));
		System.out.println(url);

		File file = new File(url.getFile());

		for (File f : file.listFiles()) {
			if (f.isFile()) {
				String className = packagePath + "." + f.getName().replace(".class", "");
				try {
					Class<?> clazz = Class.forName(className);
					if (clazz.isAnnotationPresent(MyController.class)) {
						ioc.put(toLowerFirstWord(clazz.getSimpleName()), clazz.newInstance());
					}
				} catch (Exception e) {
					log.severe(e.getMessage());
				}
			} else {
				System.out.println(packagePath + "." + f.getName());
				doScanner(packagePath + "." + f.getName());
			}
		}

	}

	private String toLowerFirstWord(String name) {
		char[] charArray = name.toCharArray();
		charArray[0] += 32;
		return String.valueOf(charArray);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doDispatcher(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doDispatcher(req, resp);
	}

	private void doDispatcher(HttpServletRequest req, HttpServletResponse resp) {
		// 项目前缀 /user
		String contextPath = req.getContextPath();
		// 请求链接 /user/search/1
		String requestURI = req.getRequestURI();

		System.out.println(contextPath + " =========== " + requestURI);

		String methodUrl = requestURI.replace(contextPath, "").replaceAll("/+", "/");
		System.out.println(methodUrl);

		if (!handleMapping.containsKey(methodUrl)) {
			try {
				resp.getWriter().println("404 PAGE NOT FOUND");
				return;
			} catch (IOException e) {
				log.log(Level.SEVERE, "404 PAGE NOT FOUND");
			}
		}

		Method method = handleMapping.get(methodUrl);

		Class<?>[] parameterTypes = method.getParameterTypes();
		Map<String, String[]> parameterMap = req.getParameterMap();
		Object[] paramObjects = new Object[parameterTypes.length];

		int i = 0;
		for (Class<?> clazz : parameterTypes) {
			String name = clazz.getSimpleName();

			if (name.equals("HttpServletRequest")) {
				paramObjects[i] = (HttpServletRequest) req;
			} else if (name.equals("HttpServletResponse")) {
				paramObjects[i] = (HttpServletResponse) resp;
			} else if (name.equals("String")) {
				for (Entry<String, String[]> param : parameterMap.entrySet()) {
					String value = Arrays.toString(param.getValue()).replaceAll("\\[|\\]", "").replaceAll(",\\s", ",");
					paramObjects[i] = value;
				}
			}

			i++;
		}

		try {
			method.invoke(controllerMap.get(methodUrl), paramObjects);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
			log.log(Level.SEVERE, e.getMessage());
		}
	}

}
