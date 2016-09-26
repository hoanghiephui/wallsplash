package com.unsplash.wallsplash.common.i.presenter;

import android.app.Activity;
import android.content.Context;

import com.unsplash.wallsplash.common.ui.adapter.PhotoAdapter;

/**
 * Created by Hoang Hiep on 9/25/2016.
 */

public interface MultiFilterPresenter {
    void requestPhotos(Context c, boolean refresh);

    void cancelRequest();

    void refreshNew(Context context, boolean notify);

    void loadMore(Context context, boolean notify);

    void initRefresh(Context context);

    boolean canLoadMore();

    boolean isRefreshing();

    boolean isLoading();

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

    int getAdapterItemCount();

    void setActivityForAdapter(Activity activity);

    PhotoAdapter getAdapter();
}
