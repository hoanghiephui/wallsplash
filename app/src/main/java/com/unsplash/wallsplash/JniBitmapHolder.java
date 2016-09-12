package com.unsplash.wallsplash;

import android.graphics.Bitmap;
import android.util.Log;

import java.nio.ByteBuffer;

/**
 * Created by Hoang Hiep on 9/10/2016.
 */

public class JniBitmapHolder {
    ByteBuffer _handler = null;

    static {
        System.loadLibrary("JniBitmapStorageTest");
    }

    private native ByteBuffer jniStoreBitmapData(Bitmap bitmap);

    private native Bitmap jniGetBitmapFromStoredBitmapData(ByteBuffer handler);

    private native void jniFreeBitmapData(ByteBuffer handler);

    public JniBitmapHolder() {
    }

    public JniBitmapHolder(final Bitmap bitmap) {
        storeBitmap(bitmap);
    }

    public void storeBitmap(final Bitmap bitmap) {
        if (_handler != null)
            freeBitmap();
        _handler = jniStoreBitmapData(bitmap);
    }

    public Bitmap getBitmap() {
        if (_handler == null)
            return null;
        return jniGetBitmapFromStoredBitmapData(_handler);
    }

    public Bitmap getBitmapAndFree() {
        final Bitmap bitmap = getBitmap();
        freeBitmap();
        return bitmap;
    }

    public void freeBitmap() {
        if (_handler == null)
            return;
        jniFreeBitmapData(_handler);
        _handler = null;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        if (_handler == null)
            return;
        Log.w("DEBUG", "JNI bitmap wasn't freed nicely.please rememeber to free the bitmap as soon as you can");
        freeBitmap();
    }
}
