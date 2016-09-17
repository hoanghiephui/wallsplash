package com.unsplash.wallsplash.common.ui.dialog;

/**
 * Created by Hoang Hiep on 9/11/2016.
 */

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.unsplash.wallsplash.R;
import com.unsplash.wallsplash.WallSplashApplication;
import com.unsplash.wallsplash.common.utils.TypefaceUtils;


/**
 * Rate limit dialog.
 */

public class RateLimitDialog extends DialogFragment
        implements View.OnClickListener {

    /**
     * <br> utils.
     */

    public static void checkAndNotify(AppCompatActivity a, String remaining) {
        if (Integer.parseInt(remaining) < 0) {
            RateLimitDialog dialog = new RateLimitDialog();
            dialog.show(a.getSupportFragmentManager(), remaining);
        }
    }

    /**
     * <br> life cycle.
     */

    @NonNull
    @SuppressLint("InflateParams")
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        WallSplashApplication.getInstance().setActivityInBackstage(true);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_rate_limit, null, false);
        initWidget(view);
        setCancelable(false);
        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .create();
    }

    /**
     * <br> UI.
     */

    private void initWidget(View v) {
        TextView content = (TextView) v.findViewById(R.id.dialog_rate_limit_content);
        TypefaceUtils.setTypeface(getActivity(), content);

        v.findViewById(R.id.dialog_rate_limit_button).setOnClickListener(this);
    }

    /**
     * <br> interface.
     */

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_rate_limit_button:
                dismiss();
                break;
        }
    }
}