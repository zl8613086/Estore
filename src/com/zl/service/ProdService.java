package com.zl.service;

import java.util.List;

import com.zl.domain.Product;

public interface ProdService extends Service{

	/**
	 * 
	 * @param prod
	 */
	void addProd(Product prod);

	/**
	 * 
	 * @return
	 */
	List<Product> findAllProd();
/**
 * 
 * @param id
 * @return
 */
	Product findProdById(String id);

	

}
