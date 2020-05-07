package com.neo.paymodel.api.pay.channel.paygoogle;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.SecurityUtils;
import com.google.api.services.androidpublisher.AndroidPublisher;
import com.google.api.services.androidpublisher.AndroidPublisherScopes;
import com.google.api.services.androidpublisher.model.ProductPurchase;
import com.neo.paymodel.api.pay.channel.Channel;
import com.neo.paymodel.api.pay.channel.IPayContext;
import com.neo.paymodel.api.pay.channel.PayChannelTemplateApi;
import com.neo.paymodel.api.pay.entity.PayOrder;
import com.neo.paymodel.api.pay.service.PayService;
import com.neo.paymodel.api.pay.web.model.RetModel;
import com.neo.paymodel.api.pay.web.vo.BankInfo;
import com.neo.paymodel.api.pay.web.vo.PaySubmitRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.security.PrivateKey;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Service
@Channel(id=5, name="谷歌官方支付")
public class GoogleApi extends PayChannelTemplateApi {
	
	static final Logger logger = LoggerFactory.getLogger(GoogleApi.class);
	
	
	@Autowired
	private PayService payService;

	@Override
	public void submitOrder(PaySubmitRequest paySubmitReq, PayOrder payOrder, HttpServletResponse resp,
							IPayContext context) {
		
		
	}

	@Override
	protected boolean checkNotifyReq(HttpServletRequest notifyReq, IPayContext context) {
		
		return false;
	}

	@Override
	protected ITransactionOrder parsingNotifyReq(HttpServletRequest notifyReq, IPayContext context) {
		
		return null;
	}

	@Override
	public ITransactionOrder checkOrderRemote(PayOrder payOrder, IPayContext context) {
		
		boolean status = false;
		
		String packageName = context.getConfig("packageName").toString();
		String productId = context.getConfig("productId").toString();
		String token = context.getConfig("token").toString();
		System.out.println(payOrder.getChannelId());
		Map<String, Object> configMap = payService.getGooglePayInfo(payOrder.getAppMame());
		System.out.println(configMap.toString());
		String emailAddress = configMap.get("email_address").toString();
		String keyPath = configMap.get("file_path").toString();
		
		try{
			HttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();

			PrivateKey privateKey = SecurityUtils.loadPrivateKeyFromKeyStore(
					SecurityUtils.getPkcs12KeyStore(),
					new FileInputStream(new File(keyPath)), // 生成的P12文件
					"notasecret", "privatekey", "notasecret");
			logger.info("privateKey:" + privateKey.toString());
			
			GoogleCredential credential = new GoogleCredential.Builder()
					.setTransport(transport).setJsonFactory(JacksonFactory.getDefaultInstance())
					.setServiceAccountId(emailAddress) // e.g.: 626891557797-frclnjv31rn4ss81ch746g9t6pd3mmej@developer.gserviceaccount.com
					.setServiceAccountScopes(AndroidPublisherScopes.all())
					.setServiceAccountPrivateKey(privateKey).build();
			
			logger.info("credential:" + credential.toString());

			AndroidPublisher publisher = new AndroidPublisher.Builder(transport,
					JacksonFactory.getDefaultInstance(), credential).build();
			
			logger.info("publisher:" + publisher.toString());
			
			

			AndroidPublisher.Purchases.Products products = publisher.purchases().products();
			
			logger.info("products:" + products.toString());

			// 参数详细说明: https://developers.google.com/android-publisher/api-ref/purchases/products/get
			AndroidPublisher.Purchases.Products.Get product = products.get(packageName,
					productId, token);
			
			logger.info("product:" + product.toString());
			
			
			// 获取订单信息
			// 返回信息说明: https://developers.google.com/android-publisher/api-ref/purchases/products
			// 通过consumptionState, purchaseState可以判断订单的状态
			ProductPurchase purchase = product.execute();

			logger.info("purchase:" + purchase.toString());

			if (purchase.getPurchaseState() == 0) {
				status = true;
			}
			
			

		}catch(Exception e){

			e.printStackTrace();

		}
		
		return new SimpleTransactionOrder(status, payOrder.getOrderNo(),  payOrder.getActualPay(), payOrder.getTransactionNo(), new Timestamp(System.currentTimeMillis()), payOrder.getCurrency(), "");
	}

	@Override
	public RetModel<List<BankInfo>> getBankInfo(int payChannelId) {
		// TODO Auto-generated method stub
		return null;
	}
	



	
}