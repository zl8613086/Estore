package com.zl.service;

import java.util.List;
import java.util.UUID;

import com.zl.dao.ProdDao;
import com.zl.domain.Product;
import com.zl.factory.BasicFactory;

public class ProdServiceImpl implements ProdService {
    ProdDao dao=BasicFactory.getFactory().getDao(ProdDao.class);
	@Override
	public void addProd(Product prod) {
		// TODO Auto-generated method stub
		prod.setId(UUID.randomUUID().toString());
		dao.addProd(prod);
	}
	@Override
	public List<Product> findAllProd() {
		// TODO Auto-generated method stub
		return dao.findAllProd();
	}
	@Override
	public Product findProdById(String id) {
		// TODO Auto-generated method stub
		return dao.findProdById(id);
	}

}
