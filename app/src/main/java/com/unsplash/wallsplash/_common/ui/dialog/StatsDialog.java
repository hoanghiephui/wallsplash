package com.unsplash.wallsplash._common.ui.dialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.unsplash.wallsplash.R;
import com.unsplash.wallsplash.WallSplashApplication;
import com.unsplash.wallsplash._common.data.data.Photo;
import com.unsplash.wallsplash._common.data.data.PhotoStats;
import com.unsplash.wallsplash._common.data.service.PhotoService;
import com.unsplash.wallsplash._common.utils.AnimUtils;
import com.unsplash.wallsplash._common.utils.TypefaceUtils;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Stats dialog.
 */

public class StatsDialog extends DialogFragment
        implements PhotoService.OnRequestStatsListener {
    // widget
    private CircularProgressView progress;
    private LinearLayout dataContainer;
    private TextView likeNum;
    private TextView viewNum;
    private TextView downloadNum;

    // data
    private PhotoService service;
    private Photo photo;

    private int state = 0;
    private final int LOADING_STATE = 0;
    private final int SUCCESS_STATE = 1;

    /**
     * <br> life cycle.
     */

    @SuppressLint("InflateParams")
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        WallSplashApplication.getInstance().setActivityInBackstage(true);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_stats, null, false);
        initWidget(view);
        service.requestStats(photo.id, this);
        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .create();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        WallSplashApplication.getInstance().setActivityInBackstage(false);
        if (service != null) {
            service.cancel();
        }
    }

    /**
     * <br> UI.
     */

    private void initWidget(View v) {
        state = LOADING_STATE;
        this.service = PhotoService.getService();

        this.progress = (CircularProgressView) v.findViewById(R.id.dialog_stats_progress);
        progress.setVisibility(View.VISIBLE);

        this.dataContainer = (LinearLayout) v.findViewById(R.id.dialog_stats_dataContainer);
        dataContainer.setVisibility(View.GONE);

        this.likeNum = (TextView) v.findViewById(R.id.dialog_stats_likeNum);
        TypefaceUtils.setTypeface(getActivity(), likeNum);

        this.viewNum = (TextView) v.findViewById(R.id.dialog_stats_viewNum);
        TypefaceUtils.setTypeface(getActivity(), viewNum);

        this.downloadNum = (TextView) v.findViewById(R.id.dialog_stats_downloadNum);
        TypefaceUtils.setTypeface(getActivity(), downloadNum);
    }

    private void setState(int stateTo) {
        switch (stateTo) {
            case SUCCESS_STATE:
                if (state == LOADING_STATE) {
                    AnimUtils.animHide(progress);
                    AnimUtils.animShow(dataContainer);
                }
                break;
        }
        this.state = stateTo;
    }

    /**
     * <br> data.
     */

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

    /**
     * <br> interface.
     */

    // on request stats listener.
    @SuppressLint("SetTextI18n")
    @Override
    public void onRequestStatsSuccess(Call<PhotoStats> call, Response<PhotoStats> response) {
        if (response.isSuccessful() && response.body() != null) {
            likeNum.setText(response.body().likes + " LIKES");
            viewNum.setText(response.body().views + " VIEWS");
            downloadNum.setText(response.body().downloads + " DOWNLOADS");
            setState(SUCCESS_STATE);
        } else {
            service.requestStats(photo.id, this);
        }
    }

    @Override
    public void onRequestStatsFailed(Call<PhotoStats> call, Throwable t) {
        service.requestStats(photo.id, this);
    }
}
