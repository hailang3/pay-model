package com.neo.paymodel.api.pay.service.impl;

import com.neo.paymodel.api.pay.dao.IPayDao;
import com.neo.paymodel.api.pay.entity.PayOrder;
import com.neo.paymodel.api.pay.entity.PayView;
import com.neo.paymodel.api.pay.service.IPayOrderService;
import com.neo.paymodel.common.util.RandomChars;
import net.sf.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;

@Service
public class PayOrderService implements IPayOrderService, InitializingBean {

	static final Logger logger = LoggerFactory.getLogger(PayOrderService.class);
	@Autowired
	private IPayDao payDao;
	
	private ThreadPoolExecutor settleThreadPool = new ThreadPoolExecutor(3, Integer.MAX_VALUE,
            60L, TimeUnit.SECONDS,
            new SynchronousQueue<Runnable>());
	
	private ExecutorService singleThreadExecutor = Executors
			.newSingleThreadExecutor();

	@Override
	public void afterPropertiesSet() throws Exception {

		singleThreadExecutor.execute(new Runnable() {
			
			Random random = new Random();

			@Override
			public void run() {
				while (true) {
					try {
						
						List<PayOrder> payOrders = getPayOrderUnsettled();
						for(PayOrder payOrder : payOrders){
							handlePayOrderAsync(payOrder);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					//等待 1～4分钟
					try {
						Thread.sleep( (60 + random.nextInt(180)) * 1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
	}
	
	
	private List<PayOrder> getPayOrderUnsettled(){
		//查询5分钟前未结算的3条订单
		return payDao.getPayOrderUnsettled(5, 3);
	}
	

	@Override
	public PayOrder createOrder(int userId, String userName, long orderAmount, int rebateRate,
			String channelId, String orderMachine, int orderGameId,
			int payViewId, int payMethodId, int payTypeId, int payMerchantId,
			int payChannelId, String ipAddr, int giftCoin, int goodsId) {
		return createOrder(genOrderNo(), userId, userName, orderAmount, 0, rebateRate, channelId, orderMachine, orderGameId, payViewId, payMethodId, payTypeId, payMerchantId, payChannelId,  ipAddr, giftCoin, goodsId);
	}
	
	@Override
	public PayOrder createOrder(String orderNo, int userId, String userName,
			long orderAmount, int rebateCoin, int rebateRate, String channelId,
			String orderMachine, int orderGameId, int payViewId, int payMethodId,
			int payTypeId, int payMerchantId, int payChannelId,
			String ipAddr, int giftCoin, int goodsId) {
		
		return payDao.createOrder(orderNo, userId, userName, orderAmount, rebateCoin, rebateRate, channelId, orderMachine, orderGameId, payViewId, payMethodId, payTypeId, payMerchantId, payChannelId,ipAddr, giftCoin, goodsId);
	}

	private String genOrderNo(){
        return new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + RandomChars.getRandomNumber(7);
	}


	@Override
	public PayOrder getPayOrder(String orderNo) {
		return payDao.getPayOrder(orderNo);
	}
	

	@Override
	public void updateOrderTransactionNo(String orderNo, String transactionNo) {
		payDao.updateOrderTransactionNo(orderNo, transactionNo);
	}


	@Override
	public boolean updateOrderPaySuccess(String orderNo, long payActual,
			String transactionNo, Timestamp transactionTime, String currency) {
		return payDao.updateOrderPaySuccess(orderNo, payActual, transactionNo, transactionTime, currency);
	}

	@Override
	public void updateOrderPayFailure(String orderNo, String transactionNo) {
		payDao.updateOrderPayFailure(orderNo, transactionNo);
	}

	@Override
	public void handlePayOrderAsync(final PayOrder payOrder) {
		try {
			settleThreadPool.execute(new Runnable() {
				@Override
				public void run() {
					handlePayOrder(payOrder);
				}
			});
		} catch (RejectedExecutionException reex) {
			reex.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	

	// 处理
	@Override
	@Transactional
	public void handlePayOrder(PayOrder payOrder){
		

		// 1.更新订单状态 =》完成
		String orderNo= payOrder.getOrderNo();
		
		int userId=payOrder.getUserId();
		long planAmount = payOrder.getPlanPay();
		long payActual = payOrder.getActualPay();
		int rebateRate = payOrder.getRebateRate();
		int giveCoin = payOrder.getGiveCoin();
		
		//返利金额
		int rebateCoin=payOrder.getRebateCoin();
		
		//返利比率
		if(rebateRate>0){
			rebateCoin+=payActual*rebateRate/1000;
		}
		
		long addCoin = 0;
		if(payOrder.getGoodsId() != 0) { // 官方支付金币按照plan amount 计算，第三方则按照实际支付计算
			
			addCoin = planAmount + rebateCoin + giveCoin;
		}else {
			long gameCoin=payActual/100;
			if(payOrder.getPayMethodId()==401) {// 401的ID为9
           	 payActual=payActual/100;
           	 PayView payMethodView = payDao.getPayView(9);
           	 JSONArray jsonArray = JSONArray.fromObject(payMethodView.getGoodsExpand());
    			Map<String,String> priceMap = new HashMap<String,String>();
    			for(int i=0;i<jsonArray.size();i++){
    				net.sf.json.JSONObject job = jsonArray.getJSONObject(i);  // 遍历 jsonarray 数组，把每一个对象转成 json 对象
    				//System.out.println(job.get("name")+"=") ;  // 得到 每个对象中的属性值
    				priceMap.put((String)job.get("idrPrice"),(String)job.get("gameCoin"));
    				}
    			 gameCoin = Long.valueOf(priceMap.get(String.valueOf(payActual)));
			}else if(payOrder.getPayMethodId()==403) {// 403的ID为12
	           	 PayView payMethodView = payDao.getPayView(12);
	           	 JSONArray jsonArray = JSONArray.fromObject(payMethodView.getGoodsExpand());
	    			Map<String,String> priceMap = new HashMap<String,String>();
	    			for(int i=0;i<jsonArray.size();i++){
	    				net.sf.json.JSONObject job = jsonArray.getJSONObject(i);  // 遍历 jsonarray 数组，把每一个对象转成 json 对象
	    				//System.out.println(job.get("name")+"=") ;  // 得到 每个对象中的属性值
	    				priceMap.put((String)job.get("idrPrice"),(String)job.get("gameCoin"));
	    				}
	    			 gameCoin = Long.valueOf(priceMap.get(String.valueOf(payActual)));
			}
			addCoin = gameCoin + rebateCoin + giveCoin;
		}
		
		boolean result=payDao.updateOrderSettled(orderNo, rebateCoin, addCoin);
		if(!result){
			//no...抛异常回滚
			logger.warn("订单[{}]已处理！", orderNo);
			return;
		}
		// 2.加银子，记操作日志
		logger.debug("充值订单[{}]处理,玩家[{}]增加金币数量[{}]",orderNo, userId, addCoin);
		//3. 插入玩家积分充值记录
		//4. 通知游戏服务端

	}


	@Override
	public int getGiftCoinByFirstPay(int goodsId) {
		
		return payDao.getGiftCoinByFirstPay(goodsId);
	}


	@Override
	public int IsFirstPay(int userId) {
		
		return payDao.getIsFirstPay(userId);
	}

}
