package com.unsplash.wallsplash;

import android.app.Application;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.ads.MobileAds;
import com.unsplash.wallsplash.common.data.data.Collection;
import com.unsplash.wallsplash.common.data.data.Photo;
import com.unsplash.wallsplash.common.data.data.User;
import com.unsplash.wallsplash.common.utils.ValueUtils;
import com.unsplash.wallsplash.main.view.activity.MainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * My application.
 */

public class WallSplashApplication extends Application {
    // data
    private List<AppCompatActivity> activityList;
    private Photo photo;
    private Collection collection;
    private User user;
    private Drawable drawable;
    private boolean myOwnCollection = false;
    private boolean activityInBackstage = false;

    // Unsplash data.
    public static final String APPLICATION_ID = "b0fc18a625db650671647a77a8f786f600658640d2598fade9a6ddad2d7c3ccb";
    public static final String SECRET = "57df2a9ff4a2a73ae4f8c119bb7d85e6c5c2fb8bebd1c08e0ef488412ec5e92b";

    // Unsplash url.
    public static final String UNSPLASH_API_BASE_URL = "https://api.unsplash.com/";
    public static final String UNSPLASH_AUTH_BASE_URL = "https://unsplash.com/";
    public static final String UNSPLASH_URL = "https://unsplash.com/";
    public static final String UNSPLASH_JOIN_URL = "https://unsplash.com/join";
    public static final String UNSPLASH_LOGIN_CALLBACK = "unsplash-auth-callback";
    public static final String UNSPLASH_LOGIN_URL = WallSplashApplication.UNSPLASH_AUTH_BASE_URL + "oauth/authorize"
            + "?client_id=" + WallSplashApplication.APPLICATION_ID
            + "&redirect_uri=" + "wallsplash%3A%2F%2F" + UNSPLASH_LOGIN_CALLBACK
            + "&response_type=" + "code"
            + "&scope=" + "public+read_user+write_user+read_photos+write_photos+write_likes+read_collections+write_collections";

    // application data.
    public static final String AUTHOR_GITHUB = "https://github.com/WangDaYeeeeee";
    public static final String MYSPLASH_GITHUB = "https://github.com/WangDaYeeeeee/MySplash";

    public static final String DATE_FORMAT = "yyyy/MM/dd";
    public static final String DOWNLOAD_PATH = Environment.getExternalStorageDirectory().toString() + "/Pictures/WallSplashApplication/";
    public static final String DOWNLOAD_FORMAT = ".jpg";
    public static final int DEFAULT_PER_PAGE = 30;
    public static final int SEARCH_PER_PAGE = 20;

    public static final int CATEGORY_TOTAL_NEW = 0;
    public static final int CATEGORY_TOTAL_FEATURED = 1;
    public static final int CATEGORY_BUILDINGS_ID = 2;
    public static final int CATEGORY_FOOD_DRINK_ID = 3;
    public static final int CATEGORY_NATURE_ID = 4;
    public static final int CATEGORY_OBJECTS_ID = 8;
    public static final int CATEGORY_PEOPLE_ID = 6;
    public static final int CATEGORY_TECHNOLOGY_ID = 7;

    public static int TOTAL_NEW_PHOTOS_COUNT = 14500;
    public static int TOTAL_FEATURED_PHOTOS_COUNT = 900;
    public static int BUILDING_PHOTOS_COUNT = 2720;
    public static int FOOD_DRINK_PHOTOS_COUNT = 650;
    public static int NATURE_PHOTOS_COUNT = 6910;
    public static int OBJECTS_PHOTOS_COUNT = 2150;
    public static int PEOPLE_PHOTOS_COUNT = 3410;
    public static int TECHNOLOGY_PHOTOS_COUNT = 350;

    // activity code.
    public static final int ME_ACTIVITY = 1;

    // permission code.
    public static final int WRITE_EXTERNAL_STORAGE = 1;

    /**
     * <br> life cycle.
     */

    @Override
    public void onCreate() {
        super.onCreate();
        initialize();
        readPhotoCount();
        MobileAds.initialize(getApplicationContext(), getString(R.string.id_app_admob));
    }

    private void initialize() {
        instance = this;
        activityList = new ArrayList<>();
    }

    private void readPhotoCount() {
        ValueUtils.readPhotoCount(this, CATEGORY_TOTAL_NEW);
        ValueUtils.readPhotoCount(this, CATEGORY_TOTAL_FEATURED);
        ValueUtils.readPhotoCount(this, CATEGORY_BUILDINGS_ID);
        ValueUtils.readPhotoCount(this, CATEGORY_FOOD_DRINK_ID);
        ValueUtils.readPhotoCount(this, CATEGORY_NATURE_ID);
        ValueUtils.readPhotoCount(this, CATEGORY_OBJECTS_ID);
        ValueUtils.readPhotoCount(this, CATEGORY_PEOPLE_ID);
        ValueUtils.readPhotoCount(this, CATEGORY_TECHNOLOGY_ID);
    }

    /**
     * <br> data.
     */

    public void addActivity(AppCompatActivity a) {
        activityList.add(a);
    }

    public void removeActivity() {
        activityList.remove(activityList.size() - 1);
    }

    /*
    public List<Activity> getActivityList() {
        return activityList;
    }
*/
    public AppCompatActivity getLatestActivity() {
        if (activityList.size() > 0) {
            return activityList.get(activityList.size() - 1);
        } else {
            return null;
        }
    }

    public MainActivity getMainActivity() {
        if (activityList.get(0) instanceof MainActivity) {
            return (MainActivity) activityList.get(0);
        } else {
            return null;
        }
    }

    public int getActivityCount() {
        return activityList.size();
    }

    public List<AppCompatActivity> getActivityList() {
        return activityList;
    }

    public void setPhoto(Photo p) {
        this.photo = p;
    }

    public Photo getPhoto() {
        return photo;
    }

    public void setCollection(Collection c) {
        this.collection = c;
    }

    public Collection getCollection() {
        return collection;
    }

    public void setUser(User u) {
        this.user = u;
    }

    public User getUser() {
        return user;
    }

    public void setDrawable(Drawable d) {
        this.drawable = d;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public void setMyOwnCollection(boolean own) {
        this.myOwnCollection = own;
    }

    public boolean isMyOwnCollection() {
        return myOwnCollection;
    }

    public void setActivityInBackstage(boolean showing) {
        this.activityInBackstage = showing;
    }

    public boolean isActivityInBackstage() {
        return activityInBackstage;
    }

    /**
     * <br> singleton.
     */

    private static WallSplashApplication instance;

    public static WallSplashApplication getInstance() {
        return instance;
    }
}
