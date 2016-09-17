package com.unsplash.wallsplash.me.view.activity;

import android.annotation.SuppressLint;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.unsplash.wallsplash.R;
import com.unsplash.wallsplash.WallSplashApplication;
import com.unsplash.wallsplash.collection.view.activity.CollectionActivity;
import com.unsplash.wallsplash.common.data.data.Collection;
import com.unsplash.wallsplash.common.data.data.Me;
import com.unsplash.wallsplash.common.data.tools.AuthManager;
import com.unsplash.wallsplash.common.i.model.PagerManageModel;
import com.unsplash.wallsplash.common.i.presenter.PagerManagePresenter;
import com.unsplash.wallsplash.common.i.presenter.PopupManagePresenter;
import com.unsplash.wallsplash.common.i.presenter.SwipeBackManagePresenter;
import com.unsplash.wallsplash.common.i.presenter.ToolbarPresenter;
import com.unsplash.wallsplash.common.i.view.PagerManageView;
import com.unsplash.wallsplash.common.i.view.PagerView;
import com.unsplash.wallsplash.common.i.view.PopupManageView;
import com.unsplash.wallsplash.common.i.view.SwipeBackManageView;
import com.unsplash.wallsplash.common.i.view.ToolbarView;
import com.unsplash.wallsplash.common.ui.activity.BaseActivity;
import com.unsplash.wallsplash.common.ui.activity.UpdateMeActivity;
import com.unsplash.wallsplash.common.ui.adapter.MyPagerAdapter;
import com.unsplash.wallsplash.common.ui.widget.SwipeBackLayout;
import com.unsplash.wallsplash.common.utils.AnimUtils;
import com.unsplash.wallsplash.common.utils.BackToTopUtils;
import com.unsplash.wallsplash.common.utils.DisplayUtils;
import com.unsplash.wallsplash.common.utils.ThemeUtils;
import com.unsplash.wallsplash.me.model.activity.PagerManageObject;
import com.unsplash.wallsplash.me.model.widget.PhotosObject;
import com.unsplash.wallsplash.me.presenter.activity.PagerManageImplementor;
import com.unsplash.wallsplash.me.presenter.activity.PopupManageImplementor;
import com.unsplash.wallsplash.me.presenter.activity.SwipeBackManageImplementor;
import com.unsplash.wallsplash.me.presenter.activity.ToolbarImplementor;
import com.unsplash.wallsplash.me.view.widget.MeCollectionsView;
import com.unsplash.wallsplash.me.view.widget.MePhotosView;
import com.unsplash.wallsplash.me.view.widget.MeProfileView;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * Me activity.
 */

public class MeActivity extends BaseActivity
        implements ToolbarView, PagerManageView, PopupManageView, SwipeBackManageView,
        Toolbar.OnMenuItemClickListener, View.OnClickListener, ViewPager.OnPageChangeListener,
        /*SwipeBackLayout.OnSwipeListener,*/ AuthManager.OnAuthDataChangedListener {
    // model.
    private PagerManageModel pagerManageModel;
    public static final int COLLECTION_ACTIVITY = 1;

    // view.
    private CoordinatorLayout container;
    private AppBarLayout appBar;
    private Toolbar toolbar;
    private CircularImageView avatar;
    private TextView title;
    private MyPagerAdapter adapter;
    private MeProfileView meProfileView;
    private ImageView imgBg;

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
        setContentView(R.layout.activity_me);
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
        AuthManager.getInstance().removeOnWriteDataListener(this);
        AuthManager.getInstance().cancelRequest();
        for (PagerView p : pagers) {
            p.cancelRequest();
        }
    }

    @Override
    protected void setTheme() {
        if (ThemeUtils.getInstance(this).isLightTheme()) {
            setTheme(R.style.MysplashTheme_light_Translucent_Me);
        } else {
            setTheme(R.style.MysplashTheme_dark_Translucent_Me);
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
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                overridePendingTransition(0, R.anim.activity_slide_out_bottom);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case COLLECTION_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    if (data.getBooleanExtra(CollectionActivity.DELETE_COLLECTION, false)) {
                        meProfileView.cutCollection(adapter);
                        ((MeCollectionsView) pagers[2]).removeCollection(WallSplashApplication.getInstance().getCollection());
                    } else {
                        ((MeCollectionsView) pagers[2]).changeCollection(WallSplashApplication.getInstance().getCollection());
                    }
                }
                break;
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

    // init.
    private void initView() {
        /*SwipeBackLayout swipeBackLayout = (SwipeBackLayout) findViewById(R.id.activity_me_swipeBackLayout);
        swipeBackLayout.setOnSwipeListener(this);*/

       /* StatusBarView statusBar = (StatusBarView) findViewById(R.id.activity_me_statusBar);
        if (ThemeUtils.getInstance(this).isNeedSetStatusBarMask()) {
            statusBar.setMask(true);
        }*/

        this.container = (CoordinatorLayout) findViewById(R.id.activity_me_container);
        this.appBar = (AppBarLayout) findViewById(R.id.activity_me_appBar);

        this.toolbar = (Toolbar) findViewById(R.id.activity_me_toolbar);
        if (ThemeUtils.getInstance(this).isLightTheme()) {
            toolbar.inflateMenu(R.menu.activity_me_toolbar_light);
            toolbar.setNavigationIcon(R.drawable.ic_toolbar_back_light);
        } else {
            toolbar.inflateMenu(R.menu.activity_me_toolbar_dark);
            toolbar.setNavigationIcon(R.drawable.ic_toolbar_back_dark);
        }
        toolbar.setOnMenuItemClickListener(this);
        toolbar.setNavigationOnClickListener(this);

        this.avatar = (CircularImageView) findViewById(R.id.activity_me_avatar);
        this.title = (TextView) findViewById(R.id.activity_me_title);
        this.meProfileView = (MeProfileView) findViewById(R.id.activity_me_profileView);
        this.imgBg = (ImageView) findViewById(R.id.imgBg);

        initPages();
        this.utils = new DisplayUtils(this);

        drawProfile();
    }

    private void initPages() {
        List<View> pageList = new ArrayList<>();
        pageList.add(new MePhotosView(this, PhotosObject.PHOTOS_TYPE_PHOTOS));
        pageList.add(new MePhotosView(this, PhotosObject.PHOTOS_TYPE_LIKES));
        pageList.add(new MeCollectionsView(this));
        for (int i = 0; i < pageList.size(); i++) {
            pagers[i] = (PagerView) pageList.get(i);
        }

        List<String> tabList = new ArrayList<>();
        tabList.add("PHOTOS");
        tabList.add("LIKES");
        tabList.add("COLLECTIONS");
        this.adapter = new MyPagerAdapter(pageList, tabList);

        ViewPager viewPager = (ViewPager) findViewById(R.id.activity_me_viewPager);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(this);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.activity_me_tabLayout);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setupWithViewPager(viewPager);
    }

    @SuppressLint("SetTextI18n")
    private void drawProfile() {
        if (AuthManager.getInstance().getMe() != null) {
            Me me = AuthManager.getInstance().getMe();
            title.setText(me.first_name + " " + me.last_name);
            meProfileView.drawMeProfile(me, adapter);
        } else if (!TextUtils.isEmpty(AuthManager.getInstance().getUsername())) {
            title.setText(AuthManager.getInstance().getFirstName()
                    + " " + AuthManager.getInstance().getLastName());
        } else {
            title.setText("...");
        }

        if (AuthManager.getInstance().getUser() != null) {
            Glide.with(this)
                    .load(AuthManager.getInstance().getUser().profile_image.large)
                    .priority(Priority.HIGH)
                    .override(128, 128)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(avatar);
            //set blur bg avatar
            Glide.with(this).load(AuthManager.getInstance().getUser().profile_image.large)
                    .bitmapTransform(new BlurTransformation(this, 25))
                    .into(imgBg);

        } else if (!TextUtils.isEmpty(AuthManager.getInstance().getAvatarPath())) {
            Glide.with(this)
                    .load(AuthManager.getInstance().getAvatarPath())
                    .priority(Priority.HIGH)
                    .override(128, 128)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(avatar);

            //set blur bg avatar
            Glide.with(this).load(AuthManager.getInstance().getAvatarPath())
                    .bitmapTransform(new BlurTransformation(this, 25))
                    .into(imgBg);
        } else {
            Glide.with(this)
                    .load(R.drawable.ic_avatar)
                    .priority(Priority.HIGH)
                    .override(128, 128)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(avatar);
        }
    }

    // interface.

    public void addCollection(Collection c) {
        meProfileView.addCollection(adapter);
        ((MeCollectionsView) pagers[1]).addCollection(c);
    }

    public void changeCollection(Collection c) {
        ((MeCollectionsView) pagers[1]).changeCollection(c);
    }

    /**
     * <br> model.
     */

    private void initModel() {
        this.pagerManageModel = new PagerManageObject(0);
        AuthManager.getInstance().addOnWriteDataListener(this);
        if (AuthManager.getInstance().getState() == AuthManager.FREEDOM_STATE) {
            AuthManager.getInstance().refreshPersonalProfile();
        }
    }

    /**
     * <br> interface.
     */

    // on click listener.
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case -1:
                toolbarPresenter.touchNavigatorIcon();
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
        if (AuthManager.getInstance().getState() != AuthManager.LOADING_ME_STATE) {
            pagerManagePresenter.checkToRefresh(position);
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        // do nothing.
    }

    // on swipe listener.(swipe back listener)

    /*@Override
    public boolean canSwipeBack(int dir) {
        return swipeBackManagePresenter.checkCanSwipeBack(dir);
    }

    @Override
    public void onSwipeFinish() {
        swipeBackManagePresenter.swipeBackFinish();
    }*/

    // on author data changed listener.

    @Override
    public void onWriteAccessToken() {
        drawProfile();
    }

    @Override
    public void onWriteUserInfo() {
        drawProfile();
        pagerManagePresenter.checkToRefresh(pagerManagePresenter.getPagerPosition());
    }

    @Override
    public void onWriteAvatarPath() {
        drawProfile();
    }

    @Override
    public void onLogout() {
        // do nothing.
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
            case R.id.action_edit:
                if (AuthManager.getInstance().isAuthorized()
                        && AuthManager.getInstance().getMe() != null) {
                    Intent u = new Intent(this, UpdateMeActivity.class);
                    startActivity(u);
                    overridePendingTransition(R.anim.activity_in, 0);
                }
                break;

            case R.id.action_open_portfolio:
                if (AuthManager.getInstance().isAuthorized()
                        && AuthManager.getInstance().getMe() != null) {
                    String url = AuthManager.getInstance().getMe().portfolio_url;
                    if (!TextUtils.isEmpty(url)) {
                        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(i);
                    } else {
                        Toast.makeText(
                                this,
                                getString(R.string.feedback_portfolio_is_null),
                                Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            case R.id.action_filter:
                if (AuthManager.getInstance().isAuthorized()
                        && AuthManager.getInstance().getMe() != null) {
                    int page = pagerManagePresenter.getPagerPosition();
                    popupManagePresenter.showPopup(
                            this,
                            toolbar,
                            pagerManagePresenter.getPagerKey(page),
                            page);
                }
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
        return pagers[position].getItemCounts();
    }

    // popup manage view.

    @Override
    public void responsePopup(String value, int position) {
        pagers[position].setKey(value);
        if (AuthManager.getInstance().getState() != AuthManager.LOADING_ME_STATE) {
            pagers[position].refreshPager();
        }
    }

    // swipe back manage view.

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
    public void swipeBackFinish(int dir) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
        } else {
            finish();
            switch (dir) {
                case SwipeBackLayout.UP_DIR:
                    overridePendingTransition(0, R.anim.activity_slide_out_top);
                    break;

                case SwipeBackLayout.DOWN_DIR:
                    overridePendingTransition(0, R.anim.activity_slide_out_bottom);
                    break;
            }
        }
    }
}
