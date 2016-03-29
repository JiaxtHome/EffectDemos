package com.xlgzs.effect.demo;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.BounceInterpolator;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DropDownView extends LinearLayout {

    public DropDownView(Context context) {
        super(context);
    }

    public DropDownView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DropDownView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        init3dAnim();
    }

    private void init3dAnim(){
        Button btn = (Button)findViewById(R.id._3dBtn);
        final TextView tv = (TextView)findViewById(R.id._3dTxt);
        final _3DAnimation anim = new _3DAnimation();
        anim.setDuration(1000);
        anim.setInterpolator(new BounceInterpolator());
        anim.setAnimationListener(new AnimationListener(){

            @Override
            public void onAnimationStart(Animation animation) {
                tv.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                
            }
        });
        btn.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View v) {
                tv.startAnimation(anim);
            }
        });
    }

    public class _3DAnimation extends Animation {

        private Camera mCamera;
        private float mCenterX;
        private float mCenterY;
        public _3DAnimation() {
            super();
            mCamera = new Camera();
        }

        public _3DAnimation(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            Matrix matrix = t.getMatrix();  
            mCamera.save();
//            mCamera.translate(0f, 0f, /*(1300 - 1300*interpolatedTime)*/);
            mCamera.rotateX(-90 + 90 * interpolatedTime);
            mCamera.getMatrix(matrix);
            matrix.preTranslate(-mCenterX, -mCenterY);
            matrix.postTranslate(mCenterX,mCenterY);
            mCamera.restore();
        }

        @Override
        public void initialize(int width, int height, int parentWidth, int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
            mCenterX = width / 2;
            mCenterY = 0;
        }
    }
}
