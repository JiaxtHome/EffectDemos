package com.xlgzs.effect.demo;

import android.view.View;
import android.view.ViewGroup.LayoutParams;

public class MeshViewActivity extends SampleActivityBase {

    @Override
    protected View getSampleView() {
        MeshView sv = new MeshView(this);
        return sv;
    }

    @Override
    protected int getLayoutWidth() {
        return LayoutParams.MATCH_PARENT;
    }

    @Override
    protected int getLayoutHeight() {
        return LayoutParams.MATCH_PARENT;
    }

}
