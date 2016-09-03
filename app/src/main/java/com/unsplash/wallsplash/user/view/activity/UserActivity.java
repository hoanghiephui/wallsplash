package com.unsplash.wallsplash.user.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.unsplash.wallsplash.R;
import com.unsplash.wallsplash.WallSplashApplication;
import com.unsplash.wallsplash._common.i.model.PagerManageModel;
import com.unsplash.wallsplash._common.i.presenter.PagerManagePresenter;
import com.unsplash.wallsplash._common.i.presenter.PopupManagePresenter;
import com.unsplash.wallsplash._common.i.presenter.SwipeBackManagePresenter;
import com.unsplash.wallsplash._common.i.presenter.ToolbarPresenter;
import com.unsplash.wallsplash._common.i.view.PagerManageView;
import com.unsplash.wallsplash._common.i.view.PagerView;
import com.unsplash.wallsplash._common.i.view.PopupManageView;
import com.unsplash.wallsplash._common.i.view.SwipeBackManageView;
import com.unsplash.wallsplash._common.i.view.ToolbarView;
import com.unsplash.wallsplash._common.ui.activity.BaseActivity;
import com.unsplash.wallsplash._common.ui.adapter.MyPagerAdapter;
import com.unsplash.wallsplash._common.ui.widget.CircleImageView;
import com.unsplash.wallsplash._common.ui.widget.StatusBarView;
import com.unsplash.wallsplash._common.ui.widget.SwipeBackLayout;
import com.unsplash.wallsplash._common.utils.AnimUtils;
import com.unsplash.wallsplash._common.utils.BackToTopUtils;
import com.unsplash.wallsplash._common.utils.DisplayUtils;
import com.unsplash.wallsplash._common.utils.ThemeUtils;
import com.unsplash.wallsplash.user.model.activity.PagerManageObject;
import com.unsplash.wallsplash.user.model.widget.PhotosObject;
import com.unsplash.wallsplash.user.presenter.activity.PagerManageImplementor;
import com.unsplash.wallsplash.user.presenter.activity.PopupManageImplementor;
import com.unsplash.wallsplash.user.presenter.activity.SwipeBackManageImplementor;
import com.unsplash.wallsplash.user.presenter.activity.ToolbarImplementor;
import com.unsplash.wallsplash.user.view.widget.UserCollectionsView;
import com.unsplash.wallsplash.user.view.widget.UserPhotosView;
import com.unsplash.wallsplash.user.view.widget.UserProfileView;

import java.util.ArrayList;
import java.util.List;

/**
 * User activity.
 */

public class UserActivity extends BaseActivity
        implements ToolbarView, PagerManageView, PopupManageView, SwipeBackManageView,
        Toolbar.OnMenuItemClickListener, View.OnClickListener,
        ViewPager.OnPageChangeListener, SwipeBackLayout.OnSwipeListener {
    // model.
    private PagerManageModel pagerManageModel;

    // view.
    private CoordinatorLayout container;
    private AppBarLayout appBar;
    private Toolbar toolbar;
    private MyPagerAdapter adapter;
    private UserProfileView userProfileView;

    private PagerView[] pagers = new PagerView[3];
    private DisplayUtils utils;

    // presenter.
    private ToolbarPresenter toolbarPresenter;
    private PagerManagePresenter pagerManagePresenter;
    private PopupManagePresenter popupManagePresenter;
    private SwipeBackManagePresenter swipeBackManagePresenter;

    /**
     * <br> life cycle.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!isStarted()) {
            setStarted();
            initModel();
            initView();
            initPresenter();
            AnimUtils.animInitShow((View) pagers[0], 400);
            pagers[0].refreshPager();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        userProfileView.cancelRequest();
        for (PagerView p : pagers) {
            p.cancelRequest();
        }
    }

    @Override
    protected void setTheme() {
        if (ThemeUtils.getInstance(this).isLightTheme()) {
            setTheme(R.style.MysplashTheme_light_Translucent_User);
        } else {
            setTheme(R.style.MysplashTheme_dark_Translucent_User);
        }
    }

    @Override
    public void onBackPressed() {
        if (WallSplashApplication.getInstance().isActivityInBackstage()) {
            super.onBackPressed();
        } else if (pagerManagePresenter.needPagerBackToTop() && BackToTopUtils.getInstance(this).isSetBackToTop(false)) {
            pagerManagePresenter.pagerScrollToTop();
        } else {
            super.onBackPressed();
        }
    }

    /**
     * <br> presenter.
     */

    private void initPresenter() {
        this.toolbarPresenter = new ToolbarImplementor(this);
        this.pagerManagePresenter = new PagerManageImplementor(pagerManageModel, this);
        this.popupManagePresenter = new PopupManageImplementor(this);
        this.swipeBackManagePresenter = new SwipeBackManageImplementor(this);
    }

    /**
     * <br> view.
     */

    private void initView() {
        SwipeBackLayout swipeBackLayout = (SwipeBackLayout) findViewById(R.id.activity_user_swipeBackLayout);
        swipeBackLayout.setOnSwipeListener(this);

        StatusBarView statusBar = (StatusBarView) findViewById(R.id.activity_user_statusBar);
        if (ThemeUtils.getInstance(this).isNeedSetStatusBarMask()) {
            statusBar.setMask(true);
        }

        this.container = (CoordinatorLayout) findViewById(R.id.activity_user_container);
        this.appBar = (AppBarLayout) findViewById(R.id.activity_user_appBar);

        this.toolbar = (Toolbar) findViewById(R.id.activity_user_toolbar);
        if (ThemeUtils.getInstance(this).isLightTheme()) {
            toolbar.inflateMenu(R.menu.activity_user_toolbar_light);
            toolbar.setNavigationIcon(R.drawable.ic_toolbar_back_light);
        } else {
            toolbar.inflateMenu(R.menu.activity_user_toolbar_dark);
            toolbar.setNavigationIcon(R.drawable.ic_toolbar_back_dark);
        }
        toolbar.setOnMenuItemClickListener(this);
        toolbar.setNavigationOnClickListener(this);

        CircleImageView avatar = (CircleImageView) findViewById(R.id.activity_user_avatar);
        Glide.with(this)
                .load(WallSplashApplication.getInstance().getUser().profile_image.large)
                .priority(Priority.HIGH)
                .override(128, 128)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(avatar);

        TextView title = (TextView) findViewById(R.id.activity_user_title);
        title.setText(WallSplashApplication.getInstance().getUser().name);

        this.userProfileView = (UserProfileView) findViewById(R.id.activity_user_profileView);

        initPages();

        userProfileView.requestUserProfile(adapter);
        this.utils = new DisplayUtils(this);
    }

    private void initPages() {
        List<View> pageList = new ArrayList<>();
        pageList.add(new UserPhotosView(this, PhotosObject.PHOTOS_TYPE_PHOTOS));
        pageList.add(new UserCollectionsView(this));
        pageList.add(new UserPhotosView(this, PhotosObject.PHOTOS_TYPE_LIKES));
        for (int i = 0; i < pageList.size(); i++) {
            pagers[i] = (PagerView) pageList.get(i);
        }

        List<String> tabList = new ArrayList<>();
        tabList.add("PHOTOS");
        tabList.add("COLLECTIONS");
        tabList.add("LIKES");
        this.adapter = new MyPagerAdapter(pageList, tabList);

        ViewPager viewPager = (ViewPager) findViewById(R.id.activity_user_viewPager);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(this);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.activity_user_tabLayout);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setupWithViewPager(viewPager);
    }

    /**
     * <br> model.
     */

    private void initModel() {
        this.pagerManageModel = new PagerManageObject(0);
    }

    /**
     * <br> interface.
     */

    // on click listener.
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case -1:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAfterTransition();
                } else {
                    finish();
                }
                break;
        }
    }

    // on menu item click listener.

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        toolbarPresenter.touchMenuItem(item.getItemId());
        return true;
    }

    // on page change listener.

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        // do nothing.
    }

    @Override
    public void onPageSelected(int position) {
        pagerManagePresenter.setPagerPosition(position);
        pagerManagePresenter.checkToRefresh(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        // do nothing.
    }

    // on swipe listener.(swipe back listener)

    @Override
    public boolean canSwipeBack(int dir) {
        return swipeBackManagePresenter.checkCanSwipeBack(dir);
    }

    @Override
    public void onSwipeFinish() {
        swipeBackManagePresenter.swipeBackFinish();
    }

    // snackbar container.

    @Override
    public View getSnackbarContainer() {
        return container;
    }

    // view.

    // toolbar view.

    @Override
    public void touchNavigatorIcon() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
        } else {
            finish();
        }
    }

    @Override
    public void touchToolbar() {
        // do nothing.
    }

    @Override
    public void touchMenuItem(int itemId) {
        switch (itemId) {
            case R.id.action_open_portfolio:
                String url = userProfileView.getUserPortfolio();
                if (!TextUtils.isEmpty(url)) {
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(i);
                } else {
                    Toast.makeText(
                            this,
                            getString(R.string.feedback_portfolio_is_null),
                            Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.action_filter:
                int page = pagerManagePresenter.getPagerPosition();
                popupManagePresenter.showPopup(
                        this,
                        toolbar,
                        pagerManagePresenter.getPagerKey(page),
                        page);
                break;
        }
    }

    // pager manage view.

    @Override
    public PagerView getPagerView(int position) {
        return pagers[position];
    }

    @Override
    public boolean canPagerSwipeBack(int position, int dir) {
        return pagers[position].canSwipeBack(dir);
    }

    @Override
    public int getPagerItemCount(int position) {
        return pagers[position].getItemCount();
    }

    // popup manage view.

    @Override
    public void responsePopup(String value, int position) {
        pagers[position].setKey(value);
        pagers[position].refreshPager();
    }

    @Override
    public boolean checkCanSwipeBack(int dir) {
        if (pagerManagePresenter.getPagerItemCount() <= 0) {
            return true;
        }
        if (dir == SwipeBackLayout.UP_DIR) {
            return pagerManagePresenter.canPagerSwipeBack(dir)
                    && appBar.getY() <= -appBar.getMeasuredHeight() + utils.dpToPx(48);
        } else {
            return pagerManagePresenter.canPagerSwipeBack(dir)
                    && appBar.getY() >= 0;
        }
    }

    @Override
    public void swipeBackFinish() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
        } else {
            finish();
        }
    }
}
