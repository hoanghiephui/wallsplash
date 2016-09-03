package com.unsplash.wallsplash._common.ui.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.unsplash.wallsplash.WallSplashApplication;
import com.unsplash.wallsplash._common.data.data.Photo;

/**
 * Created by Hoang Hiep on 9/3/2016.
 */

public class PhotoTouchView extends View {
    // data
    private float width = 1;
    private float height = 0.666F;

    public PhotoTouchView(Context context) {
        super(context);
        this.initialize();
    }

    public PhotoTouchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initialize();
    }

    public PhotoTouchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.initialize();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PhotoTouchView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        float h = (float) (getResources().getDisplayMetrics().heightPixels * 0.6);
        float w = h / this.height * this.width;
        if (w < getResources().getDisplayMetrics().widthPixels) {
            int width = getResources().getDisplayMetrics().widthPixels;
            setMeasuredDimension(
                    width,
                    (int) (width / this.width * this.height));
        } else {
            setMeasuredDimension((int) w, (int) h);
        }
    }
}
