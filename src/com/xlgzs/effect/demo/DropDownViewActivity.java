package com.xlgzs.effect.demo;

import android.view.View;

public class DropDownViewActivity extends SampleActivityBase {

    @Override
    protected View getSampleView() {
        DropDownView dv= (DropDownView) getLayoutInflater().inflate(R.layout.sample_drop_down_view, null);
        return dv;
    }

}
