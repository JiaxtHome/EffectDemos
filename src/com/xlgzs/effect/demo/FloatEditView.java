package com.xlgzs.effect.demo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Layout which an {@link android.widget.EditText} to show a floating label when
 * the hint is hidden due to the user inputting text.
 * 
 */
public final class FloatEditView extends FrameLayout {

    private static final long ANIMATION_DURATION = 150;

    private static final float DEFAULT_PADDING_LEFT_RIGHT_DP = 12f;

    private EditText mEditText;
    private TextView mLabel;

    public FloatEditView(Context context) {
        this(context, null);
    }

    public FloatEditView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatEditView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FloatEditView);

        final int sidePadding = a.getDimensionPixelSize(R.styleable.FloatEditView_floatLabelSidePadding, dipsToPix(DEFAULT_PADDING_LEFT_RIGHT_DP));
        mLabel = new TextView(context);
        mLabel.setPadding(sidePadding, 0, sidePadding, 0);
        mLabel.setVisibility(INVISIBLE);

        mLabel.setTextAppearance(context, a.getResourceId(R.styleable.FloatEditView_floatLabelTextAppearance, android.R.style.TextAppearance_Small));

        addView(mLabel, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        a.recycle();
        debugCode();
    }

    @Override
    public final void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (child instanceof EditText) {
            // If we already have an EditText, throw an exception
            if (mEditText != null) {
                throw new IllegalArgumentException("We already have an EditText, can only have one");
            }

            // Update the layout params so that the EditText is at the bottom,
            // with enough top
            // margin to show the label
            setEditText((EditText) child);
            final LayoutParams lp = new LayoutParams(params);
            lp.gravity = Gravity.BOTTOM;
            lp.topMargin = (int) mLabel.getTextSize() + 20;
            params = lp;

        }

        // Carry on adding the View...
        super.addView(child, index, params);
    }

    private void setEditText(EditText editText) {
        mEditText = editText;

        // Add a TextWatcher so that we know when the text input has changed
        mEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {
                    // The text is empty, so hide the label if it is visible
                    if (mLabel.getVisibility() == View.VISIBLE) {
                        hideLabel();
                    }
                } else {
                    // The text is not empty, so show the label if it is not
                    // visible
                    if (mLabel.getVisibility() != View.VISIBLE) {
                        showLabel();
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

        });

        // Add focus listener to the EditText so that we can notify the label
        // that it is activated.
        // Allows the use of a ColorStateList for the text color on the label
        mEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focused) {
                mLabel.setActivated(focused);
            }
        });

        mLabel.setText(mEditText.getHint());
    }

    /**
     * @return the {@link android.widget.EditText} text input
     */
    public EditText getEditText() {
        return mEditText;
    }

    /**
     * @return the {@link android.widget.TextView} label
     */
    public TextView getLabel() {
        return mLabel;
    }

    /**
     * Show the label using an animation
     */
    private void showLabel() {
        mLabel.setVisibility(View.VISIBLE);
        mLabel.setAlpha(0f);
        mLabel.setTranslationY(mLabel.getHeight());
        mLabel.animate().alpha(1f).translationY(0f).setDuration(ANIMATION_DURATION).setListener(null).start();
    }

    /**
     * Hide the label using an animation
     */
    private void hideLabel() {
        mLabel.setAlpha(1f);
        mLabel.setTranslationY(0f);
        mLabel.animate().alpha(0f).translationY(mLabel.getHeight()).setDuration(ANIMATION_DURATION).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLabel.setVisibility(View.GONE);
            }
        }).start();
    }

    /**
     * Helper method to convert dips to pixels.
     */
    private int dipsToPix(float dps) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dps, getResources().getDisplayMetrics());
    }

    private void debugCode(){
        EditText mEdit = new EditText(getContext());
        mEdit.setHeight(100);
        mEdit.setBackgroundColor(Color.LTGRAY);
        mEdit.setHint("请输入任意字符");
        mEdit.setTextColor(Color.BLACK);
        addView(mEdit);
    }
}