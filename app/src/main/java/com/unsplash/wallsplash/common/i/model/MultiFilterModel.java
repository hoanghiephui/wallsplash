package com.unsplash.wallsplash.common.i.model;

import com.unsplash.wallsplash.common.data.service.PhotoService;
import com.unsplash.wallsplash.common.ui.adapter.PhotoAdapter;

/**
 * Created by Hoang Hiep on 9/25/2016.
 */

public interface MultiFilterModel {
    PhotoAdapter getAdapter();

    PhotoService getService();

    void setQuery(String query);

    String getQuery();

    void setUsername(String username);

    String getUsername();

    void setCategory(int c);

    int getCategory();

    void setOrientation(String o);

    String getOrientation();

    void setFeatured(boolean f);

    boolean isFeatured();

    boolean isRefreshing();

    void setRefreshing(boolean refreshing);

    boolean isLoading();

    void setLoading(boolean loading);

    boolean isOver();

    void setOver(boolean over);
}
