package com.xlgzs.effect.demo;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

@SuppressLint("ClickableViewAccessibility")
public class RetroflexView extends TextView {
    // 定义两个常量，这两个常量指定该图片横向、纵向上被划分为80格
    private final int WIDTH = 80;
    private final int HEIGHT = 80;
    private Bitmap bitmap = null;
    // 记录该图片上包含81*81个顶点
    private final int COUNT = (WIDTH + 1) * (HEIGHT + 1);
    // 定义一个数组，保存Bitmap上的81*81个点得坐标
    private final float[] orig = new float[COUNT * 2];
    // 定义一个数组，记录Bitmap上的81*81个点经过扭曲后的坐标
    // 对 图片进行扭曲的关键就是修改该数组里的元素的值
    private final float[] verts = new float[COUNT * 2];
    float bitmapWidth;
    float unitWidth;
    float bitmapHeight;
    float halfHeight;
    private static final double HALF_PI = Math.PI / 2;

    public RetroflexView(Context context) {
        super(context);
        init(context);
    }

    public RetroflexView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public RetroflexView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        setFocusable(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (bitmap != null) {
            canvas.drawBitmapMesh(bitmap, WIDTH, HEIGHT, verts, 0, null, 0, null);
        } else {
            super.onDraw(canvas);
        }
    }

    private void flagWave(float input) {
        for (int j = 0; j <= HEIGHT; j++) {
            for (int i = 0; i <= WIDTH; i++) {
                float startX = input * bitmapWidth; // 变形部分最右端x值
                float cx = i * 1.0f / WIDTH * bitmapWidth;
                float cy = j * 1.0f / HEIGHT * bitmapHeight;
                float toHalf = cy - halfHeight;
                if (cx >= startX) { //扁平部分
                    verts[(j * (WIDTH + 1) + i) * 2 + 1] = halfHeight;
                    verts[(j * (WIDTH + 1) + i) * 2] = cx;
                } else if (cx <= startX - unitWidth) { //恢复部分
                    verts[(j * (WIDTH + 1) + i) * 2 + 1] = orig[(j * (WIDTH + 1) + i) * 2 + 1];
                    verts[(j * (WIDTH + 1) + i) * 2] = cx;
                } else { // 变形中部分
                    float ratio = (startX - cx) / unitWidth;
                    verts[(j * (WIDTH + 1) + i) * 2 + 1] = (float) (halfHeight + toHalf * Math.sin(HALF_PI * ratio));
                    verts[(j * (WIDTH + 1) + i) * 2] = (float) (cx - toHalf * Math.cos(HALF_PI * ratio) * 1f);
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        startPlay();
        return super.onTouchEvent(event);
    }

    public void startPlay() {
        initBitmap();
        ValueAnimator va = ValueAnimator.ofFloat(0, 1.3f);
        va.setDuration(1200);
        va.addUpdateListener(new AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                flagWave(value);
                invalidate();
            }
        });
        va.start();
    }

    private void initBitmap() {
        if (bitmap == null) {
            buildDrawingCache();
            bitmap = getDrawingCache();
            bitmapWidth = bitmap.getWidth();
            unitWidth = bitmapWidth * 0.3f;
            bitmapHeight = bitmap.getHeight();
            halfHeight = bitmapHeight / 2;

            int index = 0;
            // 算出所有端点的原始坐标
            for (int y = 0; y <= HEIGHT; y++) {
                float fy = bitmapHeight * y / HEIGHT;
                for (int x = 0; x <= WIDTH; x++) {
                    float fx = bitmapWidth * x / WIDTH;
                    orig[index * 2 + 0] = verts[index * 2 + 0] = fx;
                    orig[index * 2 + 1] = verts[index * 2 + 1] = fy;
                    index += 1;
                }
            }
        }
    }
}
