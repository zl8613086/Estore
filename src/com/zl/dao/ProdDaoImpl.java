package com.zl.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.zl.domain.Product;
import com.zl.util.TransactionManager;

public class ProdDaoImpl implements ProdDao {

	@Override
	public void addProd(Product prod) {
		// TODO Auto-generated method stub
		String sql = "insert into products values (?,?,?,?,?,?,?)";
		try {
			QueryRunner runner = new QueryRunner(TransactionManager.getSource());
			runner.update(sql, prod.getId(), prod.getName(), prod.getPrice(),
					prod.getCategory(), prod.getPnum(), prod.getImgurl(),
					prod.getDescription());
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

	}

	@Override
	public List<Product> findAllProd() {
		String sql = "select * from products";
		try {
			QueryRunner runner = new QueryRunner(TransactionManager.getSource());
			return runner.query(sql,
					new BeanListHandler<Product>(Product.class));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Override
	public Product findProdById(String id) {
		String sql = "select * from products where id = ?";
		try {
			QueryRunner runner = new QueryRunner(TransactionManager.getSource());
			return runner.query(sql, new BeanHandler<Product>(Product.class),
					id);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Override
	public void delPnum(String product_id, int buynum) throws SQLException {
		// TODO Auto-generated method stub
		String sql = "update products set pnum = pnum-? where id = ? and pnum-?>=0";
		
			QueryRunner runner = new QueryRunner(TransactionManager.getSource());
			int count = runner.update(sql, buynum, product_id, buynum);
			if (count <= 0) {
				throw new SQLException("库存不足!");
			}
		
	}

	@Override
	public void addPnum(String product_id, int buynum) {
		// TODO Auto-generated method stub
		try {
			String sql = "update products set pnum = pnum+? where id = ? ";		
			QueryRunner runner = new QueryRunner(TransactionManager.getSource());
			runner.update(sql, buynum, product_id);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
	}

}
