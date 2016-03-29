package com.xlgzs.effect.demo;

import android.view.View;

public class RetroflexViewActivity extends SampleActivityBase {

    @Override
    protected View getSampleView() {
        RetroflexView rv = new RetroflexView(this);
        rv.setTextSize(22);
        rv.setText("利用这个方法理论上可做出任何效果");
        return rv;
    }

}
