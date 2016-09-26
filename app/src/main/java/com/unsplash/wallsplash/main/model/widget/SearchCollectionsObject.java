package com.unsplash.wallsplash.main.model.widget;

import android.app.Activity;
import android.content.Context;

import com.unsplash.wallsplash.common.data.data.Collection;
import com.unsplash.wallsplash.common.data.service.SearchService;
import com.unsplash.wallsplash.common.i.model.SearchModel;
import com.unsplash.wallsplash.common.ui.adapter.CollectionAdapter;

import java.util.ArrayList;

/**
 * Created by Hoang Hiep on 9/25/2016.
 */

public class SearchCollectionsObject implements SearchModel {
    // data
    private CollectionAdapter adapter;
    private SearchService service;

    private String searchQuery;

    private int photosPage;

    private boolean refreshing;
    private boolean loading;
    private boolean over;

    /**
     * <br> life cycle.
     */

    public SearchCollectionsObject(Context c) {
        this.adapter = new CollectionAdapter(c, new ArrayList<Collection>());
        this.service = SearchService.getService();

        this.searchQuery = "";

        this.photosPage = 0;

        this.refreshing = false;
        this.loading = false;
        this.over = false;
    }

    @Override
    public CollectionAdapter getAdapter() {
        return adapter;
    }

    @Override
    public SearchService getService() {
        return service;
    }

    @Override
    public void setActivity(Activity a) {
        adapter.setActivity(a);
    }

    @Override
    public String getSearchQuery() {
        return searchQuery;
    }

    @Override
    public void setSearchQuery(String query) {
        searchQuery = query;
    }

    @Override
    public int getPhotosPage() {
        return photosPage;
    }

    @Override
    public void setPhotosPage(int page) {
        photosPage = page;
    }

    @Override
    public boolean isRefreshing() {
        return refreshing;
    }

    @Override
    public void setRefreshing(boolean refreshing) {
        this.refreshing = refreshing;
    }

    @Override
    public boolean isLoading() {
        return loading;
    }

    @Override
    public void setLoading(boolean loading) {
        this.loading = loading;
    }

    @Override
    public boolean isOver() {
        return over;
    }

    @Override
    public void setOver(boolean over) {
        this.over = over;
    }
}
