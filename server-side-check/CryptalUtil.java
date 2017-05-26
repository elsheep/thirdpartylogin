package me.huile.commonprojects.utils.util;

import java.lang.reflect.Method;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class CryptalUtil {
	public static String encodeBase64(byte[]input) throws Exception{  
        Class clazz=Class.forName("com.sun.org.apache.xerces.internal.impl.dv.util.Base64");  
        Method mainMethod= clazz.getMethod("encode", byte[].class);  
        mainMethod.setAccessible(true);  
         Object retObj=mainMethod.invoke(null, new Object[]{input});  
         return (String)retObj;  
    }
	
	public static byte[] hamcsha1(byte[] data, byte[] key) 
	{
	      try {
	          SecretKeySpec signingKey = new SecretKeySpec(key, "HmacSHA1");
	          Mac mac = Mac.getInstance("HmacSHA1");
	          mac.init(signingKey);
	          return mac.doFinal(data);
	      } catch (Exception e) {
	           e.printStackTrace();
	      }
	     return null;
	 }
}
