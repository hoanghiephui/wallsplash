package com.unsplash.wallsplash.common.i.model;

import com.unsplash.wallsplash.common.data.service.PhotoService;
import com.unsplash.wallsplash.common.ui.adapter.PhotoAdapter;

import java.util.List;

/**
 * Photos model.
 */

public interface PhotosModel {

    PhotoAdapter getAdapter();

    PhotoService getService();

    Object getRequestKey();

    void setRequestKey(Object key);

    int getPhotosType();

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
