package com.neo.paymodel.api.pay.util;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Iterator;
import java.util.Map;

public class RequestParamsUtil {
	/**
	   * 根据clazz的需求，从对象r中提取信息，并填写和返回clazz对象
	   * 
	   * @param r  request对象，其中包含clazz中所需的信息
	   * @param clazz  一个泛型对象，应该以RequestParams的注释来修饰
	   * @return       返回clazz类型的对象，其中包含了从request对象中获取的信息
	   * @throws TypeException 如果clazz对象没有以RequestParams修饰，或者实例化出错等，则抛出异常
	   * 
	   * @see  RequestParams
	   * 
	   * @author m
	   */
	  public static <T> T formObjectFromRequest(
              HttpServletRequest r, Class<T> clazz) throws TypeException {
	    //
	    return formObjectFromRequest(r,clazz,false);
	  }
	  /**
	   * 根据clazz的需求，从对象r中提取信息，并填写和返回clazz对象
	   * 
	   * @param r  request对象，其中包含clazz中所需的信息
	   * @param clazz  一个泛型对象，应该以RequestParams的注释来修饰
	   * @return       返回clazz类型的对象，其中包含了从request对象中获取的信息
	   * @throws TypeException 如果clazz对象没有以RequestParams修饰，或者实例化出错等，则抛出异常
	   * 
	   * @see  RequestParams
	   * 
	   * @author m
	   */
	  public static <T> T formObjectFromRequest(
              HttpServletRequest r, Class<T> clazz, boolean isUrlEncode) throws TypeException {
	    // 1. 只有被RequestParams注释的对象，才需要进行处理，否则抛出异常
	    if(!clazz.isAnnotationPresent(RequestParams.class)) {
	      throw new TypeException("The class " + clazz.getName() 
	          + " is not annotation with RequestParams, please check!" );
	    }
	    
	    // 2. 形成对象
	    T obj;
	    try {
	      obj = (T) clazz.newInstance();
	    } catch (Exception e1) {
	      throw new TypeException("Cannot create the instance of Class " + clazz.getName() );
	    }
	    
	    // 3. 获取对象字段信息
	    Map params = r.getParameterMap();
	    Field[] fl;
	    try {
	      fl = Class.forName(clazz.getName()).getDeclaredFields();
	    } catch (Exception e) {
	      throw new TypeException("Cannot get the declared fields of Class " + clazz.getName() );
	    } 
	    // 4. 遍历字段，根据其字段名称或者RequestType中的name属性，
	    //    在request中查找（不区分大小写），如果找到，则为该字段赋值
	    Iterator<String> iter;
	    for (Field f : fl) {
	      //if is static,final then continue
	      if(Modifier.isStatic(f.getModifiers()) || Modifier.isFinal(f.getModifiers()))
	        continue;
	      String fieldname = f.getName();
	      String paramName = "";
	      if(f.isAnnotationPresent(RequestType.class)
	          && f.getAnnotation(RequestType.class).name().length()>0) {
	        paramName = f.getAnnotation(RequestType.class).name();
	      }
	      
	      Object value = null;
	      iter = params.keySet().iterator();
	      while (iter.hasNext()) {
	        String key = iter.next();
	        // check either the name of the RequestType or the name of the field
	        if (fieldname.equalsIgnoreCase(key) 
	            || paramName.length()>0&&paramName.equalsIgnoreCase(key)) {
	          value = (String) r.getParameter(key);
	          //判断是否url编码
	          if(isUrlEncode){
	            try {
	              value = JEncodingUtil.urlDecode(String.valueOf(value));
	            } catch (UnsupportedEncodingException e) {
	              // TODO Auto-generated catch block
	              throw new TypeException(e.getMessage());
	            }
	          }
	          break;
	        }
	      }
	     
	      if(value==null){
	    	  if(f.isAnnotationPresent(RequestType.class)
	    	          && f.getAnnotation(RequestType.class).required()==false){
	    		  continue;
	    	  }
	      }
	      
	      try {
	        f.setAccessible(true);        
	        //
	        //f.set(obj, value);
	        //类型判断
	        if(f.getType().equals(String.class))
	        {
	          f.set(obj,value);
	        }else if(f.getType().equals(String[].class))
	        {   
	          f.set(obj,DBUtil.getDBString(value).split(","));
	        }else if(f.getType().equals(int.class))
	        {   
	        	
	           f.setInt(obj,DBUtil.getDBInt(value));   
	        }else if(f.getType().equals(Integer.class))
	        {   
		       f.set(obj,new Integer(DBUtil.getDBInt(value)));  
		       
		    }else if(f.getType().equals(long.class))
	        {   
	           f.setLong(obj,DBUtil.getDBLong(value));   
	        }else if(f.getType().equals(Long.class))
	        {   
		       f.set(obj,new Long(DBUtil.getDBLong(value)));  
		       
		    }else if(f.getType().equals(double.class))
	        {   
		    	System.out.println(f.getType());
	           f.setDouble(obj,DBUtil.getDBDouble(value));   
	        }else if(f.getType().equals(Double.class))
	        {   
		       f.set(obj,new Double(DBUtil.getDBDouble(value)));  
		       
		    }else{
	          //其他类型设置，默认用字符串方式
	          f.set(obj,value);  
	        }
	        //
	      } catch (Exception e) {
	    	  e.printStackTrace();
	        throw new TypeException(String.format("Cannot set the value of field %s#%s", 
	            clazz.getName(), f.getName()));
	      } 
	    }
	    return obj;
	  }
}
