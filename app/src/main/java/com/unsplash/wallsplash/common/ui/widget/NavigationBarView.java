package com.unsplash.wallsplash.common.ui.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.unsplash.wallsplash.common.utils.DisplayUtils;

/**
 * Created by Hoang Hiep on 9/3/2016.
 */

public class NavigationBarView extends View {

    /**
     * <br> life cycle.
     */

    public NavigationBarView(Context context) {
        super(context);
    }

    public NavigationBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NavigationBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public NavigationBarView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * <br> UI.
     */

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(
                getResources().getDisplayMetrics().widthPixels,
                DisplayUtils.getNavigationBarHeight(getContext()));
    }
}
