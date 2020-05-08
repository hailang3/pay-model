package com.neo.paymodel.api.pay.channel;


import com.neo.paymodel.common.util.RandomChars;

import java.util.Random;

public class PayHelper {

	
	public static String genMerchantCode(int payChannelId, int payMerchantId){
		Random random = new Random();
		
		int beforeCid=random.nextInt(6);
		int afterMid=random.nextInt(6);
		int betweenCidMid=15-beforeCid-afterMid;
		
		StringBuffer sb= new StringBuffer();
		sb.append(RandomChars.getRandomLetters(beforeCid));
		sb.append(String.valueOf(payChannelId));
		sb.append(RandomChars.getRandomLetters(1));
		sb.append(RandomChars.getRandomChars(betweenCidMid));
		sb.append(RandomChars.getRandomLetters(1));
		sb.append(String.valueOf(payMerchantId));
		sb.append(RandomChars.getRandomLetters(afterMid));
		
		return sb.toString();
	}
}
