package com.unsplash.wallsplash._common.i.model;

import com.unsplash.wallsplash._common.data.service.AuthorizeService;
import com.unsplash.wallsplash._common.data.service.UserService;

/**
 * Login model.
 */

public interface LoginModel {

    AuthorizeService getAuthService();

    UserService getUserService();
}
