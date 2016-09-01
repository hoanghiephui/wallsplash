package com.unsplash.wallsplash._common.utils;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import com.unsplash.wallsplash.R;
import com.unsplash.wallsplash.WallSplashApplication;

import java.io.File;

/**
 * File utils.
 */

public class FileUtils {

    public static boolean createDownloadPath(Context c) {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(c,
                    c.getString(R.string.feedback_no_sd_card),
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        File dirFile1 = new File(Environment.getExternalStorageDirectory(), "Pictures");
        if (!dirFile1.exists()) {
            if (!dirFile1.mkdir()) {
                Toast.makeText(c,
                        c.getString(R.string.feedback_create_file_failed) + " -1",
                        Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        File dirFile2 = new File(Environment.getExternalStorageDirectory().toString() + "/Pictures/WallSplashApplication");
        if (!dirFile2.exists()) {
            if (!dirFile2.mkdir()) {
                Toast.makeText(c,
                        c.getString(R.string.feedback_create_file_failed) + " -2",
                        Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    public static boolean isFileExist(String photoId) {
        File f = new File(WallSplashApplication.DOWNLOAD_PATH + photoId + WallSplashApplication.DOWNLOAD_FORMAT);
        return f.exists();
    }
}
