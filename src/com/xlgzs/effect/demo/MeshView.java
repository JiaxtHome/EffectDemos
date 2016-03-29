package com.xlgzs.effect.demo;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;

public class MeshView extends View {

    private static final Interpolator OUT = new MeshQuintInterpolator(MeshQuintInterpolator.OUT);
    private static final Interpolator IN = new MeshQuadInterpolator(MeshQuadInterpolator.IN);
    private static final Interpolator ELASTIC = new MeshElasticInterpolator(MeshElasticInterpolator.OUT);

    float tx, ty;

    private static final int WIDTH = 20;
    private static final int HEIGHT = 30;
    private static final int COUNT = (WIDTH + 1) * (HEIGHT + 1);

    private final Bitmap mBitmap;
    private final float[] mVerts = new float[COUNT*2];
    private final float[] mOrig = new float[COUNT*2];

    private ValueAnimator anim;
    private int mWidth;
    private int mHeight;

    public MeshView(Context context) {
        super(context);
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.mesh);
        mWidth = getResources().getDisplayMetrics().widthPixels;
        mHeight = getResources().getDisplayMetrics().heightPixels;
        init();
    }

    public MeshView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MeshView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.mesh);
        init();
    }

    private void init() {
        float w = mWidth;//mBitmap.getWidth();
        float h = mHeight;//mBitmap.getHeight();
        // construct our mesh
        int index = 0;
        for (int y = 0; y <= HEIGHT; y++) {
            float fy = h * y / HEIGHT;
            for (int x = 0; x <= WIDTH; x++) {
                float fx = w * x / WIDTH;
                setXY(mVerts, index, fx, fy);
                setXY(mOrig, index, fx, fy);
                index += 1;
            }
        }
    }

    private void updateMesh() {
        float w = mWidth;//mBitmap.getWidth();
        float h = mHeight;//mBitmap.getHeight();
        float[] src = mOrig;
        float[] dst = mVerts;
        for (int i = 0; i < COUNT*2; i += 2) {
            float x = src[i+0];
            float y = src[i+1];
            float dy = ty - y;
            float d = (w - x) * 0.5f / w * tx * OUT.getInterpolation((w - x) / w) * (1f - IN.getInterpolation(Math.abs(dy) / h));

            dst[i+0] = x + d;
            if (i == 20) {
                Log.d("zxytest", "x = " + x + " tx = " + tx + " d = " + d);
            }
        }
    }

    private static void setXY(float[] array, int index, float x, float y) {
        array[index*2] = x;
        array[index*2 + 1] = y;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmapMesh(mBitmap, WIDTH, HEIGHT, mVerts, 0,
                null, 0, null);
    }

    float dx,dy;
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (anim != null && anim.isStarted()) {
                    anim.cancel();
                }
                dx=event.getX();
                dy=event.getY();
            case MotionEvent.ACTION_MOVE:
                this.tx = event.getX() - dx;
                this.ty = event.getY();
                updateMesh();
                break;
            case MotionEvent.ACTION_UP:
                if (anim != null && anim.isStarted()) {
                    anim.cancel();
                }
                dx=0;
                dy=0;
                final float sx = tx;
                final float sy = ty;
                anim = ValueAnimator.ofFloat(0f, 1f);
                anim.setInterpolator(ELASTIC);
                anim.setDuration(1200);
                anim.addUpdateListener(new AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float input = (Float) animation.getAnimatedValue();
                        tx = (1f - input) * sx;
                        ty = sy/*h + (sy - h) * input*/;
                        updateMesh();
                        invalidate();
                    }
                });
                anim.start();
                break;
        }
        invalidate();
        return true;
    }
}
