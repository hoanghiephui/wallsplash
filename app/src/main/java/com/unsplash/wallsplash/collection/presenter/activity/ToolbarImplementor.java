package com.unsplash.wallsplash.collection.presenter.activity;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;

import com.unsplash.wallsplash.R;
import com.unsplash.wallsplash.collection.view.activity.CollectionActivity;
import com.unsplash.wallsplash.common.i.presenter.ToolbarPresenter;
import com.unsplash.wallsplash.common.ui.dialog.UpdateCollectionDialog;
import com.unsplash.wallsplash.common.ui.widget.SwipeBackLayout;

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
        ((CollectionActivity) a).finishActivity(SwipeBackLayout.DOWN_DIR, false);
    }

    @Override
    public void touchToolbar(Activity a) {
        ((CollectionActivity) a).getPhotosView().pagerBackToTop();
    }

    @Override
    public boolean touchMenuItem(AppCompatActivity a, int itemId) {
        switch (itemId) {
            case R.id.action_edit:
                UpdateCollectionDialog dialog = new UpdateCollectionDialog();
                dialog.setCollection(((CollectionActivity) a).getCollection());
                dialog.setOnCollectionChangedListener((UpdateCollectionDialog.OnCollectionChangedListener) a);
                dialog.show(a.getSupportFragmentManager(), null);
                break;
        }
        return true;
    }
}
