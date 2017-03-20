package com.cs.test_runpermission;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class SecondActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mBtnRequestPermission;
    private TextView tvPermissionStatus;
    private Button btn_phone;
    private TextView tv_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        initView();
    }

    //判读是否需要当前的环境是否为6.0
    @TargetApi(Build.VERSION_CODES.M)
    private void requestPermission() {
        //申请权限
        SecondActivityPermissionsDispatcher.NeedWithCheck(this);
    }

    //处理当用户允许该权限时需要处理的方法
    //@NeedsPermission(value = Manifest.permission.CAMERA, maxSdkVersion = 25)
    @NeedsPermission(value = Manifest.permission.CAMERA, maxSdkVersion = 25)
    void Need() {
        tvPermissionStatus.setTextColor(Color.GREEN);
        tvPermissionStatus.setText("相机权限已申请");
    }

    //简单的来说就是为什么需要此权限，这需要展现给用户，而用户可以选择“继续”或者“中止”当前的权限许可请求
    @OnShowRationale(Manifest.permission.CAMERA)
    void Show(final PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setMessage("申请相机权限")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //再次执行请求
                        request.proceed();
                    }
                })
                .show();
    }

    //如果用户不授予某权限时调用的方法
    @OnPermissionDenied(Manifest.permission.CAMERA)
    void Denied() {
        Toast.makeText(this, "权限被拒绝", Toast.LENGTH_SHORT).show();
    }

    //如果用户选择了让设备“不再询问”，而调用的方法
    @OnNeverAskAgain(Manifest.permission.CAMERA)
    void Never() {
        Toast.makeText(this, "不再询问", Toast.LENGTH_SHORT).show();
    }

    //回调方法
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        SecondActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    private void initView() {
        mBtnRequestPermission = (Button) findViewById(R.id.btn_request_permission);
        tvPermissionStatus = (TextView) findViewById(R.id.tv_permission_status);
        mBtnRequestPermission.setOnClickListener(this);
        btn_phone = (Button) findViewById(R.id.btn_phone);
        btn_phone.setOnClickListener(this);
        tv_phone = (TextView) findViewById(R.id.tv_phone);
        tv_phone.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_request_permission:
                requestPermission();
                break;
            case R.id.btn_phone:
                break;
        }
    }
}
