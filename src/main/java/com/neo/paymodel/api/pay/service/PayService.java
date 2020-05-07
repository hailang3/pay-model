package com.neo.paymodel.api.pay.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neo.paymodel.api.pay.dao.IPayDao;
import com.neo.paymodel.api.pay.entity.*;
import com.neo.paymodel.api.pay.util.Md5Util;
import com.neo.paymodel.api.pay.web.vo.PayTypeVo;
import com.neo.paymodel.api.pay.web.vo.PayViewVo;
import com.neo.paymodel.common.util.AESUtils;
import com.neo.paymodel.common.util.HttpUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class PayService {

	static final Logger logger = LoggerFactory.getLogger(PayService.class);
	
	
	@Value("${dsgame.pay.sign.key}")
	private String paySignKey;
	
	@Value("${dsgame.pay.md5.key}")
	private String md5SignKey;
	
	@Autowired
	private IPayConfService payConfService;
	
	@Autowired
	private PayAgentService payAgentService;
	
	
	@Autowired
	private IPayDao payDao;
	

	public PayMethodInstance filterPayMethodInstance(Long orderAmount, int payViewId, int payMethodId, boolean bolFixed, int whichOne) {
		
		
		//现不考虑  支付宝与支付宝固额 相互包含的情况 只根据 payTypeId 拉出payMethods
		Map<String,PayMethodInstance> payInstanceMap = payConfService.getPayMethodInstanceMap(payMethodId, whichOne);

		Map<String,PayMethodInstance> newMap = new HashMap<String,PayMethodInstance>();
		
		List<PayViewBind> payViewBinds = payConfService.getPayViewBindList(payViewId);

		List<PayMethodInstance> payMethodInstances =  new ArrayList<PayMethodInstance>();
		
		// 从缓存中取map已Integer为key可能导致序列化默认会把key设置为String类型
		for(Map.Entry<String, PayMethodInstance> entry : payInstanceMap.entrySet()) {
			newMap.put(String.valueOf(entry.getKey()), entry.getValue());
			
		}

		for(PayViewBind payViewBind : payViewBinds){
			if(newMap.containsKey(String.valueOf(payViewBind.getPayInstanceId()))){
				PayMethodInstance payInstance = payInstanceMap.get(String.valueOf(payViewBind.getPayInstanceId()));
				payInstance.setWeight(payViewBind.getWeight());
				payInstance.setSubmitWay(payViewBind.getSubmitWay());
				payMethodInstances.add(payInstance);
			}
		}
		
		if(payMethodInstances.size()==0){
			//如果支付视窗没有配置与支付实例的绑定关系，则默认绑定所有实例，所有实例权重都是默认为1
			payMethodInstances=new ArrayList<PayMethodInstance>(payInstanceMap.values());
			if(payMethodInstances.size()==0){
				return null;
			}
		}
		
		Iterator<PayMethodInstance> iter = payMethodInstances.iterator();
		
		int totalWeight=0;
		while(iter.hasNext()){
			 PayMethodInstance payInstance = iter.next();
//		  if(bolFixed){ 
//			  String[] amountArr = payInstance.getAmounts().split(",");
//		  if(!Arrays.asList(amountArr).contains(String.valueOf(orderAmount))){
//		  iter.remove(); continue; 
//		  } }else{
//			  if( (payInstance.getMinAmount()!=null &&
//		  payInstance.getMinAmount()> orderAmount) || (payInstance.getMaxAmount()!=null
//		  && orderAmount > payInstance.getMaxAmount()) ){
//			  iter.remove(); continue;
//		  } }
		  
		  totalWeight+=payInstance.getWeight();
		  }
		 
		
		if(payMethodInstances.size()==1){
			return payMethodInstances.get(0);
		}
		
		int randomNum = new Random().nextInt(totalWeight);
		int curNum=0;
		PayMethodInstance retPayInstance=payMethodInstances.get(0);
		for(PayMethodInstance payInstance : payMethodInstances){
			curNum+=payInstance.getWeight();
			if(curNum>randomNum){
				retPayInstance=payInstance;
				break;
			}
		}
		
		if(retPayInstance.getSubmitWay()==null){
			PayChannelMethod payChannelMethod = payConfService.getPayChannelMethod(retPayInstance.getPayMethodId(), retPayInstance.getPayChannelId());
			retPayInstance.setSubmitWay(payChannelMethod.getSubmitWay());
		}
		
		return retPayInstance;
	}


	public List<PayTypeVo> getPayTypeVoList(int whichOne, int payAmountTotal, String channel_id) {
		
		List<PayView> payViews = payConfService.getPayViewListSorted(whichOne);
		
		//检测充值方式的打开累计充值限制
		Iterator<PayView> iter = payViews.iterator();
		while(iter.hasNext()){
			PayView payMethodView = iter.next();
			if(payMethodView.getOpenMinLimit()>payAmountTotal){
				iter.remove();
			}
		}
		if(payViews.size()==0){
			return null;
		}
		
		return buildPayTypeVoList(payViews, whichOne, payAmountTotal,channel_id);
	}


	private List<PayTypeVo> buildPayTypeVoList(List<PayView> payViews, int whichOne, int payAmountTotal,String channel_id) {
		
		List<PayType> payTypes = payConfService.getPayTypeListSorted();
		
		Map<Integer, PayTypeVo> payTypeVoMap= new HashMap<Integer, PayTypeVo>();
		
		int curPayTypeId=-1;
		Integer recomFlag=null;
		List<PayViewVo> curMethodViews=null;
		
		Integer minRate=null;
		Integer maxRate=null;
		for(PayView payView : payViews){
			if(payView.getPayTypeId() != curPayTypeId){
				if(curPayTypeId>0){								
					/**
					 * 该充值类型下的所有充值方式的
					 * 返利比率相同，则返回该比率，
					 * 返利比率不同，则返回 0， 
					 * 没有返利，则返回 null(不返回 rebateFlag字段).
					 */
					Integer rebateFlag=null;
					if(minRate==maxRate && (minRate!=null && minRate>0)){
						rebateFlag =minRate;
					}else if(minRate < maxRate){
						rebateFlag=0;
					}
					
					payTypeVoMap.put(curPayTypeId, new PayTypeVo(curPayTypeId, recomFlag, rebateFlag, curMethodViews));
					
					recomFlag=null;
					minRate=null;
					maxRate=null;
				}
				
				curPayTypeId=payView.getPayTypeId();
				curMethodViews = new ArrayList<PayViewVo>();
			}
			
			curMethodViews.add(new PayViewVo(payView));
			if(recomFlag==null && payView.getRecomFlag()==1){
				recomFlag=1;
			}
			
			int rebateRate = payView.getRebateRate();
			if(minRate==null || rebateRate <minRate){
				minRate = rebateRate;
			}
			if(maxRate==null || rebateRate>maxRate){
				maxRate = rebateRate;
			}
			
		}
		
		if(curPayTypeId>0){
			/**
			 * 该充值类型下的所有充值方式的
			 * 返利比率相同，则返回该比率，
			 * 返利比率不同，则返回 0， 
			 * 没有返利，则返回 null(不返回 rebateFlag字段).
			 */
			Integer rebateFlag=null;
			if(minRate==maxRate && minRate>0){
				rebateFlag =minRate;
			}else if(minRate < maxRate){
				rebateFlag=0;
			}
			payTypeVoMap.put(curPayTypeId, new PayTypeVo(curPayTypeId, recomFlag, rebateFlag, curMethodViews));
		}
		
		if (payTypeVoMap.size()==0){
			return null;
		}
		List<PayTypeVo> payTypeVos =  new ArrayList<PayTypeVo>();
		return payTypeVos;
	}

	private void buildVipPayAgents(PayTypeVo payTypeVo, int whichOne, int payAmountTotal) {
		String channel_id="";
		int isagentwj=0;
		//王者归来只显示旺捷一家代理 1880000000000334   9902011903120001
		if("1880000000000334".equals(channel_id) || "9902011903120001".equals(channel_id)){
			isagentwj=1;
		}
		//1880000000000387	ios久游斗地主
		//9902991906120001	久游斗地主安卓1 只显示第4家代理
		if("1880000000000387".equals(channel_id) || channel_id.indexOf("990299")>=0){
			isagentwj=4;
		}
		
		List<AgentPayType> agentPayTypeList = payAgentService.getAgentPayTypeList(payAmountTotal, isagentwj);
		
		if(agentPayTypeList!=null){
			for(AgentPayType item : agentPayTypeList){
				List<Integer> payTypeList=Arrays.asList(0,0,0,0);
				//1:微信2:qq3:支付宝4:久聊
				//1支付宝  2微信  3银行卡 4久聊
				if(item.getUser_payway_type()==1){	
					payTypeList.set(1,1);
				}
				if(item.getUser_payway_type()==2){	
					payTypeList.set(2, 1);
				}
				if(item.getUser_payway_type()==3){	
					payTypeList.set(0, 1);
				}
//				item.setRecommended_status(agentpayRecommendStatus);
				if(item.getUser_payway_type() == 4){
					payTypeList.set(3, 1);
					item.setUser_payway_website("http://www.naychat.cn/");
				}					
				item.setPayTypeList(payTypeList);
			}
			
			JSONObject extJson = new JSONObject();
			JSONArray agentJsonArr = JSONArray.parseArray(JSON.toJSONString(agentPayTypeList));
			extJson.put("agents", agentJsonArr);
			payTypeVo.setExt(extJson);
		}
		
	}

	public boolean checkPayRequest(HttpServletRequest req) throws Exception {
		Map<String, Object> parameterMap = HttpUtil.getParameterMap(req, false);
		String sign=String.valueOf(parameterMap.get("sign"));
		if(StringUtils.isEmpty(sign)){
			return false;
		}
		
		parameterMap.remove("sign");
		StringBuffer content = new StringBuffer();
		List<String> keys = new ArrayList<String>(parameterMap.keySet());
        Collections.sort(keys);
        for (int i = 0; i < keys.size(); i++) {
            String key = (String) keys.get(i);
            String value = (String) parameterMap.get(key);
            content.append(i==0 ? "" : "&").append(key).append("=").append(value);
        }
        
        String genSign = Md5Util.encode(content.toString()+"&key="+paySignKey);
        
        return sign.equalsIgnoreCase(genSign);
	}
	
	

//	public String buildSubmitUrl(String orderNo, HttpServletRequest req) {
//			
//		Map<String, Object> parameterMap = HttpUtil.getParameterMap(req, false);
//		parameterMap.remove("sign");
//		parameterMap.put("orderNo", orderNo);
//		
//		StringBuffer content = new StringBuffer();
//		List<String> keys = new ArrayList<String>(parameterMap.keySet());
//        Collections.sort(keys);
//        for (int i = 0; i < keys.size(); i++) {
//            String key = (String) keys.get(i);
//            String value = (String) parameterMap.get(key);
//            content.append(i==0 ? "" : "&").append(key).append("=").append(value);
//        }
//        
//        String sign = Md5Util.encode(content.toString()+"&key="+paySignKey);
//		
//        String paramStr = content.toString()+"&sign="+sign;
//        
//        StringBuffer originalURL = req.getRequestURL();
//        String requstUrl = originalURL.toString().replace("order", "pay");
//        
//        return requstUrl+"?"+paramStr;
//        
//	}
	
	
	public String buildSubmitUrl(String orderNo, HttpServletRequest req) {
		
		Map<String, Object> parameterMap = HttpUtil.getParameterMap(req, false);
		parameterMap.remove("sign");
		parameterMap.put("orderNo", orderNo);
		
		StringBuffer content = new StringBuffer();
		List<String> keys = new ArrayList<String>(parameterMap.keySet());
        Collections.sort(keys);
        for (int i = 0; i < keys.size(); i++) {
            String key = (String) keys.get(i);
            String value = (String) parameterMap.get(key);
            content.append(i==0 ? "" : "&").append(key).append("=").append(value);
        }
        // 组成参数 加密参数sign urlnormal + "&key=配置的md5key";
        String sign = Md5Util.encode(content.toString()+"&key=" + md5SignKey);
        
        // urlnormal + "&sign=md5(md5Key)";
		 String paramStr = content.toString()+"&sign="+sign;
		 
		 //aes(urlnormal)
		 String aesStr = "";
		 try {
			 aesStr = AESUtils.encryptAES(paramStr);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		 // param = "data=" + aesstr + "timestamp=" + "时间戳";
		 String finalParams = "data=" + aesStr + "&timestamp=" + String.valueOf(System.currentTimeMillis() / 1000);
		 
        
        StringBuffer originalURL = req.getRequestURL();
        String requstUrl = originalURL.toString().replace("order", "pay");
        
        return requstUrl+"?"+finalParams + "&sign=" + Md5Util.encode(finalParams);
        
	}
	
	
	public Map<String, Object> getGooglePayInfo(String channel_id){
		return payDao.getGooglePayInfo(channel_id);
	}


	public List<Map<String, Object>> getPayBankInfos() {
		
		return payDao.getPayBankInfos();
	}

	
	public static void main(String[] args) {
		Map<Integer, Object> result = new HashMap<Integer, Object>();
		result.put(12, "aaa");
		int aa = 12;
		System.out.println(result.get(aa));
	}

}
