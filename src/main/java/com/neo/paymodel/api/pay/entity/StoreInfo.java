package com.neo.paymodel.api.pay.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品实体类
 */
public class StoreInfo {

    private int goods_id;
    private int goods_type;
    private String goods_name;
    private int default_num;
    private int first_gift_num;
    private BigDecimal price;
    private BigDecimal usd_price;
    private int discount_coin;
    private String remark;
    private Date create_time;
    
    private Date update_time;
	public int getGoods_id() {
		return goods_id;
	}
	public void setGoods_id(int goods_id) {
		this.goods_id = goods_id;
	}
	public int getGoods_type() {
		return goods_type;
	}
	public void setGoods_type(int goods_type) {
		this.goods_type = goods_type;
	}
	public String getGoods_name() {
		return goods_name;
	}
	public void setGoods_name(String goods_name) {
		this.goods_name = goods_name;
	}
	
	public int getDefault_num() {
		return default_num;
	}
	public void setDefault_num(int default_num) {
		this.default_num = default_num;
	}
	public int getFirst_gift_num() {
		return first_gift_num;
	}
	public void setFirst_gift_num(int first_gift_num) {
		this.first_gift_num = first_gift_num;
	}
	
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
	public Date getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(Date update_time) {
		this.update_time = update_time;
	}
	public BigDecimal getUsd_price() {
		return usd_price;
	}
	public void setUsd_price(BigDecimal usd_price) {
		this.usd_price = usd_price;
	}
	public int getDiscount_coin() {
		return discount_coin;
	}
	public void setDiscount_coin(int discount_coin) {
		this.discount_coin = discount_coin;
	}

    
}
