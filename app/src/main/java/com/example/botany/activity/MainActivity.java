package com.example.botany.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.botany.R;
import com.example.botany.fragment.SearchFragment;
import com.example.botany.fragment.BotanyInfoFragment;
import com.example.botany.fragment.MineFragment;
import com.example.botany.utils.ToastUtils;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;

import java.util.List;

public class MainActivity extends MyBaseActivity implements OnClickListener {

    FragmentManager fragmentManager;

    private BotanyInfoFragment trafficInfoFragment;
    private SearchFragment searchFragment;
    private MineFragment mineFragment;

    private ImageView groupIv;
    private ImageView sleepIv;
    private ImageView mineIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();

        groupIv = findViewById(R.id.group_tab_iv);
        sleepIv = findViewById(R.id.search_tab_iv);
        mineIv = findViewById(R.id.mine_tab_iv);

        findViewById(R.id.search_ll).setOnClickListener(this);
        findViewById(R.id.group_ll).setOnClickListener(this);
        findViewById(R.id.mine_ll).setOnClickListener(this);

        defaultClick();//设置默认

        XXPermissions.with(this)
                .permission(Permission.CAMERA)
                .permission(Permission.READ_EXTERNAL_STORAGE)
                .permission(Permission.WRITE_EXTERNAL_STORAGE)
                .request(new OnPermissionCallback() {
                    @Override
                    public void onGranted(List<String> permissions, boolean all) {
                        if(all) {
                        } else {
                            ToastUtils.showShortToast(getApplicationContext(),"Must be fully authorized to use the function normally");
                            onBackPressed();
                        }
                    }

                    @Override
                    public void onDenied(List<String> permissions, boolean never) {
                        if (never) {
                            ToastUtils.showShortToast(getApplicationContext(),"Must be fully authorized to use the function normally");
                            // 如果是被永久拒绝就跳转到应用权限系统设置页面
                            XXPermissions.startPermissionActivity(getApplicationContext(), permissions);
                        } else {
                            ToastUtils.showShortToast(getApplicationContext(),"Must be fully authorized to use the function normally");
                            onBackPressed();
                        }
                    }
                });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.group_ll:
                selectFragment(0);
                break;
            case R.id.search_ll:
                selectFragment(1);
                break;
            case R.id.mine_ll:
                selectFragment(2);
                break;
            default:
                break;
        }
    }

    private void selectFragment(int index) {
        sleepIv.setSelected(false);
        groupIv.setSelected(false);
        mineIv.setSelected(false);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        switch (index) {
            case 0:
                if (trafficInfoFragment == null) {
                    trafficInfoFragment = new BotanyInfoFragment();
                }
                fragmentTransaction.replace(R.id.fl, trafficInfoFragment);
                groupIv.setSelected(true);

                break;
            case 1:

                if (searchFragment == null) {
                    searchFragment = new SearchFragment();
                }
                fragmentTransaction.replace(R.id.fl, searchFragment);
                sleepIv.setSelected(true);

                break;
            case 2:
                if (mineFragment == null) {
                    mineFragment = new MineFragment();
                }
                fragmentTransaction.replace(R.id.fl, mineFragment);
                mineIv.setSelected(true);

                break;

            default:
                break;
        }
        fragmentTransaction.commit();
    }

    //默认进入
    private void defaultClick() {
        selectFragment(0);
    }
}