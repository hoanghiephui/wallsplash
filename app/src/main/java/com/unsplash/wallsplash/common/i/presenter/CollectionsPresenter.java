package com.unsplash.wallsplash.common.i.presenter;

import android.app.Activity;
import android.content.Context;

/**
 * Collections presenter.
 */

public interface CollectionsPresenter {

    void requestCollections(Context c, int page, boolean refresh);

    void cancelRequest();

    void refreshNew(Context c, boolean notify);

    void loadMore(Context c, boolean notify);

    void initRefresh(Context c);

    boolean canLoadMore();

    boolean isRefreshing();

    boolean isLoading();

    Object getRequestKey();

    void setRequestKey(Object k);

    void setType(String key);

    void setActivityForAdapter(Activity a);

    int getAdapterItemCount();
}
