package com.unsplash.wallsplash.common.i.model;

import com.unsplash.wallsplash.common.data.data.User;
import com.unsplash.wallsplash.common.data.service.UserService;

/**
 * User model.
 */

public interface UserModel {

    UserService getService();

    User getUser();

    void setUser(User user);
}
