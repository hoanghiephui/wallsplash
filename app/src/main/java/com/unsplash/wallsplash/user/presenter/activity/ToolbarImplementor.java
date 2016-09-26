package com.unsplash.wallsplash.user.presenter.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Toast;

import com.unsplash.wallsplash.R;
import com.unsplash.wallsplash.common.i.presenter.ToolbarPresenter;
import com.unsplash.wallsplash.user.view.activity.UserActivity;

/**
 * Toolbar implementor.
 */

public class ToolbarImplementor
        implements ToolbarPresenter {
    /**
     * <br> presenter.
     */

    @Override
    public void touchNavigatorIcon(Activity a) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            a.finishAfterTransition();
        } else {
            a.finish();
            a.overridePendingTransition(0, R.anim.activity_slide_out_bottom);
        }
    }

    @Override
    public void touchToolbar(Activity a) {
        // do nothing.
    }

    @Override
    public boolean touchMenuItem(AppCompatActivity a, int itemId) {
        UserActivity activity = (UserActivity) a;

        switch (itemId) {
            case R.id.action_open_portfolio:
                String url = activity.getUserPortfolio();
                if (!TextUtils.isEmpty(url)) {
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    a.startActivity(i);
                } else {
                    Toast.makeText(
                            a,
                            a.getString(R.string.feedback_portfolio_is_null),
                            Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.action_filter:
                activity.showPopup();
                break;
        }
        return true;
    }
}
