package com.zl.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zl.factory.BasicFactory;
import com.zl.service.OrderService;

public class DelOrderServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		OrderService service=BasicFactory.getFactory().getService(OrderService.class);
		//1获取订单id
		String id=request.getParameter("id");
		//2调用service中根据订单id删除订单的方法
		service.delOrderByID(id);
		//3回到订单列表项
		response.getWriter().write("订单删除成功!!!");
		response.setHeader("Refresh", "3;url=/OrderListServlet");

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

}
