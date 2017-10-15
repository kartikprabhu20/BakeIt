package com.bakingapp.kprabhu.bakeit.widget;

import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bakingapp.kprabhu.bakeit.BakeItApp;
import com.bakingapp.kprabhu.bakeit.R;
import com.bakingapp.kprabhu.bakeit.RecipeInfoWidgetManager;
import com.bakingapp.kprabhu.bakeit.model.Ingredient;
import com.bakingapp.kprabhu.bakeit.model.Recipe;

/**
 * Created by kprabhu on 9/18/17.
 */

public class RecipeInfoWidgetRemoteViewService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new MyRemoteViewFactory();
    }
}

class MyRemoteViewFactory implements RemoteViewsService.RemoteViewsFactory {
    private RecipeInfoWidgetManager recipeInfoWidgetManager = new RecipeInfoWidgetManager();

    private Recipe recipe;

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        recipe = recipeInfoWidgetManager.getRecipe();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if(recipe == null){
            return 0;
        }
        if(recipe.ingredients == null || recipe.ingredients.isEmpty()){
            return 0;
        }
        return recipe.ingredients.size();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        RemoteViews views = new RemoteViews(BakeItApp.getContext().getPackageName(), R.layout.cell_wigdet_recipe_ingredient);

        Ingredient ingredient = recipe.ingredients.get(i);

        views.setTextViewText(R.id.recipe_ingredient_name, ingredient.ingredient);

        views.setTextViewText(R.id.recipe_ingredient_quantity, BakeItApp.getContext().getString(R.string.ingredient_quantity_text, ingredient.quantity, ingredient.measure));
        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return (long) i;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
