package com.sucheng.gas.ui.botmsg;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sucheng.gas.R;
import com.sucheng.gas.adapter.FragmentPagerAdapter;
import com.sucheng.gas.scan.CommentScanActivity;
import com.zbar.lib.CaptureActivity;
import com.sucheng.gas.utils.Utils;
import com.yanzhenjie.nohttp.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/1/24.
 */

/**
 * 气瓶轨迹
 */
public class TrajectoryActivity extends CommentScanActivity {

    @BindView(R.id.contentPager)
    ViewPager contentPager;
    @BindView(R.id.navigation)
    BottomNavigationView navigation;
    @BindView(R.id.commentTitleTv)
    TextView commentTitleTv;
    @BindView(R.id.commentTitleScanImg)
    ImageView commentTitleScanImg;


    private FragmentPagerAdapter fragAdapter;
    private MenuItem menuItem;
    private List<Fragment> listFragment;
    private TrajectoryFragment trajectoryFragment;
    private BotMsgFragment botMsgFragment;

    private int flagCode;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_bottraj:
                    contentPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_botmsg:
                    contentPager.setCurrentItem(1);
                    return true;

            }
            return false;
        }

    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trajectory);
        ButterKnife.bind(this);
        registerReceiver(broadcastReceiver, new IntentFilter("com.sucheng.gas.ui.botmsg.action"));
        initViews();

    }

    //扫描返回
    @Override
    public void getScanResultData(String botCode) {
        super.getScanResultData(botCode);
        Logger.e("-----轨迹=-===" + botCode);
        Intent intent = new Intent();
        intent.setAction("com.sucheng.gas.ui.botmsg.action");
        intent.putExtra("botCode", botCode);
        sendBroadcast(intent);

    }

    private void initViews() {
        commentTitleTv.setText("气瓶信息");
        commentTitleScanImg.setVisibility(View.VISIBLE);
        flagCode = getIntent().getIntExtra("flagCode", 0);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        listFragment = new ArrayList<>();
        trajectoryFragment = new TrajectoryFragment();
        botMsgFragment = new BotMsgFragment();
        listFragment.add(trajectoryFragment);
        listFragment.add(botMsgFragment);
        fragAdapter = new FragmentPagerAdapter(getSupportFragmentManager(), listFragment);
        contentPager.setAdapter(fragAdapter);
        contentPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (menuItem != null) {
                    menuItem.setCheckable(false);
                } else {
                    navigation.getMenu().getItem(position).setCheckable(true);
                }
                menuItem = navigation.getMenu().getItem(position);
                menuItem.setCheckable(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

        }
    };

    @OnClick(R.id.commentTitleScanImg)
    public void onViewClicked() {
        Intent intent = new Intent(this, CaptureActivity.class);
        intent.putExtra("flagCode",flagCode);
        startActivityForResult(intent,flagCode);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logger.e("-----requestCode="+requestCode+"--="+resultCode+"-="+RESULT_OK);
        if(requestCode == flagCode){
            if(resultCode == 0 && data != null){
                String rebackScanData = data.getStringExtra("scanResult");
                Logger.e("----reback="+rebackScanData);
                if(!Utils.isEmpty(rebackScanData)){
                    Intent intent = new Intent();
                    intent.setAction("com.sucheng.gas.ui.botmsg.action");
                    intent.putExtra("botCode", rebackScanData);
                    sendBroadcast(intent);
                }
            }
        }

    }
}
