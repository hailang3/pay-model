package com.neo.paymodel.api.pay.dao;


import com.neo.paymodel.api.pay.entity.DiscountInfo;
import com.neo.paymodel.api.pay.entity.StoreInfo;

public interface DiscountPayDao {
	
	
	    DiscountInfo getDiscountInfo(int user_id);
		//更改用户下单打折状态
		boolean updateDiscountInfo(DiscountInfo discountInfo);
		StoreInfo getStoreInfoById(int goodsId);

}
