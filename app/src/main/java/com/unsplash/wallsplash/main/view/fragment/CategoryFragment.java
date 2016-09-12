package com.unsplash.wallsplash.main.view.fragment;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.unsplash.wallsplash.R;
import com.unsplash.wallsplash._common.i.model.CategoryManageModel;
import com.unsplash.wallsplash._common.i.presenter.PopupManagePresenter;
import com.unsplash.wallsplash._common.i.presenter.ToolbarPresenter;
import com.unsplash.wallsplash._common.i.view.PopupManageView;
import com.unsplash.wallsplash._common.i.view.ToolbarView;
import com.unsplash.wallsplash._common.ui.widget.StatusBarView;
import com.unsplash.wallsplash._common.utils.NotificationUtils;
import com.unsplash.wallsplash._common.utils.ThemeUtils;
import com.unsplash.wallsplash._common.utils.ValueUtils;
import com.unsplash.wallsplash.main.model.fragment.CategoryManageObject;
import com.unsplash.wallsplash.main.presenter.fragment.CategoryFragmentPopupManageImplementor;
import com.unsplash.wallsplash.main.presenter.fragment.ToolbarImplementor;
import com.unsplash.wallsplash.main.view.widget.CategoryPhotosView;

/**
 * Category fragment.
 */

public class CategoryFragment extends Fragment
        implements ToolbarView, PopupManageView,
        View.OnClickListener, Toolbar.OnMenuItemClickListener, NotificationUtils.SnackbarContainer {
    // model.
    private CategoryManageModel categoryManageModel;

    // view.
    private CoordinatorLayout container;
    private Toolbar toolbar;
    private CategoryPhotosView photosView;

    // presenter.
    private ToolbarPresenter toolbarPresenter;
    private PopupManagePresenter popupManagePresenter;
    //private CategoryManagePresenter categoryManagePresenter;

    /**
     * <br> life cycle.
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        initView(view);
        initPresenter();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        photosView.cancelRequest();
        photosView.onDestroy();
    }

    /**
     * <br> presenter.
     */

    private void initPresenter() {
        this.toolbarPresenter = new ToolbarImplementor(this);
        this.popupManagePresenter = new CategoryFragmentPopupManageImplementor(this);
        //this.categoryManagePresenter = new CategoryManageImplementor(categoryManageModel);
    }

    /**
     * <br> view.
     */

    // init.
    private void initView(View v) {
        StatusBarView statusBar = (StatusBarView) v.findViewById(R.id.fragment_category_statusBar);
        if (ThemeUtils.getInstance(getActivity()).isNeedSetStatusBarMask()) {
            statusBar.setMask(true);
        }

        this.container = (CoordinatorLayout) v.findViewById(R.id.fragment_category_container);

        this.toolbar = (Toolbar) v.findViewById(R.id.fragment_category_toolbar);
        toolbar.setTitle(ValueUtils.getToolbarTitleByCategory(getActivity(), categoryManageModel.getCategoryId()));
        if (ThemeUtils.getInstance(getActivity()).isLightTheme()) {
            toolbar.inflateMenu(R.menu.fragment_category_toolbar_light);
            toolbar.setNavigationIcon(R.drawable.ic_toolbar_menu_light);
        } else {
            toolbar.inflateMenu(R.menu.fragment_category_toolbar_dark);
            toolbar.setNavigationIcon(R.drawable.ic_toolbar_menu_dark);
        }
        toolbar.setOnMenuItemClickListener(this);
        toolbar.setNavigationOnClickListener(this);
        toolbar.setOnClickListener(this);

        this.photosView = (CategoryPhotosView) v.findViewById(R.id.fragment_category_categoryPhotosView);
        photosView.setActivity(getActivity());
        photosView.setCategory(categoryManageModel.getCategoryId());
        photosView.initRefresh();
    }

    // interface.

    public void pagerBackToTop() {
        photosView.pagerScrollToTop();
    }

    /**
     * <br> model.
     */

    // init.
    private void initModel(int categoryId) {
        this.categoryManageModel = new CategoryManageObject(categoryId);
    }

    // interface.

    public boolean needPagerBackToTop() {
        return photosView.needPagerBackToTop();
    }

    public Fragment setCategory(int category) {
        initModel(category);
        return this;
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
            case R.id.fragment_category_toolbar:
                toolbarPresenter.touchToolbar();
                break;
        }
    }

    // on menu item click listener.

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        toolbarPresenter.touchMenuItem(item.getItemId());
        return true;
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
        DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.activity_main_drawerLayout);
        drawer.openDrawer(GravityCompat.START);
    }

    @Override
    public void touchToolbar() {
        photosView.pagerScrollToTop();
    }

    @Override
    public void touchMenuItem(int itemId) {
        switch (itemId) {
            case R.id.action_filter:
                popupManagePresenter.showPopup(getActivity(), toolbar, photosView.getOrder(), 0);
                break;
        }
    }

    // popup manage view.

    @Override
    public void responsePopup(String value, int position) {
        photosView.setOrder(value);
        photosView.initRefresh();
    }
}