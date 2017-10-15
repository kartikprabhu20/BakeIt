package com.bakingapp.kprabhu.bakeit;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.bakingapp.kprabhu.bakeit.model.Recipe;
import com.bakingapp.kprabhu.bakeit.model.Step;

/**
 * Created by kprabhu on 9/17/17.
 */

class RecipeStepViewModel extends ViewModel {
    private MutableLiveData<Recipe> recipe = new MutableLiveData<>();

    private MutableLiveData<Step> step = new MutableLiveData<>();


    LiveData<Recipe> getRecipe() {
        return recipe;
    }

    LiveData<Step> getStep() {
        return step;
    }

    void setRecipe(Recipe recipe) {
        this.recipe.setValue(recipe);
    }

    void setStep(Step step) {
        this.step.setValue(step);
    }
}
