package com.ddgame.sdk.utils;

import java.math.BigInteger;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.ddgame.main.YYWMain;
import com.ddgame.sdk.bean.User;
import com.ddgame.sdk.callback.KgameSdkUserCallback;
import com.ddgame.sdk.db.UserDao;
import com.ddgame.sdk.login.Login_ho_dialog;
import com.ddgame.sdk.login.Login_success_dialog;
import com.ddgame.sdk.login.ViewConstants;
import com.ddgame.sdkmain.AgentApp;
import com.ddgame.sdkmain.DgameSdk;
import com.ddgame.utils.DeviceUtil;
import com.ddgame.utils.Sputils;
import com.ddgame.utils.Yayalog;
import com.lidroid.jxutils.HttpUtils;
import com.lidroid.jxutils.exception.HttpException;
import com.lidroid.jxutils.http.RequestParams;
import com.lidroid.jxutils.http.ResponseInfo;
import com.lidroid.jxutils.http.callback.RequestCallBack;
import com.lidroid.jxutils.http.client.HttpRequest.HttpMethod;

public class LoginUtils {

	private static final int FETCHSMS = 4;
	protected static final int FETCHSMS1 = 10;
	private static final int LOGINRESULT = 3; // 登陆返回
	protected static final int ERROR = 11;
	protected static final int SECRETLOGIN = 26;
	protected static final int LOGINSECURITYRESULT = 8;
	private static final int REGISTER = 5;

	private Activity mActivity;
	private Context mContext;
	private Basedialogview dialog;
	public static int STARTLOGIN = 1;
	public static int LOGINTYPE = 0;

	//
	public LoginUtils(Activity mActivity, Basedialogview dialog, int type) {
		this.mActivity = mActivity;
		this.mContext = mActivity;
		mUserCallback = DgameSdk.mUserCallback;
		this.LOGINTYPE = type;
		this.dialog = dialog;
	}

	public LoginUtils(Activity mActivity, Basedialogview dialog) {
		this.mActivity = mActivity;
		this.mContext = mActivity;
		mUserCallback = DgameSdk.mUserCallback;
		this.dialog = dialog;
	}

	public LoginUtils(Activity mActivity) {
		this.mActivity = mActivity;
		this.mContext = mActivity;
	}

	private User mUser;
	private KgameSdkUserCallback mUserCallback;
	private String mUsername;
	private String mPassword;

	/**
	 * 用户名手机验证码登录
	 * 
	 * @param name
	 * @param password
	 */
	public void codeLogin(final String mphone, final String code) {
		// 输入的用户名和密码符合要求
		// Utilsjf.creDialogpro(mActivity, "正在登陆...");
		if (LOGINTYPE != STARTLOGIN) {
			Utilsjf.creDialogpro(mActivity, "正在玩命登录...");
		}

	}

	/**
	 * 用户名密码登录
	 * 
	 * @param name
	 * @param password
	 */
	public void login(final String name, final String password) {
		// 输入的用户名和密码符合要求
		// Utilsjf.creDialogpro(mActivity, "正在登陆...");
		// 网络访问要在线程中
		mUsername = name;
		mPassword = password;
		if (LOGINTYPE != STARTLOGIN) {
			Utilsjf.creDialogpro(mActivity, "正在玩命登录...");
		}
		RequestParams rps = new RequestParams();
		rps.addBodyParameter("app_id", DeviceUtil.getAppid(mActivity));
		rps.addBodyParameter("username", mUsername);
		rps.addBodyParameter("password", mPassword);
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.POST, ViewConstants.loginurl, rps,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub
						Utilsjf.stopDialog();
						dialog.dialogDismiss();
						mUserCallback.onError(1);
						Toast.makeText(mActivity, "登陆失败，请检查网络是否畅通", 0).show();
					}

					@Override
					public void onSuccess(ResponseInfo<String> result) {
						// TODO Auto-generated method stub
						dialog.dialogDismiss();
						Utilsjf.stopDialog();
						Yayalog.loger("登陆结果" + result.result);
						User user = parserLoginResult(result.result);
						if (user == null) {
							mUserCallback.onError(1);
							
							Sputils.putSPint("ischanageacount", 0,
									ViewConstants.mMainActivity);
						}
						AgentApp.mUser = user;

						try {
							UserDao.getInstance(mActivity).writeUser(
									AgentApp.mUser.userName,
									AgentApp.mUser.password, AgentApp.mUser.secret);
							AgentApp.mUser.password = "";
							AgentApp.mUser.secret = "";
							//Yayalog.loger("登陆结果" + AgentApp.mUser.toString());

							Login_success_dialog login_success_dialog = new Login_success_dialog(
									mActivity);
							login_success_dialog.dialogShow();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							Sputils.putSPint("ischanageacount", 0,
									ViewConstants.mMainActivity);
							mUserCallback.onError(1);
							//mUserCallback.onLogout();
						}

						
					}

				});

	}

	private User parserLoginResult(String result) {
		// TODO Auto-generated method stub
		try {
			JSONObject jsonObject = new JSONObject(result);

			if (!result.contains("success")) {
				String errmsg = jsonObject.optString("err_msg");
				Toast.makeText(mActivity, errmsg, 0).show();
				return null;
			}
			JSONObject datas = jsonObject.getJSONObject("data");
			User user = new User();
			//System.out.println("+++++++++++++++"+datas.optString("uid"));
			// user.setPhone(datas.optString("mobile"));
			if (TextUtils.isEmpty(datas.optString("uid"))) {
				
				return null;
			}
			user.setUserName(datas.optString("username"));
			user.setToken(datas.optString("token"));
			user.setPassword(mPassword);
			user.setUid(new BigInteger(datas.optString("uid")));
			return user;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * 账号注册
	 * 
	 * @param name
	 * @param password
	 */
	public void register(final String mName, final String mPassword) {
		// 输入的用户名和密码符合要求
		// Utilsjf.creDialogpro(mActivity, "正在登陆...");
		// 网络访问要在线程中
		if (LOGINTYPE != STARTLOGIN) {
			Utilsjf.creDialogpro(mActivity, "正在玩命登录...");
		}
		

	}

	private void startlogin() {

		if (dialog != null) {
			dialog.dialogDismiss();
		}

		Login_ho_dialog login_ho_dialog = new Login_ho_dialog(mActivity);
		login_ho_dialog.dialogShow();

	}

	private void onSuccess(User mUser, int i) {
		if (mUserCallback != null) {
			mUserCallback.onSuccess(mUser, i);
		}
		mUserCallback = null;

	}

}
