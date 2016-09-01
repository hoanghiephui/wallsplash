package com.unsplash.wallsplash.main.presenter.widget;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.Snackbar;

import com.unsplash.wallsplash.R;
import com.unsplash.wallsplash.WallSplashApplication;
import com.unsplash.wallsplash._common.data.data.Collection;
import com.unsplash.wallsplash._common.data.service.CollectionService;
import com.unsplash.wallsplash._common.i.model.CollectionsModel;
import com.unsplash.wallsplash._common.i.presenter.CollectionsPresenter;
import com.unsplash.wallsplash._common.i.view.CollectionsView;
import com.unsplash.wallsplash._common.utils.NotificationUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Collections implementor.
 */

public class CollectionsImplementor
        implements CollectionsPresenter {
    // model & view.
    private CollectionsModel model;
    private CollectionsView view;

    /**
     * <br> life cycle.
     */

    public CollectionsImplementor(CollectionsModel model, CollectionsView view) {
        this.model = model;
        this.view = view;
    }

    /**
     * <br> presenter.
     */

    @Override
    public void requestCollections(Context c, int page, boolean refresh) {
        if (!model.isRefreshing() && !model.isLoading()) {
            if (refresh) {
                model.setRefreshing(true);
            } else {
                model.setLoading(true);
            }
            final String[] types = c
                    .getResources()
                    .getStringArray(R.array.collection_type_values);
            if (model.getCollectionsType().equals(types[0])) {
                requestAllCollections(c, page, refresh);
            } else if (model.getCollectionsType().equals(types[1])) {
                requestCuratedCollections(c, page, refresh);
            } else {
                requestFeaturedCollections(c, page, refresh);
            }
        }
    }

    @Override
    public void cancelRequest() {
        model.getService().cancel();
    }

    @Override
    public void refreshNew(Context c, boolean notify) {
        if (notify) {
            view.setRefreshing(true);
        }
        requestCollections(c, model.getCollectionsPage(), true);
    }

    @Override
    public void loadMore(Context c, boolean notify) {
        if (notify) {
            view.setLoading(true);
        }
        requestCollections(c, model.getCollectionsPage(), false);
    }

    @Override
    public void initRefresh(Context c) {
        model.getService().cancel();
        model.setRefreshing(false);
        model.setLoading(false);
        refreshNew(c, false);
        view.initRefreshStart();
    }

    @Override
    public boolean canLoadMore() {
        return !model.isRefreshing() && !model.isLoading() && !model.isOver();
    }

    @Override
    public boolean isRefreshing() {
        return model.isRefreshing();
    }

    @Override
    public boolean isLoading() {
        return model.isLoading();
    }

    @Override
    public Object getRequestKey() {
        return null;
    }

    @Override
    public void setRequestKey(Object k) {
        // do nothing.
    }

    @Override
    public void setType(String key) {
        model.setCollectionsType(key);
    }

    @Override
    public void setActivityForAdapter(Activity a) {
        model.getAdapter().setActivity(a);
    }

    @Override
    public int getAdapterItemCount() {
        return model.getAdapter().getRealItemCount();
    }

    /**
     * <br> utils.
     */

    private void requestAllCollections(Context c, int page, boolean refresh) {
        page = refresh ? 1 : page + 1;
        model.getService()
                .requestAllCollections(
                        page,
                        WallSplashApplication.DEFAULT_PER_PAGE,
                        new OnRequestCollectionsListener(c, page, refresh));
    }

    private void requestCuratedCollections(Context c, int page, boolean refresh) {
        page = refresh ? 1 : page + 1;
        model.getService()
                .requestCuratedCollections(
                        page,
                        WallSplashApplication.DEFAULT_PER_PAGE,
                        new OnRequestCollectionsListener(c, page, refresh));
    }

    private void requestFeaturedCollections(Context c, int page, boolean refresh) {
        page = refresh ? 1 : page + 1;
        model.getService()
                .requestFeaturedCollections(
                        page,
                        WallSplashApplication.DEFAULT_PER_PAGE,
                        new OnRequestCollectionsListener(c, page, refresh));
    }

    /**
     * <br> interface.
     */

    private class OnRequestCollectionsListener implements CollectionService.OnRequestCollectionsListener {
        // data
        private Context c;
        private int page;
        private boolean refresh;

        public OnRequestCollectionsListener(Context c, int page, boolean refresh) {
            this.c = c;
            this.page = page;
            this.refresh = refresh;
        }

        @Override
        public void onRequestCollectionsSuccess(Call<List<Collection>> call, Response<List<Collection>> response) {
            model.setRefreshing(false);
            model.setLoading(false);
            if (refresh) {
                model.getAdapter().clearItem();
                model.setOver(false);
                view.setRefreshing(false);
                view.setPermitLoading(true);
            } else {
                view.setLoading(false);
            }
            if (response.isSuccessful()
                    && model.getAdapter().getRealItemCount() + response.body().size() > 0) {
                model.setCollectionsPage(page);
                for (int i = 0; i < response.body().size(); i++) {
                    model.getAdapter().insertItem(response.body().get(i), model.getAdapter().getRealItemCount());
                }
                if (response.body().size() < WallSplashApplication.DEFAULT_PER_PAGE) {
                    model.setOver(true);
                    view.setPermitLoading(false);
                    if (response.body().size() == 0) {
                        NotificationUtils.showSnackbar(
                                c.getString(R.string.feedback_is_over),
                                Snackbar.LENGTH_SHORT);
                    }
                }
                view.requestCollectionsSuccess();
            } else {
                view.requestCollectionsFailed(c.getString(R.string.feedback_load_nothing_tv));
            }
        }

        @Override
        public void onRequestCollectionsFailed(Call<List<Collection>> call, Throwable t) {
            model.setRefreshing(false);
            model.setLoading(false);
            if (refresh) {
                view.setRefreshing(false);
            } else {
                view.setLoading(false);
            }
            NotificationUtils.showSnackbar(
                    c.getString(R.string.feedback_load_failed_toast) + " (" + t.getMessage() + ")",
                    Snackbar.LENGTH_SHORT);
            view.requestCollectionsFailed(c.getString(R.string.feedback_load_failed_tv));
        }
    }
}
