package com.zl.util;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.dbutils.DbUtils;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class TransactionManager {
	private TransactionManager() {
	}

	// --数据源,整个程序中都只有这一个数据源
	private static DataSource source = new ComboPooledDataSource();
	// --是否开启事务的标记
	private static ThreadLocal<Boolean> isTran_local = new ThreadLocal<Boolean>() {
		@Override
		protected Boolean initialValue() {
			return false;// --最开始false,表明默认不开启事务
		}
	};
	// --保存真实连接的代理连接,改造过close方法
	private static ThreadLocal<Connection> proxyconn_local = new ThreadLocal<Connection>();
	
	private static ThreadLocal<Connection> realconn_local = new ThreadLocal<Connection>(){};

	/**
	 * ��������ķ���
	 * 
	 * @throws SQLException
	 */
	public static void startTran() throws SQLException {
		isTran_local.set(true);// --设置事务标记为true
		final Connection conn = source.getConnection();
		conn.setAutoCommit(false);
		realconn_local.set(conn);
		Connection proxyConn=(Connection) Proxy.newProxyInstance(conn.getClass().getClassLoader(), conn
				.getClass().getInterfaces(), new InvocationHandler() {

			@Override
			public Object invoke(Object proxy, Method method, Object[] args)
					throws Throwable {
				if ("close".equals(method.getName())) {
					return null;
				} else {
					return method.invoke(conn, args);
				}
			}
		});
		proxyconn_local.set(proxyConn);
	}

	

	/**
	 * �ύ����
	 */
	public static void commit() {
		DbUtils.commitAndCloseQuietly(proxyconn_local.get());
	}

	/**
	 * �ع�����
	 */
	public static void rollback() {
		DbUtils.rollbackAndCloseQuietly(proxyconn_local.get());
	}

	/**
	 * 这个方法应该做到: 如果没有开启过事务,则返回最普通的数据源
	 * 如果开启过事务,则返回一个改造过getConnection方法的数据源,这个方法改造后每次都返回同一个开启过事务的Connection
	 * 
	 * @return
	 * @throws SQLException
	 */
	public static DataSource getSource() throws SQLException {
		if (isTran_local.get()) {

			return (DataSource) Proxy.newProxyInstance(source.getClass()
					.getClassLoader(), source.getClass().getInterfaces(),
					new InvocationHandler() {

						@Override
						public Object invoke(Object proxy, Method method,
								Object[] args) throws Throwable {
							if ("getConnection".equals(method.getName())) {
								return proxyconn_local.get();
							} else {
								return method.invoke(source, args);
							}

						}
					});

		} else {
			return source;
		}

	}

	/**
	 * �ͷ���Դ
	 */
	public static void release() {
		
			DbUtils.closeQuietly(realconn_local.get());
			proxyconn_local.remove();
			isTran_local.remove();
			
			realconn_local.remove();
		
	}

}
