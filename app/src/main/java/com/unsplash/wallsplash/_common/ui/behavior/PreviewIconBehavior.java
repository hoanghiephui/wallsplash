package com.unsplash.wallsplash._common.ui.behavior;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Hoang Hiep on 9/3/2016.
 */

public class PreviewIconBehavior<V extends View> extends CoordinatorLayout.Behavior<V> {

    /**
     * <br> life cycle.
     */

    public PreviewIconBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * <br> UI.
     */

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, V child, int layoutDirection) {
        child.layout(
                0,
                parent.getMeasuredHeight(),
                child.getMeasuredWidth(),
                parent.getMeasuredHeight() + child.getMeasuredHeight());
        return true;
    }
}

