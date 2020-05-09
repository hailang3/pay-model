package com.neo.paymodel.api.pay.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.RedisConnectionFailureException;

@Configuration
@EnableCaching
public class MyCachingConfigurer extends CachingConfigurerSupport {
	@Autowired
	private CacheManager cacheManager;
	
	public CacheManager getCacheManager() {
		return cacheManager;
	}

	public void setCacheManager(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	@Override
	public CacheManager cacheManager() {
		
		return cacheManager;
	}

	@Override
	public CacheErrorHandler errorHandler() {
	    return new MyCacheErrorHandler();
	}
}

class MyCacheErrorHandler implements CacheErrorHandler {

	static final Logger logger = LoggerFactory.getLogger(MyCacheErrorHandler.class);
	
    @Override
    public void handleCacheGetError(RuntimeException e, Cache cache, Object o) {

        if( e instanceof RedisConnectionFailureException){
            logger.warn("redis has lose connection:",e);
            return;
        }
        throw e;
    }

    @Override
    public void handleCachePutError(RuntimeException e, Cache cache, Object o, Object o1) {
        if(e instanceof RedisConnectionFailureException){
            logger.warn("redis has lose connection:",e);
            return;
        }
        throw e;
    }

    @Override
    public void handleCacheEvictError(RuntimeException e, Cache cache, Object o) {
        throw e;
    }

    @Override
    public void handleCacheClearError(RuntimeException e, Cache cache) {
        throw e;
    }
}