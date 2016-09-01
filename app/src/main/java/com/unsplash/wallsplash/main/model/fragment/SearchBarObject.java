package com.unsplash.wallsplash.main.model.fragment;

import com.unsplash.wallsplash._common.data.api.PhotoApi;
import com.unsplash.wallsplash._common.i.model.SearchBarModel;

/**
 * Search bar object.
 */

public class SearchBarObject
        implements SearchBarModel {
    // data
    private String orientation;

    /**
     * <br> life cycle.
     */

    public SearchBarObject() {
        orientation = PhotoApi.LANDSCAPE_ORIENTATION;
    }

    @Override
    public String getOrientation() {
        return orientation;
    }

    @Override
    public void setOrientation(String o) {
        orientation = o;
    }
}
