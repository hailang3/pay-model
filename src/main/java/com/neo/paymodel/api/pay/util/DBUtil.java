package com.neo.paymodel.api.pay.util;


import java.sql.Timestamp;
import java.util.Date;


public final class DBUtil {
  /**
   * @param o
   * @return
   * @throws DBTypeException
   */
  public static byte getDBByte(Object o) throws ServiceException {
    byte ret = 0;
    if (o != null) {      
      try{
        ret = Byte.parseByte(String.valueOf(o));
      }catch(Exception e){
        throw new ServiceException("can not convert db type to byte");
      }   
    }
    return ret;
  }
  /**
   * @param o
   * @return
   * @throws DBTypeException
   */
  public static int getDBInt(Object o) throws ServiceException {
    int ret = 0;
    if (o != null) {
      try{
        ret = Integer.parseInt(String.valueOf(o));
      }catch(Exception e){
        throw new ServiceException("can not convert db type to int");
      }     
    }
    return ret;
  }
  /**
   * @param o
   * @return
   * @throws DBTypeException
   */
  public static double getDBDouble(Object o) throws ServiceException {
    double ret = 0;
    if (o != null) {      
      try{
        ret = Double.parseDouble(String.valueOf(o));
      }catch(Exception e){
        throw new ServiceException("can not convert db type to double");
      }
    }
    return ret;
  }
  /**
   * @param o
   * @return
   * @throws DBTypeException
   */
  public static boolean getDBBoolean(Object o) throws ServiceException {
    boolean ret = false;
    if (o != null) {
      try{
        ret = Boolean.parseBoolean(String.valueOf(o));
      }catch(Exception e){
        throw new ServiceException("can not convert db type to boolean");
      }
    }
    return ret;
  }
  /**
   * @param o
   * @return
   * @throws DBTypeException
   */
  public static long getDBLong(Object o) throws ServiceException {
    long ret = 0;
    if (o != null) {      
      try{
        ret = Long.parseLong(String.valueOf(o));
      }catch(Exception e){
        throw new ServiceException("can not convert db type to long");
      }      
    }
    return ret;
  }
  /**
   * @param o
   * @return
   * @throws DBTypeException
   */
  public static String getDBString(Object o) {
    if(o == null){
      return "";
    }
    return String.valueOf(o);   
  }
  /**
   * @param o
   * @return
   * @throws DBTypeException
   */
  public static Timestamp getDBTimeStamp(Object o) throws ServiceException {
    Timestamp ret = null;
    if (o != null) {
      try{
        ret = new Timestamp(((Date) o).getTime());
      }catch(Exception e){
        throw new ServiceException("can not convert db type to Timestamp");
      }
    }
    return ret;
  }
}
