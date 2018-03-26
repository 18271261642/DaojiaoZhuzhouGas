package com.sucheng.gas;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends Activity {



    TextView tvShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        tvShow = (TextView) findViewById(R.id.mainTvShow);



    }

    @OnClick(R.id.button)
    public void onViewClicked() {

    }
}
