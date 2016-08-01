package com.zl.web;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.ProgressListener;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.zl.domain.Product;
import com.zl.domain.UploadMsg;
import com.zl.factory.BasicFactory;
import com.zl.service.ProdService;
import com.zl.util.IOUtils;
import com.zl.util.PicUtils;

public class AddprodServlet extends HttpServlet {

	public void doGet(final HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ProdService service = BasicFactory.getFactory().getService(ProdService.class);
		try {

			Map<String, String> paramMap = new HashMap<String, String>();
			//1.上传图片
			String encode = this.getServletContext().getInitParameter("encode");
			DiskFileItemFactory factory = new DiskFileItemFactory();
			factory.setSizeThreshold(1024 * 100);
			factory.setRepository(new File(this.getServletContext()
					.getRealPath("WEB-INF/temp")));

			ServletFileUpload fileUpload = new ServletFileUpload(factory);
			fileUpload.setHeaderEncoding(encode);
			//fileUpload.setFileSizeMax(1024 * 1024 * 1);
			//fileUpload.setSizeMax(1024 * 1024 * 10);
			fileUpload.setProgressListener(new ProgressListener(){
				Long beginTime = System.currentTimeMillis();
				public void update(long bytesRead, long contentLength, int items) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					UploadMsg umsg = new UploadMsg();
					BigDecimal br = new BigDecimal(bytesRead).divide(new BigDecimal(1024),2,BigDecimal.ROUND_HALF_UP);
					BigDecimal cl = new BigDecimal(contentLength).divide(new BigDecimal(1024),2,BigDecimal.ROUND_HALF_UP);
					//剩余字节数
					BigDecimal ll = cl.subtract(br);
					//上传百分比
					BigDecimal per = br.multiply(new BigDecimal(100)).divide(cl,2,BigDecimal.ROUND_HALF_UP);
					umsg.setPer(per.toString());
					//上传用时
					Long nowTime = System.currentTimeMillis();
					Long useTime = (nowTime - beginTime)/1000;
					//上传速度
					BigDecimal speed = new BigDecimal(0);
					if(useTime!=0){
						speed = br.divide(new BigDecimal(useTime),2,BigDecimal.ROUND_HALF_UP);
					}
					umsg.setSpeed(speed.toString());
					//大致剩余时间
					BigDecimal ltime = new BigDecimal(0);
					if(!speed.equals(new BigDecimal(0))){
						ltime = ll.divide(speed,0,BigDecimal.ROUND_HALF_UP);
					}
					umsg.setLtime(ltime.toString());
					request.getSession().setAttribute("umsg", umsg);
				}
			});
			if (!fileUpload.isMultipartContent(request)) {
				throw new RuntimeException("请使用正确的表单进行上传!");
			}

			List<FileItem> list = fileUpload.parseRequest(request);
			for (FileItem item : list) {
				if (item.isFormField()) {
					// 普通字段
					String name = item.getFieldName();
					String value = item.getString(encode);
					paramMap.put(name, value);
				} else {
					// 文件上传项
					String realname = item.getName();
					String uuidname = UUID.randomUUID().toString() + "_"
							+ realname;

					String hash = Integer.toHexString(uuidname.hashCode());
					String upload = this.getServletContext().getRealPath(
							"/WEB-INF/upload");
					String imgurl = "/WEB-INF/upload";
					for (char c : hash.toCharArray()) {
						upload += "/" + c;
						imgurl += "/" + c;
					}
					imgurl += "/" + uuidname;
					paramMap.put("imgurl", imgurl);

					File uploadFile = new File(upload);
					if (!uploadFile.exists())
						uploadFile.mkdirs();

					InputStream in = item.getInputStream();
					OutputStream out = new FileOutputStream(new File(upload,
							uuidname));

					IOUtils.In2Out(in, out);
					IOUtils.close(in, out);

					item.delete();
					//生成缩略图
					PicUtils pics=new PicUtils(this.getServletContext().getRealPath(imgurl));
					pics.resizeByHeight(140);

					
				}
			}
			//2.调用Service中提供的方法,在数据库中添加商品
			Product prod = new Product();
			BeanUtils.populate(prod, paramMap);
			service.addProd(prod);
			
			//3.提示成功,回到主页
			response.getWriter().write("添加商品成功!3秒回到主页..");
			response.setHeader("Refresh", "3;url=/index.jsp");
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new RuntimeException(e);
		}

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

}
