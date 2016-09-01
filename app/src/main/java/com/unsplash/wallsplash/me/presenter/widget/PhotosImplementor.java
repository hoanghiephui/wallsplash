package com.unsplash.wallsplash.me.presenter.widget;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.Snackbar;

import com.unsplash.wallsplash.R;
import com.unsplash.wallsplash.WallSplashApplication;
import com.unsplash.wallsplash._common.data.data.Photo;
import com.unsplash.wallsplash._common.data.service.PhotoService;
import com.unsplash.wallsplash._common.data.tools.AuthManager;
import com.unsplash.wallsplash._common.i.model.PhotosModel;
import com.unsplash.wallsplash._common.i.presenter.PhotosPresenter;
import com.unsplash.wallsplash._common.i.view.PhotosView;
import com.unsplash.wallsplash._common.utils.NotificationUtils;
import com.unsplash.wallsplash.user.model.widget.PhotosObject;

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
        if (!model.isRefreshing() && !model.isLoading()
                && AuthManager.getInstance().getMe() != null) {
            if (refresh) {
                model.setRefreshing(true);
            } else {
                model.setLoading(true);
            }
            switch (model.getPhotosType()) {
                case PhotosObject.PHOTOS_TYPE_PHOTOS:
                    requestUserPhotos(c, page, refresh, model.getPhotosOrder());
                    break;

                case PhotosObject.PHOTOS_TYPE_LIKES:
                    requestUserLikes(c, page, refresh, model.getPhotosOrder());
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

    private void requestUserPhotos(Context c, int page, boolean refresh, String order) {
        page = refresh ? 1 : page + 1;
        model.getService()
                .requestUserPhotos(
                        AuthManager.getInstance().getMe(),
                        page,
                        WallSplashApplication.DEFAULT_PER_PAGE,
                        order,
                        new OnRequestPhotosListener(c, page, refresh));
    }

    private void requestUserLikes(Context c, int page, boolean refresh, String order) {
        page = refresh ? 1 : page + 1;
        model.getService()
                .requestUserLikes(
                        AuthManager.getInstance().getMe(),
                        page,
                        WallSplashApplication.DEFAULT_PER_PAGE,
                        order,
                        new OnRequestPhotosListener(c, page, refresh));
    }

    /**
     * <br> interface.
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
            if (response.isSuccessful()) {
                model.setPhotosPage(page);
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
