package com.zl.factory;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Properties;

import com.zl.annotation.Tran;
import com.zl.dao.Dao;
import com.zl.service.Service;
import com.zl.util.TransactionManager;

public class BasicFactory {
	private static BasicFactory factory = new BasicFactory();
	private static Properties prop = null;
	private BasicFactory() {}
    static{
    	try {
    		prop=new Properties();
			prop.load(new FileReader(BasicFactory.class.getClassLoader().getResource("config.properties").getPath()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e);
		}
    }
	public static BasicFactory getFactory() {
		return factory;

	}

	public static <T extends Dao> T getDao(Class<T> clazz) {
		try {
			String infName=clazz.getSimpleName();
			String implName=prop.getProperty(infName);
			return (T) Class.forName(implName).newInstance();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
	}

	@SuppressWarnings("unchecked")
	public static <T extends Service> T getService(Class<T> clazz) {
		try {
			String infName=clazz.getSimpleName();
			String implName=prop.getProperty(infName);
			final T service= (T) Class.forName(implName).newInstance();
			T proxyService=(T) Proxy.newProxyInstance(service.getClass().getClassLoader(), service.getClass().getInterfaces(), new InvocationHandler() {
				
				@Override
				public Object invoke(Object proxy, Method method, Object[] args)
						throws Throwable {
					if(method.isAnnotationPresent(Tran.class)){
						try {
							TransactionManager.startTran();//--开启事务
							
							Object obj = method.invoke(service, args);//--真正执行方法
							
							TransactionManager.commit();//--提交事务
							return obj;
							
						}catch (InvocationTargetException e) {
							TransactionManager.rollback();
							throw new RuntimeException(e.getTargetException());
						} catch (Exception e) {
							TransactionManager.rollback();
							throw new RuntimeException(e);
						}finally{
							TransactionManager.release();
						}
					}else{
						return method.invoke(service, args);
					}
					
				}
			});
			
			
			
			return proxyService;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
	}
}