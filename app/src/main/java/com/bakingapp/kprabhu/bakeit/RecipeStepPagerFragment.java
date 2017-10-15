package com.bakingapp.kprabhu.bakeit;

import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bakingapp.kprabhu.bakeit.model.Recipe;
import com.bakingapp.kprabhu.bakeit.model.Step;

/**
 * Created by kprabhu on 9/17/17.
 */

class RecipeStepPagerFragment extends LifecycleFragment{
    private ViewPager mPager;
    RecipeStepViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.recipe_steps_pager, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Instantiate a ViewPager and a PagerAdapter.
        if (getView() == null) {
            return;
        }
        viewModel = ViewModelProviders.of(getActivity()).get(RecipeStepViewModel.class);
        mPager = getView().findViewById(R.id.pager);
        FragmentStatePagerAdapter mPagerAdapter = new ScreenSlidePagerAdapter(getChildFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        observeViewModel(this);
    }

    private void observeViewModel(LifecycleOwner fragment) {
        viewModel.getStep().observe(fragment, new Observer<Step>() {
            @Override
            public void onChanged(@Nullable Step step) {
                if (step == null) {
                    return;
                }
                if (getActivity() instanceof AppCompatActivity) {

                    ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(step.shortDescription);
                    mPager.setCurrentItem(step.id);

                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (getActivity() instanceof AppCompatActivity) {
                    Step step = viewModel.getRecipe().getValue().steps.get(position);
                    viewModel.setStep(step);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onPause() {
        mPager.clearOnPageChangeListeners();
        super.onPause();
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        ScreenSlidePagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);

        }

        @Override
        public Fragment getItem(int position) {
            return new RecipeStepFragment();
        }

        @Override
        public int getCount() {
            Recipe recipe = viewModel.getRecipe().getValue();
            if (recipe == null)
                return 0;
            return recipe.steps.size();
        }
    }
}
