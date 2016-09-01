package com.unsplash.wallsplash._common.i.view;

/**
 * Login view.
 */

public interface LoginView {

    void onAuthCallback();

    void requestAccessTokenSuccess();

    void requestAccessTokenFailed();
}
