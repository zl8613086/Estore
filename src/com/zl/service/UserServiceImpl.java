package com.zl.service;

import java.sql.Connection;
import java.util.Properties;
import java.util.UUID;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.dbutils.DbUtils;

import com.zl.dao.UserDao;
import com.zl.domain.User;
import com.zl.factory.BasicFactory;


public class UserServiceImpl implements UserService {
 private UserDao dao=BasicFactory.getFactory().getDao(UserDao.class);
	@Override
	public void regist(User user) {

		
		try {
			
		//1校验用户名是否存在
		if(dao.findUserByName(user.getUsername())!=null){
			throw new RuntimeException("该用户已存在");
		}
		
		//2调用dao中方法添加用户到数据库
		user.setRole("user");
		user.setState(0);
		user.setActivecode(UUID.randomUUID().toString());
		dao.addUser(user);
		//3发送激活邮件
		
			
		
		Properties prop = new Properties();
		prop.setProperty("mail.transport.protocol", "smtp");// 协议
		prop.setProperty("mail.smtp.host", "localhost");// 主机名
		prop.setProperty("mail.smtp.auth", "true");// 是否开启权限控制
		prop.setProperty("mail.debug", "true");// 如果设置为true则在发送邮件时会打印发送时的信息
		// 创建程序到邮件服务器之间的一次会话
		Session session = Session.getInstance(prop);
		Message msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress("aa@zl.com"));
		msg.setRecipients(RecipientType.TO,
				new InternetAddress[] { new InternetAddress("bb@zl.com") });
		msg.setSubject(user.getUsername()+",来自estore的激活邮件");
		msg.setText(user.getUsername()+"点击如下连接激活账户，如果不能点击请复制链接至浏览器地址栏访问：http://www.estore.com/ActiveServlet?activecode="+user.getActivecode());
		// 找到邮递员
		Transport trans = session.getTransport();
		trans.connect("aa", "123");
		trans.sendMessage(msg, msg.getAllRecipients());
		
		} catch (Exception e) {
			// TODO: handle exception
			
			e.printStackTrace();
			throw new RuntimeException("邮件发送异常");
		}
	}
	@Override
	public User active(String activecode) {
		//1.调用dao根据激活码查找用户
				User user = dao.findUserByActivecode(activecode);
				//2.如果找不到提示激活码无效
				if(user == null){
					throw new RuntimeException("激活码不正确!!!!");
				}
				//3.如果用户已经激活过,提示不要重复激活
				if(user.getState() == 1){
					throw new RuntimeException("此用户已经激活过!不要重复激活!!");
				}
				//4.如果没激活但是激活码已经超时,则提示,并删除此用户
				if(System.currentTimeMillis() - user.getUpdatetime().getTime()>1000*3600*24){
					dao.delUser(user.getId());
					throw new RuntimeException("激活码已经超时,请重新注册并在24小时内激活!");
				}
				//5.调用dao中修改用户激活状态的方法
				dao.updateState(user.getId(),1);
				return user;
	}
	@Override
	public User getUserByNameAndPsw(String username, String password) {
		
		
		return dao.getUserByNameAndPsw( username,  password);
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean hasName(String username) {
		// TODO Auto-generated method stub
		return dao.findUserByName(username)!=null;
	}

}
