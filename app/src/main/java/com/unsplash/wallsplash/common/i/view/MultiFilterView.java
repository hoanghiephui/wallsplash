package com.unsplash.wallsplash.common.i.view;

/**
 * Created by Hoang Hiep on 9/25/2016.
 */

public interface MultiFilterView {
    void setRefreshing(boolean refreshing);

    void setLoading(boolean loading);

    void setPermitRefreshing(boolean permit);

    void setPermitLoading(boolean permit);

    void initRefreshStart();

    void requestPhotosSuccess();

    void requestPhotosFailed(String feedback);
}
