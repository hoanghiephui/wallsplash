package com.unsplash.wallsplash.main.presenter.activity;

import android.support.v4.app.Fragment;

import com.unsplash.wallsplash.R;
import com.unsplash.wallsplash.WallSplashApplication;
import com.unsplash.wallsplash.common.i.model.FragmentManageModel;
import com.unsplash.wallsplash.common.i.presenter.FragmentManagePresenter;
import com.unsplash.wallsplash.common.i.view.FragmentManageView;
import com.unsplash.wallsplash.main.view.fragment.CategoryFragment;
import com.unsplash.wallsplash.main.view.fragment.HomeFragment;
import com.unsplash.wallsplash.main.view.fragment.SearchFragment;

import java.util.List;

/**
 * Fragment manage implementor.
 */

public class FragmentManageImplementor
        implements FragmentManagePresenter {
    // model & view.
    private FragmentManageModel model;
    private FragmentManageView view;

    /**
     * <br> life cycle.
     */

    public FragmentManageImplementor(FragmentManageModel model, FragmentManageView view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public List<Fragment> getFragmentList() {
        return model.getFragmentList();
    }

    /**
     * <br> presenter.
     */

    @Override
    public void addFragment(int code) {
        Fragment f = buildFragmentByCode(code);
        model.addFragmentToList(f);
        view.addFragment(f);
    }

    @Override
    public void popFragment() {
        if (model.getFragmentCount() > 0) {
            model.popFragmentFromList();
            view.popFragment();
        }
    }

    @Override
    public void changeFragment(int code) {
        if (model.getFragmentCount() > 1) {
            while (model.getFragmentCount() > 1) {
                popFragment();
            }
        }
        Fragment f = buildFragmentByCode(code);
        model.getFragmentList().clear();
        model.addFragmentToList(f);
        view.changeFragment(f);
    }

    /**
     * <br> utils.
     */

    private Fragment buildFragmentByCode(int code) {
        switch (code) {
            case R.id.action_home:
                return new HomeFragment();

            case R.id.action_search:
                return new SearchFragment();

            case R.id.action_category_buildings:
                return new CategoryFragment().setCategory(WallSplashApplication.CATEGORY_BUILDINGS_ID);

            case R.id.action_category_food_drink:
                return new CategoryFragment().setCategory(WallSplashApplication.CATEGORY_FOOD_DRINK_ID);

            case R.id.action_category_nature:
                return new CategoryFragment().setCategory(WallSplashApplication.CATEGORY_NATURE_ID);

            case R.id.action_category_objects:
                return new CategoryFragment().setCategory(WallSplashApplication.CATEGORY_OBJECTS_ID);

            case R.id.action_category_people:
                return new CategoryFragment().setCategory(WallSplashApplication.CATEGORY_PEOPLE_ID);

            case R.id.action_category_technology:
                return new CategoryFragment().setCategory(WallSplashApplication.CATEGORY_TECHNOLOGY_ID);

            default:
                return null;
        }
    }
}
