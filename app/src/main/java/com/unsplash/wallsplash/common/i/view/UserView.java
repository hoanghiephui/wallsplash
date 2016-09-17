package com.unsplash.wallsplash.common.i.view;

import com.unsplash.wallsplash.common.data.data.User;

/**
 * User view.
 */

public interface UserView {

    void drawUserInfo(User user);

    void initRefreshStart();

    void requestDetailsSuccess();

    void requestDetailsFailed();
}
