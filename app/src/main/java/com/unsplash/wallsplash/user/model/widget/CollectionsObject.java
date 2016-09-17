package com.unsplash.wallsplash.user.model.widget;

import android.app.Activity;

import com.unsplash.wallsplash.WallSplashApplication;
import com.unsplash.wallsplash.common.data.data.Collection;
import com.unsplash.wallsplash.common.data.data.User;
import com.unsplash.wallsplash.common.data.service.CollectionService;
import com.unsplash.wallsplash.common.i.model.CollectionsModel;
import com.unsplash.wallsplash.common.ui.adapter.CollectionAdapter;

import java.util.ArrayList;

/**
 * Collections object.
 */

public class CollectionsObject
        implements CollectionsModel {
    // data
    private CollectionAdapter adapter;
    private CollectionService service;

    private User requestKey;
    private int collectionsPage;

    private boolean refreshing;
    private boolean loading;
    private boolean over;

    /**
     * <br> life cycle.
     */

    public CollectionsObject(Activity a) {
        this.adapter = new CollectionAdapter(a, new ArrayList<Collection>());
        this.service = CollectionService.getService();

        this.requestKey = WallSplashApplication.getInstance().getUser();
        this.collectionsPage = 0;

        this.refreshing = false;
        this.loading = false;
        this.over = false;
    }

    /**
     * <br> model.
     */

    @Override
    public CollectionAdapter getAdapter() {
        return adapter;
    }

    @Override
    public CollectionService getService() {
        return service;
    }

    @Override
    public Object getRequestKey() {
        return requestKey;
    }

    @Override
    public void setRequestKey(Object key) {
        requestKey = (User) key;
    }

    @Override
    public String getCollectionsType() {
        return null;
    }

    @Override
    public void setCollectionsType(String order) {
        // do nothing.
    }

    @Override
    public int getCollectionsPage() {
        return collectionsPage;
    }

    @Override
    public void setCollectionsPage(int page) {
        collectionsPage = page;
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

