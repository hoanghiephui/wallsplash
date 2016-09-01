package com.unsplash.wallsplash.user.view.widget;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.unsplash.wallsplash.R;
import com.unsplash.wallsplash._common.data.data.User;
import com.unsplash.wallsplash._common.i.model.LoadModel;
import com.unsplash.wallsplash._common.i.model.UserModel;
import com.unsplash.wallsplash._common.i.presenter.LoadPresenter;
import com.unsplash.wallsplash._common.i.presenter.UserPresenter;
import com.unsplash.wallsplash._common.i.view.LoadView;
import com.unsplash.wallsplash._common.i.view.UserView;
import com.unsplash.wallsplash._common.ui.adapter.MyPagerAdapter;
import com.unsplash.wallsplash._common.utils.AnimUtils;
import com.unsplash.wallsplash._common.utils.ThemeUtils;
import com.unsplash.wallsplash._common.utils.TypefaceUtils;
import com.unsplash.wallsplash.user.model.widget.LoadObject;
import com.unsplash.wallsplash.user.model.widget.UserObject;
import com.unsplash.wallsplash.user.presenter.widget.LoadImplementor;
import com.unsplash.wallsplash.user.presenter.widget.UserImplementor;

import java.util.ArrayList;
import java.util.List;

/**
 * User profile view.
 */

public class UserProfileView extends FrameLayout
        implements UserView, LoadView {
    // model.
    private UserModel userModel;
    private LoadModel loadModel;

    // view.
    private CircularProgressView progressView;

    private RelativeLayout profileContainer;
    private TextView locationTxt;
    private TextView bioTxt;

    private MyPagerAdapter adapter;

    // presenter.
    private UserPresenter userPresenter;
    private LoadPresenter loadPresenter;

    /**
     * <br> life cycle.
     */

    public UserProfileView(Context context) {
        super(context);
        this.initialize();
    }

    public UserProfileView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initialize();
    }

    public UserProfileView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.initialize();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public UserProfileView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.initialize();
    }

    @SuppressLint("InflateParams")
    private void initialize() {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.container_user_profile, null);
        addView(v);

        initModel();
        initView();
        initPresenter();
    }

    /**
     * <br> presenter.
     */

    private void initPresenter() {
        this.userPresenter = new UserImplementor(userModel, this);
        this.loadPresenter = new LoadImplementor(loadModel, this);
    }

    /**
     * <br> view.
     */

    private void initView() {
        this.progressView = (CircularProgressView) findViewById(R.id.container_user_profile_progressView);
        progressView.setVisibility(VISIBLE);

        this.profileContainer = (RelativeLayout) findViewById(R.id.container_user_profile_profileContainer);
        profileContainer.setVisibility(GONE);

        this.locationTxt = (TextView) findViewById(R.id.container_user_profile_locationTxt);
        TypefaceUtils.setTypeface(getContext(), locationTxt);

        this.bioTxt = (TextView) findViewById(R.id.container_user_profile_bio);
        TypefaceUtils.setTypeface(getContext(), bioTxt);

        if (ThemeUtils.getInstance(getContext()).isLightTheme()) {
            ((ImageView) findViewById(R.id.container_user_profile_locationIcon)).setImageResource(R.drawable.ic_location_light);
        } else {
            ((ImageView) findViewById(R.id.container_user_profile_locationIcon)).setImageResource(R.drawable.ic_location_dark);
        }
    }

    /**
     * <br> model.
     */

    // init.
    private void initModel() {
        this.userModel = new UserObject();
        this.loadModel = new LoadObject(LoadObject.LOADING_STATE);
    }

    // interface.

    public void requestUserProfile(MyPagerAdapter adapter) {
        this.adapter = adapter;
        userPresenter.requestUser();
    }

    public void cancelRequest() {
        userPresenter.cancelRequest();
    }

    public String getUserPortfolio() {
        return userModel.getUser().portfolio_url;
    }

    /**
     * <br> interface.
     */

    // view.

    // user data view.
    @SuppressLint("SetTextI18n")
    @Override
    public void drawUserInfo(User u) {
        if (!TextUtils.isEmpty(u.location)) {
            locationTxt.setText(u.location);
        } else {
            locationTxt.setText("Unknown");
        }

        if (!TextUtils.isEmpty(u.bio)) {
            bioTxt.setText(u.bio);
        } else {
            bioTxt.setVisibility(GONE);
        }

        List<String> titleList = new ArrayList<>();
        titleList.add(u.total_photos + (u.total_photos > 1 ? " PHOTOS" : " PHOTO"));
        titleList.add(u.total_collections + (u.total_collections > 1 ? " COLLECTIONS" : " COLLECTION"));
        titleList.add(u.total_likes + (u.total_likes > 1 ? " LIKES" : " LIKE"));
        adapter.titleList = titleList;
        adapter.notifyDataSetChanged();
    }

    @Override
    public void initRefreshStart() {
        loadPresenter.setLoadingState();
    }

    @Override
    public void requestDetailsSuccess() {
        loadPresenter.setNormalState();
    }

    @Override
    public void requestDetailsFailed() {
        loadPresenter.setFailedState();
    }

    // load view.

    @Override
    public void animShow(final View v) {
        AnimUtils.animShow(v);
    }

    @Override
    public void animHide(final View v) {
        AnimUtils.animHide(v);
    }

    @Override
    public void setLoadingState() {
        animShow(progressView);
        animHide(profileContainer);
    }

    @Override
    public void setFailedState() {
        // do nothing.
    }

    @Override
    public void setNormalState() {
        animShow(profileContainer);
        animHide(progressView);
    }

    @Override
    public void resetLoadingState() {
        // do nothing.
    }
}
