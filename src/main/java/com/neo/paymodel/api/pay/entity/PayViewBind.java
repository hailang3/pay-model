package com.neo.paymodel.api.pay.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.sql.Timestamp;

public class PayViewBind implements Cloneable{

	private int id;						//id
	private int payViewId;				//PayView的id
	private int payInstanceId;			//PayMethodInstance的id
	private int weight;					//权重=>用于计算 在当前支付类型下，符合金额限制，存在多个支付方法时被选中的概率
	private int submitWay;				//提交到支付方式实例（商户）的提交方式
	@JsonIgnore
	private Timestamp createTime;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getPayViewId() {
		return payViewId;
	}
	public void setPayViewId(int payViewId) {
		this.payViewId = payViewId;
	}
	
	public int getPayInstanceId() {
		return payInstanceId;
	}
	public void setPayInstanceId(int payInstanceId) {
		this.payInstanceId = payInstanceId;
	}
	public int getWeight() {
		return weight;
	}
	public void setWeight(int weight) {
		this.weight = weight;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	
	public int getSubmitWay() {
		return submitWay;
	}
	public void setSubmitWay(int submitWay) {
		this.submitWay = submitWay;
	}
	
	
	@Override  
    public Object clone() {  
		PayViewBind stu = null;  
        try{  
            stu = (PayViewBind)super.clone();  
        }catch(CloneNotSupportedException e) {  
            e.printStackTrace();  
        }  
        return stu;  
    }
}
