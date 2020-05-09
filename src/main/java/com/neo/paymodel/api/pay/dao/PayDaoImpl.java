package com.neo.paymodel.api.pay.dao;

import com.neo.paymodel.api.pay.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Repository
public class PayDaoImpl implements IPayDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	
	@Override
	public PayOrder createOrder(String orderNo, int userId, String userName, long orderAmount,
								int rebateCoin, int rebateRate, String channelId, String orderMachine, int orderGameId,
								int payViewId, int payMethodId, int payTypeId, int payMerchantId,
								int payChannelId,  String ipAddr, int giftCoin, int goodsId) {

		
		String sql="insert into plat_pay_db.pay_order_record ("
				+ " order_no, user_id, user_name, plan_pay, rebate_coin, rebate_rate, give_coin, "
				+ " status, create_time, order_machine, order_game_id, order_ip, channel_id, which_one, "
				+ " pay_view_id, pay_merchant_id, pay_channel_id, pay_method_id, pay_type_id, goods_id, currency"
				+ " ) values ("
				+ " ?, ?, ?, ?, ?, ?, ?,"
				+ " 0, now(), ?, ?, ?, ?, ?, "
				+ " ?, ?, ?, ?, ?, ?, 'IDR')";
		
		int effectRowCount=0;
		try {
			effectRowCount = jdbcTemplate.update(sql, orderNo, userId, userName, orderAmount, rebateCoin, rebateRate, giftCoin,
					orderMachine, orderGameId, ipAddr, channelId, payViewId, payMerchantId, payChannelId, payMethodId, payTypeId, goodsId);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		
		if(effectRowCount==0){
			return null;
		}
		
		PayOrder payOrder = new PayOrder();
		payOrder.setOrderNo(orderNo);
		payOrder.setUserId(userId);
		payOrder.setPlanPay(orderAmount);
		payOrder.setRebateCoin(rebateCoin);
		payOrder.setRebateRate(rebateRate);
		payOrder.setCreateTime(new Timestamp(System.currentTimeMillis()));
		payOrder.setOrderMachine(orderMachine);
		payOrder.setOrderIp(ipAddr);
		payOrder.setChannelId(channelId);
		payOrder.setPayViewId(payViewId);
		payOrder.setPayMerchantId(payMerchantId);
		payOrder.setPayChannelId(payChannelId);
		payOrder.setPayMethodId(payMethodId);
		payOrder.setPayTypeId(payTypeId);
		payOrder.setGiveCoin(giftCoin);
		payOrder.setCurrency("IDR");
		
		return payOrder;
	}


	@Override
	public PayOrder getPayOrder(String orderNo) {
		
		String sql="select * from plat_pay_db.pay_order_record where order_no=?";
		List<PayOrder> payOrders = jdbcTemplate.query(sql, new BeanPropertyRowMapper<PayOrder>(PayOrder.class), orderNo);
		if(payOrders.size()>0){
			return payOrders.get(0);
		}
		return null;
	}

	
	@Override
	public void updateOrderTransactionNo(String orderNo, String transactionNo) {
		String sql="update plat_pay_db.pay_order_record set transaction_no=? where status=0 and transaction_no is null and order_no=?";
		jdbcTemplate.update(sql, transactionNo, orderNo);
	}


	@Override
	public boolean updateOrderPaySuccess(String orderNo, long payActual,
			String transactionNo, Timestamp transactionTime, String currency) {
		
		String sql="update plat_pay_db.pay_order_record "
				+ " set actual_pay=?, "
				+ " transaction_no=?, "
				+ " transaction_time=?, "
				+ " notify_time=now() , "
				+ " crossday_flag=(CASE WHEN TIMESTAMPDIFF(day, create_time, now()) = 0 THEN 0 ELSE 1 END), "
				+ " status=1 ,"
				+ " currency = ?"
				+ " where status=0 and order_no=?";
		int effectRowCount = jdbcTemplate.update(sql, payActual, transactionNo, transactionTime, currency, orderNo);
		return effectRowCount>0;
	}

	@Override
	public void updateOrderPayFailure(String orderNo, String transactionNo) {
		String sql="update plat_pay_db.pay_order_record set transaction_no=?, status=-1 , notify_time=now(), handle_time=now() where status=0 and order_no=?";
		jdbcTemplate.update(sql, transactionNo, orderNo);
	}

	@Override
	public boolean updateOrderSettled(String orderNo, int rebateCoin, long addCoin) {
		String sql="update plat_pay_db.pay_order_record set status=2, rebate_coin=?, add_coin=?, handle_time=now() where status=1 and order_no=?";
		int effectRowCount = jdbcTemplate.update(sql, rebateCoin, addCoin, orderNo);
		return effectRowCount>0;
	}
	

	@Override
	public PayMerchant getPayMerchant(int id) {
		String sql="select * from plat_pay_db.config_pay_merchant where id=?";
		List<PayMerchant> payMerchants = jdbcTemplate.query(sql, new BeanPropertyRowMapper<PayMerchant>(PayMerchant.class), id);
		if(payMerchants.size()>0){
			return payMerchants.get(0);
		}
		return null;
	}

	@Override
	public PayMerchant getPayMerchant(String merchantCode) {
		String sql="select * from plat_pay_db.config_pay_merchant where merchant_code=?";
		List<PayMerchant> payMerchants = jdbcTemplate.query(sql, new BeanPropertyRowMapper<PayMerchant>(PayMerchant.class), merchantCode);
		if(payMerchants.size()>0){
			return payMerchants.get(0);
		}
		return null;
	}

	@Override
	public PayMerchant getPayMerchant(int payChannelId, int whichOne) {
		String sql="select * from plat_pay_db.config_pay_merchant where pay_channel_id=? and which_one=?";
		List<PayMerchant> payMerchants = jdbcTemplate.query(sql, new BeanPropertyRowMapper<PayMerchant>(PayMerchant.class), payChannelId, whichOne);
		if(payMerchants.size()>0){
			return payMerchants.get(0);
		}
		return null;
	}


	@Override
	public PayChannel getPayChannel(int id) {
		String sql="select * from plat_pay_db.config_pay_channel where id=?";
		List<PayChannel> payChannels = jdbcTemplate.query(sql, new BeanPropertyRowMapper<PayChannel>(PayChannel.class), id);
		if(payChannels.size()>0){
			return payChannels.get(0);
		}
		return null;
	}

	
	@Override
	public PayMethod getPayMethod(int id) {
		String sql="select * from plat_pay_db.config_pay_method where id=?";
		List<PayMethod> payMethods = jdbcTemplate.query(sql, new BeanPropertyRowMapper<PayMethod>(PayMethod.class), id);
		if(payMethods.size()>0){
			return payMethods.get(0);
		}
		return null;
	}

	

	@Override
	public PayView getPayView(int id) {
		String sql="select * from plat_pay_db.config_pay_view where id=?";
		List<PayView> payMethodViews = jdbcTemplate.query(sql, new BeanPropertyRowMapper<PayView>(PayView.class), id);
		if(payMethodViews.size()>0){
			return payMethodViews.get(0);
		}
		return null;
	}
	

	@Override
	public PayView getPayView(int payMethodId, int whichOne) {
		String sql="select * from plat_pay_db.config_pay_view where pay_method_id=? and which_one=?";
		List<PayView> payMethodViews = jdbcTemplate.query(sql, new BeanPropertyRowMapper<PayView>(PayView.class), payMethodId, whichOne);
		if(payMethodViews.size()>0){
			return payMethodViews.get(0);
		}
		return null;
	}


	@Override
	public PayChannelMethod getPayChannelMethod(int payMethodId,
			int payChannelId) {
		String sql="select * from plat_pay_db.config_pay_channel_method where pay_method_id=? and pay_channel_id=?";
		List<PayChannelMethod> payChannelMethods = jdbcTemplate.query(sql, new BeanPropertyRowMapper<PayChannelMethod>(PayChannelMethod.class), payMethodId, payChannelId);
		if(payChannelMethods.size()>0){
			return payChannelMethods.get(0);
		}
		return null;
	}
	
	
	@Override
	public List<PayMethodInstance> getPayMethodInstanceList(int payMethodId) {
		String sql="select * from plat_pay_db.config_pay_method_instance where pay_method_id=? ";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<PayMethodInstance>(PayMethodInstance.class), payMethodId);
	}


	@Override
	public List<PayType> getPayTypeListSorted() {
		String sql="select * from plat_pay_db.config_pay_type order by sort_num asc";
		List<PayType> payTypes = jdbcTemplate.query(sql, new BeanPropertyRowMapper<PayType>(PayType.class));
		return payTypes;
	}

	
	@Override
	public List<PayView> getPayViewListSorted(int whichOne) {
		
		String sql="select a.*, c.bol_fixed "
				+ " from plat_pay_db.config_pay_view a "
				+ " right join (select pay_method_id from plat_pay_db.config_pay_method_instance where status=1 and which_one=? group by pay_method_id ) b on a.pay_method_id=b.pay_method_id "
				+ " left join plat_pay_db.config_pay_method c on a.pay_method_id=c.id "
				+ " where a.status=1 and a.which_one=? and c.status=1 "
				+ " order by a.pay_type_id asc, a.sort_num asc";
		
		List<PayView> payMethodViews = jdbcTemplate.query(sql, new BeanPropertyRowMapper<PayView>(PayView.class),
				whichOne, whichOne);
		
		return payMethodViews;
	}

	@Override
	public List<PayViewBind> getPayViewBindList(int payViewId) {
		String sql="select * from plat_pay_db.config_pay_view_bind where pay_view_id=?";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<PayViewBind>(PayViewBind.class), payViewId);
	}

	/**
	 * 取 minuteBefore分钟前 未结算的rowCount条订单数据返回
	 */
	@Override
	public List<PayOrder> getPayOrderUnsettled(int minuteBefore, int rowCount) {
		String sql="select * from plat_pay_db.pay_order_record where status=1 and notify_time < date_add(notify_time, interval -? minute) order by notify_time asc limit ?";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<PayOrder>(PayOrder.class), minuteBefore, rowCount);
	}


	@Override
	public Map<String, Object> getGooglePayInfo(String app_name) {
		String sql = "select channel_id,file_path,email_address from plat_pay_db.pay_google_info where app_name = ?";
		
		return jdbcTemplate.queryForMap(sql, app_name);
	}




	@Override
	public int getIsFirstPay(int userId) {
		String sql = "select count(*) from plat_user_db.user_firstpay_active where user_id = ? and active_flag = 2 and first_time is not null";
		int result = jdbcTemplate.queryForObject(sql, Integer.class, userId);
		return result;
	}


	@Override
	public int getGiftCoinByFirstPay(int goodsId) {
		// 查看活动是否开启
		String sql = "select open_or_close from qp_activity_system_db.plat_activity_config where source_id = 20191224";
		int result = jdbcTemplate.queryForObject(sql, Integer.class);
		if(result != 0) {
			String giftSql = "select first_gift_num from config_plat_db.config_store_info where goods_id = ?"; 
			int giftCoin = jdbcTemplate.queryForObject(giftSql, Integer.class, goodsId);
			return giftCoin;
		}
		
		return 0;
	}


	@Override
	public void addGiftCoinByFirstPay(int userId) {
		String sql = "insert into plat_user_db.user_firstpay_active (user_id, active_flag, first_time, update_time) values (?, 2 , now(), now())";
		jdbcTemplate.update(sql, userId);
	}


	@Override
	public List<Map<String, Object>> getPayBankInfos() {
		String sql = "select * from plat_pay_db.config_bank_info";
		
		return jdbcTemplate.queryForList(sql);
	}


	@Override
	public void updateOrderVAAndTNo(PayOrder payOrder) {
		String sql="update plat_pay_db.pay_order_record set transaction_no=?,virtual_account=? where status=0 and transaction_no is null and order_no=?";
		jdbcTemplate.update(sql, payOrder.getTransactionNo(), payOrder.getVirtualAccount(),payOrder.getOrderNo());
	}


	@Override
	public PayOrder getPayOrderByTNo(String upOrderNum) {
		String sql="select * from plat_pay_db.pay_order_record where transaction_no=?";
		List<PayOrder> payOrders = jdbcTemplate.query(sql, new BeanPropertyRowMapper<PayOrder>(PayOrder.class), upOrderNum);
		if(payOrders.size()>0){
			return payOrders.get(0);
		}
		return null;
	}
	
	
}
