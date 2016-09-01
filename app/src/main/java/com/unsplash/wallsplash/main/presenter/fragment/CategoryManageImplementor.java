package com.unsplash.wallsplash.main.presenter.fragment;

import com.unsplash.wallsplash._common.i.model.CategoryManageModel;
import com.unsplash.wallsplash._common.i.presenter.CategoryManagePresenter;

/**
 * Category manage implementor.
 */

public class CategoryManageImplementor
        implements CategoryManagePresenter {
    // model & view.
    private CategoryManageModel model;

    /**
     * <br> life cycle.
     */

    public CategoryManageImplementor(CategoryManageModel model) {
        this.model = model;
    }

    /**
     * <br> presenter.
     */

    @Override
    public int getCategoryId() {
        return model.getCategoryId();
    }

    @Override
    public void setCategoryId(int id) {
        model.setCategoryId(id);
    }
}
