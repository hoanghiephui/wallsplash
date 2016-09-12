package com.unsplash.wallsplash._common.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.format.DateFormat;
import android.text.format.DateUtils;

import com.unsplash.wallsplash.BuildConfig;
import com.unsplash.wallsplash.R;
import com.unsplash.wallsplash.WallSplashApplication;
import com.unsplash.wallsplash._common.data.api.PhotoApi;
import com.unsplash.wallsplash._common.data.data.Photo;
import com.unsplash.wallsplash._common.data.data.PhotoDetails;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Response;

/**
 * Value utils.
 */

public class ValueUtils {

    public static String getOrderName(Context c, String key) {
        switch (key) {
            case PhotoApi.ORDER_BY_LATEST:
                return c.getResources().getStringArray(R.array.photo_orders)[0];

            case PhotoApi.ORDER_BY_OLDEST:
                return c.getResources().getStringArray(R.array.photo_orders)[1];

            case PhotoApi.ORDER_BY_POPULAR:
                return c.getResources().getStringArray(R.array.photo_orders)[2];

            case "random":
                return c.getResources().getStringArray(R.array.photo_orders)[3];

            default:
                return null;
        }
    }

    public static String getCollectionName(Context c, String key) {
        switch (key) {
            case "all":
                return c.getResources().getStringArray(R.array.collection_types)[0];

            case "curated":
                return c.getResources().getStringArray(R.array.collection_types)[1];

            case "featured":
                return c.getResources().getStringArray(R.array.collection_types)[2];

            default:
                return null;
        }
    }

    public static String getScaleName(Context c, String key) {
        switch (key) {
            case "compact":
                return c.getResources().getStringArray(R.array.download_types)[0];

            case "raw":
                return c.getResources().getStringArray(R.array.download_types)[1];

            default:
                return null;
        }
    }

    public static String getBackToTopName(Context c, String key) {
        switch (key) {
            case "all":
                return c.getResources().getStringArray(R.array.back_to_top_type)[0];

            case "home":
                return c.getResources().getStringArray(R.array.back_to_top_type)[1];

            case "none":
                return c.getResources().getStringArray(R.array.back_to_top_type)[2];

            default:
                return null;
        }
    }

    public static String getLanguageName(Context c, String key) {
        switch (key) {
            case "follow_system":
                return c.getResources().getStringArray(R.array.languages)[0];

            case "english":
                return c.getResources().getStringArray(R.array.languages)[1];

            case "chinese":
                return c.getResources().getStringArray(R.array.languages)[2];

            default:
                return null;
        }
    }

    public static String getOrientationName(Context c, String key) {
        switch (key) {
            case PhotoApi.LANDSCAPE_ORIENTATION:
                return c.getResources().getStringArray(R.array.search_orientations)[0];

            case PhotoApi.PORTRAIT_ORIENTATION:
                return c.getResources().getStringArray(R.array.search_orientations)[1];

            case PhotoApi.SQUARE_ORIENTATION:
                return c.getResources().getStringArray(R.array.search_orientations)[2];

            default:
                return null;
        }
    }

    public static String getToolbarTitleByCategory(Context context, int id) {
        switch (id) {
            case WallSplashApplication.CATEGORY_BUILDINGS_ID:
                return context.getString(R.string.action_category_buildings);

            case WallSplashApplication.CATEGORY_FOOD_DRINK_ID:
                return context.getString(R.string.action_category_food_drink);

            case WallSplashApplication.CATEGORY_NATURE_ID:
                return context.getString(R.string.action_category_nature);

            case WallSplashApplication.CATEGORY_OBJECTS_ID:
                return context.getString(R.string.action_category_objects);

            case WallSplashApplication.CATEGORY_PEOPLE_ID:
                return context.getString(R.string.action_category_people);

            case WallSplashApplication.CATEGORY_TECHNOLOGY_ID:
                return context.getString(R.string.action_category_technology);

            default:
                return context.getString(R.string.app_name);
        }
    }

    public static List<Integer> getPageListByCategory(int id) {
        switch (id) {
            case WallSplashApplication.CATEGORY_TOTAL_NEW:
                return MathUtils.getPageList(WallSplashApplication.TOTAL_NEW_PHOTOS_COUNT / WallSplashApplication.DEFAULT_PER_PAGE);

            case WallSplashApplication.CATEGORY_TOTAL_FEATURED:
                return MathUtils.getPageList(WallSplashApplication.TOTAL_FEATURED_PHOTOS_COUNT / WallSplashApplication.DEFAULT_PER_PAGE);

            case WallSplashApplication.CATEGORY_BUILDINGS_ID:
                return MathUtils.getPageList(WallSplashApplication.BUILDING_PHOTOS_COUNT / WallSplashApplication.DEFAULT_PER_PAGE);

            case WallSplashApplication.CATEGORY_FOOD_DRINK_ID:
                return MathUtils.getPageList(WallSplashApplication.FOOD_DRINK_PHOTOS_COUNT / WallSplashApplication.DEFAULT_PER_PAGE);

            case WallSplashApplication.CATEGORY_NATURE_ID:
                return MathUtils.getPageList(WallSplashApplication.NATURE_PHOTOS_COUNT / WallSplashApplication.DEFAULT_PER_PAGE);

            case WallSplashApplication.CATEGORY_OBJECTS_ID:
                return MathUtils.getPageList(WallSplashApplication.OBJECTS_PHOTOS_COUNT / WallSplashApplication.DEFAULT_PER_PAGE);

            case WallSplashApplication.CATEGORY_PEOPLE_ID:
                return MathUtils.getPageList(WallSplashApplication.PEOPLE_PHOTOS_COUNT / WallSplashApplication.DEFAULT_PER_PAGE);

            case WallSplashApplication.CATEGORY_TECHNOLOGY_ID:
                return MathUtils.getPageList(WallSplashApplication.TECHNOLOGY_PHOTOS_COUNT / WallSplashApplication.DEFAULT_PER_PAGE);

            default:
                return MathUtils.getPageList(0);
        }
    }

    public static void writePhotoCount(Context c, Response<List<Photo>> response, int category) {
        String value = response.headers().get("X-Total");
        if (value != null && !value.equals("")) {
            int count = Integer.parseInt(value);
            writePhotoCount(c, category, count);
        }
    }

    public static void writePhotoCount(Context c, PhotoDetails details) {
        for (int i = 0; i < details.categories.size(); i++) {
            writePhotoCount(
                    c,
                    details.categories.get(i).id,
                    details.categories.get(i).photo_count);
        }
    }

    private static void writePhotoCount(Context c, int category, int count) {
        if (count == 0) {
            return;
        }
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(c).edit();
        switch (category) {
            case WallSplashApplication.CATEGORY_TOTAL_NEW:
                editor.putInt(
                        c.getString(R.string.key_category_total_new_count),
                        count);
                break;

            case WallSplashApplication.CATEGORY_TOTAL_FEATURED:
                editor.putInt(
                        c.getString(R.string.key_category_total_feature_count),
                        count);
                break;

            case WallSplashApplication.CATEGORY_BUILDINGS_ID:
                editor.putInt(
                        c.getString(R.string.key_category_buildings_count),
                        count);
                break;

            case WallSplashApplication.CATEGORY_FOOD_DRINK_ID:
                editor.putInt(
                        c.getString(R.string.key_category_food_drink_count),
                        count);
                break;

            case WallSplashApplication.CATEGORY_NATURE_ID:
                editor.putInt(
                        c.getString(R.string.key_category_nature_count),
                        count);
                break;

            case WallSplashApplication.CATEGORY_OBJECTS_ID:
                editor.putInt(
                        c.getString(R.string.key_category_objects_count),
                        count);
                break;

            case WallSplashApplication.CATEGORY_PEOPLE_ID:
                editor.putInt(
                        c.getString(R.string.key_category_people_count),
                        count);
                break;

            case WallSplashApplication.CATEGORY_TECHNOLOGY_ID:
                editor.putInt(
                        c.getString(R.string.key_category_technology_count),
                        count);
                break;
        }
        editor.apply();
    }

    public static void readPhotoCount(Context c, int category) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(c);
        switch (category) {
            case WallSplashApplication.CATEGORY_TOTAL_NEW:
                WallSplashApplication.TOTAL_NEW_PHOTOS_COUNT = sharedPreferences.getInt(
                        c.getString(R.string.key_category_total_new_count),
                        WallSplashApplication.TOTAL_NEW_PHOTOS_COUNT);
                break;

            case WallSplashApplication.CATEGORY_TOTAL_FEATURED:
                WallSplashApplication.TOTAL_FEATURED_PHOTOS_COUNT = sharedPreferences.getInt(
                        c.getString(R.string.key_category_total_feature_count),
                        WallSplashApplication.TOTAL_FEATURED_PHOTOS_COUNT);
                break;

            case WallSplashApplication.CATEGORY_BUILDINGS_ID:
                WallSplashApplication.BUILDING_PHOTOS_COUNT = sharedPreferences.getInt(
                        c.getString(R.string.key_category_buildings_count),
                        WallSplashApplication.BUILDING_PHOTOS_COUNT);
                break;

            case WallSplashApplication.CATEGORY_FOOD_DRINK_ID:
                WallSplashApplication.FOOD_DRINK_PHOTOS_COUNT = sharedPreferences.getInt(
                        c.getString(R.string.key_category_food_drink_count),
                        WallSplashApplication.FOOD_DRINK_PHOTOS_COUNT);
                break;

            case WallSplashApplication.CATEGORY_NATURE_ID:
                WallSplashApplication.NATURE_PHOTOS_COUNT = sharedPreferences.getInt(
                        c.getString(R.string.key_category_nature_count),
                        WallSplashApplication.NATURE_PHOTOS_COUNT);
                break;

            case WallSplashApplication.CATEGORY_OBJECTS_ID:
                WallSplashApplication.OBJECTS_PHOTOS_COUNT = sharedPreferences.getInt(
                        c.getString(R.string.key_category_objects_count),
                        WallSplashApplication.OBJECTS_PHOTOS_COUNT);
                break;

            case WallSplashApplication.CATEGORY_PEOPLE_ID:
                WallSplashApplication.PEOPLE_PHOTOS_COUNT = sharedPreferences.getInt(
                        c.getString(R.string.key_category_people_count),
                        WallSplashApplication.PEOPLE_PHOTOS_COUNT);
                break;

            case WallSplashApplication.CATEGORY_TECHNOLOGY_ID:
                WallSplashApplication.TECHNOLOGY_PHOTOS_COUNT = sharedPreferences.getInt(
                        c.getString(R.string.key_category_technology_count),
                        WallSplashApplication.TECHNOLOGY_PHOTOS_COUNT);
                break;
        }
    }

    public static OkHttpClient buildClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY
                : HttpLoggingInterceptor.Level.NONE);
        return new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();
    }

    public static String formatSameDayTime(final Context context, final long timestamp) {
        if (context == null) return null;
        if (DateUtils.isToday(timestamp))
            return DateUtils.formatDateTime(context, timestamp,
                    DateFormat.is24HourFormat(context) ? DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_24HOUR
                            : DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_12HOUR);
        return DateUtils.formatDateTime(context, timestamp, DateUtils.FORMAT_SHOW_DATE);
    }

    public static Long getFormattedDate(String created_at) {
        long time;
        try {
            //2016-09-05T03:09:13-04:00
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.getDefault());
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC -4"));
            Date date = dateFormat.parse(created_at);
            dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Timestamp tm = Timestamp.valueOf(dateFormat.format(date));
            time = tm.getTime();
            return time;
        } catch (ParseException ignored) {
        }
        return null;
    }
}
