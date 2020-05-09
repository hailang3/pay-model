package com.neo.paymodel.api.pay.cache;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.util.Assert;

import java.nio.charset.Charset;

public class CustomJackson2JsonRedisSerializer implements
		RedisSerializer<Object> {
	
	static final Logger logger = LoggerFactory.getLogger(CustomJackson2JsonRedisSerializer.class);
	
	public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

	static final byte[] EMPTY_ARRAY = new byte[0];

	private final JavaType javaType;
	private ObjectMapper objectMapper = new ObjectMapper();

	public CustomJackson2JsonRedisSerializer() {
		this.javaType = getJavaType(Object.class);
		ObjectMapper objectMapper = new ObjectMapper();
		 objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
		 objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
		this.objectMapper=objectMapper;
	}

	public Object deserialize(byte[] bytes) throws SerializationException {
		if (bytes == null || bytes.length < 1)
			return null;
		try {
			
			logger.debug("bytes=>string:{}", new String(bytes, DEFAULT_CHARSET));
			
			Object obj= this.objectMapper.readValue(bytes, 0, bytes.length,
					this.javaType);
			return obj;
		} catch (Exception ex) {
			throw new SerializationException("Could not read JSON: "
					+ ex.getMessage(), ex);
		}
	}

	public byte[] serialize(Object t) throws SerializationException {
		if (t == null)
			return EMPTY_ARRAY;
		try {
			return this.objectMapper.writeValueAsBytes(t);
		} catch (Exception ex) {
			throw new SerializationException("Could not write JSON: "
					+ ex.getMessage(), ex);
		}
	}

	public void setObjectMapper(ObjectMapper objectMapper) {
		Assert.notNull(objectMapper, "'objectMapper' must not be null");
		this.objectMapper = objectMapper;
	}

	protected JavaType getJavaType(Class<?> clazz) {
		return TypeFactory.defaultInstance().constructType(clazz);
	}
}
