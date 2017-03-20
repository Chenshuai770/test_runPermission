package com.cs.test_runpermission;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
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
public class ThreeActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTvThree1;
    private Button mBtnThree1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_three);
        initView();
    }

    private void initView() {
        mTvThree1 = (TextView) findViewById(R.id.tv_three1);
        mBtnThree1 = (Button) findViewById(R.id.btn_three1);
        mBtnThree1.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_three1:
                requestPermission();
                break;
        }
    }
    //判读是否需要当前的环境是否为6.0
    @TargetApi(Build.VERSION_CODES.M)
    private void requestPermission() {
        //申请权限
        ThreeActivityPermissionsDispatcher.NeedWithCheck(this);
    }
    //请求成功后调用的方法
    @NeedsPermission({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void Need() {
        mTvThree1.setBackgroundColor(getResources().getColor(R.color.colorAccent));

    }
   //第一次拒绝后执行的逻辑
    @OnShowRationale({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void Show(final PermissionRequest request) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("权限说明");
        dialog.setMessage("需要将数据写入SD和从SD卡读出数据的权限");
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                request.proceed();

            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        dialog.show();
    }

    @OnPermissionDenied({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void Denied() {
        Toast.makeText(this, "权限被拒绝,请手动打开应用相关权限才能用使用", Toast.LENGTH_SHORT).show();
    }
    //如果用户选择了让设备“不再询问”，而调用的方法
    @OnNeverAskAgain(Manifest.permission.CAMERA)
    void Never() {
        Toast.makeText(ThreeActivity.this, "重新打开权限权限", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull final int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ThreeActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
        if (grantResults[0]== PackageManager.PERMISSION_DENIED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,permissions[0])){
                //第一次被拒绝的代码 逻辑
            }else {
                //点击选择框的时候并拒绝的时候
                final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("是否需要重新开启权限");
                dialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.parse("package:" + getPackageName())); // 根据包名打开对应的设置界面
                        startActivity(intent);
                    }
                });
                dialog.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                dialog.setCancelable(false);
                dialog.show();
            }
        }
    }
}
