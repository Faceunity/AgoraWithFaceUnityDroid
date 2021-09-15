package com.faceunity.agorawithfaceunity.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

/**
 * @author Qinyu on 2021-04-02
 * @description
 */
public class CircleImageView extends AppCompatImageView {
    private Path mPath;
    private RectF mRectF;

    public CircleImageView(Context context) {
        super(context);
        init();
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPath = new Path();
        mRectF = new RectF();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mRectF.set(0, 0, getWidth(), getHeight());
        mPath.addOval(mRectF, Path.Direction.CCW);
        canvas.clipPath(mPath);
        super.onDraw(canvas);
    }
}
