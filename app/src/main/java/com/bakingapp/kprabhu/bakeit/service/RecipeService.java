package com.bakingapp.kprabhu.bakeit.service;

import com.bakingapp.kprabhu.bakeit.model.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by kprabhu on 9/17/17.
 */

public interface RecipeService {

    @GET("android-baking-app-json")
    Call<List<Recipe>> recipes();
}
