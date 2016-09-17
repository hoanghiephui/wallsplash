package com.unsplash.wallsplash.common.i.model;

import com.unsplash.wallsplash.common.data.service.AuthorizeService;
import com.unsplash.wallsplash.common.data.service.UserService;

/**
 * Login model.
 */

public interface LoginModel {

    AuthorizeService getAuthService();

    UserService getUserService();
}
