package com.bakingapp.kprabhu.bakeit;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.bakingapp.kprabhu.bakeit.model.Recipe;
import com.bakingapp.kprabhu.bakeit.widget.RecipeInfoWidget;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.io.IOException;

/**
 * Created by kprabhu on 9/18/17.
 */

public class RecipeInfoWidgetManager {
    private static final String TAG = "RecipeInfoWidgetManager";

    private static final String PREFERENCES_NAME = "RecipeInfoWidget";
    private static final String KEY_RECIPE = "Recipe";

    private Moshi moshi = new Moshi.Builder().build();


    private JsonAdapter<Recipe> jsonRecipeAdapter= moshi.adapter(Recipe.class);


    private SharedPreferences sharedPreferences = BakeItApp.getContext().getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);


    void updateWidgetRecipe(Recipe recipe) {
        String recipeJson = jsonRecipeAdapter.toJson(recipe);

        sharedPreferences.edit().
                putString(KEY_RECIPE, recipeJson).
                commit();
        updateWidget();
    }

    private void updateWidget() {
        Intent intent = new Intent(BakeItApp.getContext(), RecipeInfoWidget.class);

        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(BakeItApp.getContext());
        ComponentName componentName = new ComponentName(BakeItApp.getContext(), RecipeInfoWidget.class);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(componentName);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);

        BakeItApp.getContext().sendBroadcast(intent);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list);
    }

    public Recipe getRecipe() {
        String recipe = sharedPreferences.getString(KEY_RECIPE, null);
        if(recipe != null){
            try {
                return jsonRecipeAdapter.fromJson(recipe);
            } catch (IOException e) {
                Log.e(TAG, "getRecipe: ",e);
            }
        }
        return null;
    }

}
