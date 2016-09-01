package com.unsplash.wallsplash.main.model.fragment;

import com.unsplash.wallsplash._common.i.model.CategoryManageModel;

/**
 * Category manage object.
 */

public class CategoryManageObject
        implements CategoryManageModel {
    // data
    private int categoryId;

    /**
     * <br> life cycle.
     */

    public CategoryManageObject(int categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * <br> model.
     */

    @Override
    public int getCategoryId() {
        return categoryId;
    }

    @Override
    public void setCategoryId(int id) {
        categoryId = id;
    }
}
