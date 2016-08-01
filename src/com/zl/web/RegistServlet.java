package com.zl.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;

import com.zl.domain.User;
import com.zl.factory.BasicFactory;
import com.zl.service.UserService;
import com.zl.util.MD5Utils;

public class RegistServlet extends HttpServlet {

	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		UserService service=BasicFactory.getFactory().getService(UserService.class);
		try {
			// 1校验验证码
			String valistr1 = request.getParameter("valistr");
			String valistr2 = (String) request.getSession().getAttribute(
					"valistr");
			if (valistr1 == null || valistr2 == null
					|| !valistr1.equals(valistr2)) {
				request.setAttribute("msg", "验证码不正确");
				request.getRequestDispatcher("/regist.jsp").forward(request,
						response);
				return;
			}
			// 2封装数据校验数据
			User user = new User();
			BeanUtils.populate(user, request.getParameterMap());
			user.setPassword(MD5Utils.md5(user.getPassword()));
			//3调用service注册用户
			service.regist(user);
			//4重定向回主页
			response.getWriter().write("注册成功，请到邮箱激活....");
			response.setHeader("Refresh", "3;url=/index.jsp");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e);
		}

	}

	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

}
