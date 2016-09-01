package com.wangdaye.mysplash._common.data.service;

import com.google.gson.GsonBuilder;
import com.wangdaye.mysplash.BuildConfig;
import com.wangdaye.mysplash.Mysplash;
import com.wangdaye.mysplash._common.data.api.PhotoApi;
import com.wangdaye.mysplash._common.data.data.Collection;
import com.wangdaye.mysplash._common.data.data.LikePhotoResult;
import com.wangdaye.mysplash._common.data.data.Me;
import com.wangdaye.mysplash._common.data.data.Photo;
import com.wangdaye.mysplash._common.data.data.PhotoDetails;
import com.wangdaye.mysplash._common.data.data.PhotoStats;
import com.wangdaye.mysplash._common.data.data.User;
import com.wangdaye.mysplash._common.data.tools.AuthInterceptor;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Photo service.
 * */

public class PhotoService {
    // widget
    private Call call;

    /** <br> data. */

    public void requestPhotos(int page, int per_page, String order_by, final OnRequestPhotosListener l) {
        Call<List<Photo>> getPhotos = buildApi(buildClient()).getPhotos(page, per_page, order_by);
        getPhotos.enqueue(new Callback<List<Photo>>() {
            @Override
            public void onResponse(Call<List<Photo>> call, retrofit2.Response<List<Photo>> response) {
                if (l != null) {
                    l.onRequestPhotosSuccess(call, response);
                }
            }

            @Override
            public void onFailure(Call<List<Photo>> call, Throwable t) {
                if (l != null) {
                    l.onRequestPhotosFailed(call, t);
                }
            }
        });
        call = getPhotos;
    }

    public void requestCuratePhotos(int page, int per_page, String order_by, final OnRequestPhotosListener l) {
        Call<List<Photo>> getCuratePhotos = buildApi(buildClient()).getCuratedPhotos(page, per_page, order_by);
        getCuratePhotos.enqueue(new Callback<List<Photo>>() {
            @Override
            public void onResponse(Call<List<Photo>> call, retrofit2.Response<List<Photo>> response) {
                if (l != null) {
                    l.onRequestPhotosSuccess(call, response);
                }
            }

            @Override
            public void onFailure(Call<List<Photo>> call, Throwable t) {
                if (l != null) {
                    l.onRequestPhotosFailed(call, t);
                }
            }
        });
        call = getCuratePhotos;
    }

    public void searchPhotos(String query, String orientation, int page, int per_page, final OnRequestPhotosListener l) {
        Call<List<Photo>> searchPhotos = buildApi(buildClient()).searchPhotos(query, orientation, page, per_page);
        searchPhotos.enqueue(new Callback<List<Photo>>() {
            @Override
            public void onResponse(Call<List<Photo>> call, retrofit2.Response<List<Photo>> response) {
                if (l != null) {
                    l.onRequestPhotosSuccess(call, response);
                }
            }

            @Override
            public void onFailure(Call<List<Photo>> call, Throwable t) {
                if (l != null) {
                    l.onRequestPhotosFailed(call, t);
                }
            }
        });
        call = searchPhotos;
    }

    public void requestStats(String id, final OnRequestStatsListener l) {
        Call<PhotoStats> getStats = buildApi(buildClient()).getPhotoStats(id);
        getStats.enqueue(new Callback<PhotoStats>() {
            @Override
            public void onResponse(Call<PhotoStats> call, retrofit2.Response<PhotoStats> response) {
                if (l != null) {
                    l.onRequestStatsSuccess(call, response);
                }
            }

            @Override
            public void onFailure(Call<PhotoStats> call, Throwable t) {
                if (l != null) {
                    l.onRequestStatsFailed(call, t);
                }
            }
        });
        call = getStats;
    }

    public void requestPhotosInAGivenCategory(int id, int page, int per_page, final OnRequestPhotosListener l) {
        Call<List<Photo>> getPhotosInAGivenCategory = buildApi(buildClient()).getPhotosInAGivenCategory(id, page, per_page);
        getPhotosInAGivenCategory.enqueue(new Callback<List<Photo>>() {
            @Override
            public void onResponse(Call<List<Photo>> call, retrofit2.Response<List<Photo>> response) {
                if (l != null) {
                    l.onRequestPhotosSuccess(call, response);
                }
            }

            @Override
            public void onFailure(Call<List<Photo>> call, Throwable t) {
                if (l != null) {
                    l.onRequestPhotosFailed(call, t);
                }
            }
        });
        call = getPhotosInAGivenCategory;
    }

    public void setLikeForAPhoto(String id, boolean like, final OnSetLikeListener l) {
        Call<LikePhotoResult> setLikeForAPhoto = like ?
                buildApi(buildClient()).likeAPhoto(id) : buildApi(buildClient()).unlikeAPhoto(id);
        setLikeForAPhoto.enqueue(new Callback<LikePhotoResult>() {
            @Override
            public void onResponse(Call<LikePhotoResult> call, Response<LikePhotoResult> response) {
                if (l != null) {
                    l.onSetLikeSuccess(call, response);
                }
            }

            @Override
            public void onFailure(Call<LikePhotoResult> call, Throwable t) {
                if (l != null) {
                    l.onSetLikeFailed(call, t);
                }
            }
        });
    }

    public void requestPhotoDetails(Photo p, final OnRequestPhotoDetailsListener l) {
        Call<PhotoDetails> getAPhoto = buildApi(buildClient()).getAPhoto(p.id, p.width, p.height, "0,0," + p.width + "," + p.height);
        getAPhoto.enqueue(new Callback<PhotoDetails>() {
            @Override
            public void onResponse(Call<PhotoDetails> call, Response<PhotoDetails> response) {
                if (l != null) {
                    l.onRequestPhotoDetailsSuccess(call, response);
                }
            }

            @Override
            public void onFailure(Call<PhotoDetails> call, Throwable t) {
                if (l != null) {
                    l.onRequestPhotoDetailsFailed(call, t);
                }
            }
        });
    }

    public void requestUserPhotos(User u, int page, int per_page, String order_by, final OnRequestPhotosListener l) {
        Call<List<Photo>> getUserPhotos = buildApi(buildClient()).getUserPhotos(u.username, page, per_page, order_by);
        getUserPhotos.enqueue(new Callback<List<Photo>>() {
            @Override
            public void onResponse(Call<List<Photo>> call, retrofit2.Response<List<Photo>> response) {
                if (l != null) {
                    l.onRequestPhotosSuccess(call, response);
                }
            }

            @Override
            public void onFailure(Call<List<Photo>> call, Throwable t) {
                if (l != null) {
                    l.onRequestPhotosFailed(call, t);
                }
            }
        });
    }

    public void requestUserPhotos(Me me, int page, int per_page, String order_by, final OnRequestPhotosListener l) {
        Call<List<Photo>> getUserPhotos = buildApi(buildClient()).getUserPhotos(me.username, page, per_page, order_by);
        getUserPhotos.enqueue(new Callback<List<Photo>>() {
            @Override
            public void onResponse(Call<List<Photo>> call, retrofit2.Response<List<Photo>> response) {
                if (l != null) {
                    l.onRequestPhotosSuccess(call, response);
                }
            }

            @Override
            public void onFailure(Call<List<Photo>> call, Throwable t) {
                if (l != null) {
                    l.onRequestPhotosFailed(call, t);
                }
            }
        });
    }

    public void requestUserLikes(User u, int page, int per_page, String order_by, final OnRequestPhotosListener l) {
        Call<List<Photo>> getUserLikes = buildApi(buildClient()).getUserLikes(u.username, page, per_page, order_by);
        getUserLikes.enqueue(new Callback<List<Photo>>() {
            @Override
            public void onResponse(Call<List<Photo>> call, retrofit2.Response<List<Photo>> response) {
                if (l != null) {
                    l.onRequestPhotosSuccess(call, response);
                }
            }

            @Override
            public void onFailure(Call<List<Photo>> call, Throwable t) {
                if (l != null) {
                    l.onRequestPhotosFailed(call, t);
                }
            }
        });
    }

    public void requestUserLikes(Me me, int page, int per_page, String order_by, final OnRequestPhotosListener l) {
        Call<List<Photo>> getUserLikes = buildApi(buildClient()).getUserLikes(me.username, page, per_page, order_by);
        getUserLikes.enqueue(new Callback<List<Photo>>() {
            @Override
            public void onResponse(Call<List<Photo>> call, retrofit2.Response<List<Photo>> response) {
                if (l != null) {
                    l.onRequestPhotosSuccess(call, response);
                }
            }

            @Override
            public void onFailure(Call<List<Photo>> call, Throwable t) {
                if (l != null) {
                    l.onRequestPhotosFailed(call, t);
                }
            }
        });
    }

    public void requestCollectionPhotos(Collection c, int page, int per_page, final OnRequestPhotosListener l) {
        Call<List<Photo>> getCollectionPhotos = buildApi(buildClient()).getCollectionPhotos(c.id, page, per_page);
        getCollectionPhotos.enqueue(new Callback<List<Photo>>() {
            @Override
            public void onResponse(Call<List<Photo>> call, Response<List<Photo>> response) {
                if (l != null) {
                    l.onRequestPhotosSuccess(call, response);
                }
            }

            @Override
            public void onFailure(Call<List<Photo>> call, Throwable t) {
                if (l != null) {
                    l.onRequestPhotosFailed(call, t);
                }
            }
        });
    }

    public void requestCuratedCollectionPhotos(Collection c, int page, int per_page, final OnRequestPhotosListener l) {
        Call<List<Photo>> getCuratedCollectionPhotos = buildApi(buildClient()).getCuratedCollectionPhotos(c.id, page, per_page);
        getCuratedCollectionPhotos.enqueue(new Callback<List<Photo>>() {
            @Override
            public void onResponse(Call<List<Photo>> call, Response<List<Photo>> response) {
                if (l != null) {
                    l.onRequestPhotosSuccess(call, response);
                }
            }

            @Override
            public void onFailure(Call<List<Photo>> call, Throwable t) {
                if (l != null) {
                    l.onRequestPhotosFailed(call, t);
                }
            }
        });
    }

    public void cancel() {
        if (call != null) {
            call.cancel();
        }
    }

    /** <br> build. */

    public static PhotoService getService() {
        return new PhotoService();
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

    private PhotoApi buildApi(OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl(Mysplash.UNSPLASH_API_BASE_URL)
                .client(client)
                .addConverterFactory(
                        GsonConverterFactory.create(
                                new GsonBuilder()
                                        .setDateFormat(Mysplash.DATE_FORMAT)
                                        .create()))
                .build()
                .create((PhotoApi.class));
    }

    /** <br> interface. */

    public interface OnRequestPhotosListener {
        void onRequestPhotosSuccess(Call<List<Photo>> call, retrofit2.Response<List<Photo>> response);
        void onRequestPhotosFailed(Call<List<Photo>> call, Throwable t);
    }

    public interface OnRequestStatsListener {
        void onRequestStatsSuccess(Call<PhotoStats> call, retrofit2.Response<PhotoStats> response);
        void onRequestStatsFailed(Call<PhotoStats> call, Throwable t);
    }

    public interface OnSetLikeListener {
        void onSetLikeSuccess(Call<LikePhotoResult> call, retrofit2.Response<LikePhotoResult> response);
        void onSetLikeFailed(Call<LikePhotoResult> call, Throwable t);
    }

    public interface OnRequestPhotoDetailsListener {
        void onRequestPhotoDetailsSuccess(Call<PhotoDetails> call, retrofit2.Response<PhotoDetails> response);
        void onRequestPhotoDetailsFailed(Call<PhotoDetails> call, Throwable t);
    }
}
