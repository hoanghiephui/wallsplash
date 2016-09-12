package com.unsplash.wallsplash.collection.view.widget;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.clockbyte.admobadapter.expressads.AdmobExpressRecyclerAdapterWrapper;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.unsplash.wallsplash.R;
import com.unsplash.wallsplash.WallSplashApplication;
import com.unsplash.wallsplash._common.data.data.Collection;
import com.unsplash.wallsplash._common.i.model.LoadModel;
import com.unsplash.wallsplash._common.i.model.PhotosModel;
import com.unsplash.wallsplash._common.i.model.ScrollModel;
import com.unsplash.wallsplash._common.i.presenter.LoadPresenter;
import com.unsplash.wallsplash._common.i.presenter.PhotosPresenter;
import com.unsplash.wallsplash._common.i.presenter.ScrollPresenter;
import com.unsplash.wallsplash._common.i.presenter.SwipeBackPresenter;
import com.unsplash.wallsplash._common.i.view.LoadView;
import com.unsplash.wallsplash._common.i.view.PhotosView;
import com.unsplash.wallsplash._common.i.view.ScrollView;
import com.unsplash.wallsplash._common.i.view.SwipeBackView;
import com.unsplash.wallsplash._common.ui.widget.SwipeBackLayout;
import com.unsplash.wallsplash._common.ui.widget.swipeRefreshLayout.BothWaySwipeRefreshLayout;
import com.unsplash.wallsplash._common.utils.AnimUtils;
import com.unsplash.wallsplash._common.utils.BackToTopUtils;
import com.unsplash.wallsplash._common.utils.ThemeUtils;
import com.unsplash.wallsplash.collection.model.widget.LoadObject;
import com.unsplash.wallsplash.collection.model.widget.PhotosObject;
import com.unsplash.wallsplash.collection.model.widget.ScrollObject;
import com.unsplash.wallsplash.collection.presenter.widget.LoadImplementor;
import com.unsplash.wallsplash.collection.presenter.widget.PhotosImplementor;
import com.unsplash.wallsplash.collection.presenter.widget.ScrollImplementor;
import com.unsplash.wallsplash.collection.presenter.widget.SwipeBackImplementor;

/**
 * Collection photos view.
 */

public class CollectionPhotosView extends FrameLayout
        implements PhotosView, LoadView, ScrollView, SwipeBackView,
        View.OnClickListener, BothWaySwipeRefreshLayout.OnRefreshAndLoadListener {
    // model.
    private PhotosModel photosModel;
    private LoadModel loadModel;
    private ScrollModel scrollModel;

    // view.
    private CircularProgressView progressView;
    private RelativeLayout feedbackContainer;
    private TextView feedbackText;

    private BothWaySwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;

    // presenter.
    private PhotosPresenter photosPresenter;
    private LoadPresenter loadPresenter;
    private ScrollPresenter scrollPresenter;
    private SwipeBackPresenter swipeBackPresenter;
    private AdmobExpressRecyclerAdapterWrapper adapterWrapper;

    /**
     * <br> life cycle.
     */

    public CollectionPhotosView(Context context) {
        super(context);
        this.initialize();
    }

    public CollectionPhotosView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initialize();
    }

    public CollectionPhotosView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.initialize();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CollectionPhotosView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.initialize();
    }

    @SuppressLint("InflateParams")
    public void initialize() {
        View loadingView = LayoutInflater.from(getContext()).inflate(R.layout.container_loading_view_large, null);
        addView(loadingView);

        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.container_photo_list, null);
        addView(contentView);

        initModel();
        initView();
        initPresenter();
    }

    /**
     * <br> presenter.
     */

    private void initPresenter() {
        this.photosPresenter = new PhotosImplementor(photosModel, this);
        this.loadPresenter = new LoadImplementor(loadModel, this);
        this.scrollPresenter = new ScrollImplementor(scrollModel, this);
        this.swipeBackPresenter = new SwipeBackImplementor(this);
    }

    /**
     * <br> view.
     */

    // init.
    private void initView() {
        this.initContentView();
        this.initLoadingView();
    }

    private void initContentView() {
        this.refreshLayout = (BothWaySwipeRefreshLayout) findViewById(R.id.container_photo_list_swipeRefreshLayout);
        refreshLayout.setOnRefreshAndLoadListener(this);
        refreshLayout.setPermitRefresh(false);
        refreshLayout.setVisibility(GONE);
        if (ThemeUtils.getInstance(getContext()).isLightTheme()) {
            refreshLayout.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.colorTextContent_light));
            refreshLayout.setProgressBackgroundColorSchemeResource(R.color.colorPrimary_light);
        } else {
            refreshLayout.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.colorTextContent_dark));
            refreshLayout.setProgressBackgroundColorSchemeResource(R.color.colorPrimary_dark);
        }

        this.recyclerView = (RecyclerView) findViewById(R.id.container_photo_list_recyclerView);
        String[] testDevicesIds = new String[]{getContext().getString(R.string.testDeviceID), AdRequest.DEVICE_ID_EMULATOR};
        adapterWrapper = new AdmobExpressRecyclerAdapterWrapper(getContext(), "ca-app-pub-2257698129050878/7146096743", testDevicesIds, new AdSize(AdSize.FULL_WIDTH, 250));
        adapterWrapper.setAdapter(photosModel.getAdapter());
        adapterWrapper.setLimitOfAds(3);
        adapterWrapper.setNoOfDataBetweenAds(10);
        adapterWrapper.setFirstAdIndex(2);
        adapterWrapper.setViewTypeBiggestSource(100);
        recyclerView.setAdapter(adapterWrapper);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.addOnScrollListener(onScrollListener);
    }

    private void initLoadingView() {
        this.progressView = (CircularProgressView) findViewById(R.id.container_loading_view_large_progressView);

        this.feedbackContainer = (RelativeLayout) findViewById(R.id.container_loading_view_large_feedbackContainer);
        feedbackContainer.setVisibility(GONE);

        ImageView feedbackImg = (ImageView) findViewById(R.id.container_loading_view_large_feedbackImg);
        Glide.with(getContext())
                .load(R.drawable.feedback_load_failed)
                .dontAnimate()
                .into(feedbackImg);

        this.feedbackText = (TextView) findViewById(R.id.container_loading_view_large_feedbackTxt);

        Button retryButton = (Button) findViewById(R.id.container_loading_view_large_feedbackBtn);
        retryButton.setOnClickListener(this);
    }

    // interface.

    public void pagerBackToTop() {
        scrollPresenter.scrollToTop();
    }

    /**
     * <br> model.
     */

    // init.
    private void initModel() {
        this.photosModel = new PhotosObject(
                getContext(),
                WallSplashApplication.getInstance().getCollection(),
                WallSplashApplication.getInstance().getCollection().curated
                        ? PhotosObject.PHOTOS_TYPE_CURATED : PhotosObject.PHOTOS_TYPE_NORMAL);
        this.loadModel = new LoadObject(LoadObject.LOADING_STATE);
        this.scrollModel = new ScrollObject();
    }

    // interface.

    public void setActivity(Activity a) {
        photosPresenter.setActivityForAdapter(a);
    }

    public void initRefresh() {
        photosPresenter.initRefresh(getContext());
    }

    public void cancelRequest() {
        photosPresenter.cancelRequest();
    }

    public Collection getCollection() {
        return (Collection) photosPresenter.getRequestKey();
    }

    public boolean canSwipeBack(int dir) {
        return swipeBackPresenter.checkCanSwipeBack(dir);
    }

    public boolean needPagerBackToTop() {
        return scrollPresenter.needBackToTop();
    }

    /**
     * <br> interface.
     */

    // on click listener.
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.container_loading_view_large_feedbackBtn:
                photosPresenter.initRefresh(getContext());
                break;
        }
    }

    // on refresh and load listener.

    @Override
    public void onRefresh() {
        photosPresenter.refreshNew(getContext(), false);
    }

    @Override
    public void onLoad() {
        photosPresenter.loadMore(getContext(), false);
    }

    // on scroll listener.

    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            scrollPresenter.autoLoad(dy);
        }
    };

    // view.

    // photos view.

    @Override
    public void setRefreshing(boolean refreshing) {
        refreshLayout.setRefreshing(refreshing);
    }

    @Override
    public void setLoading(boolean loading) {
        refreshLayout.setLoading(loading);
    }

    @Override
    public void setPermitRefreshing(boolean permit) {
        refreshLayout.setPermitRefresh(permit);
    }

    @Override
    public void setPermitLoading(boolean permit) {
        refreshLayout.setPermitLoad(permit);
    }

    @Override
    public void initRefreshStart() {
        loadPresenter.setLoadingState();
    }

    @Override
    public void requestPhotosSuccess() {
        loadPresenter.setNormalState();
    }

    @Override
    public void requestPhotosFailed(String feedback) {
        feedbackText.setText(feedback);
        loadPresenter.setFailedState();
    }

    // load view.

    @Override
    public void animShow(View v) {
        AnimUtils.animShow(v);
    }

    @Override
    public void animHide(final View v) {
        AnimUtils.animHide(v);
    }

    @Override
    public void setLoadingState() {
        animShow(progressView);
        animHide(feedbackContainer);
    }

    @Override
    public void setFailedState() {
        animShow(feedbackContainer);
        animHide(progressView);
    }

    @Override
    public void setNormalState() {
        animShow(refreshLayout);
        animHide(progressView);
    }

    @Override
    public void resetLoadingState() {
        animShow(progressView);
        animHide(refreshLayout);
    }

    // scroll view.

    @Override
    public void scrollToTop() {
        int firstVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
        if (firstVisibleItem > 5) {
            recyclerView.scrollToPosition(5);
        }
        recyclerView.smoothScrollToPosition(0);

        if (!BackToTopUtils.getInstance(getContext()).isNotified()) {
            BackToTopUtils.getInstance(getContext()).showSetBackToTopSnackbar();
        }
    }

    @Override
    public void autoLoad(int dy) {
        int lastVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
        int totalItemCount = recyclerView.getAdapter().getItemCount();
        if (photosPresenter.canLoadMore()
                && lastVisibleItem >= totalItemCount - 10 && totalItemCount > 0 && dy > 0) {
            photosPresenter.loadMore(getContext(), false);
        }
        if (!ViewCompat.canScrollVertically(recyclerView, -1)) {
            scrollPresenter.setToTop(true);
        } else {
            scrollPresenter.setToTop(false);
        }
        if (!ViewCompat.canScrollVertically(recyclerView, 1) && photosPresenter.isLoading()) {
            refreshLayout.setLoading(true);
        }
    }

    @Override
    public boolean needBackToTop() {
        return !scrollPresenter.isToTop()
                && loadPresenter.getLoadState() == LoadObject.NORMAL_STATE;
    }

    // swipe back view.

    @Override
    public boolean checkCanSwipeBack(int dir) {
        return SwipeBackLayout.canSwipeBack(recyclerView, dir)
                || photosPresenter.getAdapterItemCount() <= 0;
    }
}
