package com.unsplash.wallsplash.user.model.widget;

import com.unsplash.wallsplash.WallSplashApplication;
import com.unsplash.wallsplash.common.data.data.User;
import com.unsplash.wallsplash.common.data.service.UserService;
import com.unsplash.wallsplash.common.i.model.UserModel;

/**
 * User object.
 */

public class UserObject
        implements UserModel {
    // data
    private UserService service;
    private User user;

    /**
     * <br> life cycle.
     */

    public UserObject() {
        service = UserService.getService();
        user = WallSplashApplication.getInstance().getUser();
    }

    /**
     * <br> model.
     */

    @Override
    public UserService getService() {
        return service;
    }

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public void setUser(User user) {
        this.user = user;
    }
}
