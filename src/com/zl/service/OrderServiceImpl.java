package com.zl.service;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

import com.zl.dao.OrderDao;
import com.zl.dao.ProdDao;
import com.zl.dao.UserDao;
import com.zl.domain.Order;
import com.zl.domain.OrderItem;
import com.zl.domain.OrderListForm;
import com.zl.domain.Product;
import com.zl.domain.SaleInfo;
import com.zl.domain.User;
import com.zl.factory.BasicFactory;

public class OrderServiceImpl implements OrderService {
	OrderDao orderDao = BasicFactory.getFactory().getDao(OrderDao.class);
	ProdDao prodDao = BasicFactory.getFactory().getDao(ProdDao.class);
	UserDao userDao = BasicFactory.getFactory().getDao(UserDao.class);
	@Override
	public void addOrder(Order order) {
		// TODO Auto-generated method stub
		
		try{
			
			
			//1.生成订单
			orderDao.addOrder(order);
			//2.生成订单项/扣除商品数量
			for(OrderItem item : order.getList()){
				orderDao.addOrderItem(item);
				prodDao.delPnum(item.getProduct_id(),item.getBuynum());
			}
			
		}catch (Exception e) {
			e.printStackTrace();
			
			throw new RuntimeException(e);
		}
	}
	@Override
	public List<OrderListForm> findOrders(int user_id) {
		try{

			List<OrderListForm> olfList = new ArrayList<OrderListForm>();
			//1.根据用户id查询这个id用户的所有订单
			List<Order> list = orderDao.findOrderByUserId(user_id);
			//2.遍历订单 生成orderListForm对象,存入List中
			for(Order order : list){
				//--设置订单信息
				OrderListForm olf = new OrderListForm();
				BeanUtils.copyProperties(olf, order);
				//--设置用户名
				User user = userDao.findUserById(order.getUser_id());
				olf.setUsername(user.getUsername());
				//--设置商品信息
				//----查询当前订单所有订单项
				List<OrderItem> itemList = orderDao.findOrderItems(order.getId());
				//----遍历所有订单项,获取商品id,查找对应的商品,存入list
				Map<Product,Integer> prodMap = new HashMap<Product,Integer>();
				for(OrderItem item : itemList){
					String prod_id = item.getProduct_id();
					Product prod = prodDao.findProdById(prod_id);
					prodMap.put(prod, item.getBuynum());
				}
				olf.setProdMap(prodMap);
				
				olfList.add(olf);
			}
			
			return olfList;
		}catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	@Override
	public void delOrderByID(String id) {
		// TODO Auto-generated method stub
		//1根据id查询出所有订单项
		List<OrderItem> list=orderDao.findOrderItems(id);
		
		//2遍历订单项，将对应prod_id对应库存加上去
		for(OrderItem item:list){
			prodDao.addPnum(item.getProduct_id(),item.getBuynum());
		}
		//3删除订单项
		orderDao.delOrderItem(id);
		
		//4删除订单
		orderDao.delOrder(id);
	}
	@Override
	public Order findOrderById(String order_id) {
		return orderDao.findOrderById(order_id);
	}
	@Override
	public void changePayState(String order, int i) {
		orderDao.changePayState(order,i);
		
	}
	@Override
	public List<SaleInfo> saleList() {
		// TODO Auto-generated method stub
		return orderDao.saleList();
	}

}
