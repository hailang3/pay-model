package com.neo.paymodel.api.pay.service.impl;


import com.neo.paymodel.api.pay.dao.IPayDao;
import com.neo.paymodel.api.pay.entity.*;
import com.neo.paymodel.api.pay.service.IPayConfService;
import com.neo.paymodel.api.pay.util.PayConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PayConfService implements IPayConfService {

	static final Logger logger = LoggerFactory.getLogger(PayConfService.class);

	@Autowired
	private IPayDao payDao;

	@Override
	@Cacheable(value = PayConstant.CACHE,
		key = "T(com.dsgame.pay.api.util.PayConstant).PREFIX_PAY_VIEW_LIST_SORTED + #whichOne")
	public List<PayView> getPayViewListSorted(int whichOne) {
		return payDao.getPayViewListSorted(whichOne);
	}

	@Override
	@Cacheable(value = PayConstant.CACHE,
		key = "T(com.dsgame.pay.api.util.PayConstant).PREFIX_PAY_VIEW_BIND_LIST + #payViewId")
	public List<PayViewBind> getPayViewBindList(int payViewId) {
		System.out.println("开始");
		System.out.println("1");
		return payDao.getPayViewBindList(payViewId);
	}
	

	@Override
	@Cacheable(value = PayConstant.CACHE,
		key = "T(com.dsgame.pay.api.util.PayConstant).PREFIX_PAY_TYPE_LIST_SORTED")
	public List<PayType> getPayTypeListSorted() {
		return payDao.getPayTypeListSorted();
	}

	@Override
	// @Cacheable(value=PayConstant.CACHE,
	// key="T(com.dsgame.pay.api.util.PayConstant).PREFIX_PAY_METHOD + #payMethodId")
	@Cacheable(value = "PayCache", key = "'PayCache:PayMethod:'+#payMethodId")
	public PayMethod getPayMethod(int payMethodId) {
		return payDao.getPayMethod(payMethodId);
	}

	@Override
	@Cacheable(value = PayConstant.CACHE,
		key = "T(com.dsgame.pay.api.util.PayConstant).PREFIX_PAY_VIEW + #payViewId")
	public PayView getPayView(int payViewId) {
		return payDao.getPayView(payViewId);
	}
	
	@Override
	@Cacheable(value = PayConstant.CACHE,
	key = "T(com.dsgame.pay.api.util.PayConstant).PREFIX_PAY_VIEW + #whichOne + ':' + #payMethodId")
	public PayView getPayView(int payMethodId, int whichOne) {
		return payDao.getPayView(payMethodId, whichOne);
	}
	

	@Override
	@Cacheable(value = PayConstant.CACHE,
		key = "T(com.dsgame.pay.api.util.PayConstant).PREFIX_PAY_MERCHANT + #merchantCode")
	public PayMerchant getPayMerchant(String merchantCode) {
		return payDao.getPayMerchant(merchantCode);
	}

	@Override
	@Cacheable(value = PayConstant.CACHE,
		key = "T(com.dsgame.pay.api.util.PayConstant).PREFIX_PAY_MERCHANT + #payMerchantId")
	public PayMerchant getPayMerchant(int payMerchantId) {
		logger.debug("get PayMerchant[id={}] from db.", payMerchantId);
		return payDao.getPayMerchant(payMerchantId);
	}

	@Override
	@Cacheable(value = PayConstant.CACHE,
		key = "T(com.dsgame.pay.api.util.PayConstant).PREFIX_PAY_MERCHANT + #whichOne + ':' + #payChannelId")
	public PayMerchant getPayMerchant(int payChannelId, int whichOne) {
		return payDao.getPayMerchant(payChannelId, whichOne);
	}

	@Override
	@Cacheable(value = PayConstant.CACHE,
		key = "T(com.dsgame.pay.api.util.PayConstant).PREFIX_PAY_CHANNEL_METHOD + #payChannelId +':'+ #payMethodId")
	public PayChannelMethod getPayChannelMethod(int payMethodId,
			int payChannelId) {
		return payDao.getPayChannelMethod(payMethodId, payChannelId);
	}

	@Override
	@Cacheable(value = PayConstant.CACHE,
		key = "T(com.dsgame.pay.api.util.PayConstant).PREFIX_PAY_CHANNEL + #id")
	public PayChannel getPayChannel(int id) {
		return payDao.getPayChannel(id);
	}

	@Override
	@Cacheable(value = PayConstant.CACHE,
		key = "T(com.dsgame.pay.api.util.PayConstant).PREFIX_PAY_METHOD_INSTANCE_MAP +#payMethodId ")
	public Map<String, PayMethodInstance> getPayMethodInstanceMap(
			int payMethodId) {

		Map<String, PayMethodInstance> payInstanceMap = new HashMap<String, PayMethodInstance>();
		List<PayMethodInstance> payMethodInstances = payDao
				.getPayMethodInstanceList(payMethodId);
		for (PayMethodInstance payInstance : payMethodInstances) {
			// 状态开启的支付方式实例都会有权重，默认为1
			payInstance.setWeight(1);
			payInstanceMap.put(String.valueOf(payInstance.getId()), payInstance);
		}
		return payInstanceMap;
	}


}
