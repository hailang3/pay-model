package com.neo.paymodel.api.pay.channel;

import com.neo.paymodel.api.pay.entity.PayMerchant;
import com.neo.paymodel.api.pay.entity.PayOrder;
import com.neo.paymodel.api.pay.entity.RetModel;
import com.neo.paymodel.api.pay.entity.BankInfo;
import com.neo.paymodel.api.pay.entity.PaySubmitRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface IPayChannelApi {
	/**
	 * 提交订单
	 * @param paySubmitReq
	 * @param resp
	 */
	void submitOrder(PaySubmitRequest paySubmitReq, HttpServletResponse resp);
	/**
	 * 查询订单
	 * @param payOrder
	 * @return
	 */
	int checkOrder(PayOrder payOrder);
	/**
	 * 处理回调
	 * @param notifyReq
	 * @param notifyResp
	 * @param payMerchant
	 */
	void handleNotify(HttpServletRequest notifyReq, HttpServletResponse notifyResp, PayMerchant payMerchant);
	/**
	 * 获取银行列表
	 * @param payChannelId
	 * @return
	 */
	RetModel<List<BankInfo>> getBankInfo(int payChannelId);
	
}
