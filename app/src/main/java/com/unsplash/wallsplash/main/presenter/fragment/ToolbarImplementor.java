package com.unsplash.wallsplash.main.presenter.fragment;

import android.app.Activity;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;

import com.unsplash.wallsplash.R;
import com.unsplash.wallsplash.common.i.presenter.ToolbarPresenter;
import com.unsplash.wallsplash.main.view.activity.MainActivity;
import com.unsplash.wallsplash.main.view.fragment.CategoryFragment;
import com.unsplash.wallsplash.main.view.fragment.HomeFragment;

/**
 * Toolbar implementor.
 */

public class ToolbarImplementor
        implements ToolbarPresenter {
    // model & view.
    @Override
    public void touchNavigatorIcon(Activity a) {
        DrawerLayout drawer = (DrawerLayout) a.findViewById(R.id.activity_main_drawerLayout);
        drawer.openDrawer(GravityCompat.START);
    }

    @Override
    public void touchToolbar(Activity a) {
        Fragment f = ((MainActivity) a).getTopFragment();
        if (f instanceof HomeFragment) {
            ((HomeFragment) f).pagerBackToTop();
        } else {
            ((CategoryFragment) f).pagerBackToTop();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean touchMenuItem(AppCompatActivity a, int itemId) {
        MainActivity activity = (MainActivity) a;
        switch (itemId) {
            case R.id.action_search:
                activity.insertFragment(itemId);
                break;

            case R.id.action_filter:
                Fragment f = activity.getTopFragment();

                if (f instanceof HomeFragment) {
                    ((HomeFragment) f).showPopup();
                } else {
                    ((CategoryFragment) f).showPopup();
                }
                break;
        }
        return true;
    }
}
