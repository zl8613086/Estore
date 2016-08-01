package com.zl.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zl.domain.Product;
import com.zl.factory.BasicFactory;
import com.zl.service.ProdService;

public class ImgServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ProdService service = BasicFactory.getFactory().getService(ProdService.class);
		//1.获取要查询的商品id
		/*String id = request.getParameter("id");
		//2.调用Service中的方法查询制定id的商品
		Product prod = service.findProdById(id);*/
		String imgurl=request.getParameter("imgurl");
		request.getRequestDispatcher(imgurl).forward(request, response);

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

}
