package com.unsplash.wallsplash.main.view.widget;

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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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
import com.unsplash.wallsplash.common.i.view.LoadView;
import com.unsplash.wallsplash.common.i.view.PagerView;
import com.unsplash.wallsplash.common.i.view.PhotosView;
import com.unsplash.wallsplash.common.i.view.ScrollView;
import com.unsplash.wallsplash.common.ui.widget.swipeRefreshLayout.BothWaySwipeRefreshLayout;
import com.unsplash.wallsplash.common.utils.AnimUtils;
import com.unsplash.wallsplash.common.utils.BackToTopUtils;
import com.unsplash.wallsplash.common.utils.ThemeUtils;
import com.unsplash.wallsplash.main.model.widget.LoadObject;
import com.unsplash.wallsplash.main.model.widget.PhotosObject;
import com.unsplash.wallsplash.main.model.widget.ScrollObject;
import com.unsplash.wallsplash.main.presenter.widget.LoadImplementor;
import com.unsplash.wallsplash.main.presenter.widget.PagerImplementor;
import com.unsplash.wallsplash.main.presenter.widget.PhotosImplementor;
import com.unsplash.wallsplash.main.presenter.widget.ScrollImplementor;

/**
 * Home photos view.
 */

@SuppressLint("ViewConstructor")
public class HomePhotosView extends FrameLayout
        implements PhotosView, PagerView, LoadView, ScrollView,
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
    private AdmobExpressRecyclerAdapterWrapper adapterWrapper;

    // presenter.
    private PhotosPresenter photosPresenter;
    private PagerPresenter pagerPresenter;
    private LoadPresenter loadPresenter;
    private ScrollPresenter scrollPresenter;
    /**
     * <br> life cycle.
     */


    public HomePhotosView(Activity a, int photosType) {
        super(a);
        this.initialize(a, photosType);
    }

    @SuppressLint("InflateParams")
    private void initialize(Activity a, int photosType) {
        View loadingView = LayoutInflater.from(getContext()).inflate(R.layout.container_loading_view_large, null, false);
        addView(loadingView);

        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.container_photo_list, null, false);
        addView(contentView);

        initModel(a, photosType);
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
    }

    /**
     * <br> view.
     */

    private void initView() {
        this.initContentView();
        this.initLoadingView();
    }

    private void initContentView() {
        this.refreshLayout = (BothWaySwipeRefreshLayout) findViewById(R.id.container_photo_list_swipeRefreshLayout);
        refreshLayout.setOnRefreshAndLoadListener(this);
        refreshLayout.setVisibility(GONE);
        if (ThemeUtils.getInstance(getContext()).isLightTheme()) {
            refreshLayout.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.colorWhite));
            refreshLayout.setProgressBackgroundColorSchemeResource(R.color.colorPrimary_light);
        } else {
            refreshLayout.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.colorTextContent_dark));
            refreshLayout.setProgressBackgroundColorSchemeResource(R.color.colorPrimary_dark);
        }

        this.recyclerView = (RecyclerView) findViewById(R.id.container_photo_list_recyclerView);
        String[] testDevicesIds = new String[]{getContext().getString(R.string.testDeviceID), AdRequest.DEVICE_ID_EMULATOR};
        adapterWrapper = new AdmobExpressRecyclerAdapterWrapper(getContext(), getContext().getString(R.string.id_ads), testDevicesIds, new AdSize(AdSize.FULL_WIDTH, 250));
        adapterWrapper.setAdapter(photosModel.getAdapter());
        adapterWrapper.setLimitOfAds(100);
        adapterWrapper.setNoOfDataBetweenAds(10);
        adapterWrapper.setFirstAdIndex(2);
        adapterWrapper.setViewTypeBiggestSource(100);
        recyclerView.setAdapter(adapterWrapper);
        //recyclerView.setAdapter(photosModel.getAdapter());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        recyclerView.addOnScrollListener(onScrollListener);
    }

    private void initLoadingView() {
        this.progressView = (CircularProgressView) findViewById(R.id.container_loading_view_large_progressView);

        this.feedbackContainer = (RelativeLayout) findViewById(R.id.container_loading_view_large_feedbackContainer);
        feedbackContainer.setVisibility(GONE);

        ImageView feedbackImg = (ImageView) findViewById(R.id.container_loading_view_large_feedbackImg);
        Glide.with(getContext())
                .load(R.drawable.ic_launcher)
                .dontAnimate()
                .into(feedbackImg);

        this.feedbackText = (TextView) findViewById(R.id.container_loading_view_large_feedbackTxt);

        Button retryButton = (Button) findViewById(R.id.container_loading_view_large_feedbackBtn);
        retryButton.setOnClickListener(this);
    }

    /**
     * <br> model.
     */

    private void initModel(Activity a, int photosType) {
        this.photosModel = new PhotosObject(a, photosType);
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
            case R.id.container_loading_view_large_feedbackBtn:
                photosPresenter.initRefresh(getContext());
                break;
        }
    }

    // on refresh an load listener.

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

    // pager view.

    @Override
    public void checkToRefresh() { // interface
        if (pagerPresenter.checkNeedRefresh()) {
            pagerPresenter.refreshPager();
        }
    }

    @Override
    public boolean checkNeedRefresh() {
        return photosPresenter.getAdapterItemCount() <= 0
                && !photosPresenter.isRefreshing() && !photosPresenter.isLoading();
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
        onDestroy();
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
        return false;
    }

    @Override
    public int getItemCounts() {
        if (loadPresenter.getLoadState() != LoadObject.NORMAL_STATE) {
            return 0;
        } else {
            return photosModel.getAdapter().getRealItemCount();
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

    public void onDestroy() {
        adapterWrapper.destroyAds();
    }
}
