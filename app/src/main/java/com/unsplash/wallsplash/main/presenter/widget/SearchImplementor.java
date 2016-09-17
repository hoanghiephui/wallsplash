package com.unsplash.wallsplash.main.presenter.widget;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.unsplash.wallsplash.R;
import com.unsplash.wallsplash.WallSplashApplication;
import com.unsplash.wallsplash.common.data.data.Photo;
import com.unsplash.wallsplash.common.data.service.PhotoService;
import com.unsplash.wallsplash.common.i.model.SearchModel;
import com.unsplash.wallsplash.common.i.presenter.SearchPresenter;
import com.unsplash.wallsplash.common.i.view.SearchView;
import com.unsplash.wallsplash.common.ui.dialog.RateLimitDialog;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Search implementor.
 */

public class SearchImplementor
        implements SearchPresenter {
    // model & view.
    private SearchModel model;
    private SearchView view;

    /**
     * <br> life cycle.
     */

    public SearchImplementor(SearchModel model, SearchView view) {
        this.model = model;
        this.view = view;
    }

    /**
     * <br> presenter.
     */

    @Override
    public void requestPhotos(Context c, int page, boolean refresh) {
        if (!model.isRefreshing() && !model.isLoading()) {
            if (refresh) {
                model.setRefreshing(true);
            } else {
                model.setLoading(true);
            }
            page = refresh ? 1 : page + 1;
            model.getService()
                    .searchPhotos(
                            model.getSearchQuery(),
                            model.getSearchOrientation(),
                            page,
                            WallSplashApplication.DEFAULT_PER_PAGE,
                            new OnRequestPhotosListener(c, page, refresh));
        }
    }

    @Override
    public void cancelRequest() {
        model.getService().cancel();
        model.getAdapter().cancelService();
    }

    @Override
    public void refreshNew(Context c, boolean notify) {
        if (notify) {
            view.setRefreshing(true);
        }
        requestPhotos(c, model.getPhotosPage(), true);
    }

    @Override
    public void loadMore(Context c, boolean notify) {
        if (notify) {
            view.setLoading(true);
        }
        requestPhotos(c, model.getPhotosPage(), false);
    }

    @Override
    public void initRefresh(Context c) {
        model.getService().cancel();
        model.setRefreshing(false);
        model.setLoading(false);
        refreshNew(c, false);
        view.setBackgroundOpacity();
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
    public void setQuery(String key) {
        model.setSearchQuery(key);
    }

    @Override
    public void setOrientation(String key) {
        model.setSearchOrientation(key);
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
     * >br> interface.
     */

    private class OnRequestPhotosListener implements PhotoService.OnRequestPhotosListener {
        // data
        private Context c;
        private int page;
        private boolean refresh;

        public OnRequestPhotosListener(Context c, int page, boolean refresh) {
            this.c = c;
            this.page = page;
            this.refresh = refresh;
        }

        @Override
        public void onRequestPhotosSuccess(Call<List<Photo>> call, Response<List<Photo>> response) {
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
                model.setPhotosPage(page);
                for (int i = 0; i < response.body().size(); i++) {
                    model.getAdapter().insertItem(response.body().get(i));
                }
                if (response.body().size() < WallSplashApplication.DEFAULT_PER_PAGE) {
                    model.setOver(true);
                    view.setPermitLoading(false);
                    if (response.body().size() == 0) {
                        Toast.makeText(
                                c,
                                c.getString(R.string.feedback_is_over) + "\n" + response.message(),
                                Toast.LENGTH_SHORT).show();
                    }
                }
                view.requestPhotosSuccess();
            } else {

                view.requestPhotosFailed(c.getString(R.string.feedback_search_failed_tv));
                RateLimitDialog.checkAndNotify(
                        WallSplashApplication.getInstance().getActivityList().get(WallSplashApplication.getInstance().getActivityList().size() - 1),
                        response.headers().get("X-Ratelimit-Remaining"));
            }
        }

        @Override
        public void onRequestPhotosFailed(Call<List<Photo>> call, Throwable t) {
            model.setRefreshing(false);
            model.setLoading(false);
            if (refresh) {
                view.setRefreshing(false);
            } else {
                view.setLoading(false);
            }
            Toast.makeText(
                    c,
                    c.getString(R.string.feedback_search_failed_toast) + "\n" + t.getMessage(),
                    Toast.LENGTH_SHORT).show();

            view.requestPhotosFailed(c.getString(R.string.feedback_load_failed_tv));
        }
    }
}
