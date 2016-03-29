package com.xlgzs.effect.demo;

import android.view.View;
import android.view.ViewGroup.LayoutParams;

public class FloatEditViewActivity extends SampleActivityBase {

    @Override
    protected View getSampleView() {
        FloatEditView fv= (FloatEditView) getLayoutInflater().inflate(R.layout.sample_float_edit_view, null);
        return fv;
    }

    @Override
    protected int getLayoutWidth() {
        return LayoutParams.MATCH_PARENT;
    }

}
