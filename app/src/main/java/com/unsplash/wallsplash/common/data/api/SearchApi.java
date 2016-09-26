package com.unsplash.wallsplash.common.data.api;

import com.unsplash.wallsplash.common.data.data.SearchCollectionsResult;
import com.unsplash.wallsplash.common.data.data.SearchPhotosResult;
import com.unsplash.wallsplash.common.data.data.SearchUsersResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Hoang Hiep on 9/25/2016.
 */

public interface SearchApi {
    @GET("search/photos")
    Call<SearchPhotosResult> searchPhotos(@Query("query") String query,
                                          @Query("page") int page);

    @GET("search/users")
    Call<SearchUsersResult> searchUsers(@Query("query") String query,
                                        @Query("page") int page);

    @GET("search/collections")
    Call<SearchCollectionsResult> searchCollections(@Query("query") String query,
                                                    @Query("page") int page);
}
