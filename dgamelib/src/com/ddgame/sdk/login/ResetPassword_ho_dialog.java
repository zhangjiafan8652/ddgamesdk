package com.ddgame.sdk.login;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ddgame.sdk.bean.User;
import com.ddgame.sdk.db.UserDao;
import com.ddgame.sdk.utils.AuthNumReceiver;
import com.ddgame.sdk.utils.Basedialogview;
import com.ddgame.sdk.utils.CodeCountDown;
import com.ddgame.sdk.utils.CounterDown;
import com.ddgame.sdk.utils.Utilsjf;
import com.ddgame.sdk.utils.AuthNumReceiver.MessageListener;
import com.ddgame.sdk.xml.GetAssetsutils;
import com.ddgame.sdk.xml.MachineFactory;
import com.ddgame.sdkmain.AgentApp;
import com.ddgame.utils.DeviceUtil;
import com.ddgame.utils.Yayalog;
import com.lidroid.jxutils.HttpUtils;
import com.lidroid.jxutils.exception.HttpException;
import com.lidroid.jxutils.http.RequestParams;
import com.lidroid.jxutils.http.ResponseInfo;
import com.lidroid.jxutils.http.callback.RequestCallBack;
import com.lidroid.jxutils.http.client.HttpRequest.HttpMethod;

public class ResetPassword_ho_dialog extends Basedialogview {

	private LinearLayout ll_mPre;
	private ImageButton iv_mPre;
	private EditText et_mPhone;
	private Button bt_mGetsecurity;
	private EditText et_mSecurity;
	private EditText et_mNewpassword;
	private Button bt_mOk;
	private String mUserName;
	private String mCode;
	private String mResult;
	private String mNewPassword;
	private static final int AUTHCODE = 3;

	private static final int RESULT = 7;

	private static final int LOGINFINDRESULT = 8;

	protected static final int ERROR = 9;
	private boolean flag;
	private CodeCountDown mCodeCountDown;

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			Utilsjf.stopDialog();
			switch (msg.what) {
			case AUTHCODE:
				String result = (String) msg.obj;
				bt_mGetsecurity.setEnabled(false);
				flag = true;
				Toast.makeText(mActivity, result, Toast.LENGTH_LONG).show();
				/*
				 * if (mCodeCountDown == null) { mCodeCountDown = mCodeCountDown
				 * = new CodeCountDown(60000,1000, bt_mGetsecurity); }
				 */
				mCountDown.startCounter();
				// mCounterdown.startCounter();
				break;
			case RESULT:
				if ("操作成功".equals(mResult)) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							mActivity);
					builder.setMessage("您的新密码为:" + mNewPassword + ",请牢记");
					builder.setNegativeButton("取消",
							new AlertDialog.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub

								}

							});
					builder.setPositiveButton("立即登录",
							new AlertDialog.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {
									// 将修改后的数据写入到数据库
									UserDao.getInstance(mActivity).writeUser(
											mUserName, mNewPassword, "");
									Utilsjf.creDialogpro(mActivity, "正在登陆...");
									/*
									 * new Thread() {
									 * 
									 * @Override public void run() { try { User
									 * user = ObtainData.login( mActivity,
									 * mUserName, mNewPassword); Message message
									 * = new Message(); message.obj = user;
									 * message.what = LOGINFINDRESULT;
									 * mHandler.sendMessage(message); } catch
									 * (Exception e) { e.printStackTrace(); } }
									 * }.start();
									 */
								}
							});
					builder.create().show();

				} else {
					Toast.makeText(mActivity, mResult, Toast.LENGTH_SHORT)
							.show();
				}
				break;
			case LOGINFINDRESULT:
				User user = (User) msg.obj;
				if (user != null) {
					if (user.success == 0) {
						// 登录成功
						/*
						 * Toast.makeText(mActivity, user.body,
						 * Toast.LENGTH_SHORT) .show();
						 */
						// 将用户信息保存到全局变量
						AgentApp.mUser = user;
						// 开启悬浮窗服务
						onSuccess(user, 1);
						// YayaWan.init(mActivity);
						Login_success_dialog login_success_dialog = new Login_success_dialog(
								mActivity);
						login_success_dialog.dialogShow();
					} else {
						Toast.makeText(mActivity, user.body, Toast.LENGTH_SHORT)
								.show();
					}
				}

				break;
			case ERROR:
				Toast.makeText(mActivity, "网络连接错误,请重新连接", Toast.LENGTH_SHORT)
						.show();
				break;
			default:
				break;
			}
		}
	};
	private AuthNumReceiver mAuthNumReceiver;
	private CounterDown mCounterdown;
	private CounterDown mCountDown;

	public ResetPassword_ho_dialog(Activity activity) {
		super(activity);
	}

	@Override
	public void createDialog(Activity mActivity) {

		onStart();

		dialog = new Dialog(mActivity);

		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		int ho_height = 650;
		int ho_with = 750;
		int po_height = 650;
		int po_with = 700;

		int height = 0;
		int with = 0;
		// 设置横竖屏
		String orientation = DeviceUtil.getOrientation(mContext);
		if (orientation == "") {

		} else if ("landscape".equals(orientation)) {
			height = ho_height;
			with = ho_with;
		} else if ("portrait".equals(orientation)) {
			height = po_height;
			with = po_with;
		}

		baselin = new LinearLayout(mActivity);
		baselin.setOrientation(LinearLayout.VERTICAL);
		MachineFactory machineFactory = new MachineFactory(mActivity);
		machineFactory.MachineView(baselin, with, height, "LinearLayout");
		baselin.setBackgroundColor(Color.TRANSPARENT);
		baselin.setGravity(Gravity.CENTER_VERTICAL);

		// 过度中间层
		LinearLayout ll_content = new LinearLayout(mActivity);
		machineFactory.MachineView(ll_content, with, height, "LinearLayout", 2,
				25);
		ll_content.setBackgroundColor(Color.WHITE);
		ll_content.setGravity(Gravity.CENTER_HORIZONTAL);
		ll_content.setOrientation(LinearLayout.VERTICAL);

		// 标题栏
		RelativeLayout rl_title = new RelativeLayout(mActivity);
		machineFactory.MachineView(rl_title, MATCH_PARENT, 96, mLinearLayout);
		rl_title.setBackgroundColor(Color.parseColor("#999999"));

		ll_mPre = new LinearLayout(mActivity);
		machineFactory.MachineView(ll_mPre, 96, MATCH_PARENT, 0,
				mRelativeLayout, 0, 0, 0, 0, RelativeLayout.CENTER_VERTICAL);
		ll_mPre.setGravity(Gravity_CENTER);
		ll_mPre.setClickable(true);
		// 返回上一层的图片
		iv_mPre = new ImageButton(mActivity);
		machineFactory.MachineView(iv_mPre, 40, 40, 0, mLinearLayout, 0, 0, 0,
				0, RelativeLayout.CENTER_VERTICAL);
		iv_mPre.setClickable(false);

		iv_mPre.setBackgroundDrawable(GetAssetsutils.getDrawableFromAssetsFile(
				"yaya_pre.png", mActivity));
		ll_mPre.addView(iv_mPre);
		// 设置点击事件.点击窗口消失
		ll_mPre.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		// 注册textview
		TextView tv_zhuce = new TextView(mActivity);
		machineFactory.MachineTextView(tv_zhuce, MATCH_PARENT, MATCH_PARENT, 0,
				"密码重设", 38, mLinearLayout, 0, 0, 0, 0);
		tv_zhuce.setTextColor(Color.WHITE);
		tv_zhuce.setGravity(Gravity_CENTER);

		// TODO
		rl_title.addView(ll_mPre);
		rl_title.addView(tv_zhuce);

		// 中间内容层
		LinearLayout ll_content1 = new LinearLayout(mActivity);
		ll_content1 = (LinearLayout) machineFactory.MachineView(ll_content1,
				660, MATCH_PARENT, 0, mLinearLayout, 0, 70, 0, 0,
				LinearLayout.VERTICAL);
		ll_content1.setOrientation(LinearLayout.VERTICAL);

		// 手机号码输入行
		LinearLayout ll_phone = new LinearLayout(mActivity);
		ll_phone = (LinearLayout) machineFactory.MachineView(ll_phone,
				MATCH_PARENT, 96, mLinearLayout);

		// 手机号码输入框
		et_mPhone = new EditText(mActivity);
		machineFactory.MachineEditText(et_mPhone, 400, MATCH_PARENT, 0,
				"请输入用户名或者手机号", 32, mLinearLayout, 0, 0, 0, 0);
		et_mPhone
				.setBackgroundDrawable(GetAssetsutils
						.get9DrawableFromAssetsFile("yaya_biankuang2.9.png",
								mActivity));
		et_mPhone.setPadding(machSize(20), 0, 0, 0);

		// 获取验证码按钮
		bt_mGetsecurity = new Button(mActivity);
		bt_mGetsecurity = machineFactory.MachineButton(bt_mGetsecurity, 240,
				MATCH_PARENT, 0, "获取验证码", 32, mLinearLayout, 30, 0, 0, 0);
		bt_mGetsecurity.setTextColor(Color.WHITE);
		bt_mGetsecurity.setBackgroundDrawable(GetAssetsutils.crSelectordraw(
				"yaya_bulebutton.9.png", "yaya_bulebutton1.9.png", mActivity));

		// TODO
		ll_phone.addView(et_mPhone);
		ll_phone.addView(bt_mGetsecurity);

		// 验证码输入框
		et_mSecurity = new EditText(mActivity);
		machineFactory.MachineEditText(et_mSecurity, MATCH_PARENT, 96, 0,
				"请输入验证码(未绑定手机用户,请登录游戏在个人中心绑定)", 32, mLinearLayout, 0, 20, 0, 0);
		et_mSecurity
				.setBackgroundDrawable(GetAssetsutils
						.get9DrawableFromAssetsFile("yaya_biankuang2.9.png",
								mActivity));
		et_mSecurity.setPadding(machSize(20), 0, 0, 0);

		// 验证码输入框
		et_mNewpassword = new EditText(mActivity);
		machineFactory.MachineEditText(et_mNewpassword, MATCH_PARENT, 96, 0,
				"请输入密码", 32, mLinearLayout, 0, 20, 0, 0);
		et_mNewpassword
				.setBackgroundDrawable(GetAssetsutils
						.get9DrawableFromAssetsFile("yaya_biankuang2.9.png",
								mActivity));
		et_mNewpassword.setPadding(machSize(20), 0, 0, 0);

		// 确定按钮
		bt_mOk = new Button(mActivity);
		machineFactory.MachineButton(bt_mOk, MATCH_PARENT, 96, 0, "确认", 36,
				mLinearLayout, 0, 30, 0, 0);
		bt_mOk.setTextColor(Color.WHITE);
		bt_mOk.setBackgroundDrawable(GetAssetsutils.crSelectordraw(
				"yaya_yellowbutton.9.png", "yaya_yellowbutton1.9.png",
				mActivity));
		bt_mOk.setGravity(Gravity_CENTER);

		// TODO
		ll_content1.addView(ll_phone);
		ll_content1.addView(et_mSecurity);
		ll_content1.addView(et_mNewpassword);

		ll_content1.addView(bt_mOk);

		ll_content.addView(rl_title);

		ll_content.addView(ll_content1);

		baselin.addView(ll_content);

		dialog.setContentView(baselin);

		Window dialogWindow = dialog.getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		dialogWindow.setGravity(Gravity.CENTER);

		lp.alpha = 0.9f; // 透明度

		lp.dimAmount = 0.5f; // 设置背景色对比度
		dialogWindow.setAttributes(lp);
		dialog.setCanceledOnTouchOutside(false);

		android.widget.RelativeLayout.LayoutParams ap2 = new android.widget.RelativeLayout.LayoutParams(
				android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT,
				android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT);

		dialog.setCanceledOnTouchOutside(true);
		dialog.getWindow().setBackgroundDrawable(new BitmapDrawable());

		initlogic();
	}

	private void initlogic() {
		// mCounterdown = CounterDown.getInstance();
		// mCounterdown.setView(bt_mGetsecurity);
		mCountDown = CounterDown.getInstance();
		mCountDown.setView(bt_mGetsecurity);
		bt_mGetsecurity.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mUserName = et_mPhone.getText().toString().trim();
				if ("".equals(mUserName) || mUserName.length() == 0) {
					Toast.makeText(mActivity, "请输入用户名信息", Toast.LENGTH_SHORT)
							.show();
				} else {
					Utilsjf.creDialogpro(mActivity, "正在获取验证码...");

					RequestParams rps = new RequestParams();
					rps.addBodyParameter("type", 2 + "");
					rps.addBodyParameter("mobile", mUserName);

					HttpUtils httpUtils = new HttpUtils();
					httpUtils.send(HttpMethod.POST, ViewConstants.getphonecode,
							rps, new RequestCallBack<String>() {

								@Override
								public void onFailure(HttpException arg0,
										String arg1) {
									// TODO Auto-generated method stub
									Utilsjf.stopDialog();
									Toast.makeText(mActivity, "验证码发送失败，请检查网络",
											0).show();
								}

								@Override
								public void onSuccess(
										ResponseInfo<String> result) {
									// TODO Auto-generated method stub
									Utilsjf.stopDialog();
									Yayalog.loger(result.result);
									Toast.makeText(mActivity, "验证码已经发送", 0)
											.show();

								}
							});
				}
			}
		});

		bt_mOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mCode = et_mSecurity.getText().toString().trim();
				mUserName = et_mPhone.getText().toString().trim();
				if ("".equals(mUserName) || mUserName.length() < 0) {
					Toast.makeText(mActivity, "请输入用户名", Toast.LENGTH_SHORT)
							.show();
				} else if ("".equals(mCode) || mCode.length() < 0) {
					Toast.makeText(mActivity, "请输入验证码", Toast.LENGTH_SHORT)
							.show();
				} else {
					//
					mNewPassword = et_mNewpassword.getText().toString().trim();
					Utilsjf.creDialogpro(mActivity, "正在重设密码...");
					RequestParams rps = new RequestParams();
					rps.addBodyParameter("app_id",
							DeviceUtil.getAppid(mActivity));
					/*
					 * rps.addBodyParameter("imei",
					 * DeviceUtil.getIMEI(mActivity));
					 */
					rps.addBodyParameter("mobile", mUserName);
					rps.addBodyParameter("password", mNewPassword);
					rps.addBodyParameter("code", mCode);

					HttpUtils httpUtils = new HttpUtils();
					httpUtils.send(HttpMethod.POST,
							ViewConstants.resetpassword, rps,
							new RequestCallBack<String>() {

								@Override
								public void onFailure(HttpException arg0,
										String arg1) {
									// TODO Auto-generated method stub

									Utilsjf.stopDialog();
									Toast.makeText(mActivity, "请检查网络是否畅通", 0)
											.show();
								}

								@Override
								public void onSuccess(
										ResponseInfo<String> result) {
									// TODO Auto-generated method stub
									Utilsjf.stopDialog();
									Yayalog.loger("找回密码：" + result.result);
									Toast.makeText(mActivity, "修改成功", 0).show();

								}

							});
				}
			}
		});

	}

	public void onStart() {
		// 生成广播处理
		mAuthNumReceiver = new AuthNumReceiver();

		// 实例化过滤器并设置要过滤的广播
		IntentFilter intentFilter = new IntentFilter(
				"android.provider.Telephony.SMS_RECEIVED");
		intentFilter.setPriority(Integer.MAX_VALUE);
		// 注册广播
		mActivity.registerReceiver(mAuthNumReceiver, intentFilter);

		mAuthNumReceiver.setOnReceivedMessageListener(new MessageListener() {

			@Override
			public void onReceived(String message) {
				et_mSecurity.setText(message);
			}
		});

	}

}
