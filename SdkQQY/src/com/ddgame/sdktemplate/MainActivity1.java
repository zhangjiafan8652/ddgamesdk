package com.ddgame.sdktemplate;

import java.util.UUID;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

import com.ddgame.callback.YYWAnimCallBack;
import com.ddgame.callback.YYWExitCallback;
import com.ddgame.callback.YYWPayCallBack;
import com.ddgame.callback.YYWUserCallBack;
import com.ddgame.domain.YYWOrder;
import com.ddgame.domain.YYWUser;
import com.ddgame.proxy.GameProxy;

public class MainActivity1 extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		GameProxy.getInstent().onCreate(this);
		// setContentView(R.layout.sdk_activity_main);
		System.out.println("oncreate");
		final LinearLayout mLinearLayout = new LinearLayout(this);

		mLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		mLinearLayout.setOrientation(LinearLayout.VERTICAL);
		mLinearLayout.setPadding(10, 10, 10, 10);

		Button animButton = new Button(this);
		animButton.setText("anim");
		animButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				anim(mLinearLayout);

			}
		});
		mLinearLayout.addView(animButton, new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

		Button lgoinButton = new Button(this);
		lgoinButton.setText("login");
		lgoinButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				login(mLinearLayout);

			}
		});
		mLinearLayout.addView(lgoinButton, new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

		Button payButton = new Button(this);
		payButton.setText("pay");
		payButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pay(mLinearLayout);

			}
		});
		mLinearLayout.addView(payButton, new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

		Button extButton = new Button(this);
		extButton.setText("exit");
		extButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				exit(mLinearLayout);

			}
		});
		mLinearLayout.addView(extButton, new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		setContentView(mLinearLayout);

	}

	public void anim(View v) {
		System.out.println("登录");
		GameProxy.getInstent().anim(this, new YYWAnimCallBack() {

			@Override
			public void onAnimSuccess(String arg0, Object arg1) {
				// TODO Auto-generated method stub
				Toast.makeText(MainActivity1.this, "播放动画回调", Toast.LENGTH_SHORT)
						.show();
			}

			@Override
			public void onAnimFailed(String arg0, Object arg1) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimCancel(String arg0, Object arg1) {
				// TODO Auto-generated method stub

			}
		});
	}

	public void login(View v) {
		System.out.println("登录");
		GameProxy.getInstent().login(this, new YYWUserCallBack() {

			@Override
			public void onLogout(Object arg0) {
				System.out.println("登出");

			}

			@Override
			public void onLoginSuccess(YYWUser user, Object arg1) {
				// TODO Auto-generated method stub
				System.out.println(user);
				Toast.makeText(MainActivity1.this, "登录回调" + user,
						Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onLoginFailed(String arg0, Object arg1) {
				// TODO Auto-generated method stub
				System.out.println("登录失败");
			}

			@Override
			public void onCancel() {
				// TODO Auto-generated method stub
				
			}
		});
	}

	public void pay(View v) {
		YYWOrder order = new YYWOrder(UUID.randomUUID().toString(), "霜之�?�?",
				100l, "xxxx");
		GameProxy.getInstent().pay(this, order, new YYWPayCallBack() {

			@Override
			public void onPaySuccess(YYWUser arg0, YYWOrder arg1, Object arg2) {
				// TODO Auto-generated method stub
				Toast.makeText(MainActivity1.this, "充�?�成功回�?", Toast.LENGTH_SHORT)
						.show();
			}

			@Override
			public void onPayFailed(String arg0, Object arg1) {
				// TODO Auto-generated method stub
				System.out.println("支付失败");
			}

			@Override
			public void onPayCancel(String arg0, Object arg1) {
				// TODO Auto-generated method stub
				System.out.println("支付�?�?");
			}
		});
	}

	public void exit(View v) {
		System.out.println("�?�?");
		GameProxy.getInstent().exit(this, new YYWExitCallback() {

			@Override
			public void onExit() {
				Toast.makeText(MainActivity1.this, "�?出回�?", Toast.LENGTH_SHORT)
						.show();
			}
		});
	}

	public void accountManage(View v) {
		GameProxy.getInstent().manager(this);
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		GameProxy.getInstent().onRestart(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		GameProxy.getInstent().onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		GameProxy.getInstent().onPause(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		GameProxy.getInstent().onStop(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		GameProxy.getInstent().onDestroy(this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		GameProxy.getInstent().onActivityResult(this, requestCode, resultCode,
				data);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		GameProxy.getInstent().onNewIntent(intent);
		super.onNewIntent(intent);
		// System.out.println("onNewIntent");
		// WGPlatform.handleCallback(intent);
	
	}

	private void nidongde() {
	}

}