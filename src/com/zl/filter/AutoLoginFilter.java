package com.zl.filter;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zl.domain.User;
import com.zl.factory.BasicFactory;
import com.zl.service.UserService;

public class AutoLoginFilter implements Filter {

	@Override
	public void init(FilterConfig paramFilterConfig) throws ServletException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest paramServletRequest,
			ServletResponse paramServletResponse, FilterChain paramFilterChain)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		HttpServletRequest req=(HttpServletRequest) paramServletRequest;
		HttpServletResponse resp=(HttpServletResponse) paramServletResponse;
		if(req.getSession(false)==null||req.getSession().getAttribute("user")==null){
			Cookie [] cs=req.getCookies();
			Cookie findC=null;
			for(Cookie c:cs){
				if("autologin".equals(c.getName())){
					findC=c;
					break;
				}
			}
			if(findC!=null){
				String v=URLDecoder.decode(findC.getValue(), "utf-8");
				String username=v.split(":")[0];
				String password=v.split(":")[1];
				UserService service = BasicFactory.getFactory().getService(UserService.class);
				User user = service.getUserByNameAndPsw(username, password);
				if(user!=null){
					req.getSession().setAttribute("user", user);
				}
			}
		}
		
		paramFilterChain.doFilter(paramServletRequest, paramServletResponse);
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
