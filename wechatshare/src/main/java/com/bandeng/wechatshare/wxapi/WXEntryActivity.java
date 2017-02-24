package com.bandeng.wechatshare.wxapi;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * Created by WangSummer on 15/11/16.
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
	// IWXAPI 是第三方app和微信通信的openapi接口
	private IWXAPI wxApi;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		try {
			ApplicationInfo info = this.getPackageManager().getApplicationInfo(getPackageName()
					, PackageManager.GET_META_DATA);
			String ID = info.metaData.getString("WXAPPID");
			wxApi = WXAPIFactory.createWXAPI(this, ID, true);
			wxApi.handleIntent(getIntent(), this);
		} catch (Exception e) {

		}
	}

	// 微信发送请求到第三方应用时，会回调到该方法
	@Override
	public void onReq(BaseReq arg0) {
		System.out.println("result+onReq  ==== ");
	}

	// 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
	@Override
	public void onResp(BaseResp resp) {
		String result = "";
		finish();
		switch (resp.errCode) {
			case BaseResp.ErrCode.ERR_OK:
				result = "分享成功";
				break;
			case BaseResp.ErrCode.ERR_USER_CANCEL:
				result = "取消分享";
				break;
			case BaseResp.ErrCode.ERR_AUTH_DENIED:
				result = "认证失败";
				break;
			default:
				result = "认证未知";
				break;
		}
		Toast.makeText(this, result, Toast.LENGTH_LONG).show();
	}
}