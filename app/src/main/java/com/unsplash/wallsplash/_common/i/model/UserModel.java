package com.unsplash.wallsplash._common.i.model;

import com.unsplash.wallsplash._common.data.data.User;
import com.unsplash.wallsplash._common.data.service.UserService;

/**
 * User model.
 */

public interface UserModel {

    UserService getService();

    User getUser();

    void setUser(User user);
}
