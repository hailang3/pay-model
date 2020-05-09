package com.neo.paymodel.api.pay.controller;

import com.alibaba.fastjson.JSONObject;
import com.neo.paymodel.api.pay.channel.PayChannelApiManager;
import com.neo.paymodel.api.pay.entity.*;
import com.neo.paymodel.api.pay.service.*;
import com.neo.paymodel.common.util.PayConstant;
import com.neo.paymodel.common.util.RequestParamsUtil;
import com.neo.paymodel.common.util.TypeException;
import com.neo.paymodel.common.util.HttpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("/pay")
public class PayController {

	@Autowired
	PayService payService;
	@Autowired
	IPayConfService payConfService;
	@Autowired
	IPayOrderService payOrderService;
	@Autowired
	PayChannelApiManager payChannelApiManager;
	// 拉取充值配置
	@ResponseBody
	@RequestMapping(value = "/config")
	public RetModel<List<PayTypeVo>> getPayConfig(HttpServletRequest request, HttpServletResponse response,
												  PayConfigRequest payConfigReq) {
		String channelId = payConfigReq.getChannelId().trim();
		// 获取充值界面需要的配置
		List<PayTypeVo> payTypeVos = payService.getPayTypeVoList();
		//
		if (payTypeVos == null || payTypeVos.size() == 0) {
			return new RetModel<List<PayTypeVo>>(1, "当前没有充值通道可用");
		}
		List<Map<String, Object>> bankInfos = payService.getPayBankInfos();

		RetModel<List<PayTypeVo>> result = new RetModel<List<PayTypeVo>>(payTypeVos);
		result.setBankInfos(bankInfos);
		return result;
	}
	//拉去银行配置，特定支付渠道需要
	@ResponseBody
	@RequestMapping("/getBankInfo")
	public RetModel<List<BankInfo>> getBankInfo(HttpServletRequest request, HttpServletResponse response, int payChannelId){
		return payChannelApiManager.getBankInfo(payChannelId);

	}
	// 创建充值订单
	@ResponseBody
	@RequestMapping("/order")
	public RetModel<Map<String, Object>> createOrder(HttpServletRequest req, HttpServletResponse resp,
                                                     PayOrderRequest payOrderReq) {
		// 验证签名
		// 验证参数
		int payViewId = payOrderReq.getViewId();
		PayView payMethodView = payConfService.getPayView(payViewId);
		String channelId = payOrderReq.getChannelId();
		int userId = payOrderReq.getUserId();
		Long orderAmount = payOrderReq.getOrderAmount(); // 单位：分
		String orderMachine = payOrderReq.getMachineSerial();
		int orderGameId = payOrderReq.getOrderGameId();
		int goodsId = payOrderReq.getGoodsId();
		String userName = payOrderReq.getUserName();
//		String bankCode = payOrderReq.getBankCode();
		// 获取商品配置
		// StoreInfo storeInfo = discountService.getStoreInfoById(goodsId);
		// 获取渠道配置

		// 判断是否首次充值，若为首次充值giftCoin不为0
		int giftCoin = 0;
		if (payOrderReq.getDiscountCoin() != 0) {
			if (JudgeDiscountStatus(goodsId, userId) == 1) {
				giftCoin = payOrderReq.getDiscountCoin();
			}
		}
		// 黑名单判断
		// 充值点击频率判断
		// 充值金额判断
		// 获取充值渠道配置
		PayMethodInstance payInstance = payService.filterPayMethodInstance(orderAmount, payViewId,
				payMethodView.getPayMethodId(), payMethodView.isBolFixed());
		// 创建本地订单
		PayOrder payOrder = payOrderService.createOrder(userId, userName, orderAmount, payMethodView.getRebateRate(),
				channelId, orderMachine, orderGameId, payMethodView.getId(), payInstance.getPayMethodId(),
				payInstance.getPayTypeId(), payInstance.getPayMerchantId(), payInstance.getPayChannelId(),
				HttpUtil.getIpAddr(req), giftCoin, goodsId);

		if (payOrder == null) {
			return new RetModel<Map<String, Object>>(2, "interal-error");
		}

		String submitUrl = null;
		try {
			submitUrl = payService.buildSubmitUrl(payOrder.getOrderNo(), req);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(submitUrl);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("submitWay", payInstance.getSubmitWay());
		data.put("submitUrl", submitUrl);
		data.put("orderNo", payOrder.getOrderNo());
		return new RetModel<Map<String, Object>>(data);
	}

	/**
	 * 提交订单到充值渠道
	 * @param req
	 * @param resp
	 * @throws IOException
	 */
	@RequestMapping("/pay")
	public void payOrder(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		// 解析参数获得payOrder
		PaySubmitRequest paySubmitReq = null;
		try {
			paySubmitReq = RequestParamsUtil.formObjectFromRequest(req, PaySubmitRequest.class);
		} catch (TypeException e) {
			e.printStackTrace();
			resp.getWriter().write("请求参数错误");
		}

		payChannelApiManager.submitOrder(paySubmitReq, resp);
	}

	// 充值渠道回调
	@ResponseBody
	@RequestMapping("/notify/{mechantCode}")
	public void payNotify(@PathVariable String mechantCode, HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		payChannelApiManager.handleNotify(mechantCode, req, resp);
	}

	// 充值渠道回调
	@ResponseBody
	@RequestMapping("/thirdPayNotify/{mechantCode}")
	public void thirdPayNotify(@PathVariable String mechantCode, HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		payChannelApiManager.handleNotify(mechantCode, req, resp);
	}
	// FlashPay充值渠道回调
	@RequestMapping("/flashPayNotify")
	@ResponseBody
	public void flashPayNotify(String appId, HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		payChannelApiManager.handleNotify(appId, req, resp);
	}

	@ResponseBody
	@RequestMapping("/check")
	public RetModel<Map<String, Object>> payCheck(HttpServletRequest req, HttpServletResponse resp) {
		PayCheckRequest payCheckReq = null;
		try {
			payCheckReq = RequestParamsUtil.formObjectFromRequest(req, PayCheckRequest.class);
		} catch (TypeException e) {
			e.printStackTrace();
			return new RetModel<>(-1, "error");
		}

		String orderNo = payCheckReq.getOrderNo();
		PayOrder payOrder = payOrderService.getPayOrder(orderNo);

		// 谷歌等官方支付参数设定，其余正常支付对应参数为null
		payOrder.setTransactionNo(payCheckReq.getTransactionNo());
		payOrder.setAppMame(payCheckReq.getPackageName());
		if (payCheckReq.getCurrency().equals(PayConstant.USD)) {
			payOrder.setActualPay(new BigDecimal(payCheckReq.getActualPay())
					.multiply(new BigDecimal(PayConstant.PAY_MULTIPLE)).longValue());
		} else {
			payOrder.setActualPay(new BigDecimal(payCheckReq.getActualPay()).longValue());
		}
		payOrder.setCurrency(payCheckReq.getCurrency());
		if (payCheckReq.getPackageName() != null && !payCheckReq.getPackageName().equals("")) {
			JSONObject obj = new JSONObject();
			obj.put("packageName", payCheckReq.getPackageName());
			obj.put("productId", payCheckReq.getProductId());
			obj.put("token", payCheckReq.getToken());
			payOrder.setExtend(obj);
			System.out.println(payOrder.getExtend());
		}

		if (payOrder == null || payOrder.getUserId() != payCheckReq.getUserId()) {
			return new RetModel<>(-2, "订单号错误");
		}

		if (payOrder.getStatus() == PayOrder.PAY_FAIL) {
			return new RetModel<>(2, "玩家支付失败！");
		}

		if (payOrder.getStatus() == PayOrder.PAY_SUCCESS) {
			return new RetModel<>(3, "支付成功，在结算！");
		}

		if (payOrder.getStatus() == PayOrder.ORDER_SETTLED) {

			long gameCoin = 0L;
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("gameCoin", gameCoin);
			return new RetModel<Map<String, Object>>(data);
		}

		int orderStatus = payChannelApiManager.checkOrder(payOrder);

		if (orderStatus == PayOrder.PAY_WAITING) {
			return new RetModel<>(1, "订单尚未支付！");
		} else if (orderStatus == PayOrder.PAY_FAIL) {
			return new RetModel<>(2, "支付失败！");
		} else if (orderStatus == PayOrder.PAY_SUCCESS) {
			return new RetModel<>(3, "支付成功，在结算！");
		} else if (orderStatus == PayOrder.ORDER_SETTLED) {
			return new RetModel<Map<String, Object>>();
		} else {
			return new RetModel<>(-3, "server-error!");
		}

	}

	public int JudgeDiscountStatus(int goodsId, int user_id) {
			return 0;
	}

}
