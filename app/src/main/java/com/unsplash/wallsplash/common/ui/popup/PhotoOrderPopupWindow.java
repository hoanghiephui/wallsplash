package com.unsplash.wallsplash.common.ui.popup;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.unsplash.wallsplash.R;
import com.unsplash.wallsplash.WallSplashApplication;
import com.unsplash.wallsplash.common.utils.ThemeUtils;
import com.unsplash.wallsplash.common.utils.TypefaceUtils;

/**
 * Order popup window.
 */

public class PhotoOrderPopupWindow extends PopupWindow
        implements View.OnClickListener {
    // widget
    private OnPhotoOrderChangedListener listener;

    // data
    private String[] names;
    private String[] values;
    private String valueNow;

    private int type;
    public static final int NORMAL_TYPE = 0;
    public static final int CATEGORY_TYPE = 1;
    public static final int NO_RANDOM_TYPE = 2;

    /**
     * <br> life cycle.
     */

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public PhotoOrderPopupWindow(Context c, View anchor, String valueNow, int type) {
        super(c);
        this.initialize(c, anchor, valueNow, type);
        WallSplashApplication.getInstance().setActivityInBackstage(true);
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                WallSplashApplication.getInstance().setActivityInBackstage(false);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("InflateParams")
    private void initialize(Context c, View anchor, String valueNow, int type) {
        View v = LayoutInflater.from(c).inflate(R.layout.popup_photo_order, null);
        setContentView(v);
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        initData(c, valueNow, type);
        initWidget();

        setFocusable(true);
        setTouchable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setElevation(10);
        }
        showAsDropDown(anchor, anchor.getMeasuredWidth(), 0, Gravity.CENTER);
    }

    /**
     * <br> UI.
     */

    private void initWidget() {
        View v = getContentView();

        v.findViewById(R.id.popup_photo_order_latest).setOnClickListener(this);
        v.findViewById(R.id.popup_photo_order_oldest).setOnClickListener(this);
        v.findViewById(R.id.popup_photo_order_popular).setOnClickListener(this);
        v.findViewById(R.id.popup_photo_order_random).setOnClickListener(this);
        if (type == CATEGORY_TYPE) {
            v.findViewById(R.id.popup_photo_order_oldest).setVisibility(View.GONE);
            v.findViewById(R.id.popup_photo_order_popular).setVisibility(View.GONE);
        } else if (type == NO_RANDOM_TYPE) {
            v.findViewById(R.id.popup_photo_order_random).setVisibility(View.GONE);
        }

        TextView latestTxt = (TextView) v.findViewById(R.id.popup_photo_order_latestTxt);
        TypefaceUtils.setTypeface(v.getContext(), latestTxt);
        latestTxt.setText(names[0]);
        if (values[0].equals(valueNow)) {
            if (ThemeUtils.getInstance(v.getContext()).isLightTheme()) {
                latestTxt.setTextColor(ContextCompat.getColor(v.getContext(), R.color.colorAccent_light));
            } else {
                latestTxt.setTextColor(ContextCompat.getColor(v.getContext(), R.color.colorAccent_light));
            }
        }

        TextView oldestTxt = (TextView) v.findViewById(R.id.popup_photo_order_oldestTxt);
        TypefaceUtils.setTypeface(v.getContext(), oldestTxt);
        oldestTxt.setText(names[1]);
        if (values[1].equals(valueNow)) {
            if (ThemeUtils.getInstance(v.getContext()).isLightTheme()) {
                oldestTxt.setTextColor(ContextCompat.getColor(v.getContext(), R.color.colorAccent_light));
            } else {
                oldestTxt.setTextColor(ContextCompat.getColor(v.getContext(), R.color.colorAccent_light));
            }
        }

        TextView popularTxt = (TextView) v.findViewById(R.id.popup_photo_order_popularTxt);
        TypefaceUtils.setTypeface(v.getContext(), popularTxt);
        popularTxt.setText(names[2]);
        if (values[2].equals(valueNow)) {
            if (ThemeUtils.getInstance(v.getContext()).isLightTheme()) {
                popularTxt.setTextColor(ContextCompat.getColor(v.getContext(), R.color.colorAccent_light));
            } else {
                popularTxt.setTextColor(ContextCompat.getColor(v.getContext(), R.color.colorAccent_light));
            }
        }

        TextView randomTxt = (TextView) v.findViewById(R.id.popup_photo_order_randomTxt);
        TypefaceUtils.setTypeface(v.getContext(), randomTxt);
        randomTxt.setText(names[3]);
        if (values[3].equals(valueNow)) {
            if (ThemeUtils.getInstance(v.getContext()).isLightTheme()) {
                randomTxt.setTextColor(ContextCompat.getColor(v.getContext(), R.color.colorAccent_light));
            } else {
                randomTxt.setTextColor(ContextCompat.getColor(v.getContext(), R.color.colorAccent_light));
            }
        }

        if (ThemeUtils.getInstance(v.getContext()).isLightTheme()) {
            ((ImageView) v.findViewById(R.id.popup_photo_order_latestIcon)).setImageResource(R.drawable.ic_timer_light);
            ((ImageView) v.findViewById(R.id.popup_photo_order_oldestIcon)).setImageResource(R.drawable.ic_timer_off_light);
            ((ImageView) v.findViewById(R.id.popup_photo_order_popularIcon)).setImageResource(R.drawable.ic_fire_light);
            ((ImageView) v.findViewById(R.id.popup_photo_order_randomIcon)).setImageResource(R.drawable.ic_random_light);
        } else {
            ((ImageView) v.findViewById(R.id.popup_photo_order_latestIcon)).setImageResource(R.drawable.ic_timer_dark);
            ((ImageView) v.findViewById(R.id.popup_photo_order_oldestIcon)).setImageResource(R.drawable.ic_timer_off_dark);
            ((ImageView) v.findViewById(R.id.popup_photo_order_popularIcon)).setImageResource(R.drawable.ic_fire_dark);
            ((ImageView) v.findViewById(R.id.popup_photo_order_randomIcon)).setImageResource(R.drawable.ic_random_dark);
        }
    }

    /**
     * <br> data.
     */

    private void initData(Context c, String valueNow, int type) {
        names = c.getResources().getStringArray(R.array.photo_orders);
        values = c.getResources().getStringArray(R.array.photo_order_values);
        this.valueNow = valueNow;

        this.type = type;
    }

    /**
     * <br> interface.
     */

    public interface OnPhotoOrderChangedListener {
        void onPhotoOrderChange(String orderValue);
    }

    public void setOnPhotoOrderChangedListener(OnPhotoOrderChangedListener l) {
        listener = l;
    }

    @Override
    public void onClick(View view) {
        String newValue = valueNow;
        switch (view.getId()) {
            case R.id.popup_photo_order_latest:
                newValue = values[0];
                break;

            case R.id.popup_photo_order_oldest:
                newValue = values[1];
                break;

            case R.id.popup_photo_order_popular:
                newValue = values[2];
                break;

            case R.id.popup_photo_order_random:
                newValue = values[3];
                break;
        }

        if (!newValue.equals(valueNow) && listener != null) {
            listener.onPhotoOrderChange(newValue);
            dismiss();
        }
    }
}