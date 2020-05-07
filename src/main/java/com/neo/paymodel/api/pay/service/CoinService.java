package com.neo.paymodel.api.pay.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CoinService {

	@Autowired
	private CoinDataDao coinDataDao;
	
	
	public long getGameCoin(int userId){
		return coinDataDao.getGameCoin(userId);
	}
	
	public long getBankCoin(int userId){
		return coinDataDao.getBankCoin(userId);
	}
	
}
