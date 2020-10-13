### 重定向redirect:
 第一种方式：controller中返回值为String
 ````java
 public String login(HttpServletRequest req, HttpServletResponse resp)
 return "redirect:http://localhost:8080/index";
 ````
 
 第二种方式：controller中返回值为void
 ````java
 public void login(HttpServletRequest req, HttpServletResponse resp)
 resp.sendRedirect("http://localhost:8080/index");
````
      
 第三种方式：controller中返回值为ModelAndView
 ````
 return new ModelAndView("redirect:/toList");
 ````