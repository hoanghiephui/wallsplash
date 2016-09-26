package com.unsplash.wallsplash.common.i.model;

/**
 * Created by Hoang Hiep on 9/25/2016.
 */

public interface MultiFilterBarModel {
    void setQuery(String query);

    String getQuery();

    void setUsername(String username);

    String getUsername();

    void setCategory(int c);

    int getCategory();

    void setOrientation(String o);

    String getOrientation();

    void setFeatured(boolean f);

    boolean isFeatured();
}
