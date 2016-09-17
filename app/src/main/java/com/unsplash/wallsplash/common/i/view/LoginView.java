package com.unsplash.wallsplash.common.i.view;

/**
 * Login view.
 */

public interface LoginView {

    void onAuthCallback();

    void requestAccessTokenSuccess();

    void requestAccessTokenFailed();
}
