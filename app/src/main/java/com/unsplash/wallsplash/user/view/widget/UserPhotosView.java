package com.unsplash.wallsplash.user.view.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.clockbyte.admobadapter.expressads.AdmobExpressRecyclerAdapterWrapper;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.unsplash.wallsplash.R;
import com.unsplash.wallsplash.common.i.model.LoadModel;
import com.unsplash.wallsplash.common.i.model.PhotosModel;
import com.unsplash.wallsplash.common.i.model.ScrollModel;
import com.unsplash.wallsplash.common.i.presenter.LoadPresenter;
import com.unsplash.wallsplash.common.i.presenter.PagerPresenter;
import com.unsplash.wallsplash.common.i.presenter.PhotosPresenter;
import com.unsplash.wallsplash.common.i.presenter.ScrollPresenter;
import com.unsplash.wallsplash.common.i.presenter.SwipeBackPresenter;
import com.unsplash.wallsplash.common.i.view.LoadView;
import com.unsplash.wallsplash.common.i.view.PagerView;
import com.unsplash.wallsplash.common.i.view.PhotosView;
import com.unsplash.wallsplash.common.i.view.ScrollView;
import com.unsplash.wallsplash.common.i.view.SwipeBackView;
import com.unsplash.wallsplash.common.ui.widget.SwipeBackLayout;
import com.unsplash.wallsplash.common.ui.widget.swipeRefreshLayout.BothWaySwipeRefreshLayout;
import com.unsplash.wallsplash.common.utils.AnimUtils;
import com.unsplash.wallsplash.common.utils.BackToTopUtils;
import com.unsplash.wallsplash.common.utils.ThemeUtils;
import com.unsplash.wallsplash.user.model.widget.LoadObject;
import com.unsplash.wallsplash.user.model.widget.PhotosObject;
import com.unsplash.wallsplash.user.model.widget.ScrollObject;
import com.unsplash.wallsplash.user.presenter.widget.LoadImplementor;
import com.unsplash.wallsplash.user.presenter.widget.PagerImplementor;
import com.unsplash.wallsplash.user.presenter.widget.PhotosImplementor;
import com.unsplash.wallsplash.user.presenter.widget.ScrollImplementor;
import com.unsplash.wallsplash.user.presenter.widget.SwipeBackImplementor;

/**
 * User photos view.
 */

@SuppressLint("ViewConstructor")
public class UserPhotosView extends FrameLayout
        implements PhotosView, PagerView, LoadView, ScrollView, SwipeBackView,
        View.OnClickListener, BothWaySwipeRefreshLayout.OnRefreshAndLoadListener {
    // model.
    private PhotosModel photosModel;
    private LoadModel loadModel;
    private ScrollModel scrollModel;

    // view.
    private CircularProgressView progressView;
    private Button retryButton;

    private BothWaySwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;

    // presenter.
    private PhotosPresenter photosPresenter;
    private PagerPresenter pagerPresenter;
    private LoadPresenter loadPresenter;
    private ScrollPresenter scrollPresenter;
    private SwipeBackPresenter swipeBackPresenter;
    private AdmobExpressRecyclerAdapterWrapper adapterWrapper;

    /**
     * <br> life cycle.
     */

    public UserPhotosView(Activity a, int type) {
        super(a);
        this.initialize(a, type);
    }

    @SuppressLint("InflateParams")
    private void initialize(Activity a, int type) {
        View loadingView = LayoutInflater.from(getContext()).inflate(R.layout.container_loading_view_mini, null);
        addView(loadingView);
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.container_photo_list, null);
        addView(contentView);

        initModel(a, type);
        initView();
        initPresenter();
    }

    /**
     * <br> presenter.
     */

    private void initPresenter() {
        this.photosPresenter = new PhotosImplementor(photosModel, this);
        this.pagerPresenter = new PagerImplementor(this);
        this.loadPresenter = new LoadImplementor(loadModel, this);
        this.scrollPresenter = new ScrollImplementor(scrollModel, this);
        this.swipeBackPresenter = new SwipeBackImplementor(this);
    }

    /**
     * <br> view.
     */

    private void initView() {
        this.progressView = (CircularProgressView) findViewById(R.id.container_loading_view_mini_progressView);
        this.retryButton = (Button) findViewById(R.id.container_loading_view_mini_retryButton);
        retryButton.setOnClickListener(this);
        retryButton.setVisibility(GONE);

        this.refreshLayout = (BothWaySwipeRefreshLayout) findViewById(R.id.container_photo_list_swipeRefreshLayout);
        if (ThemeUtils.getInstance(getContext()).isLightTheme()) {
            refreshLayout.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.colorTextContent_light));
            refreshLayout.setProgressBackgroundColorSchemeResource(R.color.colorPrimary_light);
        } else {
            refreshLayout.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.colorTextContent_dark));
            refreshLayout.setProgressBackgroundColorSchemeResource(R.color.colorPrimary_dark);
        }
        refreshLayout.setPermitRefresh(false);
        refreshLayout.setVisibility(GONE);

        this.recyclerView = (RecyclerView) findViewById(R.id.container_photo_list_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        String[] testDevicesIds = new String[]{getContext().getString(R.string.testDeviceID), AdRequest.DEVICE_ID_EMULATOR};
        adapterWrapper = new AdmobExpressRecyclerAdapterWrapper(getContext(), getContext().getString(R.string.id_ads), testDevicesIds, new AdSize(AdSize.FULL_WIDTH, 250));
        adapterWrapper.setAdapter(photosModel.getAdapter());
        adapterWrapper.setLimitOfAds(50);
        adapterWrapper.setNoOfDataBetweenAds(10);
        adapterWrapper.setFirstAdIndex(2);
        adapterWrapper.setViewTypeBiggestSource(100);
        recyclerView.setAdapter(adapterWrapper);
        recyclerView.addOnScrollListener(scrollListener);
    }

    /**
     * <br> model.
     */

    private void initModel(Activity a, int type) {
        this.photosModel = new PhotosObject(a, type);
        this.loadModel = new LoadObject(LoadObject.LOADING_STATE);
        this.scrollModel = new ScrollObject();
    }

    /**
     * <br> interface.
     */

    // on click listener.
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.container_loading_view_mini_retryButton:
                photosPresenter.initRefresh(getContext());
                break;
        }
    }// on refresh an load listener.

    @Override
    public void onRefresh() {
        photosPresenter.refreshNew(getContext(), false);
    }

    @Override
    public void onLoad() {
        photosPresenter.loadMore(getContext(), false);
    }

    // on scroll listener.

    private RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {

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
        loadPresenter.setFailedState();
    }

    // pager view.

    @Override
    public void checkToRefresh() { // interface
        if (pagerPresenter.checkNeedRefresh()) {
            pagerPresenter.refreshPager();
        }
    }

    @Override
    public boolean checkNeedRefresh() {
        return loadPresenter.getLoadState() == com.unsplash.wallsplash.me.model.widget.LoadObject.FAILED_STATE
                || (loadPresenter.getLoadState() == com.unsplash.wallsplash.me.model.widget.LoadObject.LOADING_STATE
                && !photosPresenter.isRefreshing() && !photosPresenter.isLoading());
    }

    @Override
    public boolean checkNeedBackToTop() {
        return scrollPresenter.needBackToTop();
    }

    @Override
    public void refreshPager() {
        photosPresenter.initRefresh(getContext());
    }

    @Override
    public void scrollToPageTop() { // interface.
        scrollPresenter.scrollToTop();
    }

    @Override
    public void cancelRequest() {
        photosPresenter.cancelRequest();
    }

    @Override
    public void setKey(String key) {
        photosPresenter.setOrder(key);
    }

    @Override
    public String getKey() {
        return photosModel.getPhotosOrder();
    }

    @Override
    public boolean canSwipeBack(int dir) {
        return swipeBackPresenter.checkCanSwipeBack(dir);
    }

    @Override
    public int getItemCounts() {
        if (loadPresenter.getLoadState() != LoadObject.NORMAL_STATE) {
            return 0;
        } else {
            return photosPresenter.getAdapterItemCount();
        }
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
        animHide(retryButton);
    }

    @Override
    public void setFailedState() {
        animShow(retryButton);
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
