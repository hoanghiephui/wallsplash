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

import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.unsplash.wallsplash.R;
import com.unsplash.wallsplash._common.i.model.CollectionsModel;
import com.unsplash.wallsplash._common.i.model.LoadModel;
import com.unsplash.wallsplash._common.i.model.ScrollModel;
import com.unsplash.wallsplash._common.i.presenter.CollectionsPresenter;
import com.unsplash.wallsplash._common.i.presenter.LoadPresenter;
import com.unsplash.wallsplash._common.i.presenter.PagerPresenter;
import com.unsplash.wallsplash._common.i.presenter.ScrollPresenter;
import com.unsplash.wallsplash._common.i.presenter.SwipeBackPresenter;
import com.unsplash.wallsplash._common.i.view.CollectionsView;
import com.unsplash.wallsplash._common.i.view.LoadView;
import com.unsplash.wallsplash._common.i.view.PagerView;
import com.unsplash.wallsplash._common.i.view.ScrollView;
import com.unsplash.wallsplash._common.i.view.SwipeBackView;
import com.unsplash.wallsplash._common.ui.widget.SwipeBackLayout;
import com.unsplash.wallsplash._common.ui.widget.swipeRefreshLayout.BothWaySwipeRefreshLayout;
import com.unsplash.wallsplash._common.utils.AnimUtils;
import com.unsplash.wallsplash._common.utils.BackToTopUtils;
import com.unsplash.wallsplash._common.utils.ThemeUtils;
import com.unsplash.wallsplash.user.model.widget.CollectionsObject;
import com.unsplash.wallsplash.user.model.widget.LoadObject;
import com.unsplash.wallsplash.user.model.widget.ScrollObject;
import com.unsplash.wallsplash.user.presenter.widget.CollectionsImplementor;
import com.unsplash.wallsplash.user.presenter.widget.LoadImplementor;
import com.unsplash.wallsplash.user.presenter.widget.PagerImplementor;
import com.unsplash.wallsplash.user.presenter.widget.ScrollImplementor;
import com.unsplash.wallsplash.user.presenter.widget.SwipeBackImplementor;

/**
 * User collections view.
 */

@SuppressLint("ViewConstructor")
public class UserCollectionsView extends FrameLayout
        implements CollectionsView, PagerView, LoadView, ScrollView, SwipeBackView,
        View.OnClickListener, BothWaySwipeRefreshLayout.OnRefreshAndLoadListener {
    // model.
    private CollectionsModel collectionsModel;
    private LoadModel loadModel;
    private ScrollModel scrollModel;

    // view.
    private CircularProgressView progressView;
    private Button retryButton;

    private BothWaySwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;

    // presenter.
    private CollectionsPresenter collectionsPresenter;
    private PagerPresenter pagerPresenter;
    private LoadPresenter loadPresenter;
    private ScrollPresenter scrollPresenter;
    private SwipeBackPresenter swipeBackPresenter;

    /**
     * <br> life cycle.
     */

    public UserCollectionsView(Activity a) {
        super(a);
        this.initialize(a);
    }

    @SuppressLint("InflateParams")
    private void initialize(Activity a) {
        View loadingView = LayoutInflater.from(getContext()).inflate(R.layout.container_loading_view_mini, null);
        addView(loadingView);
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.container_photo_list, null);
        addView(contentView);

        initModel(a);
        initView();
        initPresenter();
    }

    /**
     * <br> presenter.
     */

    private void initPresenter() {
        this.collectionsPresenter = new CollectionsImplementor(collectionsModel, this);
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
        recyclerView.setAdapter(collectionsModel.getAdapter());
        recyclerView.addOnScrollListener(scrollListener);
    }

    /**
     * <br> model.
     */

    private void initModel(Activity a) {
        this.collectionsModel = new CollectionsObject(a);
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
                collectionsPresenter.initRefresh(getContext());
                break;
        }
    }// on refresh an load listener.

    @Override
    public void onRefresh() {
        collectionsPresenter.refreshNew(getContext(), false);
    }

    @Override
    public void onLoad() {
        collectionsPresenter.loadMore(getContext(), false);
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

    // collections view.

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
    public void requestCollectionsSuccess() {
        loadPresenter.setNormalState();
    }

    @Override
    public void requestCollectionsFailed(String feedback) {
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
                && !collectionsPresenter.isRefreshing() && !collectionsPresenter.isLoading());
    }

    @Override
    public boolean checkNeedBackToTop() {
        return scrollPresenter.needBackToTop();
    }

    @Override
    public void refreshPager() {
        collectionsPresenter.initRefresh(getContext());
    }

    @Override
    public void scrollToPageTop() { // interface.
        scrollPresenter.scrollToTop();
    }

    @Override
    public void cancelRequest() {
        collectionsPresenter.cancelRequest();
    }

    @Override
    public void setKey(String key) {
        // do nothing.
    }

    @Override
    public String getKey() {
        return null;
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
            return collectionsPresenter.getAdapterItemCount();
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
        if (collectionsPresenter.canLoadMore()
                && lastVisibleItem >= totalItemCount - 10 && totalItemCount > 0 && dy > 0) {
            collectionsPresenter.loadMore(getContext(), false);
        }
        if (!ViewCompat.canScrollVertically(recyclerView, -1)) {
            scrollPresenter.setToTop(true);
        } else {
            scrollPresenter.setToTop(false);
        }
        if (!ViewCompat.canScrollVertically(recyclerView, 1) && collectionsPresenter.isLoading()) {
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
                || collectionsPresenter.getAdapterItemCount() <= 0;
    }
}