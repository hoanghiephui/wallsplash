package com.unsplash.wallsplash.common.i.presenter;

import android.app.Activity;
import android.support.v4.app.Fragment;

import java.util.List;

/**
 * Fragment manage presenter.
 */

public interface FragmentManagePresenter {

    List<Fragment> getFragmentList();

    Fragment getTopFragment();

    void addFragment(Activity activity, int code);

    void popFragment(Activity activity);

    void changeFragment(Activity activity, int code);
}
