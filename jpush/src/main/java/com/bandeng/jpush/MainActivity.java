package com.bandeng.jpush;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.sms.SMSSDK;
import cn.jpush.sms.listener.SmscheckListener;
import cn.jpush.sms.listener.SmscodeListener;

import static android.view.View.*;

public class MainActivity extends AppCompatActivity implements OnClickListener {
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;


    private EditText et_inputPhoneNum;
    private Button btn_getVerifyCode;
    private EditText et_inputVerifyCode;
    private Button btn_startSign;
    private TimerTask timerTask;
    private Timer timer;
    private int timess;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getPermission();     // android 6.0获取权限

        /****************极光短息验证    从此开始***************/
        progressDialog = new ProgressDialog(this);
        SMSSDK.getInstance().initSdk(this);
        SMSSDK.getInstance().setDebugMode(true);

        et_inputPhoneNum = (EditText) findViewById(R.id.et_inputPhoneNum);
        et_inputVerifyCode = (EditText) findViewById(R.id.et_inputVerifyCode);
        btn_getVerifyCode = (Button) findViewById(R.id.btn_getVerifyCode);
        btn_startSign = (Button) findViewById(R.id.btn_startSign);

        // 获取验证码
        btn_getVerifyCode.setOnClickListener(this);
        // 开始验证
        btn_startSign.setOnClickListener(this);

    }

    private String getMCC() {
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String networkOperator = tm.getNetworkOperator();
        return !TextUtils.isEmpty(networkOperator) ? networkOperator : tm
                .getSimOperator();
    }

    /**定时器60秒到计时*/
    private void startTimer() {
        timess = 60;
        btn_getVerifyCode.setText(timess + "s");
        if (timerTask == null) {
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            timess--;
                            if (timess <= 0) {
                                stopTimer();
                                return;
                            }
                            btn_getVerifyCode.setText(timess + "s");
                        }
                    });
                }
            };
        }
        if (timer == null) {
            timer = new Timer();
        }
        timer.schedule(timerTask, 1000, 1000);
    }

    /**停止定时器*/
    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
        btn_getVerifyCode.setText("重新获取");
        btn_getVerifyCode.setClickable(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_getVerifyCode:    // 获取手机验证码

                String phoneNum = et_inputPhoneNum.getText().toString();
                if (TextUtils.isEmpty(phoneNum)) {
                    Toast.makeText(MainActivity.this, "请输入手机号码",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                btn_getVerifyCode.setClickable(false);
                // 开始倒计时
                startTimer();

                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.READ_PHONE_STATE)
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_PHONE_STATE},
                            MY_PERMISSIONS_REQUEST_CALL_PHONE);
                }

                // 得到校验码，并且执行回调
                SMSSDK.getInstance().getSmsCodeAsyn(phoneNum, ""+1 ,
                        new SmscodeListener() {
                            @Override
                            public void getCodeSuccess(final String uuid) {
                                 Toast.makeText(MainActivity.this,uuid,Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void getCodeFail(int errCode,
                                                    final String errmsg) {
                                // 失败后停止计时
                                if (errCode != 2996) {
                                    stopTimer();
                                }
                                if (errCode == 3002) {
                                    et_inputPhoneNum.setEnabled(true);
                                }
                                Toast.makeText(MainActivity.this,
                                        errmsg + "|code=" + errCode,
                                        Toast.LENGTH_SHORT).show();
                                System.out.println("获取校验码时的errCode = "+errCode);
                            }
                        });
                break;

            case R.id.btn_startSign:    // 开始验证校验码

                String verifyCode = et_inputVerifyCode.getText().toString();
                String phoneNumSign = et_inputPhoneNum.getText().toString();
                if (TextUtils.isEmpty(verifyCode)) {
                    Toast.makeText(MainActivity.this, "请输入验证码",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(phoneNumSign)) {
                    Toast.makeText(MainActivity.this, "请输入手机号码",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                progressDialog.setTitle("正在验证...");
                progressDialog.show();
                SMSSDK.getInstance().checkSmsCodeAsyn(phoneNumSign, verifyCode,
                        new SmscheckListener() {
                            @Override
                            public void checkCodeSuccess(final String code) {
                                if (progressDialog != null
                                        && progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }
                                Toast.makeText(MainActivity.this, code,
                                        Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void checkCodeFail(int errCode,
                                                      final String errmsg) {
                                if (progressDialog != null
                                        && progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }
                                Toast.makeText(MainActivity.this,
                                        errmsg + "|code=" + errCode,
                                        Toast.LENGTH_SHORT).show();
                                System.out.println("校验校验码时的errCode = "+errCode);
                            }
                        });
                break;
        }
    }
    /****************极光短息验证  到此结束***************/


    /**
     * android 6.0获取权限
     */
    private void getPermission() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_PHONE_STATE},
                    MY_PERMISSIONS_REQUEST_CALL_PHONE);
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_CALL_PHONE);
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_CALL_PHONE);
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_CALL_PHONE);
        }
    }

    /**
     * 权限回调
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_CALL_PHONE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                callPhone();
                System.out.println("获得权限");
            } else {
                System.out.println("拒绝权限");
            }
//            return;
        }
    }

    /**
     * 极光推送
     */
    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }

    /**
     * 极光推送
     */
    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }


}
