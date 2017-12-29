package com.ddgame.impl;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;

import com.ddgame.sdk.utils.Base64;
import com.ddgame.sdk.utils.MD5;
import com.ddgame.utils.DeviceUtil;
import com.ddgame.utils.Sputils;
import com.ddgame.utils.Yibuhttputils;
import com.qq.e.track.GDTTracker;
import com.qq.e.track.TrackConstants;



public class GuangdiantongUtils {

	
	/**
	 * 新激�?
	 * @param mcontext
	 */
	public static void guangDiantongInit(Context mcontext){
		String sPstring = DeviceUtil.getGameInfo(mcontext, "guangdiantong");
		if (!sPstring.equals("no")) {

			  /**
	         * SDK初始化接口：必须在Application的onCreate中成功调用一次，否则其他接口都将无法使用�?
	         * 注意：只�?要调用一次即可，第一次调用之后的任何调用都将无效。Logcat中会有初始化信息输出，请�?发�?�注意�??
	         *
	         * @param context   applicationContext
	         * @param channel   渠道标识，注意：目前仅支持传�?"TrackConstants.APP_CHANNEL"类中预定义的渠道标识，传入其他字符串代表的渠道将无法被统计�??
	         */
	        GDTTracker.init(mcontext, TrackConstants.APP_CHANNEL.OPEN_APP);

	        /**
	         * SDK�?活上报接口：Application创建的时候是�?活接口调用的�?佳时机，调用此接口会向广点�?�上报激活事件�??
	         * 注意�?30天内App被打�?只会被记录为�?次激活�??
	         *
	         * @param context   applicationContext
	         */
	        GDTTracker.activateApp(mcontext);
		}
		
		
	}
	
	/**
	 * 注册
	 * @param mcontext
	 */
	public static void guangDiantongRegister(Context mcontext){
		String sPstring = DeviceUtil.getGameInfo(mcontext, "guangdiantong");
		if (!sPstring.equals("no")) {

			 GDTTracker.logEvent(mcontext, TrackConstants.CONVERSION_TYPE.REGISTER);
		}
		
		
	}
	
	
	/**
	 * 交易�?
	 * @param mcontext
	 */
	public static void guangDiantongGiveMoney(Context mcontext,String money){
		String sPstring = DeviceUtil.getGameInfo(mcontext, "guangdiantong");
		if (!sPstring.equals("no")) {

			 GDTTracker.logEvent(mcontext, TrackConstants.CONVERSION_TYPE.PURCHASE,Integer.parseInt(money));
		}
		
		
	}
	
	
	
	
	
	
	/**
	 * 上报�?活到广点�?
	 * @param conv_type
	 * @return
	 */
	public static void getGuangdiantong(final Context mcontext,final String conv_type){
		
		String gameInfo = DeviceUtil.getGameInfo(mcontext, "guangdiantong");
		if (!gameInfo.equals("yes")) {
			return;
		}
		long startPaintLogoTime=System.currentTimeMillis()/1000;
		//System.out.println("startPaintLogoTime="+startPaintLogoTime);
		String qqappid=DeviceUtil.getGameInfo(mcontext, "androidAppId");
		//System.out.println("appid="+qqappid);
		String muid= pingjieMuid(DeviceUtil.getIMEI(mcontext));
		String sign_key=DeviceUtil.getGameInfo(mcontext, "sign_key") ;
		//System.out.println("sign_key="+sign_key);
		String advertiser_id=DeviceUtil.getGameInfo(mcontext, "advertiser_id");
		//System.out.println("advertiser_id="+advertiser_id);
		//System.out.println("conv_type="+conv_type);
		String encrypt_key=DeviceUtil.getGameInfo(mcontext, "encrypt_key");
		//System.out.println("encrypt_key="+encrypt_key);
		String pingjieUrl = pingjieUrl(qqappid,muid, startPaintLogoTime+"", null,sign_key, conv_type, "UNIONANDROID", advertiser_id, encrypt_key);
		//return pingjieUrl;
		Yibuhttputils yibuhttputils = new Yibuhttputils() {
			
			@Override
			public void sucee(String str) {
				// TODO Auto-generated method stub
				System.out.println("广点通上报数据返回结�?"+str);
				try {
					JSONObject jsonObject = new JSONObject(str);
					int int1 = jsonObject.getInt("ret");
					if (int1==0) {
						Sputils.putSPstring(conv_type, "yes", mcontext);
						System.out.println("广点通激活成�?"+str);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			@Override
			public void faile(String err, String rescode) {
				// TODO Auto-generated method stub
				//System.out.println("广点通上报数据失败返回结�?"+err);
				
			}
		};
		yibuhttputils.runHttp(pingjieUrl, "", "GET", "");
		
	}
	
	
	/**
	 * 
	 * @param appid
	 *            应用id
	 * @param muid
	 *            IME的md5�?
	 * @param conv_time
	 *            发生转化的时�?
	 * @param client_ip
	 *            IP可以为空
	 * @param sign_key
	 *            平台提供的sign_key
	 * @param conv_type
	 *            上报�?么数�?
	 *            �?�?
	* 	MOBILEAPP_ACTIVITE
	*	注册
	*	MOBILEAPP_REGISTER
	*	加入购物�?
	*	MOBILEAPP_ADDTOCART
	*	付费行为
	*	MOBILEAPP_COST
	 * @param apptype  填入 ANDROID
	 * 
	 * @param advertiser_id  广告主标�?
	 * 
	 * @param encrypt_key  平台提供的encrypt_key
	 * @return 
	 */
	public static String pingjieUrl(String appid, String muid,
			String conv_time, String client_ip, String sign_key,
			String conv_type, String apptype, String advertiser_id,String encrypt_key) {
		String query_string;
		
		if (client_ip != null) {
			query_string = "muid=" + muid + "&conv_time=" + conv_time
					+ "&client_ip=" + client_ip;
		} else {
			query_string = "muid=" + muid + "&conv_time=" + conv_time;
		}

		String page = "http://t.gdt.qq.com/conv/app/" + appid + "/conv?"
				+ query_string;

		String encode_page = java.net.URLEncoder.encode(page);
		//System.out.println("encode_page=" + encode_page);

		String property = sign_key + "&GET&" + encode_page;
		//System.out.println("property=" + property);

		String md5_property = "123";
		
		try {
			md5_property = MD5.MD5(property);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//System.out.println("md5_property=" + md5_property);

		String base_data = query_string + "&sign=" + md5_property;
		String en_base_data = xorEncryption(base_data, encrypt_key);
		//System.out.println("base_data=" + en_base_data);

		String finalurl = "http://t.gdt.qq.com/conv/app/" + appid + "/conv?v="
				+ java.net.URLEncoder.encode(en_base_data) + "&conv_type="
				+ conv_type + "&app_type=" + apptype + "&advertiser_id="
				+ advertiser_id;

		//System.out.println("finalurl=" + finalurl);
		return finalurl;
	}

	public static String xorEncryption(String info, String key) {
		byte[] result = new byte[info.length()];
		if (TextUtils.isEmpty(info) || TextUtils.isEmpty(key)) {
			return null;
		}
		for (int i = 0, j = 0; i < info.length(); ++i) {
			result[i] = (byte) (info.charAt(i) ^ key.charAt(j));
			j = (++j) % (key.length());
		}
		return Base64.encode(result);
	}

	public static String pingjieMuid(String IMEI) {
		String md5 = "123";
		//System.out.println("IMEI=" + IMEI);
		try {
			md5 = MD5.MD5(IMEI);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println("muid=" + md5);
		return md5.toLowerCase();
	}

}
