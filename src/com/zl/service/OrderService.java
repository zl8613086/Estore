package com.zl.service;

import java.util.List;


import com.zl.annotation.Tran;
import com.zl.domain.Order;
import com.zl.domain.OrderListForm;
import com.zl.domain.SaleInfo;


public interface OrderService extends Service {
	/**
	 * 
	 * @param order
	 */
@Tran
	void addOrder(Order order);
/**
 * 
 * @param id
 * @return
 */
	List<OrderListForm> findOrders(int id);
	/**
	 * 
	 * @param id
	 */
	@Tran
void delOrderByID(String id);
	/**
	 * 
	 * @param p2_Order
	 * @return
	 */
	Order findOrderById(String p2_Order);
	/**
	 * 
	 * @param r6_Order
	 * @param i
	 */
	void changePayState(String r6_Order, int i);
	/**
	 * 
	 * @return
	 */
	List<SaleInfo> saleList();

}
