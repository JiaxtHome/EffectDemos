package com.xlgzs.effect.demo;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

public abstract class SampleActivityBase extends Activity {

    private FrameLayout mContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.sample_base_layout);
        mContainer = (FrameLayout) findViewById(R.id.base_container);
        mContainer.setBackgroundColor(getBackGroundColor());
        addSampleView(getSampleView());
    }

    private void addSampleView(View v) {
        LayoutParams lp = new FrameLayout.LayoutParams(getLayoutWidth(), getLayoutHeight(), Gravity.CENTER);
        mContainer.addView(v, lp);
    }

    protected int getLayoutWidth() {
        return LayoutParams.WRAP_CONTENT;
    }

    protected int getLayoutHeight() {
        return LayoutParams.WRAP_CONTENT;
    }

    abstract protected View getSampleView();

    protected int getBackGroundColor() {
        return Color.WHITE;
    }
}
