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
 * Collection type popup window.
 */

public class CollectionTypePopupWindow extends PopupWindow
        implements View.OnClickListener {
    // widget
    private OnCollectionTypeChangedListener listener;

    // data
    private String[] names;
    private String[] values;
    private String valueNow;

    /**
     * <br> life cycle.
     */

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public CollectionTypePopupWindow(Context c, View anchor, String valueNow) {
        super(c);
        this.initialize(c, anchor, valueNow);
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
    private void initialize(Context c, View anchor, String valueNow) {
        View v = LayoutInflater.from(c).inflate(R.layout.popup_collection_type, null);
        setContentView(v);
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        initData(c, valueNow);
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

        v.findViewById(R.id.popup_collection_type_all).setOnClickListener(this);
        v.findViewById(R.id.popup_collection_type_curated).setOnClickListener(this);
        v.findViewById(R.id.popup_collection_type_featured).setOnClickListener(this);

        TextView allTxt = (TextView) v.findViewById(R.id.popup_collection_type_allTxt);
        TypefaceUtils.setTypeface(v.getContext(), allTxt);
        allTxt.setText(names[0]);
        if (values[0].equals(valueNow)) {
            if (ThemeUtils.getInstance(v.getContext()).isLightTheme()) {
                allTxt.setTextColor(ContextCompat.getColor(v.getContext(), R.color.colorAccent_light));
            } else {
                allTxt.setTextColor(ContextCompat.getColor(v.getContext(), R.color.colorAccent_light));
            }
        }

        TextView curatedTxt = (TextView) v.findViewById(R.id.popup_collection_type_curatedTxt);
        TypefaceUtils.setTypeface(v.getContext(), curatedTxt);
        curatedTxt.setText(names[1]);
        if (values[1].equals(valueNow)) {
            if (ThemeUtils.getInstance(v.getContext()).isLightTheme()) {
                curatedTxt.setTextColor(ContextCompat.getColor(v.getContext(), R.color.colorAccent_light));
            } else {
                curatedTxt.setTextColor(ContextCompat.getColor(v.getContext(), R.color.colorAccent_light));
            }
        }

        TextView featuredTxt = (TextView) v.findViewById(R.id.popup_collection_type_featuredTxt);
        TypefaceUtils.setTypeface(v.getContext(), featuredTxt);
        featuredTxt.setText(names[2]);
        if (values[2].equals(valueNow)) {
            if (ThemeUtils.getInstance(v.getContext()).isLightTheme()) {
                featuredTxt.setTextColor(ContextCompat.getColor(v.getContext(), R.color.colorAccent_light));
            } else {
                featuredTxt.setTextColor(ContextCompat.getColor(v.getContext(), R.color.colorAccent_light));
            }
        }

        if (ThemeUtils.getInstance(v.getContext()).isLightTheme()) {
            ((ImageView) v.findViewById(R.id.popup_collection_type_allIcon)).setImageResource(R.drawable.ic_select_all_grey_600_24dp);
            ((ImageView) v.findViewById(R.id.popup_collection_type_curatedIcon)).setImageResource(R.drawable.ic_filter_vintage_grey_600_24dp);
            ((ImageView) v.findViewById(R.id.popup_collection_type_featuredIcon)).setImageResource(R.drawable.ic_featured);
        } else {
            ((ImageView) v.findViewById(R.id.popup_collection_type_allIcon)).setImageResource(R.drawable.ic_select_all_white_24dp);
            ((ImageView) v.findViewById(R.id.popup_collection_type_curatedIcon)).setImageResource(R.drawable.ic_filter_vintage_white_24dp);
            ((ImageView) v.findViewById(R.id.popup_collection_type_featuredIcon)).setImageResource(R.drawable.ic_featured_white);
        }
    }

    /**
     * <br> data.
     */

    private void initData(Context c, String valueNow) {
        names = c.getResources().getStringArray(R.array.collection_types);
        values = c.getResources().getStringArray(R.array.collection_type_values);
        this.valueNow = valueNow;
    }

    /**
     * <br> interface.
     */

    public interface OnCollectionTypeChangedListener {
        void CollectionTypeChange(String typeValue);
    }

    public void setOnCollectionTypeChangedListener(OnCollectionTypeChangedListener l) {
        listener = l;
    }

    @Override
    public void onClick(View view) {
        String newValue = valueNow;
        switch (view.getId()) {
            case R.id.popup_collection_type_all:
                newValue = values[0];
                break;

            case R.id.popup_collection_type_curated:
                newValue = values[1];
                break;

            case R.id.popup_collection_type_featured:
                newValue = values[2];
                break;
        }

        if (!newValue.equals(valueNow) && listener != null) {
            listener.CollectionTypeChange(newValue);
            dismiss();
        }
    }
}
