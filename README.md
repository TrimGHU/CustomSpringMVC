# CustomSpringMVC
参考SpringMVC的基本原理，仿制定义一个简易MVC结构。

## SpringMVC基本原理图
![SpringMVC基本原理图](https://github.com/TrimGHU/CustomSpringMVC/blob/master/src/main/resources/static/springmvc.png?raw=true)

## 实现基本构思步骤
1. 定义入口配置文件web.xml,配置Servlet启动类，配置扫描路径。
2. 扫描包以及文件，找到相应的controller以及method，放入map中。
3. 根据url请求，从map找出相对应的类以及方法，通过反射的方式执行处理。
