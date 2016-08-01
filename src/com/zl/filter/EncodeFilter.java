package com.zl.filter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class EncodeFilter implements Filter {
	private FilterConfig config = null;
	private ServletContext context = null;
	private String encode = null;
	private boolean isNotEncode = true;

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		response.setCharacterEncoding(encode);
		response.setContentType("text/html;charset=" + encode);
        chain.doFilter(new MyHttpServletRequest((HttpServletRequest) request), response);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		this.config = filterConfig;
		this.context = filterConfig.getServletContext();
		this.encode = context.getInitParameter("encode");
	}

	private class MyHttpServletRequest extends HttpServletRequestWrapper {
		private HttpServletRequest request = null;

		public MyHttpServletRequest(HttpServletRequest request) {
			super(request);
			this.request = request;
			// TODO Auto-generated constructor stub
		}

		@Override
		public Map<String, String[]> getParameterMap() {
			// TODO Auto-generated method stub
			try {
				if (request.getMethod().equalsIgnoreCase("post")) {
					request.setCharacterEncoding(encode);
					return request.getParameterMap();
				} else if (request.getMethod().equalsIgnoreCase("GET")) {
					Map<String, String[]> map = request.getParameterMap();
					if (isNotEncode) {
						for (Map.Entry<String, String[]> entry : map.entrySet()) {
							String[] vs = entry.getValue();
							for (int i = 0; i < vs.length; i++) {
								vs[i] = new String(vs[i].getBytes("iso8859-1"),
										encode);
							}
						}
						isNotEncode=false;
					}

					return map;

				} else {
					return request.getParameterMap();
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}

		}

		@Override
		public String getParameter(String name) {
			// TODO Auto-generated method stub
			return getParameterValues(name)==null?null:getParameterValues(name)[0];
		}

		@Override
		public String[] getParameterValues(String name) {
			// TODO Auto-generated method stub
			return getParameterMap().get(name);
		}
	}

}
