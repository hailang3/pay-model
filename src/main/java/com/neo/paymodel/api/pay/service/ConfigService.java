package com.neo.paymodel.api.pay.service;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
public class ConfigService {
	
	private final static Logger logger = LoggerFactory.getLogger(ConfigService.class);

	@Autowired
    private JedisLobbyUtils jedisLobbyUtils;
	
	
	@Autowired
    private ConfigDAO configDao;
	
	
	public Object get(String key) {
		return jedisLobbyUtils.getObject(key);
	}
    


	public void setHasOvertime(String cacheKey, Object buildOriginalURL, int i) {
		jedisLobbyUtils.setObject(cacheKey, buildOriginalURL, i);
		
	}
	
	
	 public Map<String, Object> getGameTask(int taskId){
		 Map<String, Object> resultMap = configDao.getGameTask(taskId);
		 if(resultMap != null) {
			 return resultMap;
		 }
		 
		 return null;
		 
	 }


	public boolean verifyToken(long userId, String userToken) {
		String key = String.format(JedisConstantsUtil.REDIS_AUTHEN_TOKEN_FOLDER_KEY, userId);
		String str = jedisLobbyUtils.get(key);
		if(!StringUtils.isEmpty(str) && userToken.equals(str)) {
			return true;
		}
		return false;
	}
	
}
