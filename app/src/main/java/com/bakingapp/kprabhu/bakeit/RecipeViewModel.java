package com.bakingapp.kprabhu.bakeit;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.widget.Toast;

import com.bakingapp.kprabhu.bakeit.model.Recipe;
import com.bakingapp.kprabhu.bakeit.service.RecipeService;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by kprabhu on 9/17/17.
 */

public class RecipeViewModel extends AndroidViewModel {
    private static final String TAG = "RecipeViewModel";
    private static final String BASE_URL = "http://go.udacity.com";
    MutableLiveData<List<Recipe>> recipes;
    Context context;

    public RecipeViewModel(Application application) {
        super(application);
        context = application;
    }

    public LiveData<List<Recipe>> getRecipes() {
        if (recipes == null) {
            recipes = new MutableLiveData<>();
            loadRecipes(); //async
        }
        return recipes;
    }

    private void loadRecipes() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient.Builder().build())
                .build();

        RecipeService recipeService = retrofit.create(RecipeService.class);
        Call<List<Recipe>> listCall = recipeService.recipes();
        listCall.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                recipes.postValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                if (!call.isCanceled()) {
                    Toast.makeText(context, R.string.recipes_retrieve_error,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
