package com.unsplash.wallsplash.main.view.fragment;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.unsplash.wallsplash.R;
import com.unsplash.wallsplash.common.i.model.CategoryManageModel;
import com.unsplash.wallsplash.common.i.presenter.PopupManagePresenter;
import com.unsplash.wallsplash.common.i.presenter.ToolbarPresenter;
import com.unsplash.wallsplash.common.i.view.PopupManageView;
import com.unsplash.wallsplash.common.ui.widget.StatusBarView;
import com.unsplash.wallsplash.common.utils.NotificationUtils;
import com.unsplash.wallsplash.common.utils.ThemeUtils;
import com.unsplash.wallsplash.common.utils.ValueUtils;
import com.unsplash.wallsplash.main.model.fragment.CategoryManageObject;
import com.unsplash.wallsplash.main.presenter.fragment.CategoryFragmentPopupManageImplementor;
import com.unsplash.wallsplash.main.presenter.fragment.ToolbarImplementor;
import com.unsplash.wallsplash.main.view.widget.CategoryPhotosView;

/**
 * Category fragment.
 */

public class CategoryFragment extends Fragment
        implements PopupManageView,
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
        this.toolbarPresenter = new ToolbarImplementor();
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

    public void showPopup() {
        popupManagePresenter.showPopup(getActivity(), toolbar, photosView.getOrder(), 0);
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
                toolbarPresenter.touchNavigatorIcon(getActivity());
                break;
            case R.id.fragment_category_toolbar:
                toolbarPresenter.touchToolbar(getActivity());
                break;
        }
    }

    // on menu item click listener.

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return toolbarPresenter.touchMenuItem((AppCompatActivity) getActivity(), item.getItemId());
    }

    // snackbar container.

    @Override
    public View getSnackbarContainer() {
        return container;
    }

    // view.



    // popup manage view.

    @Override
    public void responsePopup(String value, int position) {
        photosView.setOrder(value);
        photosView.initRefresh();
    }
}