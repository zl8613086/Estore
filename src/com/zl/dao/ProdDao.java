package com.zl.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.zl.domain.Product;

public interface ProdDao extends Dao{

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
/**
 * 
 * @param product_id
 * @param buynum
 * @throws SQLException 
 */

void delPnum(String product_id, int buynum) throws SQLException;
/**
 * 
 * @param product_id
 * @param buynum
 */
void addPnum(String product_id, int buynum);

}
