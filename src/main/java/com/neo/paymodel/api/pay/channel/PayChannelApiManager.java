package com.neo.paymodel.api.pay.channel;

import com.neo.paymodel.api.pay.entity.PayMerchant;
import com.neo.paymodel.api.pay.entity.PayOrder;
import com.neo.paymodel.api.pay.service.IPayConfService;
import com.neo.paymodel.api.pay.service.IPayOrderService;
import com.neo.paymodel.api.pay.service.PayService;
import com.neo.paymodel.common.util.WebUtil;
import com.neo.paymodel.api.pay.web.model.RetModel;
import com.neo.paymodel.api.pay.web.vo.BankInfo;
import com.neo.paymodel.api.pay.web.vo.PaySubmitRequest;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Lazy(false)
@Service
public class PayChannelApiManager implements ApplicationContextAware {

	static final Logger logger = LoggerFactory.getLogger(PayChannelApiManager.class);
	
	private Map<Integer, IPayChannelApi> payChannelApis =  new HashMap<Integer, IPayChannelApi>();
	
	private ApplicationContext context;
	
	@Autowired
	private PayService payService;
	@Autowired
	private IPayOrderService payOrderService;
	@Autowired
	private IPayConfService payConfService;
	
	@Override
	public void setApplicationContext(ApplicationContext context)
			throws BeansException {
		this.context = context;
	}

	void init(){
		//加载充值渠道实例
		//com.dsgame.pay.newapi.channel--->
		String packageName = "com.dsgame.pay.api.channel";
		// List<String> classNames = getClassName(packageName);
		List<String> classNames = PackageUtil.getChildPackageClassNames(packageName, true);
		if (classNames == null) {
			logger.debug("{} has no classfile.", packageName);
			return;
		}
		for(String className : classNames) {
			logger.debug("{}", className);
			Class<?> clazz=null;
			try {
				clazz = Class.forName(className);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				continue;
			}
			if(!IPayChannelApi.class.isAssignableFrom(clazz)){
				logger.debug("{} was not implements interface IPayChannelApi", className);
				continue;
			}
			if(!clazz.isAnnotationPresent(Channel.class)){
				logger.debug("{} was not annotationed by @interface @ChannelId", className);
				continue;
			}
				
			Channel anno = clazz.getAnnotation(Channel.class);
			int channelId=anno.id();
			String channelName = anno.name();
			if(channelId<=0){
				logger.warn("{}=> @ChannelId value must >0", className);
				continue;
			}
			
			if(payChannelApis.containsKey(channelId)){
				//payChannelApis.get(channelId).getClass()
				logger.warn("{} @ChannelId 和 {} @ChannelId 冲突了，值都为：{}", className, payChannelApis.get(channelId).getClass(), channelId);
				continue;
			}
			
			
			IPayChannelApi apiInstance=null;
			//兼容 api实现类 托管 spring容器
			if(context.containsBean(getBeanName(clazz))){
				apiInstance = (IPayChannelApi)context.getBean(clazz);
			}else{
				try {
					apiInstance = (IPayChannelApi) clazz.newInstance();
				} catch (InstantiationException e) {
					e.printStackTrace();
					continue;
				} catch (IllegalAccessException e) {
					e.printStackTrace();
					continue;
				}
				
				if(ApplicationContextAware.class.isAssignableFrom(clazz)){
					((ApplicationContextAware)apiInstance).setApplicationContext(context);
				}
			}

			logger.debug("{} 实例创建成功[{}]", className, apiInstance);

			if(PayChannelContextApi.class.isAssignableFrom(clazz)){
				((PayChannelContextApi)apiInstance).setChannelId(channelId);
				((PayChannelContextApi)apiInstance).setChannelName(channelName);
				logger.debug("{} payOrderService 设置成功 ", className);
				((PayChannelContextApi)apiInstance).init();
			}
			
			payChannelApis.put(channelId, apiInstance);
		}
	}
	
	private String getBeanName(Class<?> clazz){
		
		String beanName=null;
		if(clazz.isAnnotationPresent(Service.class)){
			Service anno = (Service) clazz.getAnnotation(Service.class);
			beanName = anno.value();
		}
			
		if(StringUtils.isEmpty(beanName)){
			char[] cs=clazz.getSimpleName().toCharArray();
	        cs[0] += 32;	//首字母大写到小写
	        beanName=String.valueOf(cs);
		}
        return beanName;
	}
	
	public void submitOrder(PaySubmitRequest paySubmitReq, HttpServletResponse resp){
		
		String orderNo = paySubmitReq.getOrderNo();
		PayOrder payOrder = payOrderService.getPayOrder(orderNo);
		if(payOrder==null){
			WebUtil.write(resp, "非法订单");
			return;
		}
		// bankCode 不为空
		if(paySubmitReq.getBankCode() != null && !paySubmitReq.getBankCode().equals("")) {
			payOrder.setBankCode(paySubmitReq.getBankCode());
		}
		
		paySubmitReq.setPayOrder(payOrder);
		
		IPayChannelApi payChannelApi = payChannelApis.get(payOrder.getPayChannelId());
		if(payChannelApi==null){
			WebUtil.write(resp, "非法充值渠道");
			return;
		}
		
		payChannelApi.submitOrder(paySubmitReq, resp);
	}
	
	
	public int checkOrder(PayOrder payOrder){
		IPayChannelApi payChannelApi = payChannelApis.get(payOrder.getPayChannelId());
		if(payChannelApi==null){
			return -99;
		}
		return payChannelApi.checkOrder(payOrder);
	}
	
	public void handleNotify(String merchantCode, HttpServletRequest notifyReq, HttpServletResponse notifyResp){
		//获取渠道信息
		
		PayMerchant payMerchant = payConfService.getPayMerchant(merchantCode);
		if(payMerchant==null){
			return;
		}
		IPayChannelApi payChannelApi = payChannelApis.get(payMerchant.getPayChannelId());
		if(payChannelApi==null){
			return;
		}
		payChannelApi.handleNotify(notifyReq, notifyResp, payMerchant);
	}

	public RetModel<List<BankInfo>> getBankInfo(int payChannelId) {
		IPayChannelApi payChannelApi = payChannelApis.get(payChannelId);
		if(payChannelApi==null){
			return null;
		}
		return payChannelApi.getBankInfo(payChannelId);
	}
}
