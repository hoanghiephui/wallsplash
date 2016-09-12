package com.unsplash.wallsplash.main.presenter.widget;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.Snackbar;

import com.unsplash.wallsplash.R;
import com.unsplash.wallsplash.WallSplashApplication;
import com.unsplash.wallsplash._common.data.api.PhotoApi;
import com.unsplash.wallsplash._common.data.data.Photo;
import com.unsplash.wallsplash._common.data.service.PhotoService;
import com.unsplash.wallsplash._common.i.model.PhotosModel;
import com.unsplash.wallsplash._common.i.presenter.PhotosPresenter;
import com.unsplash.wallsplash._common.i.view.PhotosView;
import com.unsplash.wallsplash._common.ui.dialog.RateLimitDialog;
import com.unsplash.wallsplash._common.utils.NotificationUtils;
import com.unsplash.wallsplash._common.utils.ValueUtils;
import com.unsplash.wallsplash.main.model.widget.PhotosObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Photos implementor.
 */

public class PhotosImplementor
        implements PhotosPresenter {
    // model & view.
    private PhotosModel model;
    private PhotosView view;

    /**
     * <br> life cycle.
     */

    public PhotosImplementor(PhotosModel model, PhotosView view) {
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
            switch (model.getPhotosType()) {
                case PhotosObject.PHOTOS_TYPE_NEW:
                    if (model.isRandomType()) {
                        requestNewPhotosRandom(c, page, refresh);
                    } else {
                        requestNewPhotosOrders(c, page, refresh);
                    }
                    break;

                case PhotosObject.PHOTOS_TYPE_FEATURED:
                    if (model.isRandomType()) {
                        requestFeaturePhotosRandom(c, page, refresh);
                    } else {
                        requestFeaturePhotosOrders(c, page, refresh);
                    }
                    break;
            }
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
    public void setOrder(String key) {
        model.setPhotosOrder(key);
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

    private void requestNewPhotosOrders(Context c, int page, boolean refresh) {
        page = refresh ? 1 : page + 1;
        model.getService()
                .requestPhotos(
                        page,
                        WallSplashApplication.DEFAULT_PER_PAGE,
                        model.getPhotosOrder(),
                        new OnRequestPhotosListener(c, page, WallSplashApplication.CATEGORY_TOTAL_NEW, refresh, false));
    }

    private void requestNewPhotosRandom(Context c, int page, boolean refresh) {
        if (refresh) {
            page = 0;
            model.setPageList(ValueUtils.getPageListByCategory(WallSplashApplication.CATEGORY_TOTAL_NEW));
        }
        model.getService()
                .requestPhotos(
                        model.getPageList().get(page),
                        WallSplashApplication.DEFAULT_PER_PAGE,
                        PhotoApi.ORDER_BY_LATEST,
                        new OnRequestPhotosListener(c, page, WallSplashApplication.CATEGORY_TOTAL_NEW, refresh, true));
    }

    private void requestFeaturePhotosOrders(Context c, int page, boolean refresh) {
        page = refresh ? 1 : page + 1;
        model.getService()
                .requestCuratePhotos(
                        page,
                        WallSplashApplication.DEFAULT_PER_PAGE,
                        model.getPhotosOrder(),
                        new OnRequestPhotosListener(c, page, WallSplashApplication.CATEGORY_TOTAL_FEATURED, refresh, false));
    }

    private void requestFeaturePhotosRandom(Context c, int page, boolean refresh) {
        if (refresh) {
            page = 0;
            model.setPageList(ValueUtils.getPageListByCategory(WallSplashApplication.CATEGORY_TOTAL_FEATURED));
        }
        model.getService()
                .requestCuratePhotos(
                        model.getPageList().get(page),
                        WallSplashApplication.DEFAULT_PER_PAGE,
                        PhotoApi.ORDER_BY_LATEST,
                        new OnRequestPhotosListener(c, page, WallSplashApplication.CATEGORY_TOTAL_FEATURED, refresh, true));
    }

    /**
     * <br> interface.
     */

    private class OnRequestPhotosListener implements PhotoService.OnRequestPhotosListener {
        // data
        private Context c;
        private int page;
        private int category;
        private boolean refresh;
        private boolean random;

        public OnRequestPhotosListener(Context c, int page, int category, boolean refresh, boolean random) {
            this.c = c;
            this.page = page;
            this.category = category;
            this.refresh = refresh;
            this.random = random;
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
                ValueUtils.writePhotoCount(c, response, category);
                if (random) {
                    model.setPhotosPage(page + 1);
                } else {
                    model.setPhotosPage(page);
                }
                for (int i = 0; i < response.body().size(); i++) {
                    model.getAdapter().insertItem(response.body().get(i));
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
                view.requestPhotosSuccess();
            } else {
                view.requestPhotosFailed(c.getString(R.string.feedback_load_nothing_tv));
                RateLimitDialog.checkAndNotify(
                        WallSplashApplication.getInstance().getActivityList().get(WallSplashApplication.getInstance().getActivityList().size()),
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
            NotificationUtils.showSnackbar(
                    c.getString(R.string.feedback_load_failed_toast) + " (" + t.getMessage() + ")",
                    Snackbar.LENGTH_SHORT);
            view.requestPhotosFailed(c.getString(R.string.feedback_load_failed_tv));
        }
    }
}
