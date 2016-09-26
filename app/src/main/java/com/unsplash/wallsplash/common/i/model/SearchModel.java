package com.unsplash.wallsplash.common.i.model;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;

import com.unsplash.wallsplash.common.data.service.SearchService;

/**
 * Search model.
 */

public interface SearchModel {

    RecyclerView.Adapter getAdapter();

    SearchService getService();

    void setActivity(Activity a);

    String getSearchQuery();

    void setSearchQuery(String query);


    int getPhotosPage();

    void setPhotosPage(int page);

    boolean isRefreshing();

    void setRefreshing(boolean refreshing);

    boolean isLoading();

    void setLoading(boolean loading);

    boolean isOver();

    void setOver(boolean over);
}
