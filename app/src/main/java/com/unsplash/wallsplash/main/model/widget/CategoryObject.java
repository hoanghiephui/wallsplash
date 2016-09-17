package com.unsplash.wallsplash.main.model.widget;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.unsplash.wallsplash.R;
import com.unsplash.wallsplash.WallSplashApplication;
import com.unsplash.wallsplash.common.data.api.PhotoApi;
import com.unsplash.wallsplash.common.data.data.Photo;
import com.unsplash.wallsplash.common.data.service.PhotoService;
import com.unsplash.wallsplash.common.i.model.CategoryModel;
import com.unsplash.wallsplash.common.ui.adapter.PhotoAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Category object.
 */

public class CategoryObject
        implements CategoryModel {
    // data
    private PhotoAdapter adapter;
    private PhotoService service;

    private int photosCategory;
    private String photosOrder;

    private int photosPage;
    private List<Integer> pageList;

    private boolean refreshing;
    private boolean loading;
    private boolean over;

    private final String RANDOM_TXT;

    /**
     * <br> life cycle.
     */

    public CategoryObject(Context c) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(c);

        this.adapter = new PhotoAdapter(c, new ArrayList<Photo>());
        this.service = PhotoService.getService();

        String defaultOrder = sharedPreferences.getString(
                c.getString(R.string.key_default_photo_order),
                PhotoApi.ORDER_BY_LATEST);
        RANDOM_TXT = c.getResources().getStringArray(R.array.photo_order_values)[3];
        this.photosCategory = WallSplashApplication.CATEGORY_BUILDINGS_ID;
        this.photosOrder = defaultOrder.equals(RANDOM_TXT) ? RANDOM_TXT : PhotoApi.ORDER_BY_LATEST;

        this.photosPage = 0;
        this.pageList = new ArrayList<>();

        this.refreshing = false;
        this.loading = false;
        this.over = false;
    }

    @Override
    public PhotoAdapter getAdapter() {
        return adapter;
    }

    @Override
    public PhotoService getService() {
        return service;
    }

    @Override
    public int getPhotosCategory() {
        return photosCategory;
    }

    @Override
    public void setPhotosCategory(int category) {
        photosCategory = category;
    }

    @Override
    public String getPhotosOrder() {
        return photosOrder;
    }

    @Override
    public void setPhotosOrder(String order) {
        photosOrder = order;
    }

    @Override
    public boolean isRandomType() {
        return photosOrder.equals(RANDOM_TXT);
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
    public List<Integer> getPageList() {
        return pageList;
    }

    @Override
    public void setPageList(List<Integer> list) {
        pageList = list;
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
