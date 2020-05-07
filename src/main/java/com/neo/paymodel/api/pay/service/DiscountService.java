package com.neo.paymodel.api.pay.service;

import com.neo.paymodel.api.pay.dao.DiscountPayDao;
import com.neo.paymodel.api.pay.entity.DiscountInfo;
import com.neo.paymodel.api.pay.entity.PayOrder;
import com.neo.paymodel.api.pay.entity.StoreInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DiscountService {
	
	@Autowired
	private DiscountPayDao discountPayDao;
	
	public DiscountInfo getDiscountInfo(int user_id) {
		return discountPayDao.getDiscountInfo(user_id);
		
	}
	public StoreInfo getStoreInfoById(int goodsId) {
		return discountPayDao.getStoreInfoById(goodsId);
	}
}
