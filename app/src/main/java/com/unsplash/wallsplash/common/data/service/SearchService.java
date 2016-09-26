package com.unsplash.wallsplash.common.data.service;

import com.google.gson.GsonBuilder;
import com.unsplash.wallsplash.BuildConfig;
import com.unsplash.wallsplash.WallSplashApplication;
import com.unsplash.wallsplash.common.data.api.SearchApi;
import com.unsplash.wallsplash.common.data.data.SearchCollectionsResult;
import com.unsplash.wallsplash.common.data.data.SearchPhotosResult;
import com.unsplash.wallsplash.common.data.data.SearchUsersResult;
import com.unsplash.wallsplash.common.data.tools.AuthInterceptor;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Hoang Hiep on 9/25/2016.
 */

public class SearchService {
    // widget
    private Call call;

    /**
     * <br> data.
     */

    public void searchPhotos(String query, int page, final OnRequestPhotosListener l) {
        Call<SearchPhotosResult> searchPhotos = buildApi(buildClient()).searchPhotos(query, page);
        searchPhotos.enqueue(new Callback<SearchPhotosResult>() {
            @Override
            public void onResponse(Call<SearchPhotosResult> call, retrofit2.Response<SearchPhotosResult> response) {
                if (l != null) {
                    l.onRequestPhotosSuccess(call, response);
                }
            }

            @Override
            public void onFailure(Call<SearchPhotosResult> call, Throwable t) {
                if (l != null) {
                    l.onRequestPhotosFailed(call, t);
                }
            }
        });
        call = searchPhotos;
    }

    public void searchUsers(String query, int page, final OnRequestUsersListener l) {
        Call<SearchUsersResult> searchUsers = buildApi(buildClient()).searchUsers(query, page);
        searchUsers.enqueue(new Callback<SearchUsersResult>() {
            @Override
            public void onResponse(Call<SearchUsersResult> call, retrofit2.Response<SearchUsersResult> response) {
                if (l != null) {
                    l.onRequestUsersSuccess(call, response);
                }
            }

            @Override
            public void onFailure(Call<SearchUsersResult> call, Throwable t) {
                if (l != null) {
                    l.onRequestUsersFailed(call, t);
                }
            }
        });
        call = searchUsers;
    }

    public void searchCollections(String query, int page, final OnRequestCollectionsListener l) {
        Call<SearchCollectionsResult> searchCollections = buildApi(buildClient()).searchCollections(query, page);
        searchCollections.enqueue(new Callback<SearchCollectionsResult>() {
            @Override
            public void onResponse(Call<SearchCollectionsResult> call, retrofit2.Response<SearchCollectionsResult> response) {
                if (l != null) {
                    l.onRequestCollectionsSuccess(call, response);
                }
            }

            @Override
            public void onFailure(Call<SearchCollectionsResult> call, Throwable t) {
                if (l != null) {
                    l.onRequestCollectionsFailed(call, t);
                }
            }
        });
        call = searchCollections;
    }

    public void cancel() {
        if (call != null) {
            call.cancel();
        }
    }

    /**
     * <br> build.
     */

    public static SearchService getService() {
        return new SearchService();
    }

    private OkHttpClient buildClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY
                : HttpLoggingInterceptor.Level.NONE);
        return new OkHttpClient.Builder()
                .addInterceptor(new AuthInterceptor())
                .addInterceptor(logging)
                .build();
    }

    private SearchApi buildApi(OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl(WallSplashApplication.UNSPLASH_API_BASE_URL)
                .client(client)
                .addConverterFactory(
                        GsonConverterFactory.create(
                                new GsonBuilder()
                                        .setDateFormat(WallSplashApplication.DATE_FORMAT)
                                        .create()))
                .build()
                .create((SearchApi.class));
    }

    /**
     * <br> interface.
     */

    public interface OnRequestPhotosListener {
        void onRequestPhotosSuccess(Call<SearchPhotosResult> call, retrofit2.Response<SearchPhotosResult> response);

        void onRequestPhotosFailed(Call<SearchPhotosResult> call, Throwable t);
    }

    public interface OnRequestCollectionsListener {
        void onRequestCollectionsSuccess(Call<SearchCollectionsResult> call, retrofit2.Response<SearchCollectionsResult> response);

        void onRequestCollectionsFailed(Call<SearchCollectionsResult> call, Throwable t);
    }

    public interface OnRequestUsersListener {
        void onRequestUsersSuccess(Call<SearchUsersResult> call, retrofit2.Response<SearchUsersResult> response);

        void onRequestUsersFailed(Call<SearchUsersResult> call, Throwable t);
    }
}
