package com.unsplash.wallsplash._common.i.model;

import android.app.Activity;

import com.unsplash.wallsplash._common.data.service.PhotoService;
import com.unsplash.wallsplash._common.ui.adapter.PhotoAdapter;

/**
 * Search model.
 */

public interface SearchModel {

    PhotoAdapter getAdapter();

    PhotoService getService();

    void setActivity(Activity a);

    String getSearchQuery();

    void setSearchQuery(String query);

    String getSearchOrientation();

    void setSearchOrientation(String orientation);

    int getPhotosPage();

    void setPhotosPage(int page);

    boolean isRefreshing();

    void setRefreshing(boolean refreshing);

    boolean isLoading();

    void setLoading(boolean loading);

    boolean isOver();

    void setOver(boolean over);
}
