package com.zl.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zl.domain.User;
import com.zl.factory.BasicFactory;
import com.zl.service.UserService;
import com.zl.util.MD5Utils;

public class LoginServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		UserService service=BasicFactory.getFactory().getService(UserService.class);

		String username=request.getParameter("username");
		String password=MD5Utils.md5(request.getParameter("password"));
		User user=service.getUserByNameAndPsw(username,password);
		if(user==null){
			request.setAttribute("msg", "用户名密码错误");
			request.getRequestDispatcher("/login.jsp").forward(request, response);
		}
		if(user.getState()==0){
			request.setAttribute("msg", "用户尚未激活请到邮箱中激活");
			request.getRequestDispatcher("/login.jsp").forward(request, response);
		}
		request.getSession().setAttribute("user", user);
		if("true".equals(request.getParameter("remname"))){
			Cookie remname=new Cookie("remname", URLEncoder.encode(user.getUsername(),"utf-8"));
			remname.setPath("/");
			remname.setMaxAge(3600*24*30);
			response.addCookie(remname);
		}else{
			Cookie remname=new Cookie("remname", "");
			remname.setPath("/");
			remname.setMaxAge(0);
			response.addCookie(remname);
		}
		if("true".equals(request.getParameter("autologin"))){
			Cookie autologin=new Cookie("autologin", URLEncoder.encode(user.getUsername()+":"+user.getPassword(),"utf-8"));
			autologin.setPath("/");
			autologin.setMaxAge(3600*24*30);
			response.addCookie(autologin);
		}
		response.sendRedirect("/index.jsp");
	}

	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

}
