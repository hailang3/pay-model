package com.neo.paymodel.api.pay.controller;

import com.alibaba.fastjson.JSONObject;
import com.neo.paymodel.api.pay.channel.PayChannelApiManager;
import com.neo.paymodel.api.pay.entity.PayMethodInstance;
import com.neo.paymodel.api.pay.entity.PayOrder;
import com.neo.paymodel.api.pay.entity.PayView;
import com.neo.paymodel.api.pay.service.*;
import com.neo.paymodel.common.util.PayConstant;
import com.neo.paymodel.common.util.RequestParamsUtil;
import com.neo.paymodel.common.util.TypeException;
import com.neo.paymodel.api.pay.web.model.RetModel;
import com.neo.paymodel.api.pay.web.vo.*;
import com.neo.paymodel.common.util.HttpUtil;
import net.sf.json.JSONArray;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
		// int payAmountTotal=payConfigReq.getVipGrade();
		// 验证参数

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
		// int payMethodId=payOrderReq.getMethodId();
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
		boolean isOrderAmountValid = false;
		

		boolean bolFixed = payMethodView.isBolFixed();
		/*
		 * 重写金额验证逻辑
		 */
		if(StringUtils.isBlank(payMethodView.getGoodsExpand())) {
			if(bolFixed){
				String[] amountArr = payMethodView.getAmounts().split(",");
				if(Arrays.asList(amountArr).contains(String.valueOf(orderAmount))){
					isOrderAmountValid=true;
				}
			}else{
				if(!( (payMethodView.getMinAmount()!=null&& payMethodView.getMinAmount() > orderAmount) ||
					(payMethodView.getMaxAmount()!=null && orderAmount >payMethodView.getMaxAmount()) )){
                     isOrderAmountValid=true;
					}
			}
		}else {
			JSONArray jsonArray = JSONArray.fromObject(payMethodView.getGoodsExpand());
			Set<Long> priceSet = new HashSet<Long>();
			for(int i=0;i<jsonArray.size();i++){
				net.sf.json.JSONObject job = jsonArray.getJSONObject(i);  // 遍历 jsonarray 数组，把每一个对象转成 json 对象
				//System.out.println(job.get("name")+"=") ;  // 得到 每个对象中的属性值
				priceSet.add(Long.valueOf((String)job.get("idrPrice")));
				}
			if(priceSet.contains(orderAmount)) {
				isOrderAmountValid=true;
			}
		}

		  if(!isOrderAmountValid){
			  //返回错误
			  return new RetModel<Map<String,Object>>(1,  "订单金额错误");
		  }


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
//		data.put("bankCode", bankCode);
		return new RetModel<Map<String, Object>>(data);
	}

	/**
	 * 提交订单到充值渠道
	 *
	 * @param req
	 * @param resp
	 * @throws IOException
	 */
	@RequestMapping("/pay")
	public void payOrder(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//		if(!payService.checkPayRequest(req)){
//			WebUtil.write(resp, "请求参数错误0");
//		}

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

//		if(!payService.checkPayRequest(req)){
//			return new RetModel<Void>(0, "请求参数错误0");
//		}

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
			return new RetModel<>(2, "玩家支付失败！");
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
