package com.bld.state;

import android.content.Context;
import android.telephony.TelephonyManager;

/** 需要此权限<uses-permission android:name="android.permission.READ_PHONE_STATE"/> */
public class SIMInfo {
	// 获取手机号，有些手机可能无法获取到
	private String phoneId;
	// 获取sim状态
	private int simstate;
	/** sim良好 TelephonyManager.SIM_STATE_READY */
	/** 无sim TelephonyManager.SIM_STATE_ABSENT */
	private String simSerialNumber;
	// sim卡提供商代码
	private String simOperator;
	// sim卡提供商名称
	private String simOperatorName;
	// sim卡国别
	private String simCountryIso;
	// 手机串号
	private String deviceId;
	// 信号类型
	private int phoneType;
	/** 无信号 TelephonyManager.PHONE_TYPE_NONE */
	/** GSM信号TelephonyManager.PHONE_TYPE_GSM */
	/** CDMA信号TelephonyManager.PHONE_TYPE_CDMA */

	//imsi号
	private String subscriberId;
	// 运营商名称
	private String networkOperatorName;
	// 语音邮件识别符
	private String voiceMailAlphaTag;
	// 语音邮件号码
	private String voiceMailNumber;
	// 数据连接状态
	private int dataState;
	/** 已连接TelephonyManager.DATA_CONNECTED */
	/** 正在连接TelephonyManager.DATA_CONNECTING */
	/** 断开TelephonyManager.DATA_DISCONNECTED */
	/** 暂停TelephonyManager.DATA_SUSPENDED */
	// 数据活动状态
	private int dataActivity;

	/** 活动，正在接受数据TelephonyManager.DATA_ACTIVITY_IN */
	/** 活动，正在发送数据TelephonyManager.DATA_ACTIVITY_OUT */
	/** 活动，但无数据发送和接受TelephonyManager.DATA_ACTIVITY_INOUT */
	/** 活动，但无数据发送和接受TelephonyManager.DATA_ACTIVITY_NONE */

	public SIMInfo(Context context) {
		TelephonyManager manager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		phoneId = manager.getLine1Number();
		simstate = manager.getSimState();
		simSerialNumber = manager.getSimSerialNumber();
		simOperator = manager.getSimOperator();
		simOperatorName = manager.getSimOperatorName();
		simCountryIso = manager.getSimCountryIso();
		deviceId = manager.getDeviceId();
		phoneType = manager.getPhoneType();
		subscriberId = manager.getSubscriberId();
		networkOperatorName = manager.getNetworkOperatorName();
		voiceMailAlphaTag = manager.getVoiceMailAlphaTag();
		voiceMailNumber = manager.getVoiceMailNumber();
		dataState = manager.getDataState();
	
		dataActivity = manager.getDataActivity();


	}

	public String getPhoneId() {
		return phoneId;
	}

	

	public int getSimstate() {
		return simstate;
	}


	public String getSimSerialNumber() {
		return simSerialNumber;
	}



	public String getSimOperator() {
		return simOperator;
	}


	public String getSimOperatorName() {
		return simOperatorName;
	}



	public String getSimCountryIso() {
		return simCountryIso;
	}



	public String getDeviceId() {
		return deviceId;
	}



	public int getPhoneType() {
		return phoneType;
	}



	public String getSubscriberId() {
		return subscriberId;
	}


	public String getNetworkOperatorName() {
		return networkOperatorName;
	}



	public String getVoiceMailAlphaTag() {
		return voiceMailAlphaTag;
	}


	public String getVoiceMailNumber() {
		return voiceMailNumber;
	}


	public int getDataState() {
		return dataState;
	}



	public int getDataActivity() {
		return dataActivity;
	}



}
