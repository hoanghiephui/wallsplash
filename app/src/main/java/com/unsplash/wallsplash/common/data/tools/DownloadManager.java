package com.unsplash.wallsplash.common.data.tools;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.thin.downloadmanager.DownloadRequest;
import com.thin.downloadmanager.DownloadStatusListener;
import com.thin.downloadmanager.ThinDownloadManager;
import com.unsplash.wallsplash.R;
import com.unsplash.wallsplash.WallSplashApplication;
import com.unsplash.wallsplash.common.data.data.Photo;
import com.unsplash.wallsplash.common.ui.activity.DownloadManageActivity;
import com.unsplash.wallsplash.common.utils.FileUtils;
import com.unsplash.wallsplash.common.utils.NotificationUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Download manager.
 */

public class DownloadManager {
    // widget
    private ThinDownloadManager manager;

    // data
    private List<Mission> missionList;

    public static final int CANCELED_CODE = 1008;
    public static final int FAILED_CODE = -1;

    public static final int DOWNLOAD_TYPE = 1;
    public static final int SHARE_TYPE = 2;
    public static final int WALLPAPER_TYPE = 3;

    /**
     * <br> life cycle.
     */

    private DownloadManager() {
        this.manager = new ThinDownloadManager();
        this.missionList = new ArrayList<>();
    }

    /**
     * <br> data.
     */

    public int add(Photo p, int type, OnDownloadListener l) {
        for (int i = 0; i < missionList.size(); i++) {
            if (missionList.get(i).photo.id.equals(p.id)) {
                Context c = WallSplashApplication.getInstance().getActivityList().get(0);
                NotificationUtils.showSnackbar(
                        c.getString(R.string.feedback_download_repeat),
                        Snackbar.LENGTH_SHORT);
                return FAILED_CODE;
            }
        }
        Mission m = new Mission(p, type);
        m.id = manager.add(m.request);
        m.addOnDownloadListener(l);
        missionList.add(m);
        return m.id;
    }

    public int cancel(String photoId) {
        for (int i = 0; i < missionList.size(); i++) {
            if (missionList.get(i).photo.id.equals(photoId)) {
                if (missionList.get(i).failed) {
                    missionList.remove(i);
                    return FAILED_CODE;
                } else {
                    int id = missionList.get(i).id;
                    missionList.remove(i);
                    return manager.cancel(id);
                }
            }
        }
        return FAILED_CODE;
    }

    public void cancelAll() {
        missionList.clear();
        manager.cancelAll();
    }

    public void deleteMission(Mission m) {
        missionList.remove(m);
    }

    public Mission retry(String photoId, OnDownloadListener l) {
        for (int i = 0; i < missionList.size(); i++) {
            if (missionList.get(i).photo.id.equals(photoId)) {
                Photo p = missionList.get(i).photo;
                int type = missionList.get(i).downloadType;
                missionList.remove(i);
                add(p, type, l);
                return missionList.get(missionList.size() - 1);
            }
        }
        return null;
    }

    public List<Mission> getMissionList() {
        return missionList;
    }

    /**
     * <br> interface.
     */

    public interface OnDownloadListener {
        void onDownloadComplete(int id);

        void onDownloadFailed(int id, int code);

        void onDownloadProgress(int id, int percent);
    }

    public void addOnDownloadListener(OnDownloadListener l) {
        for (int i = 0; i < missionList.size(); i++) {
            missionList.get(i).addOnDownloadListener(l);
        }
    }

    public void removeDownloadListener(OnDownloadListener l) {
        for (int i = 0; i < missionList.size(); i++) {
            missionList.get(i).removeOnDownloadListener(l);
        }
    }

    /**
     * <br> singleton.
     */

    private static DownloadManager instance;

    public static DownloadManager getInstance() {
        if (instance == null) {
            instance = new DownloadManager();
        }
        return instance;
    }

    /**
     * <br> inner class.
     */

    public static class Mission implements DownloadStatusListener, View.OnClickListener {
        // data
        public int id = FAILED_CODE;
        public Photo photo;
        public int downloadType;
        public int progress = 0;
        public boolean failed = false;
        public DownloadRequest request;
        public List<OnDownloadListener> listenerList;

        // life cycle.

        public Mission(Photo p, int type) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(WallSplashApplication.getInstance());
            String scaleType = sharedPreferences.getString(
                    WallSplashApplication.getInstance().getString(R.string.key_download_scale),
                    WallSplashApplication.getInstance().getResources().getStringArray(R.array.download_type_values)[0]);

            Uri downloadUri;
            if (scaleType.equals(WallSplashApplication.getInstance().getResources().getStringArray(R.array.download_type_values)[0])) {
                downloadUri = Uri.parse(p.urls.full);
            } else {
                downloadUri = Uri.parse(p.urls.raw);
            }
            Uri fileUri = Uri.parse(WallSplashApplication.DOWNLOAD_PATH + p.id + WallSplashApplication.DOWNLOAD_FORMAT);

            this.request = new DownloadRequest(downloadUri)
                    .setDestinationURI(fileUri)
                    .setPriority(DownloadRequest.Priority.HIGH)
                    .setDownloadListener(this);
            this.photo = p;
            this.downloadType = type;
            this.listenerList = new ArrayList<>();
        }

        // interface.

        public void addOnDownloadListener(OnDownloadListener l) {
            listenerList.add(l);
        }

        public void removeOnDownloadListener(OnDownloadListener l) {
            listenerList.remove(l);
        }

        @Override
        public void onDownloadComplete(int id) {
            if (!FileUtils.isFileExist(photo.id)) {
                return;
            }

            for (int i = 0; i < listenerList.size(); i++) {
                listenerList.get(i).onDownloadComplete(id);
            }
            listenerList.clear();

            Uri file = Uri.parse("file://" + WallSplashApplication.DOWNLOAD_PATH + photo.id + WallSplashApplication.DOWNLOAD_FORMAT);
            Intent broadcast = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, file);
            WallSplashApplication.getInstance().sendBroadcast(broadcast);
            switch (downloadType) {
                case DOWNLOAD_TYPE:
                    Context c = WallSplashApplication.getInstance().getActivityList().get(0);
                    NotificationUtils.showActionSnackbar(
                            c.getString(R.string.feedback_download_success),
                            c.getString(R.string.check),
                            Snackbar.LENGTH_LONG,
                            this);
                    break;

                case SHARE_TYPE: {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_STREAM, file);
                    intent.setType("image/*");
                    List<AppCompatActivity> list = WallSplashApplication.getInstance().getActivityList();
                    list.get(list.size() - 1).startActivity(
                            Intent.createChooser(
                                    intent,
                                    WallSplashApplication.getInstance().getString(R.string.feedback_choose_share_app)));
                    break;
                }

                case WALLPAPER_TYPE: {
                    Intent intent = new Intent(Intent.ACTION_ATTACH_DATA);
                    intent.setDataAndType(file, "image/jpg");
                    intent.putExtra("mimeType", "image/jpg");
                    List<AppCompatActivity> list = WallSplashApplication.getInstance().getActivityList();
                    list.get(list.size() - 1).startActivity(
                            Intent.createChooser(
                                    intent,
                                    WallSplashApplication.getInstance().getString(R.string.feedback_choose_wallpaper_app)));
                    break;
                }
            }
            DownloadManager.getInstance().deleteMission(this);
        }

        @Override
        public void onDownloadFailed(int id, int i1, String s) {
            for (int i = 0; i < listenerList.size(); i++) {
                listenerList.get(i).onDownloadFailed(id, i1);
            }
            if (i1 == CANCELED_CODE) {
                listenerList.clear();
                DownloadManager.getInstance().deleteMission(this);
            } else {
                failed = true;
                Context c = WallSplashApplication.getInstance().getActivityList().get(0);
                NotificationUtils.showActionSnackbar(
                        c.getString(R.string.feedback_download_failed),
                        c.getString(R.string.check),
                        Snackbar.LENGTH_LONG,
                        this);
            }
        }

        @Override
        public void onProgress(int id, long l, long l1, int i1) {
            progress = i1;
            for (int i = 0; i < listenerList.size(); i++) {
                listenerList.get(i).onDownloadProgress(id, i1);
            }
        }

        @Override
        public void onClick(View v) {
            if (failed) {
                Context c = WallSplashApplication.getInstance().getActivityList().get(0);
                Intent intent = new Intent(c, DownloadManageActivity.class);
                c.startActivity(intent);
            } else {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                Uri file = Uri.parse(WallSplashApplication.DOWNLOAD_PATH + photo.id + WallSplashApplication.DOWNLOAD_FORMAT);
                intent.setDataAndType(file, "image/*");

                Context c = WallSplashApplication.getInstance().getActivityList().get(0);
                c.startActivity(intent);
            }
        }
    }
}
