package com.faceunity.agorawithfaceunity.view.beautybox;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.faceunity.agorawithfaceunity.R;

/**
 * @author Qinyu on 2021-03-30
 * @description
 */
public class DetectBox extends LinearLayout implements Checkable {
    private int textNormalColor;
    private int textCheckedColor;
    private Drawable drawableOpenChecked;
    private Drawable drawableCloseNormal;

    private TextView boxText;
    private ImageView boxImg;

    private boolean isChecked;

    public DetectBox(Context context) {
        this(context, null);
    }

    public DetectBox(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DetectBox(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
        obtainStyle(context, attrs, defStyleAttr);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_beauty_box, this);
        boxImg = findViewById(R.id.beauty_box_img);
        boxText = findViewById(R.id.beauty_box_text);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    protected void obtainStyle(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BeautyBox, defStyleAttr, 0);
        String textNormalStr = a.getString(R.styleable.BeautyBox_text_normal);
        textNormalColor = a.getColor(R.styleable.BeautyBox_textColor_normal, getResources().getColor(R.color.main_color_c5c5c5));
        textCheckedColor = a.getColor(R.styleable.BeautyBox_textColor_checked, getResources().getColor(R.color.main_color));
        boxText.setText(textNormalStr);
        boxText.setTextColor(getResources().getColor(R.color.main_color_c5c5c5));
        drawableOpenChecked = a.getDrawable(R.styleable.BeautyBox_drawable_open_checked);
        drawableCloseNormal = a.getDrawable(R.styleable.BeautyBox_drawable_close_normal);
        boolean checked = a.getBoolean(R.styleable.BeautyBox_checked, false);
        setChecked(checked);
        a.recycle();
    }

    @Override
    public boolean performClick() {
        toggle();
        return super.performClick();
    }

    @Override
    public void setChecked(boolean b) {
        isChecked = b;
        if (isChecked) {
            boxText.setTextColor(textCheckedColor);
            boxImg.setImageDrawable(drawableOpenChecked);
        } else {
            boxText.setTextColor(textNormalColor);
            boxImg.setImageDrawable(drawableCloseNormal);
        }
        if (null != mListener) {
            mListener.onCheckStateChange(this, b);
        }
    }

    @Override
    public boolean isChecked() {
        return isChecked;
    }

    @Override
    public void toggle() {
        setChecked(!isChecked);
    }

    public interface OnCheckStateListener {
        void onCheckStateChange(DetectBox view, boolean checked);
    }
    private OnCheckStateListener mListener;

    public void setCheckStateListener(OnCheckStateListener listener) {
        mListener = listener;
    }
}
