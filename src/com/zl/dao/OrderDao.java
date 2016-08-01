package com.zl.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.zl.domain.Order;
import com.zl.domain.OrderItem;
import com.zl.domain.SaleInfo;

public interface OrderDao extends Dao {

	void addOrder(Order order) throws SQLException;

	void addOrderItem(OrderItem item) throws SQLException;

	/**
	 * 
	 * @param user_id
	 * @return
	 */
	List<Order> findOrderByUserId(int user_id);
/**
 * 
 * @param id
 * @return
 */
	List<OrderItem> findOrderItems(String id);
/**
 * 
 * @param order_id
 */
void delOrderItem(String order_id);
/**
 * 
 * @param order_id
 */

void delOrder(String order_id);
/**
 * 
 * @param order_id
 * @return
 */
Order findOrderById(String order_id);
/**
 * 
 * @param order
 * @param i
 */
void changePayState(String order, int i);
/**
 * 
 * @return
 */
List<SaleInfo> saleList();

}
