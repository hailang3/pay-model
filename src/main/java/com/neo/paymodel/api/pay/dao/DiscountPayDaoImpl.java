package com.neo.paymodel.api.pay.dao;

import com.neo.paymodel.api.pay.entity.DiscountInfo;
import com.neo.paymodel.api.pay.entity.StoreInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DiscountPayDaoImpl implements DiscountPayDao{

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	
	@Override
	public DiscountInfo getDiscountInfo(int user_id) {
		// TODO Auto-generated method stub
		String sql="select * from plat_pay_db.discount_info where user_id=?";
		List<DiscountInfo> discountInfos = jdbcTemplate.query(sql, new BeanPropertyRowMapper<DiscountInfo>(DiscountInfo.class), user_id);
		if(discountInfos.size()>0){
			return discountInfos.get(0);
		}
		return null;
	}


	@Override
	public boolean updateDiscountInfo(DiscountInfo discountInfo) {
		// TODO Auto-generated method stub
		String sql="update plat_pay_db.discount_info set domino_gold_t_001=?, domino_gold_t_002=?, domino_gold_t_003=?, domino_gold_t_004=?, domino_gold_t_005=?, domino_gold_t_006=?, domino_gold_t_first=? where user_id=?";
		int effectRowCount = jdbcTemplate.update(sql, discountInfo.getDomino_gold_t_001(), discountInfo.getDomino_gold_t_002(), discountInfo.getDomino_gold_t_003()
				, discountInfo.getDomino_gold_t_004(), discountInfo.getDomino_gold_t_005(), discountInfo.getDomino_gold_t_006()
				, discountInfo.getDomino_gold_t_first(), discountInfo.getUser_id());
		return effectRowCount>0;
	}


	@Override
	public StoreInfo getStoreInfoById(int goodsId) {
		String querySql = " SELECT * FROM config_plat_db.config_store_info where goods_id = ? ";
		StoreInfo storeInfo = (StoreInfo) jdbcTemplate.queryForObject(querySql,new BeanPropertyRowMapper<>(StoreInfo.class),goodsId);
		return storeInfo;
	}
}
