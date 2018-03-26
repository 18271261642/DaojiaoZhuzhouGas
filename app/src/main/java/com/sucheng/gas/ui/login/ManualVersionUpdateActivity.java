package com.sucheng.gas.ui.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.sucheng.gas.R;
import com.sucheng.gas.base.BaseActivity;
import com.sucheng.gas.constants.Constants;
import com.sucheng.gas.constants.UrlCode;
import com.sucheng.gas.utils.AppUtils;
import com.sucheng.gas.utils.UpdateChecker;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/1/24.
 */

/**
 * 手动检测更新
 */
public class ManualVersionUpdateActivity extends BaseActivity {

    @BindView(R.id.commentTitleTv)
    TextView commentTitleTv;
    UpdateChecker updateChecker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual);
        ButterKnife.bind(this);
        commentTitleTv.setText("手动检查更新");
    }

    @OnClick(R.id.manualUpdateBtn)
    public void onViewClicked() {
        updateChecker = new UpdateChecker(ManualVersionUpdateActivity.this);
        updateChecker.setCheckUrl(Constants.getAbsoluteUrl()+UrlCode.APP_UPDATE.getUrl()+"versionCode="+ AppUtils.getVersionCode(ManualVersionUpdateActivity.this));
        updateChecker.checkForUpdates();
    }
}
