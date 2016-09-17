package com.unsplash.wallsplash.collection.view.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.unsplash.wallsplash.R;
import com.unsplash.wallsplash.WallSplashApplication;
import com.unsplash.wallsplash.collection.model.activity.EditResultObject;
import com.unsplash.wallsplash.collection.presenter.activity.EditResultImplementor;
import com.unsplash.wallsplash.collection.presenter.activity.SwipeBackManageImplementor;
import com.unsplash.wallsplash.collection.presenter.activity.ToolbarImplementor;
import com.unsplash.wallsplash.collection.view.widget.CollectionPhotosView;
import com.unsplash.wallsplash.common.data.data.Collection;
import com.unsplash.wallsplash.common.data.data.User;
import com.unsplash.wallsplash.common.i.model.EditResultModel;
import com.unsplash.wallsplash.common.i.presenter.EditResultPresenter;
import com.unsplash.wallsplash.common.i.presenter.SwipeBackManagePresenter;
import com.unsplash.wallsplash.common.i.presenter.ToolbarPresenter;
import com.unsplash.wallsplash.common.i.view.EditResultView;
import com.unsplash.wallsplash.common.i.view.SwipeBackManageView;
import com.unsplash.wallsplash.common.i.view.ToolbarView;
import com.unsplash.wallsplash.common.ui.activity.BaseActivity;
import com.unsplash.wallsplash.common.ui.dialog.UpdateCollectionDialog;
import com.unsplash.wallsplash.common.ui.widget.StatusBarView;
import com.unsplash.wallsplash.common.ui.widget.SwipeBackLayout;
import com.unsplash.wallsplash.common.utils.AnimUtils;
import com.unsplash.wallsplash.common.utils.BackToTopUtils;
import com.unsplash.wallsplash.common.utils.ThemeUtils;
import com.unsplash.wallsplash.common.utils.TypefaceUtils;
import com.unsplash.wallsplash.user.view.activity.UserActivity;

/**
 * Collection activity.
 */

public class CollectionActivity extends BaseActivity
        implements ToolbarView, SwipeBackManageView, EditResultView,
        View.OnClickListener, Toolbar.OnMenuItemClickListener, SwipeBackLayout.OnSwipeListener,
        UpdateCollectionDialog.OnCollectionChangedListener {
    // model.
    private EditResultModel editResultModel;
    public static final String DELETE_COLLECTION = "delete_collection";

    // view.
    private CoordinatorLayout container;
    private AppBarLayout appBar;
    private RelativeLayout creatorBar;
    private CircularImageView avatarImage;
    private CollectionPhotosView photosView;

    // presenter.
    private ToolbarPresenter toolbarPresenter;
    private SwipeBackManagePresenter swipeBackManagePresenter;
    private EditResultPresenter editResultPresenter;

    /**
     * <br> life cycle.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        initModel();
        initPresenter();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!isStarted()) {
            setStarted();
            initView();
            AnimUtils.animInitShow(photosView, 400);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        photosView.cancelRequest();
        photosView.onDestroy();
    }

    @Override
    protected void setTheme() {
        if (ThemeUtils.getInstance(this).isLightTheme()) {
            setTheme(R.style.MysplashTheme_light_Translucent);
        } else {
            setTheme(R.style.MysplashTheme_dark_Translucent);
        }
    }

    @Override
    public void onBackPressed() {
        if (WallSplashApplication.getInstance().isActivityInBackstage()) {
            super.onBackPressed();
        } else if (photosView.needPagerBackToTop() && BackToTopUtils.getInstance(this).isSetBackToTop(false)) {
            photosView.pagerBackToTop();
        } else {
            Intent result = new Intent();
            result.putExtra(DELETE_COLLECTION, false);
            setResult(RESULT_OK, result);
            super.onBackPressed();
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                overridePendingTransition(0, R.anim.activity_slide_out_bottom);
            }
        }
    }

    private void finishActivity(int dir, boolean delete) {
        Intent result = new Intent();
        result.putExtra(DELETE_COLLECTION, delete);
        setResult(RESULT_OK, result);
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

    /**
     * <br> presenter.
     */

    private void initPresenter() {
        this.toolbarPresenter = new ToolbarImplementor(this);
        this.swipeBackManagePresenter = new SwipeBackManageImplementor(this);
        this.editResultPresenter = new EditResultImplementor(editResultModel, this);
    }

    /**
     * <br> view.
     */

    @SuppressLint("SetTextI18n")
    private void initView() {
        Collection c = (Collection) editResultModel.getEditKey();

        SwipeBackLayout swipeBackLayout = (SwipeBackLayout) findViewById(R.id.activity_collection_swipeBackLayout);
        swipeBackLayout.setOnSwipeListener(this);

        StatusBarView statusBar = (StatusBarView) findViewById(R.id.activity_collection_statusBar);
        if (ThemeUtils.getInstance(this).isNeedSetStatusBarMask()) {
            statusBar.setMask(true);
        }

        this.container = (CoordinatorLayout) findViewById(R.id.activity_collection_container);
        this.appBar = (AppBarLayout) findViewById(R.id.activity_collection_appBar);

        TextView title = (TextView) findViewById(R.id.activity_collection_title);
        title.setText(c.title);

        TextView description = (TextView) findViewById(R.id.activity_collection_description);
        if (TextUtils.isEmpty(c.description)) {
            description.setVisibility(View.GONE);
        } else {
            TypefaceUtils.setTypeface(this, description);
            description.setText(c.description);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_collection_toolbar);
        if (ThemeUtils.getInstance(this).isLightTheme()) {
            toolbar.setNavigationIcon(R.drawable.ic_toolbar_back_light);
            if (WallSplashApplication.getInstance().isMyOwnCollection()) {
                toolbar.inflateMenu(R.menu.activity_collection_toolbar_light);
                toolbar.setOnMenuItemClickListener(this);
            }
        } else {
            toolbar.setNavigationIcon(R.drawable.ic_toolbar_back_dark);
            if (WallSplashApplication.getInstance().isMyOwnCollection()) {
                toolbar.inflateMenu(R.menu.activity_collection_toolbar_dark);
                toolbar.setOnMenuItemClickListener(this);
            }
        }
        toolbar.setNavigationOnClickListener(this);

        this.creatorBar = (RelativeLayout) findViewById(R.id.activity_collection_creatorBar);
        creatorBar.setOnClickListener(this);
        this.avatarImage = (CircularImageView) findViewById(R.id.activity_collection_avatar);
        avatarImage.setOnClickListener(this);
        Glide.with(this)
                .load(c.user.profile_image.large)
                .priority(Priority.HIGH)
                .override(128, 128)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(avatarImage);

        TextView subtitle = (TextView) findViewById(R.id.activity_collection_subtitle);
        TypefaceUtils.setTypeface(this, subtitle);
        subtitle.setText("By " + c.user.name);

        this.photosView = (CollectionPhotosView) findViewById(R.id.activity_collection_photosView);
        photosView.initMP(this, (Collection) editResultPresenter.getEditKey());
        photosView.initRefresh();
    }

    /**
     * <br> model.
     */

    private void initModel() {
        this.editResultModel = new EditResultObject();
    }

    public Collection getCollection() {
        return (Collection) editResultPresenter.getEditKey();
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

            case R.id.activity_collection_creatorBar:
                toolbarPresenter.touchToolbar();
                break;

            case R.id.activity_collection_avatar:
                toolbarPresenter.touchToolbar();
                User u = User.buildUser((Collection) editResultModel.getEditKey());
                WallSplashApplication.getInstance().setUser(u);

                Intent intent = new Intent(this, UserActivity.class);
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    startActivity(intent);
                    overridePendingTransition(R.anim.activity_in, 0);
                } else {
                    View v = avatarImage;
                    ActivityOptionsCompat options = ActivityOptionsCompat
                            .makeSceneTransitionAnimation(
                                    this,
                                    Pair.create(v, getString(R.string.transition_user_avatar)));
                    ActivityCompat.startActivity(this, intent, options.toBundle());
                }
                break;
        }
    }

    // on menu item click listener.

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        toolbarPresenter.touchMenuItem(item.getItemId());
        return false;
    }

    // on swipe listener.

    @Override
    public boolean canSwipeBack(int dir) {
        return swipeBackManagePresenter.checkCanSwipeBack(dir);
    }

    @Override
    public void onSwipeFinish(int dir) {
        swipeBackManagePresenter.swipeBackFinish(dir);
    }

    // on collection changed listener.

    @Override
    public void onEditCollection(Collection c) {
        editResultPresenter.updateSomething(c);
    }

    @Override
    public void onDeleteCollection(Collection c) {
        editResultPresenter.deleteSomething(c);
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
        finishActivity(SwipeBackLayout.NULL_DIR, false);
    }

    @Override
    public void touchToolbar() {
        photosView.pagerBackToTop();
    }

    @Override
    public void touchMenuItem(int itemId) {
        switch (itemId) {
            case R.id.action_edit:
                UpdateCollectionDialog dialog = new UpdateCollectionDialog();
                dialog.setCollection((Collection) editResultPresenter.getEditKey());
                dialog.setOnCollectionChangedListener(this);
                dialog.show(getSupportFragmentManager(), null);
                break;
        }
    }

    // swipe back manage view.

    @Override
    public boolean checkCanSwipeBack(int dir) {
        if (dir == SwipeBackLayout.UP_DIR) {
            return photosView.canSwipeBack(dir)
                    && appBar.getY() <= -appBar.getMeasuredHeight() + creatorBar.getMeasuredHeight();
        } else {
            return photosView.canSwipeBack(dir)
                    && appBar.getY() >= 0;
        }
    }

    @Override
    public void swipeBackFinish(int dir) {
        finishActivity(dir, false);
    }

    // edit result view.

    @Override
    public void drawCreateResult(Object newKey) {
        // do nothing.
    }

    @Override
    public void drawUpdateResult(Object newKey) {
        Collection collection = (Collection) newKey;

        TextView title = (TextView) findViewById(R.id.activity_collection_title);
        title.setText(collection.title);

        TextView description = (TextView) findViewById(R.id.activity_collection_description);
        if (TextUtils.isEmpty(collection.description) || collection.description.equals("null")) {
            description.setVisibility(View.GONE);
        } else {
            TypefaceUtils.setTypeface(this, description);
            description.setText(collection.description);
        }
    }

    @Override
    public void drawDeleteResult(Object oldKey) {
        finishActivity(SwipeBackLayout.NULL_DIR, true);
    }
}
