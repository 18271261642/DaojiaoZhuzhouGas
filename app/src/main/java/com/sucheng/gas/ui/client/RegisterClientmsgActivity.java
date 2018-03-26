package com.sucheng.gas.ui.client;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;
import com.sucheng.gas.R;
import com.sucheng.gas.base.BaseActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/1/30.
 */

public class RegisterClientmsgActivity extends BaseActivity {

    @BindView(R.id.commentTitleTv)
    TextView commentTitleTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_clientmsg);
        ButterKnife.bind(this);

        initViews();

    }

    private void initViews() {
        commentTitleTv.setText("客户信息注册");
    }
}
