package com.unsplash.wallsplash.common.ui.widget;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.unsplash.wallsplash.WallSplashApplication;
import com.unsplash.wallsplash.common.data.data.Photo;
import com.unsplash.wallsplash.common.utils.DisplayUtils;

/**
 * Created by Hoang Hiep on 9/11/2016.
 */

public class FreedomTouchView extends View {
    // data
    private float width = 1;
    private float height = 0.666F;

    /**
     * <br> life cycle.
     */

    public FreedomTouchView(Context context) {
        super(context);
        this.initialize();
    }

    public FreedomTouchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initialize();
    }

    public FreedomTouchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.initialize();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FreedomTouchView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.initialize();
    }

    private void initialize() {
        Photo p = WallSplashApplication.getInstance().getPhoto();
        if (p != null) {
            width = p.width;
            height = p.height;
        }
    }

    /**
     * <br> UI.
     */

    @SuppressLint("DrawAllocation")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int[] size = getMeasureSize(MeasureSpec.getSize(widthMeasureSpec));
        setMeasuredDimension(size[0], size[1]);
    }

    public void setSize(final Photo p) {
        post(new Runnable() {
            @Override
            public void run() {
                width = p.width;
                height = p.height;
                int[] size = getMeasureSize(getMeasuredWidth());

                ViewGroup.LayoutParams params = getLayoutParams();
                params.width = size[0];
                params.height = size[1];
                setLayoutParams(params);
            }
        });
    }

    /**
     * <br> data.
     */

    public void setSize(int w, int h) {
        width = w;
        height = h;

        if (getMeasuredWidth() != 0) {
            int[] size = getMeasureSize(getMeasuredWidth());

            ViewGroup.LayoutParams params = getLayoutParams();
            params.width = size[0];
            params.height = size[1];
            setLayoutParams(params);
        }
    }

    private int[] getMeasureSize(int measureWidth) {
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int screenHeight = getResources().getDisplayMetrics().heightPixels;
        float limitHeight = screenHeight
                - new DisplayUtils(getContext()).dpToPx(280);

        if (1.0 * height / width * screenWidth <= limitHeight) {
            return new int[]{
                    (int) (limitHeight * width / height),
                    (int) limitHeight};
        } else {
            return new int[]{
                    measureWidth,
                    (int) (measureWidth * height / width)};
        }
    }
}
