package com.unsplash.wallsplash.common.i.model;

import com.unsplash.wallsplash.common.data.service.PhotoService;
import com.unsplash.wallsplash.common.ui.adapter.PhotoAdapter;

import java.util.List;

/**
 * Category model.
 */

public interface CategoryModel {

    PhotoAdapter getAdapter();

    PhotoService getService();

    int getPhotosCategory();

    void setPhotosCategory(int category);

    String getPhotosOrder();

    void setPhotosOrder(String order);

    boolean isRandomType();

    int getPhotosPage();

    void setPhotosPage(int page);

    List<Integer> getPageList();

    void setPageList(List<Integer> list);

    boolean isRefreshing();

    void setRefreshing(boolean refreshing);

    boolean isLoading();

    void setLoading(boolean loading);

    boolean isOver();

    void setOver(boolean over);
}
