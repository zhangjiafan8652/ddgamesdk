package com.ddgame.impl;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;

import com.ddgame.proxy.YYWActivityStub;
import com.ddgame.sdk.utils.LogoWindow;
import com.ddgame.utils.Handle;
import com.ddgame.utils.Sputils;
import com.tencent.ysdk.api.YSDKApi;
import com.yayawan.impl.qqhelper.QqYsdkHelp;


public class ActivityStubImpl implements YYWActivityStub {

	@Override
	public void applicationInit(Activity paramActivity) {
		// TODO Auto-generated method stub

	}

	private Activity mActivity;
	public static ProgressDialog mAutoLoginWaitingDlg;

	@Override
	public void onCreate(Activity paramActivity) {
		// TODO Auto-generated method stub
		Myconstants.mpayinfo=new Payinfo();
		Handle.active_handler(paramActivity);
		
		//广点通激活
		GuangdiantongUtils.guangDiantongInit(paramActivity.getApplicationContext());
		
		mActivity = paramActivity;
		QqYsdkHelp.onCreate(paramActivity);
		QqYsdkHelp.inintsdk(paramActivity);
		YSDKApi.handleIntent(paramActivity.getIntent());
	}

	@Override
	public void onStop(Activity paramActivity) {
		// TODO Auto-generated method stub
		if (Sputils.getSPint("center_status", 0, paramActivity)==2) {
			LogoWindow.getInstants(paramActivity).Stop();
		}
		
		YSDKApi.onStop(paramActivity);
	}

	@Override
	public void onResume(Activity paramActivity) {
		// TODO Auto-generated method stub
		if (Sputils.getSPint("center_status", 0, paramActivity)==2) {
			LogoWindow.getInstants(paramActivity).start();
		}
		
		YSDKApi.onResume(paramActivity);
	}

	@Override
	public void onPause(Activity paramActivity) {
		// TODO Auto-generated method stub
		// Utilsjf.stopDialog();
		YSDKApi.onPause(paramActivity);
	}

	@Override
	public void onRestart(Activity paramActivity) {
		// TODO Auto-generated method stub
		YSDKApi.onRestart(paramActivity);
	}

	@Override
	public void onDestroy(Activity paramActivity) {
		//YSDKApi.logout();
		YSDKApi.onDestroy(paramActivity);
	}

	@Override
	public void applicationDestroy(Activity paramActivity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onActivityResult(Activity paramActivity, int paramInt1,
			int paramInt2, Intent paramIntent) {
		// TODO Auto-generated method stub
		YSDKApi.onActivityResult(paramInt1, paramInt2, paramIntent);
	}

	@Override
	public void onNewIntent(Intent paramIntent) {

		// TODO Auto-generated method stub
		// Logger.d("onNewIntent");
		System.out.println("onNewIntent");
		// TODO GAME 处理游戏被拉起的情况
		// launchActivity的onCreat()和onNewIntent()中必须调用
		// YSDKApi.handleCallback()。否则会造成微信登录无回调
		
		YSDKApi.handleIntent(paramIntent);
		
	}

	@Override
	public void initSdk(Activity arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStart(Activity arg0) {
		// TODO Auto-generated method stub
		
	}

}
