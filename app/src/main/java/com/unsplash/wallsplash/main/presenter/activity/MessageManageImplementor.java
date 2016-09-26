package com.unsplash.wallsplash.main.presenter.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.unsplash.wallsplash.R;
import com.unsplash.wallsplash.common.data.tools.DownloadManager;
import com.unsplash.wallsplash.common.i.presenter.MessageManagePresenter;
import com.unsplash.wallsplash.common.i.view.MessageManageView;
import com.unsplash.wallsplash.common.ui.activity.AboutActivity;
import com.unsplash.wallsplash.common.ui.activity.DownloadManageActivity;
import com.unsplash.wallsplash.common.ui.activity.SettingsActivity;
import com.unsplash.wallsplash.common.ui.dialog.ConfirmRebootDialog;
import com.unsplash.wallsplash.main.view.activity.MainActivity;

/**
 * Message manage implementor.
 */

public class MessageManageImplementor
        implements MessageManagePresenter {
    // model & view.
    private MessageManageView view;

    /**
     * <br> life cycle.
     */

    public MessageManageImplementor(MessageManageView view) {
        this.view = view;
    }

    @Override
    public void sendMessage(int what, Object o) {
        view.sendMessage(what, o);
    }

    @Override
    public void responseMessage(final AppCompatActivity a, int what, Object o) {
        switch (what) {
            case R.id.action_change_theme:
                if (DownloadManager.getInstance().getMissionList().size() > 0) {
                    ConfirmRebootDialog dialog = ConfirmRebootDialog.buildDialog(
                            ConfirmRebootDialog.CHANGE_THEME_TYPE);
                    dialog.setOnConfirmRebootListener(new ConfirmRebootDialog.OnConfirmRebootListener() {
                        @Override
                        public void onConfirm() {
                            ((MainActivity) a).changeTheme();
                        }
                    });
                    dialog.show(a.getSupportFragmentManager(), null);
                } else {
                    ((MainActivity) a).changeTheme();
                }
                break;

            case R.id.action_download_manage:
                Intent d = new Intent(a, DownloadManageActivity.class);
                a.startActivity(d);
                a.overridePendingTransition(R.anim.activity_in, 0);
                break;

            case R.id.action_settings:
                Intent s = new Intent(a, SettingsActivity.class);
                a.startActivity(s);
                a.overridePendingTransition(R.anim.activity_in, 0);
                break;

            case R.id.action_about:
                Intent about = new Intent(a, AboutActivity.class);
                a.startActivity(about);
                a.overridePendingTransition(R.anim.activity_in, 0);
                break;

            default:
                ((MainActivity) a).changeFragment(what);
                break;
        }
    }
}
